package redisCurrentLimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.UUID;


/**
 * @author cry777
 * @program demo1
 * @description 基于Redis的setnx的操作实现的限流: 综上，代码实现起始都不是很难，针对这些限流方式我们可以在AOP或者filter中加入以上代码，用来做到接口的限流，最终保护你的网站。
 * @create 2022-01-26
 */
public class ZSetCurrentLimit {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 10秒
     */
    long intervalTime = 10000;

    public Response limitFlow() {
        Long currentTime = new Date().getTime();
        System.out.println(currentTime);
        if (redisTemplate.hasKey("limit")) {
            // intervalTime是限流的时间，拦截器拦截所有请求/指定接口，在redis新增计数
            Integer count = redisTemplate.opsForZSet().rangeByScore("limit", currentTime - intervalTime, currentTime).size();
            System.out.println(count);
            if (count != null && count > 5) {
                return Response.ok("每分钟最多只能访问5次");
            }
        }
        redisTemplate.opsForZSet().add("limit", UUID.randomUUID().toString(), currentTime);
        return Response.ok("访问成功");
    }
}
