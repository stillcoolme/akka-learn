package com.stillcoolme.rpc.simple

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * @author: stillcoolme
 * @date: 2019/8/25 15:15
 * @description:
 *
 **/
class Master extends Actor{
  println("constructor invoked")

  override def preStart(): Unit = {
    println("master preStart invoked")
  }

  override def receive: Receive = {
    case "connect" => {
      println("master: a client connected")
      //返回信息给Worker
      sender ! "reply"
    }
    case "hello" => {
      println("master: hello")
    }
  }
}

//伴生对象
object Master {
  def main(args: Array[String]) {
//    val host = args(0)
//    val port = args(1).toInt
    val host = "127.0.0.1"
    val port = 5000
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
         |""".stripMargin
      val config = ConfigFactory.parseString(configStr)
      val actorSystem = ActorSystem("MasterSystem", config)
      val master = actorSystem.actorOf(Props[Master], "Master")
  }
}
