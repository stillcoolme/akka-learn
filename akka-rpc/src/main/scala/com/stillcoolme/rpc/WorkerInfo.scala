package com.stillcoolme.rpc

/**
 * @author: stillcoolme
 * @date: 2019/8/25 16:03
 * @description:
 *
 **/
class WorkerInfo(val id:String, val memory:Int, val cores:Int) {

  //TODO 上一次心跳
  var lastHeartbeatTime: Long = _
}