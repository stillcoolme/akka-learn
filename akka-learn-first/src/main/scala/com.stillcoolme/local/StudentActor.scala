package com.stillcoolme.local

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * @author: stillcoolme
  * @date: 2019/8/22 19:56
  * @description:
  *
  **/
class StudentActor extends Actor{
  // 远程Actor的路径，通过该路径能够获取远程Actor的一个引用
  val path = "akka.tcp://TeacherService@127.0.0.1:4999/user/teacherActor"
  // 获取到远程Actor的一个引用，通过该引用可以向远程Actor发送消息
  val remoteServiceRef = context.actorSelection(path)

  override def receive: Receive = {
    case res:String => {
      println(res)
    }
    case time:Long => {
      println("get up ing...")
      remoteServiceRef ! "历史上规模最大的众筹行动是什么？";
    }
  }
}

object StudentSimulator extends App{
  //读入客户端配置
  val config = ConfigFactory
    .parseResources("lietal.conf")
    .getConfig("RemoteClientSideActor")

  //使用配置，建立 ActorSystem
  val actorSystem = ActorSystem("StudentClient", config)
  // 获得 StudentActor 的一个引用。
  // 在程序中 Actor 不能直接被访问，所有操作都必须通过 ActorRef 引用。
  val studentActor = actorSystem.actorOf(Props[StudentActor])
  while(true) {
    studentActor ! 7.toLong
    Thread.sleep(5000)
  }

}
