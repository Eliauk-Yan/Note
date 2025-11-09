## seata低层工作原理

基于二阶提交协议，保证每个分支事务同时成功或失败
![img.png](images/cloud-70-01.png)
![img.png](images/cloud-70-02.png)

### 一阶段
每一份RM在一阶段都要完成各自的本地事务，  
对分支事务的sql操作，保存sql执行的前后数据，存为前后镜像到undo_log表中

## 二阶段
是否所有RM都执行成功，都成功，二阶段所有分支提交。  

任意一个RM执行失败，二阶段则回滚所有分支。 
![img.png](images/cloud-70-03.png)