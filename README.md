## akka-learn-first模块
akka入门项目
* remote包下 实现了经典的 学生及老师的处理逻辑，参考: [Akka 使用系列](https://mp.weixin.qq.com/s/nYDi1BWB1EcwNBUcz1bDYA)
    * 相当于实现了一个简单的rpc
    * 先执行 RemoteStudentSimulatorApp，再执行 RemoteTeacherServicesApp
* translate包下 实现层次结构的actor，参考：[层次结构和容错机制](https://mp.weixin.qq.com/s/2eM_o0PernPxV87uV0nI9A)

## akka-rpc模块
模拟rpc实现