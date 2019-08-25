package com.stillcoolme.rpc.simple

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * @author: stillcoolme
 * @date: 2019/8/25 15:23
 * @description:
 *
 **/
class Worker(val masterHost: String, val masterPort: Int) extends Actor {

  var master : ActorSelection = _
  //建立连接
  override def preStart(): Unit = {
    // 在master启动时会打印下面的那个协议, 可以先用这个做一个标志, 连接哪个master
    // 需要有/user, Master要和master那边创建的名字保持一致
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    master ! "connect"
  }

  override def receive: Receive = {
    case "reply" => {
      println("client: get a reply form master")
    }
  }

}

object Worker {
  def main(args: Array[String]) {
/*    val host = args(0)
    val port = args(1).toInt
    val masterHost = args(2)
    val masterPort = args(3).toInt*/
    val host = "127.0.0.1"
    val port = 4999
    val masterHost = "127.0.0.1"
    val masterPort = 5000

    // 准备配置
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem("WorkerSystem", config)
    val worker = actorSystem.actorOf(Props(new Worker(masterHost, masterPort)), "Worker")
  }


}