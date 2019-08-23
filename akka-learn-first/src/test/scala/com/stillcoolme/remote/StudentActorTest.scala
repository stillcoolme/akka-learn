package com.stillcoolme.remote

import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

/**
  * @author: stillcoolme
  * @date: 2019/8/23 9:07
  * @description:
  * Akka-testkit 的主要工具包括,
   *  1) testProbe 用于测试 Actor 回应和发送消息，
   *  2) testActor 用于简便情况下测试 Actor 回应消息，
   *  3) testActorRef 用于测试 Actor 内部状态的改变。
  *
  **/
@RunWith(classOf[JUnitRunner])
class StudentActorTest
  extends TestKit(ActorSystem("SummerSchool",
    ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")))
    with ImplicitSender  // 加这个让测试类实现特质ImplicitSender，用来第二个测试，把testActor 设置为消息发出Actor
    with WordSpecLike
    with BeforeAndAfterAll{

  val teacherActor = system.actorOf(Props[TeacherActor])
  //test its responses
  "The countAnswer " must {
    "response a correct answer order" in {
      val studentActor = system.actorOf(Props(new StudentActor(teacherActor)))
      val testProb = new TestProbe(system)
      // 首先将 testProbe 给被测 Actor 发送消息
      testProb.send(studentActor, 7.toLong)
      testProb.send(studentActor, 7.toLong)
      // 再看 testProbe 是否接受到期望的回应消息。
      testProb.expectMsg("关闭闹钟")
      testProb.expectMsg("关闭闹钟")
    }
  }

  //test its response with simple testActor
  //testActor 最大的缺失是只能接受被测 Actor 发来的一个回应消息。
  "StudentActor" must {
    "response correctly" in {
      val studentActorRef = system.actorOf(Props(new StudentActor(teacherActor)))
      // testActor 发出 7.toLong 消息给 studentActor
      studentActorRef ! 7.toLong
      // testActor 应该收到 studentActor 回应消息 "关闭闹钟"
      expectMsg("关闭闹钟")
    }
  }

  // 内部状态测试
  "StudentActor" must {
    "increase the DayInSchool" in {
      // 建立 Actor 的 TestActorRef
      val testActorRef = TestActorRef(new StudentActor(teacherActor))
      testActorRef ! 7.toLong
      // testActorRef.underlyingActor 探测 dayInSchool 变量是否符合预期
      assert(testActorRef.underlyingActor.dayInSchool == 1)
    }
  }

  // 发出消息的测试
  "StudentActor" must {
    val questionReceiver = TestProbe()
    // 设置 questionReceiver 为 studentActorRef 发送消息的目标
    val studentActorRef = system.actorOf(Props(new StudentActor(questionReceiver.ref)))
    "send a question after waking up" in {
      // 给学生发送 7点了的信息，然后学生起床会给老师（现在是questionReceiver）发送问题
      studentActorRef ! 7.toLong
      studentActorRef ! 7.toLong
      // 模拟老师的 testProbe 是否接收到预期问题
      questionReceiver.expectMsg("历史上规模最大的众筹行动是什么？")
      questionReceiver.expectMsg("历史上规模最大的众筹行动是什么？")
    }

  }



}