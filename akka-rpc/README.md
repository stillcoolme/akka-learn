## akka-rpc模块
目前大多数的分布式架构底层通信都是通过RPC实现的，比如Hadoop的RPC通信框架。

但是Hadoop在设计之初就是为了运行长达数小时的批量而设计的，在某些极端的情况下，任务提交的延迟很高。

所有Hadoop的RPC显得有些笨重。

Spark1.6前的RPC是通过Akka类库实现的。

Akka基于Actor并发模型实现，Akka具有高可靠、高性能、可扩展等特点，使用Akka可以轻松实现分布式RPC功能。

本模块就基于akka实现RPC框架

* hreatbeat包
模拟Spark的rpc实现
1. Worker启动后，建立和Master的连接（preStart方法里面建立连接），将Worker的信息通过case class封装起来发送给Master
2. Master接受到Worker的注册消息后将Worker的信息保存起来。再向Worker反馈注册成功。
3. Worker定期向Master发送心跳，Master定期清理超时的Worker。
