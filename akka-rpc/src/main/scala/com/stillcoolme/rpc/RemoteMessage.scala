package com.stillcoolme.rpc

/**
 * @author: stillcoolme
 * @date: 2019/8/25 15:57
 * @description:
 *
 **/
// 进程之间通信，对象需要实现对象化
trait RemoteMessage extends Serializable

//Worker -> Master

case class RegisterWorker(id: String, memory: Int, core: Int) extends RemoteMessage

case class Heartbeat(id: String) extends RemoteMessage

//Master -> Worker
case class RegisteredWorker(masterUrl: String) extends RemoteMessage

//Worker -> 自己发送self
case object SendHeartbeat

//Master -> self
case object CheckTimeOutWorker