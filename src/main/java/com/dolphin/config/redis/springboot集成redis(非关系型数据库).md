# [springboot技术栈](https://github.com/2425358736/dolphin/blob/master/README.md)

### [redis教程（redis安装及使用请点击这里）](http://www.runoob.com/redis/redis-intro.html)


### Redis 简介
Redis 是完全开源免费的，遵守BSD协议，是一个高性能的key-value数据库。

#### Redis 与其他 key - value 缓存产品有以下三个特点：

Redis支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用。

Redis不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储。

Redis支持数据的备份，即master-slave模式的数据备份。

### Redis 优势
性能极高 – Redis能读的速度是110000次/s,写的速度是81000次/s 。

丰富的数据类型 – Redis支持二进制案例的 Strings, Lists, Hashes, Sets 及 Ordered Sets 数据类型操作。

原子 – Redis的所有操作都是原子性的，意思就是要么成功执行要么失败完全不执行。单个操作是原子性的。多个操作也支持事务，即原子性，通过MULTI和EXEC指令包起来。

丰富的特性 – Redis还支持 publish/subscribe, 通知, key 过期等等特性。

### Redis与其他key-value存储有什么不同？
Redis有着更为复杂的数据结构并且提供对他们的原子性操作，这是一个不同于其他数据库的进化路径。Redis的数据类型都是基于基本数据结构的同时对程序员透明，无需进行额外的抽象。

Redis运行在内存中但是可以持久化到磁盘，所以在对不同数据集进行高速读写时需要权衡内存，因为数据量不能大于硬件内存。在内存数据库方面的另一个优点是，相比在磁盘上相同的复杂的数据结构，在内存中操作起来非常简单，这样Redis可以做很多内部复杂性很强的事情。同时，在磁盘格式方面他们是紧凑的以追加的方式产生的，因为他们并不需要进行随机访问。


### springboot也集成了redis开发包

## 集成

1. pom.xml引入redis开发包

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
2. application.yml增加redis配置

```
spring:
  #redis配置数据
  redis:
    hostName: 123.206.19.217
    port: 6379
    database: 2
    password:
    pool:
      maxActive: 10
      maxWait: -1
      maxIdle: 100
      minIdle: 0
    timeout: 5000
```

3. 配置Redis创建RedisTemplate实例，新建RedisConfig.java

```
package com.dolphin.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * RedisConfig
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@Configuration
public class RedisConfig {

    @Bean(name = "jedisPoolConfig")
    @ConfigurationProperties(prefix = "spring.redis.pool")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    @Bean(name = "jedisConnectionFactory")
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        JedisPoolConfig config = jedisPoolConfig;
        factory.setPoolConfig(config);
        return factory;
    }


    @Bean(name = "redisTemplate" )
    public RedisTemplate<?, ?> getRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<?, ?> redisTemplate = new StringRedisTemplate(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value的序列化类型
        return redisTemplate;
    }
}
```

4. 新建测试controller RedisController.java

```
package com.dolphin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 开发公司：青岛海豚数据技术有限公司
 * 版权：青岛海豚数据技术有限公司
 * <p>
 * RedisController
 *
 * @author 刘志强
 * @created Create Time: 2019/2/15
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    public RedisTemplate redisTemplate;

    @GetMapping("index")
    @ResponseBody
    public String index(){
        redisTemplate.opsForValue().set("id","22222");
        return (String) redisTemplate.opsForValue().get("id");
    }

    @GetMapping("index2")
    @ResponseBody
    public Map index2(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("userName", "刘志强");
        ValueOperations<String,Map> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("map",map);
        return valueOperations.get("map");
    }

    @GetMapping("index3")
    @ResponseBody
    public List index3() throws InterruptedException {
        List<Map> list = new ArrayList();
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("userName", "刘志强");
        list.add(map);
        redisTemplate.opsForValue().set("list",list,1, TimeUnit.SECONDS);
        List list1 = (List) redisTemplate.opsForValue().get("list");
        return list1;
    }
}
```
###### ValueOperations 有多个set同名方法，主要介绍下面几方法
###### set(第一个参数,第二个参数,第三个参数, 第四个参数)，第一个参数是key，第二个参数是value，第三个参数是有效期，第四个参数是描述第三个参数的时间类型

###### RedisTemplate也提供了删除方法 delete(key)








