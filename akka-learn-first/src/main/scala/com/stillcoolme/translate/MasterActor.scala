package com.stillcoolme.translate

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorLogging, ActorSystem, OneForOneStrategy, Props}


/**
 * @author: stillcoolme
 * @date: 2019/8/23 19:57
 * @description:
 * 我们需要实现一个翻译模块，其功能是输入中文输出多国语言。
 * 我们可以让一个 Master Actor 负责接收外界输入，多个 Worker Actor 负责将输入翻译成特定语言，
 * Master Actor 和 Worker Actor 之间是上下级层次关系。
 **/
class MasterActor extends Actor with ActorLogging{
  // 使用 context.actorOf 实例化 English2Chinese 和 English2Cat，便可以在它们之间形成层次关系。
  // 这点通过它们的 actor 地址得到证实
  val english2chinese = context.actorOf(Props[English2Chinese], "English2Chinese")
  val english2cat = context.actorOf(Props[English2Cat],"English2Cat")

  override def receive: Receive = {
    case eng1:String => {
      english2chinese ! eng1
      english2cat ! eng1
    }
  }

}

class English2Chinese extends Actor with ActorLogging {
  override def receive: Receive = {
    case english:String => {
      println("English2Chineser don't understand")
    }
  }
}

class English2Cat extends Actor with ActorLogging {
  override def receive: Receive = {
    case english:String => {
      println("喵 喵 喵 ！")
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val sys = ActorSystem("system")
    val master = sys.actorOf(Props[MasterActor], "Master")
    master ! "hello world"
  }
}
