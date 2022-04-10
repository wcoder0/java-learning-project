import java.util.*;
import java.util.concurrent.*;

public class TestJava {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        Object obj = map.get("11");

        if(obj == null){
            obj = "22";
        }

        System.out.println(obj);
        System.out.println(map);

        Map map2 = new ConcurrentHashMap();
        map2.get(null);
    }
}
