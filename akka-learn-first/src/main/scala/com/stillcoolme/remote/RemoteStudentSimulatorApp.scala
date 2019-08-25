package com.stillcoolme.remote

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

/**
  * @author: stillcoolme
  * @date: 2019/8/23 8:26
  * @description:
  *
  **/
object RemoteStudentSimulatorApp extends App{
  //读入客户端配置
  val config = ConfigFactory
    .parseResources("example.conf")
    .getConfig("RemoteClientSideActor")
  //使用配置，建立 ActorSystem
  val actorSystem = ActorSystem("StudentClient", config)

  implicit val resolveTimeout = Timeout(new FiniteDuration(5, java.util.concurrent.TimeUnit.SECONDS))
  // 远程Actor的路径，通过该路径能够获取到远程Actor的一个引用
  val teacherActor = Await.result(actorSystem
    .actorSelection("akka.tcp://TeacherService@127.0.0.1:4999/user/teacherActor")
    .resolveOne(), resolveTimeout.duration)
  //获得 StudentActor 的一个引用。在程序中 Actor 不能直接被访问。所有操作都必须通过 ActorRef 引用。
  val studentActor = actorSystem.actorOf(Props(new StudentActor(teacherActor)))
  while (true) {
    studentActor ! 7.toLong
    Thread.sleep(5000)
  }

}
