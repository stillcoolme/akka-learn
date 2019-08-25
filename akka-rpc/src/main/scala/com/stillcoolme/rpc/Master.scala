package com.stillcoolme.rpc

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration

/**
 * @author: stillcoolme
 * @date: 2019/8/25 14:53
 * @description:
 *
 **/
class Master(val host:String, val port:Int) extends Actor{

  val idToWorker = new mutable.HashMap[String, WorkerInfo]()
  val workers = new mutable.HashSet[WorkerInfo]()
  //超时检查间隔
  val CHECK_INTERVAL = 15000

  override def preStart(): Unit = { //构造器之后，receive方法之前
    //导入隐式转换
    import context.dispatcher
    // 启动定时任务不断检查worker是否存活 CheckTimeOutWorker
    context.system.scheduler.schedule(
      new FiniteDuration(0, java.util.concurrent.TimeUnit.MILLISECONDS),
      new FiniteDuration(CHECK_INTERVAL, java.util.concurrent.TimeUnit.MILLISECONDS),
      self, CheckTimeOutWorker)
  }

  override def receive: Receive = {  //偏函数
    case RegisterWorker(id, memory, cores) => {
      println("master: Worker" + id + " register")
      //判断一下，是不是已经注册过
      if (!idToWorker.contains(id)){
        //把Worker的信息封装起来保存在内存当中
        val workerInfo = new WorkerInfo(id, memory, cores)
        idToWorker(id) = workerInfo
        workers += workerInfo
        sender ! RegisteredWorker(s"akka.tcp://MasterSystem@$host:$port/user/Master")
      }
    }
    case Heartbeat(id) => {
      if(idToWorker.contains(id)) {
        val workerInfo = idToWorker(id)
        // 报活
        val currentTime = System.currentTimeMillis()
        workerInfo.lastHeartbeatTime = currentTime
      }
    }

    case CheckTimeOutWorker => {
      val currentTime = System.currentTimeMillis()
      val toRemove = workers.filter(x => currentTime - x.lastHeartbeatTime > CHECK_INTERVAL)
      for (w <- toRemove){
        workers -= w
        idToWorker -= w.id
      }
      println("current worker num : " + workers.size)
    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 9999
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem("MasterSystem", config)
    val master = actorSystem.actorOf(Props(new Master(host, port)), "Master")
  }
}
