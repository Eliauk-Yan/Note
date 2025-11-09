# Spring

**作者：YanShijie**

---

## 1. AOP

### 1.1 AOP 概念

  AOP（Aspect-Oriented Programming）面向切面编程

  Spring AOP底层通过**动态代理机制**实现对目标方法的编程，动态代理是目前面向方法编程最主流的实现技术。

### 1.2 应用场景

	1. 目标方法运行耗时统计（不修改源代码的情况下）

```java
@Aspect
@Component
public class BookAdvice {

    @Around("execution(* com.ysj.*.*.select(..))")
    public Object method(ProceedingJoinPoint joinPoint) throws Throwable { // 代表了目标方法
        // 1. 目标方法运行开始时间
        long start = System.nanoTime(); // 纳秒
        // 2. 执行目标方法
        Object result = joinPoint.proceed();
        // 3. 目标方法运行结束时间
        long end = System.nanoTime();
        // 4. 运行耗时
        System.out.println("目标方法" + joinPoint.toShortString() + "耗时" + (end - start));
        return result;
    }

}
```

	1. 目标方法添加事务管理
	1. 给目标方法添加访问权限控制
	1. 对目标方法进行读写分离save、update、delete操作一个数据源，select操作另一个数据源。
	1. ......

