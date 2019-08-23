package com.stillcoolme.local

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * @author: stillcoolme
  * @date: 2019/8/22 20:15
  * @description:
  *
  **/
class TeacherActor extends Actor{

  override def receive: Receive = {
    case "1+1等于多少?" => {
      // originalSender就是发送给我消息的人，现在获取到他，把消息发送回去
      val originalSender = sender;
      sender ! "1+1等于2"
    }
    case "历史上规模最大的众筹行动是什么？" => {
      val originalSender = sender;
      sender ! "历史上规模最大的众筹行动是 +1s"
    }
    case "腾讯第一网红是谁？" => {
      val originalSender = sender;//
      sender ! "腾讯第一网红是\"我去\""
    }
    case _ => {
      val originalSender = sender;//
      sender ! "这个问题，老师也得查查书"
    }
  }
}

object TeacherService extends App{
  // 读入配置
  val config = ConfigFactory
    .parseResources("lietal.conf")
    .getConfig("RemoteServerSideActor")
  // 使用配置，建立 Actor 模型系统 ActorSystem。
  // ActorSystem 是访问 Actor 模型系统的接口，类似于 Spark 的 SparkContext。
  val system = ActorSystem("TeacherService", config)
  //创建TearcherActor，返回一个引用
  //这里，我们并不需要使用这个引用
  val actor = system.actorOf(Props[TeacherActor], "TeacherActor")
  println(actor.toString())
}
