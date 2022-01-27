package redisCurrentLimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;

/**
 * @author cry777
 * @program demo1
 * @description 基于Redis的令牌桶算法实现限流，我们可以结合Redis的List数据结构很轻易的做到这样的代码
 * @create 2022-01-27
 */
public class TokenBucketLimit {

    @Autowired
    RedisTemplate redisTemplate;

    // 输出令牌
    public Response limitFlow2(Long id){
        Object result = redisTemplate.opsForList().leftPop("limit_list");
        if(result == null){
            return Response.ok("当前令牌桶中无令牌");
        }
        return Response.ok("当前令牌桶中有令牌");
    }

    // 再依靠Java的定时任务，定时往List中rightPush令牌，当然令牌也需要唯一性，所以我这里还是用UUID进行了生成
    // 10S的速率往令牌桶中添加UUID，只为保证唯一性
    @Scheduled(fixedDelay = 10_000,initialDelay = 0)
    public void setIntervalTimeTask(){
        redisTemplate.opsForList().rightPush("limit_list", UUID.randomUUID().toString());
    }
}
