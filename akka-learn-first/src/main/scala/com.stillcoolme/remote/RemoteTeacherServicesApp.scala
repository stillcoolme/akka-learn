package com.stillcoolme.remote

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * @author: stillcoolme
  * @date: 2019/8/23 8:35
  * @description:
  *
  **/
object RemoteTeacherServicesApp extends App{
  //读入配置
  val config = ConfigFactory
    .parseResources("example.conf")
    .getConfig("RemoteServerSideActor")

  val actorSystem = ActorSystem("TeacherService", config)
  //创建TearcherActor，返回一个引用。这里，我们并不需要使用这个引用
  actorSystem.actorOf(Props[TeacherActor], "teacherActor")
}
