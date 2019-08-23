package com.stillcoolme.remote

import akka.actor.{Actor, ActorLogging}

/**
  * @author: stillcoolme
  * @date: 2019/8/23 8:23
  * @description:
  *
  **/
class TeacherActor extends Actor with ActorLogging{
  var countAnswer = 0
  override def receive: Receive = {
    case "1+1等于多少?" => {
      // originalSender就是发送给我消息的人，现在获取到他，把消息发送回去
      val originalSender = sender;
      sender ! "1+1等于2"
      countAnswer += 1;
    }
    case "历史上规模最大的众筹行动是什么？" => {
      val originalSender = sender;
      sender ! "历史上规模最大的众筹行动是 乐视贾总"
      countAnswer += 1;
    }
    case "腾讯第一网红是谁？" => {
      val originalSender = sender;
      sender ! "腾讯第一网红是\"我去\""
      countAnswer += 1;
    }
    case _ => {
      log.info("it receives aa")
      val originalSender = sender;
      sender ! "这个问题，老师也得查查书"
      countAnswer += 1;
    }

  }
}
