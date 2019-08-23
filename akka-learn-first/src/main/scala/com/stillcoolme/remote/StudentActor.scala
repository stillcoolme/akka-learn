package com.stillcoolme.remote

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * @author: stillcoolme
  * @date: 2019/8/23 8:18
  * @description:
  *
  **/
class StudentActor (remoteTeacher:ActorRef) extends Actor with ActorLogging{

  // 获取到远程teacherActor的一个引用，通过该引用可以向远程Actor发送消息
  val remoteServerRef = remoteTeacher

  var dayInSchool = 0

  override def receive: Receive = {
    case res:String => {
      println ("老师的答案是:"+res)
    }
    case time:Long => {
      // originalSender就是发送给我消息的人，现在获取到他，把消息发送回去
      val originalSender = sender
      sender ! "关闭闹钟"

      dayInSchool += 1
      log.info("dayInSchool is %d".format(dayInSchool))

      // 向老师发送问题
      remoteServerRef ! "历史上规模最大的众筹行动是什么？"
    }
  }
}
