package com.stillcoolme.remote

import akka.actor.{ActorSystem, Props}

/**
  * @author: stillcoolme
  * @date: 2019/8/23 8:39
  * @description:
  *
  **/
object RemoteApp {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("SummerSchool")
    val teacher = actorSystem.actorOf(Props[TeacherActor], "teacher")
    val student = actorSystem.actorOf(Props(new StudentActor(teacher)), "student")

    student ! 7.toLong
    Thread.sleep(1000)

    actorSystem.shutdown()
  }

}
