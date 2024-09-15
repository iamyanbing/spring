# spring boot
学习 && 实战

服务名|端口号
--- | ---
boot|9999
serverpush|9998
stomp|9997
security|9996
online-taxi|9995
cloud-gateway|9994


online-taxi 服务启动需要 redis、zookeeper、nacos、sentinel等服务

cloud-gateway 会转发请求至 boot 服务或者 security 服务