package redis;

import com.java.redis.DemoApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = {DemoApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class RedisDemoApp {


    @Autowired
    private RedisTemplate redisTemplate;

    @org.junit.Test
    public void testLog() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @org.junit.Test
    public void test() {
        String key = "htest";
        Map<String, String> data = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            data.put("" + i, "" + i);
            redisTemplate.opsForList().leftPush("ltest", "" + i);
        }

        redisTemplate.opsForHash().putAll(key, data);
        ScanOptions options = new ScanOptions.ScanOptionsBuilder().count(2).match("*").build();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options);


        while (cursor.hasNext()) {
            long cursorId = cursor.getCursorId();
            Map.Entry<Object, Object> entry = cursor.next();
            Object hkey = entry.getKey();
            Object hval = entry.getValue();

            System.out.println(cursorId + " cursor.hasNext(): " + hkey + " : " + hval);
        }
    }


}
