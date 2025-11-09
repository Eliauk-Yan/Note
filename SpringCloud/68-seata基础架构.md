## seata基础架构
![img.png](images/cloud-68-01.png)

### TC transaction coordinator
维护全局/分支事务的状态，驱动全局事务提交或回滚。  
负责接收，注册事务

### TM transaction manager
开始全局事务，负责全局事务的提交和回滚。

### RM resource manager
分支事务的资源管理器，负责分支事务的提交和回滚。