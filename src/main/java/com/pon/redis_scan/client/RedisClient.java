/**
 * 
 */
package com.pon.redis_scan.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pon.redis_scan.iterator.RedisIterator;
import com.pon.redis_scan.strategy.ScanStrategy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
/**
 * @author Sanjeev
 *
 */
public class RedisClient {
	private static Logger log = LoggerFactory.getLogger(RedisClient.class);

    private static volatile RedisClient instance = null;

    private static JedisPool jedisPool;

    
    
    /**
     * Get an instance of jedis client
     * */
    public static RedisClient getInstance(String ip, final int port) {
        if (instance == null) {
            synchronized (RedisClient.class) {
                if (instance == null) {
                    instance = new RedisClient(ip, port);
                }
            }
        }
        return instance;
    }

    /**
     * It is initializing jedis client and taking a jedis client from the pool.
     * */
    private RedisClient(String ip, int port) {
        try {
            if (jedisPool == null) {
                jedisPool = new JedisPool(new URI("http://" + ip + ":" + port));
            }
        } catch (URISyntaxException e) {
            log.error("Malformed server address", e);
        }
    }

    /**
     * It will be used for pushing data into a list.
     * [1] Here list name is key
     * [2] values as array of string
     * [3] Return is a long value telling you how many values have been inserted into list
     * 
     * */
    public Long lpush(final String key, final String[] strings) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, strings);
        } catch (Exception ex) {
            log.error("Exception caught in lpush", ex);
        }
        return null;
    }

    /**
     * It is reading data from list and returning you as list<String>
     * **/
    public List<String> lrange(final String key, final long start, final long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, stop);
        } catch (Exception ex) {
            log.error("Exception caught in lrange", ex);
        }
        return new LinkedList<String>();
    }

    /**
     * It is used to set multiple values into hash
     * **/
    public String hmset(final String key, final Map<String, String> hash) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmset(key, hash);
        } catch (Exception ex) {
            log.error("Exception caught in hmset", ex);
        }
        return null;
    }

    /**
     * It will return all the hash [Map<String, String>] stored with key
     * **/
    public Map<String, String> hgetAll(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception ex) {
            log.error("Exception caught in hgetAll", ex);
        }
        return new HashMap<String, String>();
    }
    
    /**
     * It is used to add data into set
     * **/

    public Long sadd(final String key, final String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, members);
        } catch (Exception ex) {
            log.error("Exception caught in sadd", ex);
        }
        return null;
    }

    /**
     * It is used to read all members of a set with key as key
     * **/
    public Set<String> smembers(final String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        } catch (Exception ex) {
            log.error("Exception caught in smembers", ex);
        }
        return new HashSet<String>();
    }

    /**
     * It is used to add data into a sorted set
     * **/
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, scoreMembers);
        } catch (Exception ex) {
            log.error("Exception caught in zadd", ex);
        }
        return 0L;
    }

    /**
     * It is used to read sorted set
     * **/
    public Set<String> zrange(final String key, final long start, final long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrange(key, start, stop);
        } catch (Exception ex) {
            log.error("Exception caught in zrange", ex);
        }
        return new HashSet<String>();
    }
   /**
    * It is used to set multiple key value pair
    * **/
    public String mset(final HashMap<String, String> keysValues) {
        try (Jedis jedis = jedisPool.getResource()) {
            ArrayList<String> keysValuesArrayList = new ArrayList<String>();
            keysValues.forEach((key, value) -> {
                keysValuesArrayList.add(key);
                keysValuesArrayList.add(value);
            });
            return jedis.mset((keysValuesArrayList.toArray(new String[keysValues.size()])));
        } catch (Exception ex) {
            log.error("Exception caught in mset", ex);
        }
        return null;
    }

    /**
     * It will be used for listing all key matching with the pattern
     * **/
	public Set<String> keys(final String pattern) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.keys(pattern);
		} catch (Exception ex) {
			log.error("Exception caught in keys", ex);
		}
		return new HashSet<String>();
	}

    public RedisIterator iterator(int initialScanCount, String pattern, ScanStrategy strategy) {
        return new RedisIterator(jedisPool, initialScanCount, pattern, strategy);
    }

    /**
     * It will flush all the keys
     * **/
    public void flushAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushAll();
        } catch (Exception ex) {
            log.error("Exception caught in flushAll", ex);
        }
    }
    
    public void destroyInstance() {
        jedisPool = null;
        instance = null;
    }
}
