package redisCurrentLimit;

/**
 * @author cry777
 * @program demo1
 * @description
 * @create 2022-01-27
 */
public class Response {

    String msg;

    public Response(String msg) {
        this.msg = msg;
    }

    public static Response ok(String msg){
        return new Response(msg);
    }
}
