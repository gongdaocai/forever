package com.xcly.forever.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

@Component
public class RedisLockUtil {
    /**
     * 加锁成功
     */
    private String LOCK_SUCCESS = "OK";
    /**
     * 解锁成功
     */
    private Long RELEASE_SUCCESS = 1L;
    /**
     * 设置key如果不存在
     */
    private String SET_IF_NOT_EXIST = "NX";
    /**
     * 设置redis锁超时时间
     */
    private String SET_WITH_EXPIRE_TIME = "PX";
    /**
     * 默认的锁等待时间，防止线程饥饿
     */
    private int TIMEOUT_SECONDS = 10 * 1000;
    /**
     * 默认的锁失效时间，防止线程获取锁服务宕机引发死锁
     */
    private final int EXPIRE_SECONDS = 60 * 1000;
    /**
     * 阻塞获取锁的间隔时间以及休眠时间
     **/
    private final int DEFAULT_ACQUIRE_RESOLUTION_MILLIS = 100;

    @Autowired
    private JedisPool jedisPool;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取锁
     *
     * @param lockKey   锁唯一key
     * @param requestId 请求标识
     * @param block     是否阻塞
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, boolean block) throws InterruptedException {
        Jedis jedis = jedisPool.getResource();
        //阻塞锁
        if (block) {
            int timeout = TIMEOUT_SECONDS;
            while (timeout >= 0) {
                String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, EXPIRE_SECONDS);
                if (LOCK_SUCCESS.equals(result)) {
                    logger.info("<<<===获取锁成功 {key={},value={}}", lockKey, requestId);
                    return true;
                }
                timeout -= DEFAULT_ACQUIRE_RESOLUTION_MILLIS;
                Thread.sleep(DEFAULT_ACQUIRE_RESOLUTION_MILLIS);
            }
        } else {
            //非阻塞锁
            if (LOCK_SUCCESS.equals(jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, EXPIRE_SECONDS))) {
                logger.info("<<<===获取锁成功 {key={},value={}}", lockKey, requestId);
                return true;
            }
        }
        logger.info("<<<===获取锁失败 {key={},value={}}", lockKey, requestId);
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        Jedis jedis = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        if (jedis.get(lockKey) != null) {
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                logger.info("<<<===释放锁成功 {key={},value={}}", lockKey, requestId);
                return true;
            }
        }
        return false;
    }
}