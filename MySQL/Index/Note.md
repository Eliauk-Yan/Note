# MySQL 索引

Author：Shijie Yan

---

## 1. 概述

### 1.1 介绍

​	帮助MySQL高效获取数据的数据结构。

### 1.2 优缺点

|                          Advantage                          |                         DIsadvantage                         |
| :---------------------------------------------------------: | :----------------------------------------------------------: |
|               提高数据检索的效率，降低IO成本                |                       索引列也要占空间                       |
| 通过索引列对数据进行排序，降低数据排序的成本，降低CPU的消耗 | 提高查询效率的同时，降低了更新表的速度，对表进行INSERT、UPDATE、DELETE时效率降低。 |

## 2. 索引结构

> MySQL 的索引是在存储引擎实现的，不同的存储引擎有不同的结构，主要包含以下几种。

### 2.1 分类

|    Index Structure    |                         Description                          |
| :-------------------: | :----------------------------------------------------------: |
|   **B+Tree Index**    |        **最常见的索引类型，大部分引擎都支持B+树索引**        |
|      Hash Index       | 底层数据结构是用哈希表实现的，只有精确匹配索引列的查询才有效，不支持范围查询 |
|  R-Tree（空间索引）   | 空间索引是MyISAM引擎的一个特殊索引类型，主要用于地理空间数据类型，通常使用较少 |
| Full-text（全文索引） |      是一种通过建立倒排序，快速匹配文档的方式。类似于ES      |

### 2.2 支持情况

> 如果没有特别指明，默认都是B+树结构组织的索引。

|    Index     |      InnoDB       | MyISAM | Memory |
| :----------: | :---------------: | :----: | :----: |
| B+Tree Index |         ✔️         |   ✔️    |   ✔️    |
|  Hash Index  |         ❌         |   ❌    |   ✔️    |
| R-Tree Index |         ❌         |   ✔️    |   ❌    |
| Full-text I  | **5.6版本后支持** |   ✔️    |   ❌    |

### 2.3 数据结构

#### 2.3.1 B+树 索引

1. Binary Tree Or Red-Black Tree

![image-20251209220848961](images/image-20251209220848961.png)

- Disadvantages of Binary trees：顺序插入时会形成一个链表，查询性能大大降低。大量数据的情况，层级较深，检索速度慢。
- Disavantages of Red-Black trees：大量数据的情况，层级较深，检索速度慢。

2. B-Tree

![image-20251209220918003](images/image-20251209220918003.png)

3. B+Tree

![image-20251209221017340](images/image-20251209221017340.png)

**Difference compared to B-trees:**

- 所有数据都会出现在叶子节点
- 叶子节点形成一个单向链表

4. MySQL B+Tree

> 对B+Tree进行了优化，增加了一个指向相邻节点的链表指针，形成了带有顺序的指针B+Tree，提高了区间访问的性能。

![image-20251209221848401](images/image-20251209221848401.png)

#### 2.3.2 Hash Index

![image-20251209222304506](images/image-20251209222304506.png)

1. Characteristics
   - Hash Index 只能用于对等比较 ( = , in) ，不支持范围查询 ( between , > , < , ...)
   - 无法利用索引完成排序操作
   - 查询效率高，通常只需要一次检索就可以，效率通常要高于B+Tree
2. Sporrt status

​	在MySQL中，支持Hash索引的是Memory引擎，**但是**InnoDB中具有自适应Hash功能，hash索引是存储引擎根据B+Tree索引在指定条件下自动构建的。

#### 2.3.3 Thinking

1. 为什么InnoDB存储引擎选择使用B+tree索引结构？
   - 相对于二叉树，层级更少，搜索效率高。
   - 对于B-Tree，无论是叶子节点还是非叶子节点，都会保存数据，这样导致一页中存储的键值减少，要保存同样大小的数据，只能增加树的高度，导致性能降低。
   - 对于Hash索引，B+Tree支持范围匹配以及排序操作。

## 3. 索引分类

### 3.1 介绍

| Category |                     Description                      |    Characteristics     | keyword  |
| :------: | :--------------------------------------------------: | :--------------------: | :------: |
| 主键索引 |                针对表中主键创建的索引                | 默认自动创建只能有一个 | PRIMARY  |
| 唯一索引 |           避免同一个表中的某些数据列的重复           |       可以有多个       |  UNIQUE  |
| 常规索引 |                   快速定位特定数据                   |       可以有多个       |          |
| 全文索引 | 全文索引查找的是文本中的关键词，而不是比较索引中的值 |       可以有多个       | FULLTEXT |

​	在InnoDB存储引擎中，根据索引的存储形式，又可以分为以下两种。

|           Category           |                        Description                         |    Characteristic    |
| :--------------------------: | :--------------------------------------------------------: | :------------------: |
| 聚集索引（Clusterexd Index） | 将数据存储与索引放到了一块，索引结构的叶子节点保存了行数据 | 必须有，而且只有一个 |
| 二级索引（Secondary Index）  | 将数据与索引分开存储，索引结构的叶子节点关联的是对于的主键 |     可以存在多个     |

聚集索引的选取规则：

 - 如果存在主键，主键索引就是聚集索引。
 - 如果不存在主键，将使用第一个唯一索引（UNIQUE）作为聚集索引。
 - 如果表没有主键，或没有合适的唯一索引，则InnoDB会自动生成一个rowid作为隐藏的聚集索引。

### 3.2 举例

![image-20251209224832945](images/image-20251209224832945.png)

![image-20251209224901635](images/image-20251209224901635.png)

### 3.3 思考题

<img src="images/image-20251209225132002.png" alt="image-20251209225132002" style="zoom:33%;" />

1. 以上SQL语句，那个执行效率高，为什么。
   - 第一条高，因为第二条还要进行回表查询，然后又根据id又查了一遍。（原理见3.2）

2. InnoDB主键索引的B+Tree高度为多高呢？
   - 假设一行数据大小为1k，一页中可以存储16行这样的数据。InnoDB的指针占用6个字节的空间，主键即使为bigint，占用字节数为8。

<img src="images/image-20251209225816697.png" alt="image-20251209225816697" style="zoom: 33%;" /gg

## 4. 索引语法

```sql
# region 数据初始化
CREATE TABLE tb_user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name        VARCHAR(64),
    phone       VARCHAR(64),
    email       VARCHAR(128),
    profession  VARCHAR(128),
    age         INT,
    gender      INT,
    status      INT,
    create_time DATETIME DEFAULT NOW()
) COMMENT '用户表';

# 查看当前表的索引
show index from tb_user;

# 为name字段创建普通索引
create index idx_user_name on tb_user(name);

# 为phone字段创建唯一索引
create unique index idx_user_phone on tb_user(phone);

# 为profession ,age, status 创建联合索引
create index idx_user_pro_age_sta on tb_user(profession, age, status);

# 为email创建索引提升查询效率
create index idx_user_email on tb_user(email);

# 删除索引
drop index idx_user_email on tb_user;
```

![image-20251211215815198](images/image-20251211215815198.png)

## 5. SQL 性能分析

### 5.1 SQL执行频率

> 通过`show [session | global] status`命令可以提供服务器状态信息。通过如下指令，可以查看当前数据库的`INSERT、UPDATE、DELETE、SELECT`的访问频次

```sql
# 查看 SQL执行频率
show global status like 'Com_______';
```

<img src="images/image-20251211215956480.png" alt="image-20251211215956480" style="zoom:33%;" />

### 5.2 慢查询日志

> 记录所有执行时间超过指定参数（long_query_time，单位：秒，默认10秒）的所有SQL语句的日志，MySQL的慢查询日志默认没有开启，需要在MySQL的配置文件（`/etc/my.cnf`）中配置如下信息。

```sql
# 查看 慢查询日志开启状态
show variables like 'slow_query_log';  # OFF
```

```shell
# 开启MySQL慢查询日志开关
slow_query_log=1

# 设置慢查询日志时间为2秒，SQL语句执行时间超过2秒，就会视为慢查询
long_query_time=2

# 修改完配置文件,重启服务
systemctl restart mysqld
```

```sql
# 查看 慢查询日志开启状态
show variables like 'slow_query_log';  # ON
```

```shell
# 打开慢查询日志会在/var/lib/mysql下创建localhost-slow.log（初始状态）超过配置的2秒会记录日志
/usr/sbin/mysqld, Version: 8.4.7 (MySQL Community Server - GPL). started with:
Tcp port: 3306  Unix socket: /var/lib/mysql/mysql.sock
Time                 Id Command    Argument
```

```shell
# 生成日志举例
# Time: 181230 17:50:35
# User@Host: root[root] @ localhost [127.0.0.1]
# Query_time: 136.500000  Lock_time: 0.000000 Rows_sent: 1  Rows_examined: 0
SET timestamp=1314697835;
SELECT BENCHMARK(100000000, PASSWORD('newpwd'));
```

### 5.3 Profile详情

> show profiles 能够在做 sql优化时帮助我们了解时间都耗费到哪里去了。通过have_profiling参数，能够看到当前MySQL是否支持profile操作。

```sql
# 查看 当前数据库书否支持是否支持profile操作
select @@have_profiling; # YES
```

```sql
# 查看 是否开启profile操作
select @@profiling; # 0（没有开启）
```

```sql
# 设置为1打开profile
set profiling = 1;
```

```sql
# 测试指令
select *
from tb_user
where id = 1;
# 查看 profile（指令耗时情况）
show profiles; 
# 在各个阶段的耗时情况
show profile cpu for query 221;
```

### 5.4 Explain执行计划

> EXPLAIN或者DESC命令获取MySQL如何执行SELECT语句的信息，包括在SELECT语句执行过程中表如何连接和连接的顺序

```sql
explain select * from tb_user where id = 1;
```

![image-20251215155045491](images/image-20251215155045491.png)

1. 各字段含义
   - id：select查询的序列号，表示查询中执行select子句或者是操作表的顺序（id相同，执行顺序从上到下；不同，值越大越靠前）

```sql
explain select
    tb_user.id,
    tb_user.name,
    tb_course.name as course_name
from tb_user
left join tb_student_course on tb_user.id = tb_student_course.student_id
left join tb_course on tb_student_course.course_id = tb_course.id
```

![image-20251215162324925](images/image-20251215162324925.png)

## 6. 索引使用规则

### 6.1 验证索引

```sql
select * from tb_sku where sku_code = 'SKU00979887';
```

![image-20251223161144037](images/image-20251223161144037.png)

```sql
# 创建索引
create index idx_sku_code on tb_sku(sku_code);
# 查看索引
show index from tb_sku;
# 再次验证
select * from tb_sku where sku_code = 'SKU00979887';
```

![image-20251223161258076](images/image-20251223161258076.png)

### 6.2 最左前缀法则

> 如果索引了多列（联合索引），要遵循最左前缀法则。最左前缀法则指的是**查询从索引的最左列开始**，并且**不跳过索引中的列**。

```sql
explain select * from tb_user where profession = 'CEO' and age = 45 and status = 1;
```

![image-20251223161945481](images/image-20251223161945481.png)

```sql
explain select * from tb_user where profession = 'CEO' and age = 45;
```

![image-20251223162026728](images/image-20251223162026728.png)

```sql
explain select * from tb_user where profession = 'CEO';
```

![image-20251223162119273](images/image-20251223162119273.png)

```sql
explain select * from tb_user where age = 45 and status = 1;
```

![image-20251223162227977](images/image-20251223162227977.png)

> **从好到坏排序：**`system` > `const` > `eq_ref` > `ref` > `range` > `index` > `ALL`

```sql
explain select * from tb_user where profession = 'CEO' and status = 1;
```

![image-20251223162944289](images/image-20251223162944289.png)

```sql
explain select * from tb_user where status = 1 and age = 45 and profession = 'CEO';
```

![image-20251223163755044](images/image-20251223163755044.png)

### 6.3 范围查询

> 联合索引中，出现了范围查询(>,<)，范围查询右侧的列索引失效。

```sql
explain select * from tb_user where profession = 'CEO' and age > 30 and status = 1;
```

![image-20251223164615950](images/image-20251223164615950.png)

```sql
explain select * from tb_user where profession = 'CEO' and age >= 30 and status = 1;
```

![image-20251223164844927](images/image-20251223164844927.png)

### 6.4 索引列运算

```sql
explain select * from tb_user where phone = '13800000037';
```

![image-20251223165900325](images/image-20251223165900325.png)

```sql
explain select * from tb_user where substring(phone, 10, 2) = '37';
```

![image-20251223165922119](images/image-20251223165922119.png)

### 6.5 字符串不加引号

> 查询时字符串不加引号索引失效。

```sql
explain select * from tb_user where phone = 13800000037;
```

![image-20251223170342586](images/image-20251223170342586.png)

### 6.6 头部模糊匹配

> 仅尾部模糊匹配索引不会失效，如果头部模糊匹配索引会失效。

```sql
explain select * from tb_user where profession like 'C%';
```

![image-20251223174211974](images/image-20251223174211974.png)

```sql
explain select * from tb_user where profession like '%O';
```

![image-20251223174234647](images/image-20251223174234647.png)

```sql
explain select * from tb_user where profession like '%E%';
```

![image-20251223174405260](images/image-20251223174405260.png)
