package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @Classname SimpleRedisLock
 * @Description Redis分布式锁
 * @Date 2025/11/18 19:28
 * @Created by YanShijie
 */
public class SimpleRedisLock implements ILock {

    private final StringRedisTemplate stringRedisTemplate;

    private final String name;

    public SimpleRedisLock(StringRedisTemplate stringRedisTemplate, String name) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.name = name;
    }

    private static final String KEY_PREFIX = "lock:";

    private static final String ID_PREFIX = UUID.randomUUID().toString() + "-";

    /*
     * 泛型是脚本返回值
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        // 配置脚本路径
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        // 配置返回值值类型
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 自动拆箱 有空指针危险
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS));
    }

    @Override
    public void unlock() {
        // 调用 Lua脚本
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(KEY_PREFIX + name), ID_PREFIX + Thread.currentThread().getId());
    }
}
