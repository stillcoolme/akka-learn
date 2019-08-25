package com.stillcoolme.translate

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorLogging, ActorSystem, AllForOneStrategy, OneForOneStrategy, Props}


/**
 * @author: stillcoolme
 * @date: 2019/8/23 19:57
 * @description:
 * 我们需要实现一个翻译模块，其功能是输入中文输出多国语言。
 * 我们可以让一个 Master Actor 负责接收外界输入，多个 Worker Actor 负责将输入翻译成特定语言，
 * Master Actor 和 Worker Actor 之间是上下级层次关系。
 *
 * 对于分布式系统来说，容错机制是很重要的指标。看 Akka 是怎么实现容错。
 *
 **/
class MasterActor2 extends Actor with ActorLogging{
  // 使用 context.actorOf 实例化 English2Chinese 和 English2Cat，便可以在它们之间形成层次关系。
  // 这点通过它们的 actor 地址得到证实
  val english2chinese = context.actorOf(Props[English2Chinese2], "English2Chinese")
  val english2cat = context.actorOf(Props[English2Cat2],"English2Cat")

  // 监控的策略，一共2种：
  // OneForOne：只对抛出 Exception 的子 Actor 执行相应动作
  // AllForOne：如果有子 Actor 抛出 Exception，对所有子 Actor 执行动作。
/*  override val supervisorStrategy = OneForOneStrategy() {
    case _:Exception => {
      // 监控的动作，一共4种：Stop，Resume，Restart，Escalate
      Stop
    }
  }*/

  override val supervisorStrategy = AllForOneStrategy() {
    case _:Exception => Stop
  }

  override def receive: Receive = {
    case eng1:String => {
      english2chinese ! eng1
      english2cat ! eng1
    }
  }

}

class English2Chinese2 extends Actor with ActorLogging {
  override def receive: Receive = {
    case english:String => {
      println("English2Chineser don't understand")
    }
  }
}

class English2Cat2 extends Actor with ActorLogging {
  override def receive: Receive = {
    case english:String => {
      throw new Exception("Exception in English2Cat1")
    }
  }
}

/**
 * 运行这段代码，我们得到结果可以看出:
 * 如果是 OneForOne策略：
 *  第一轮 English2Cat1 抛出了 Exception，English2Chinese1 正常工作；
 *  第二轮，English2Cat1 已经死了，English2Chinese1 还在正常工作。
 * 如果是 OneForOne策略：
 *  第一轮 English2Cat1 抛出了 Exception, English2Chinese1 正常工作；
 *  第二轮 English2Cat1 已经死了，English2Chinese1 也已经死亡了。
 *  说明监控策略已经将 MasterActor 的所有子 Actor 停止了。
 */
object Main2 {
  def main(args: Array[String]): Unit = {
    val sys = ActorSystem("system")
    val master = sys.actorOf(Props[MasterActor2], "Master")
    master ! "hello world"
    Thread.sleep(1000)
    master ! "Hello, world"
  }
}
