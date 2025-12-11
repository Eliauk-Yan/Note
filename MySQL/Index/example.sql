# region 数据初始化
USE study;

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

# endregion 数据初始化

# region 基本语法
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

# endregion 基本语法

# region SQL 性能分析
# 查看 SQL执行频率
show global status like 'Com_______';

# 查看 慢查询日志开启状态
show variables like 'slow_query_log';

# 查看 当前数据库书否支持是否支持profile操作
select @@have_profiling;

# 查看 是否开启profile操作
select @@profiling;

# 设置为1打开profile
set profiling = 1;

# 测试指令
select *
from tb_user
where id = 1;
# 查看 profile（指令耗时情况）
show profiles;

select *
from tb_user
where email = 'liwei@test.com';
# 查看 profile（指令耗时情况）
show profiles;
show profile cpu for query 221;

select count(*)
from tb_user;
# 查看 profile（指令耗时情况）
show profiles;
# 测试指令

# 查看 profile（指令耗时情况）
show profiles;
# endregion SQL 性能分析

