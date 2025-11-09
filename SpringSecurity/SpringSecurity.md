# SpringSecurity

## 一、简介

> 安全框架

### 1、作用

> 认证：登录...
>
> 授权：对资源进行一种保护、登录前后、VIP...
>
> 安全管理：防攻击、跨站攻击

## 二、快速入门

### 1、准备工作

（1）设置父工程、添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.ysj</groupId>
    <artifactId>SpringSecurity</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.4</version>
    </parent>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
         <!-- Web启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>annotationProcessor</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

（2）添加SpringSeccurity依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## 三、认证

### 1、登录认证流程

![image-20250809153706217](images/image-20250809153706217.png)

### 2、原理分析

> SpringSecurity的原理其实就是一个过滤器，内部包含了提供各种功能的过滤器。（入门案例过滤器举例）

![image-20250809154155122](images/image-20250809154155122.png)

#### （1）过滤器

> **默认配置下15个过滤器、SpringSecurity5有16个默认过滤器。**
>
> **执行顺序就是表格顺序**

| 顺序 | 过滤器类名                                | 作用说明                                                     |
| ---- | ----------------------------------------- | ------------------------------------------------------------ |
| 1    | `DisableEncodeUrlFilter`                  | 禁用对 URL 的 `;jsessionid` 编码                             |
| 2    | `WebAsyncManagerIntegrationFilter`        | 处理 Spring MVC 异步请求上下文集成                           |
| 3    | `SecurityContextHolderFilter`             | 在请求开始时绑定 `SecurityContext`，替代旧的 `SecurityContextPersistenceFilter` |
| 4    | `HeaderWriterFilter`                      | 添加安全相关 HTTP 头（HSTS、X-Frame-Options 等）             |
| 5    | `CsrfFilter`                              | 处理跨站请求伪造（CSRF）防护                                 |
| 6    | `LogoutFilter`                            | 处理 `/logout` 请求，清理认证信息                            |
| 7    | `UsernamePasswordAuthenticationFilter`    | 处理基于表单的用户名密码登录                                 |
| 8    | `DefaultLoginPageGeneratingFilter`        | 自动生成默认登录页面（未自定义登录页时才启用）               |
| 9    | `DefaultLogoutPageGeneratingFilter`       | 自动生成默认注销页面                                         |
| 10   | `BasicAuthenticationFilter`               | 处理 HTTP Basic 认证                                         |
| 11   | `RequestCacheAwareFilter`                 | 从缓存恢复原始请求（登录成功后重定向）                       |
| 12   | `SecurityContextHolderAwareRequestFilter` | 包装 `HttpServletRequest`，提供基于安全上下文的 API          |
| 13   | `AnonymousAuthenticationFilter`           | 为未认证用户创建匿名身份                                     |
| 14   | `ExceptionTranslationFilter`              | 处理认证/授权异常并重定向或返回错误                          |
| 15   | `AuthorizationFilter`                     | 最终执行访问控制（基于 `AccessDecisionManager`）             |

#### （2）认证流程

![image-20250809160307371](images/image-20250809160307371.png)

![image-20250809160424312](images/image-20250809160424312.png)

### 3、解决问题

#### （1）思路分析

![image-20250809160553597](images/image-20250809160553597.png)

![image-20250809161011731](images/image-20250809161011731.png)

#### （2）添加依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.4</version>
</parent>

<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <jjwt-version>0.12.6</jjwt-version>
    <fastjson2-version>2.0.57</fastjson2-version>
    <mysql-version>9.3.0</mysql-version>
    <mybatis-plus-version>3.5.12</mybatis-plus-version>
    <springdoc-openapi-starter-webmvc-ui-version>2.8.9</springdoc-openapi-starter-webmvc-ui-version>
    <druid-version>1.2.25</druid-version>
</properties>

<dependencies>
    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!-- mysql -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>${mysql-version}</version>
        <scope>runtime</scope>
        <!-- 只在运行程序时需要 MySQL 驱动，编译时不必引入。 -->
    </dependency>
    <!-- mybatis-plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        <version>${mybatis-plus-version}</version>
    </dependency>
    <!-- mybatis-plus分页插件 -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-jsqlparser</artifactId>
        <version>${mybatis-plus-version}</version>
    </dependency>
    <!-- SpringSecurity -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- fastjson2 -->
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2</artifactId>
        <version>${fastjson2-version}</version>
    </dependency>
    <!-- druid连接池 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>${druid-version}</version>
    </dependency>
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>${jjwt-version}</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>${jjwt-version}</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>${jjwt-version}</version>
        <scope>runtime</scope>
    </dependency>
    <!-- validation 数据校验依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <!-- api文档生成工具 -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>${springdoc-openapi-starter-webmvc-ui-version}</version>
    </dependency>
    <!-- lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>annotationProcessor</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
</project>
```

#### （3）配置Redis

```java
package com.ysj.security.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * <p>
 * 作用：
 * 1. 创建并配置 RedisTemplate，替换默认的 JDK 序列化方式
 * 2. 使用 fastjson2 作为 Value 序列化器，提高可读性与性能
 * 3. 保证 key 使用字符串序列化，便于直接查看和调试
 *
 * @Date 2025/08/09 16:36
 * @Author YanShijie
 */
@Configuration
public class RedisConfig {

    /**
     * 自定义 RedisTemplate Bean
     *
     * @param redisConnectionFactory Spring Boot 自动配置的 Redis 连接工厂
     * @return 配置完成的 RedisTemplate
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        // 设置 Redis 连接工厂
        template.setConnectionFactory(redisConnectionFactory);

        // 创建 fastjson2 序列化器（用于序列化 Value）
        // Object.class 表示支持任意类型对象
        FastJson2RedisSerializer<Object> fastJsonRedisSerializer =
                new FastJson2RedisSerializer<>(Object.class);


        // key 和 hashKey 序列化方式：String
        // 使用 StringRedisSerializer 保证 key 是可读字符串（而不是二进制）
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // value 和 hashValue 序列化方式：fastjson2
        // 使用 fastjson2 进行 JSON 序列化，提高可读性与跨语言兼容性
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);

        template.setDefaultSerializer(fastJsonRedisSerializer);

        return template;
    }

}
```

```java
package com.ysj.security.config.redis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 使用 Fastjson2 实现 Redis 序列化/反序列化的工具类
 * <p>
 * 特点：
 * 1. 基于泛型 T，可以序列化/反序列化任意类型对象
 * 2. 使用 Fastjson2 的 WriteClassName / SupportClassForName 保留并解析类型信息
 * 3. 实现 Spring Data Redis 提供的 RedisSerializer 接口，可直接在 RedisTemplate 中使用
 *
 * @param type 目标类型的 Class 对象，用于反序列化时确定对象类型
 * @param <T>  序列化的泛型类型
 */
public record FastJson2RedisSerializer<T>(Class<T> type) implements RedisSerializer<T> {

    /**
     * 序列化方法：Java 对象 -> byte[]
     *
     * @param value 要序列化的 Java 对象
     * @return 序列化后的字节数组，如果对象为 null 则返回空数组
     */
    @Override
    public byte[] serialize(T value) {
        if (value == null) return new byte[0]; // 空对象返回空字节数组，避免 Redis 存储 null
        // WriteClassName 特性：在 JSON 中保留 @type 类型信息，方便反序列化时恢复原始类型
        return JSON.toJSONBytes(value, JSONWriter.Feature.WriteClassName);
    }

    /**
     * 反序列化方法：byte[] -> Java 对象
     *
     * @param bytes 从 Redis 中取出的字节数组
     * @return 反序列化后的 Java 对象，如果字节为空则返回 null
     */
    @Override
    public T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null; // 空字节返回 null
        // SupportClassForName 特性：允许解析 @type 中的类名，恢复原始对象类型
        return JSON.parseObject(bytes, type, JSONReader.Feature.SupportClassForName);
    }
}
```

#### （4）添加响应类

```java
package com.atguigu.gulimall.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 返回结果集
 *
 * @author javadog
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private Boolean status;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    private static <T> ResponseResult<T> response(Integer code, Boolean status, String message, T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setStatus(status);
        responseResult.setMessage(message);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    private static <T> ResponseResult<T> response(Integer code, Boolean status, String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setStatus(status);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 成功返回（无参）
     *
     * @param <T> 泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success() {


        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回（枚举参数）
     *
     * @param httpResponseEnum 枚举参数
     * @param <T>              泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(HttpStatusEnum httpResponseEnum) {


        return response(httpResponseEnum.getCode(), true, httpResponseEnum.getMessage());
    }

    /**
     * 成功返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(Integer code, String message) {


        return response(code, true, message);
    }

    /**
     * 成功返回（返回信息 + 数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(String message, T data) {


        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, data);
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(Integer code, String message, T data) {


        return response(code, true, message, data);
    }

    /**
     * 成功返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(T data) {


        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(String message) {


        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, null);
    }

    /**
     * 失败返回（无参）
     *
     * @param <T> 泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail() {


        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), null);
    }

    /**
     * 失败返回（枚举）
     *
     * @param httpResponseEnum 枚举
     * @param <T>              泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(HttpStatusEnum httpResponseEnum) {


        return response(httpResponseEnum.getCode(), false, httpResponseEnum.getMessage());
    }

    /**
     * 失败返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(Integer code, String message) {


        return response(code, false, message);
    }

    /**
     * 失败返回（返回信息+数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(String message, T data) {


        return response(HttpStatusEnum.ERROR.getCode(), false, message, data);
    }

    /**
     * 失败返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回消息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(Integer code, String message, T data) {


        return response(code, false, message, data);
    }

    /**
     * 失败返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(T data) {


        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), data);
    }

    /**
     * 失败返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(String message) {


        return response(HttpStatusEnum.ERROR.getCode(), false, message, null);
    }
}
```

```java
package com.atguigu.gulimall.common.result;

import lombok.Getter;

/**
 * Http状态返回枚举
 *
 * @author javadog
 **/
@Getter
public enum HttpStatusEnum {
   
   
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 对象创建成功
     */
    CREATED(201, "对象创建成功"),
    /**
     * 请求已经被接受
     */
    ACCEPTED(202, "请求已经被接受"),
    /**
     * 操作已经执行成功，但是没有返回数据
     */
    NO_CONTENT(204, "操作已经执行成功，但是没有返回数据"),
    /**
     * 资源已被移除
     */
    MOVED_PERM(301, "资源已被移除"),
    /**
     * 重定向
     */
    SEE_OTHER(303, "重定向"),
    /**
     * 资源没有被修改
     */
    NOT_MODIFIED(304, "资源没有被修改"),
    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    BAD_REQUEST(400, "参数列表错误（缺少，格式不匹配）"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    /**
     * 访问受限，授权过期
     */
    FORBIDDEN(403, "访问受限，授权过期"),
    /**
     * 资源，服务未找到
     */
    NOT_FOUND(404, "资源，服务未找！"),
    /**
     * 不允许的http方法
     */
    BAD_METHOD(405, "不允许的http方法"),
    /**
     * 资源冲突，或者资源被锁
     */
    CONFLICT(409, "资源冲突，或者资源被锁"),
    /**
     * 不支持的数据，媒体类型
     */
    UNSUPPORTED_TYPE(415, "不支持的数据，媒体类型"),
    /**
     * 系统内部错误
     */
    ERROR(500, "系统内部错误"),
    /**
     * 接口未实现
     */
    NOT_IMPLEMENTED(501, "接口未实现"),
    /**
     * 系统警告消息
     */
    WARN(601,"系统警告消息");

    private final Integer code;
    private final String message;

    HttpStatusEnum(Integer code, String message) {
   
   
        this.code = code;
        this.message = message;
    }
}
```

#### （5）添加JWT

##### **1. SSO介绍**

> **SSO（单点登录）：**是一种认证机制，允许用户在一个系统（或一次登录）中完成身份验证后，就能访问多个相互信任的应用系统，而不需要在每个系统中重复登录。
>
> SSO 就是 **“一次登录，处处通行”**，它的价值在于 **提升用户体验** 和 **增强安全性**，尤其适合**多系统、多业务**的企业或平台。

##### **2. JWT介绍**

> **JWT（JSON Web Token）** 是一种 **基于 JSON 格式** 的 **轻量级、跨语言的令牌标准**（RFC 7519），常用于：
>
> - **身份认证**（Authentication）
> - **信息安全传输**（Information Exchange）
>
> JWT 一般由三部分组成，用 `.` 分隔：
>
> ```
> Header.Payload.Signature
> ```
>
> **Header（头部）**
>
> - 说明 Token 类型和签名算法
>
> - 示例（JSON 格式）：
>
>   ```json
>   {
>     "alg": "HS256",
>     "typ": "JWT"
>   }
>   ```
>
> - Base64URL 编码后变成字符串
>
> **Payload（载荷）**
>
> - 存放实际传递的数据（声明，Claims）
>
> - 分为三类：
>
>   - **Registered Claims**（标准字段，如 `iss` 签发者、`exp` 过期时间）
>   - **Public Claims**（自定义但要注册的）
>   - **Private Claims**（完全自定义）
>
> - 示例：
>
>   ```json
>   {
>     "sub": "1234567890",
>     "name": "John Doe",
>     "admin": true,
>     "exp": 1733700000
>   }
>   ```
>
> - 也是 Base64URL 编码
>
> **Signature（签名）**
>
> - 防篡改验证部分
>
> - 算法：
>
>   ```json
>   HMACSHA256(
>     base64UrlEncode(Header) + "." + base64UrlEncode(Payload),
>     secret (盐-密钥)
>   )
>   ```
>
> 完整 JWT 示例：
>
> ```jwt
> eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
> ```

##### 3.  JWT配置

```java
package com.ysj.security.utils.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * JWT 相关配置项（支持 Spring Boot 自动绑定）
 * 使用方式：
 *  - 在 application.yml 中配置 jwt.secret/jwt.expire/jwt.issuer
 *  - 本类通过 @ConfigurationProperties(prefix="jwt") 自动注入
 */
@Data
@Validated // 让 @NotBlank 等校验注解生效
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * 对称密钥（建议长度 ≥ 32 字符；生产请使用高熵随机串）
     * 若使用 Base64 编码的原始随机字节，可在 JwtUtils 中改为 BASE64 解码后再生成 SecretKey
     */
    @NotBlank(message = "jwt.secret 不能为空")
    private String secret;

    /**
     * 令牌有效期（默认 2 小时）
     * - 支持 Spring 的 Duration 格式，例如：PT2H、2h、30m、45s
     * - 置为 null/0/负数 时，表示不写入 exp（“无过期”——不建议生产使用）
     */
    private Duration expire = Duration.ofHours(2);

    /**
     * 签发者（建议填写，利于审计与多服务区分）
     */
    private String issuer;
}
```

```java
package com.ysj.security.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * JWT 工具类（JJWT 0.12.x）
 * 关键实现要点：
 * 1. 初始化时构建 SecretKey 与 JwtParser，并设置允许的时钟偏移（clockSkew）
 * 2. createToken()：先设置「自定义 claims」，再设置标准字段（iat/nbf/exp/sub/iss），避免被覆盖
 * 3. validate()/parseClaims()：解析校验，异常收敛为 JwtException/IllegalArgumentException
 * 4. refresh()：保留原有自定义 claims 与 subject，仅刷新 iat/nbf/exp
 * 5. 过期语义：当 expire==null 或 <=0 时，不写入 exp（无过期）
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    /**
     * 对称密钥（HS256）
     */
    private SecretKey key;

    /**
     * 线程安全的解析器
     */
    private JwtParser parser;

    /**
     * 允许的时钟偏移（解决多机间 NTP 轻微漂移），这里给 60s
     */
    private static final Duration CLOCK_SKEW = Duration.ofSeconds(60);


    /**
     * 容器启动后初始化密钥与解析器
     */
    @PostConstruct
    void init() {
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.length() < 32) {
            // 256-bit 强度建议，长度≥32的 UTF-8 字符串能大致满足
            // 生产可改为 BASE64 解码原始随机字节：Decoders.BASE64.decode(...)
            throw new IllegalArgumentException("jwt.secret 至少需要 32 个字符（≈256bit），请使用高熵随机串");
        }

        // 若你改用 BASE64：byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        this.parser = Jwts.parser()
                .verifyWith(key)                                 // 验签密钥
                .clockSkewSeconds(CLOCK_SKEW.getSeconds())       // 允许轻微时钟偏移
                .build();
    }

    /**
     * 最简签发：仅设置 subject，过期时间取配置
     */
    public String createToken(String subject) {
        return createToken(subject, null, jwtProperties.getExpire());
    }

    /**
     * 完整签发：支持自定义 claims 与自定义有效期
     *
     * @param subject 主体（一般放用户唯一标识）
     * @param claims  自定义负载（可为 null）
     * @param expire  有效期（为 null/<=0 则不设置 exp ——“无过期”）
     */
    public String createToken(String subject, Map<String, Object> claims, Duration expire) {
        Objects.requireNonNull(subject, "subject 不能为空");
        long now = System.currentTimeMillis();

        Date iat = new Date(now);     // 签发时间
        Date nbf = iat;               // 生效时间：立即生效

        // JJWT 0.12.x：若先设置标准字段，再 .claims(map) 会被覆盖
        // 规范顺序→先 claims，再标准字段
        JwtBuilder builder = Jwts.builder()
                .claims().add(claims == null ? Map.of() : claims).and() // 添加自定义 claims

                // 设置标准字段
                .issuedAt(iat)
                .notBefore(nbf)
                .subject(subject)

                // 显式声明算法更清晰（根据 key 类型选择 HS256）
                .signWith(key, Jwts.SIG.HS256);

        // 过期时间：当 expire 不为 null 且 >0 时才设置 exp
        if (expire != null && !expire.isZero() && !expire.isNegative()) {
            builder.expiration(new Date(now + expire.toMillis()));
        }

        // 可选的 iss（签发者）
        if (jwtProperties.getIssuer() != null && !jwtProperties.getIssuer().isEmpty()) {
            builder.issuer(jwtProperties.getIssuer());
        }

        // （可选）更“标准”的 Header 类型声明
        builder.header().type("JWT");

        return builder.compact();
    }

    /**
     * 解析为 Claims（失败抛 JwtException / IllegalArgumentException）
     */
    public Claims parseClaims(String token) throws JwtException, IllegalArgumentException {
        return parser.parseSignedClaims(stripBearer(token)).getPayload();
    }

    /**
     * 校验令牌是否“语法正确且验签通过且未过期”
     * - 注意：仅返回 true/false，不抛异常
     */
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 是否过期（解析失败也视为“无效/过期”）
     */
    public boolean isExpired(String token) {
        try {
            Date exp = getClaim(token, Claims::getExpiration);
            return exp != null && exp.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // 解析失败：保守地认为不可用
            return true;
        }
    }

    /**
     * 获取任意 claim
     */
    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        Claims claims = parseClaims(token);
        return resolver.apply(claims);
    }

    /**
     * 获取 subject（用户唯一标识）
     */
    public String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * 刷新令牌：保留 subject + 自定义 claims，仅刷新 iat/nbf/exp
     * - 注意：若配置的 expire == null/<=0，则刷新后仍为“无过期”Token
     */
    public String refresh(String token) {
        Claims raw = parseClaims(token);
        String subject = raw.getSubject();

        // 拷贝一份 Map，避免直接操作 Claims 实现导致潜在副作用
        Map<String, Object> data = new HashMap<>(raw);

        // 移除标准时间相关字段，避免覆盖冲突
        data.remove(Claims.ISSUED_AT);
        data.remove(Claims.NOT_BEFORE);
        data.remove(Claims.EXPIRATION);
        data.remove(Claims.SUBJECT);
        data.remove(Claims.ISSUER); // issuer 由配置决定，避免 payload 冲突

        return createToken(subject, data, jwtProperties.getExpire());
    }

    /**
     * 从请求头解析 Bearer Token（形如：Authorization: Bearer x.y.z）
     */
    public static String resolveFromHeader(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank()) return null;
        if (auth.startsWith("Bearer ")) return auth.substring(7).trim();
        return null;
    }

    /**
     * 去掉 "Bearer " 前缀（对直接传入 header 值时很有用）
     */
    private static String stripBearer(String token) {
        if (token == null) return null;
        String t = token.trim();
        return t.startsWith("Bearer ") ? t.substring(7).trim() : t;
        // 也可兼容小写前缀："bearer "
    }
}
```

```yaml
# jwt 配置
jwt:
  secret: w9FJmkF3x3+QbHqHZq2lLVdcmRbth9+PpMNeWjVlzGk=
  expire: 2h     # 支持 10m, 30m, 1h, 2h, PT90M 等 Spring Boot Duration 格式
  issuer: "ysj-security"
```

#### （6）数据库配置

```mysql
CREATE TABLE `sys_user`
(
    `id`                      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`               varchar(64)  DEFAULT NULL COMMENT '用户名',
    `nick_name`               varchar(64)  DEFAULT NULL COMMENT '昵称',
    `password`                varchar(64)  DEFAULT NULL COMMENT '密码',
    `email`                   varchar(64)  DEFAULT NULL COMMENT '邮箱',
    `phone_number`            varchar(32)  DEFAULT NULL COMMENT '手机号',
    `sex`                     tinyint(1)   DEFAULT 2 COMMENT '用户性别（0男，1女，2未知）',
    `avatar`                  varchar(128) DEFAULT NULL COMMENT '头像',
    `user_type`               tinyint(1)   DEFAULT 1 COMMENT '用户类型（0管理员，1普通用户）',
    `account_non_expired`     tinyint(1)   DEFAULT 1 COMMENT '账户是否未过期（1是，0否）',
    `account_non_locked`      tinyint(1)   DEFAULT 1 COMMENT '账户是否未锁定（1是，0否）',
    `credentials_non_expired` tinyint(1)   DEFAULT 1 COMMENT '凭证是否未过期（1是，0否）',
    `enabled`                 tinyint(1)   DEFAULT 1 COMMENT '账户是否启用（1是，0否）',
    `create_by`               bigint(20)   DEFAULT NULL COMMENT '创建人的用户id',
    `create_time`             datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`               bigint(20)   DEFAULT NULL COMMENT '更新人',
    `update_time`             datetime     DEFAULT NULL COMMENT '更新时间',

    -- 删除标志改 tinyint（0未删除 1已删除）
    `deleted`                 tinyint(1)   DEFAULT 0 COMMENT '删除标志（0未删除，1已删除）',

    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户表';
```

### 4、认证流程代码

