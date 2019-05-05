package com.pgy.ups.common.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisUtils {

	@Resource
	private JedisPool jedisPool;

	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";

	private static final Long RELEASE_SUCCESS = 1L;
	
	public static RedisUtils getInstance() {
		return SpringUtils.getBean(RedisUtils.class);
	}

	public Long setnx(String key, String value) {

		Jedis jedis = jedisPool.getResource();
		Long result = jedis.setnx(key.getBytes(), value.getBytes());
		jedis.close();
		return result;
	}

	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}

	public boolean setex(String key, String value, int expire) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.setex(key.getBytes(), expire, value.getBytes());
		jedis.close();
		return LOCK_SUCCESS.equals(result);
	}

	public boolean delete(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result > 0;
	}

	/**
	 * @Description redis 分布式加锁
	 * @param lockKey
	 * @param requestId
	 * @param expireTime
	 * @return
	 */
	public boolean redisLock(String lockKey, String requestId, int expireTime) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
		jedis.close();
		return LOCK_SUCCESS.equals(result);
	}

	/**
	 * @Description redis 分布式解锁
	 * @param lockKey
	 * @param requestId
	 * @return
	 */
	public boolean redisUnLock(String lockKey, String requestId) {
		Jedis jedis = jedisPool.getResource();
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
		jedis.close();
		return RELEASE_SUCCESS.equals(result);
	}

	/**
	 * 存放list
	 * 
	 * @param listKey
	 * @param list
	 */
	public void setList(String listKey, List<Serializable> list) {
		Jedis jedis = jedisPool.getResource();
		jedis.set(listKey.getBytes(), SerializerUtils.serialize(list));
		jedis.close();
	}

	/**
	 * 获取list
	 * 
	 * @param listKey
	 * @return
	 */
	public List<Serializable> getList(String listKey) {
		Jedis jedis = jedisPool.getResource();
		byte[] bytes = jedis.get(listKey.getBytes());
		jedis.close();
		return SerializerUtils.deserialize(bytes);
	}

	public <T extends Serializable> boolean hmset(String key, Map<String, T> value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key.getBytes(), SerializerUtils.serialize(value));
		jedis.close();
		return LOCK_SUCCESS.equals(result);
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> Map<String, T> getMap(String key, Class<T> clazz) {
		Jedis jedis = jedisPool.getResource();
		byte[] bytes = jedis.get(key.getBytes());
		jedis.close();
		return (Map<String, T>) SerializerUtils.deserialize(bytes);
	}

}
