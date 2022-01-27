package redisCurrentLimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.UUID;


/**
 * @author cry777
 * @program demo1
 * @description 基于Redis的setnx的操作实现的限流：比如我们需要在10秒内限定20个请求，那么我们在setnx的时候可以设置过期时间10，当请求的setnx数量达到20时候即达到了限流效果
 * @create 2022-01-26
 */
public class SetNxCurrentLimit {

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
            Integer count = (Integer) redisTemplate.opsForValue().get("limit");
            if (count != null && count > 5) {
                return Response.ok("每分钟最多只能访问5次");
            }
        }
        redisTemplate.opsForZSet().add("limit", UUID.randomUUID().toString(), currentTime);
        return Response.ok("访问成功");
    }
}
