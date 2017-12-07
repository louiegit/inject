#### inject (louie-ioc)

这是用来理解最基本IOC原理的:

(1)首先扫描包初始化上下文

(2)把@Bean注解对应Bean和基于Java反射对应的Field,存储相互依赖

(3)循环依赖,将Instance用反射setField进入parentBean

(4)在AppTest中根据IOC容器获取Bean,即可实现最基本的IOC

喜欢一起聊技术的coder可以mail我^_^

Gmail:luolouiegit@gmail.com

QQ :510629251@qq.com

# Quick Start

ApplicationContext context = new ApplicationContext("classpath");

context.init();

TargetBean bean =(TargetBean)context.getBeans().get("bean");

bean.callMethod();

