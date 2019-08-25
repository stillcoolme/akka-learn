package com.stillcoolme.rpc

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.FiniteDuration

/**
 * @author: stillcoolme
 * @date: 2019/8/25 16:33
 * @description:
 *
 **/
class Worker(val masterHost:String, val masterPort:Int, val memory:Int, val cores:Int) extends Actor{

  var master : ActorSelection = _
  val workerId = UUID.randomUUID().toString
  val HEART_INTERVAL = 10000

  //在preStart里面建立连接
  override def preStart(): Unit = {
    // user必须得写，Master为主Master的名字
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    // 向Master发送注册信息
    master ! RegisterWorker(workerId, memory, cores)
  }

  override def receive: Receive = {
    case RegisteredWorker(masterUrl) => {
      println("client: i get masterUrl: " + masterUrl)
      import context.dispatcher
      //启动定时器发送心跳
      context.system.scheduler.schedule(
        new FiniteDuration(0, java.util.concurrent.TimeUnit.MILLISECONDS),
        new FiniteDuration(HEART_INTERVAL, java.util.concurrent.TimeUnit.MILLISECONDS),
        self, SendHeartbeat)
    }
    case SendHeartbeat => {
      println("client: send heartbeat to master")
      master ! Heartbeat(workerId)
    }
  }
}

object Worker {

  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 9998
    val masterHost = "127.0.0.1"
    val masterPort = 9999
    val memory = 512
    val cores = 3
    //准备配置
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    //ActorSystem老大，辅助创建和监控下面的Actor，它是单例的
    val actorSystem = ActorSystem("WorkerSystem", config)
    actorSystem.actorOf(Props(new Worker(masterHost,masterPort,memory,cores)), "Worker")
  }
}