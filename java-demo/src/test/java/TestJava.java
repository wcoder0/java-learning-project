import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class TestJava {

    public static void main(String[] args) throws Exception {
        List list = new ArrayList();
        list.contains("");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        int year = DateUtil.year(format.parse("2022-08"));
        int month = DateUtil.month(format.parse("2022-8"));

        System.out.println(year + "-" + month);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String yearMonth = sdf.format(sdf.parse("2019" + "-" + "7"));

        System.out.println("yearMonth" + yearMonth);

        Set<String> periods = new HashSet<>();
        String[] split = "2022-08".split("-");
        periods.add(Integer.parseInt(split[0]) + "-" + Integer.parseInt(split[1]));
        System.out.println(periods);

        Map<String, Object> map = new HashMap<>();
        Object obj = map.get("11");

        if (obj == null) {
            obj = "22";
        }

        System.out.println(obj);
        System.out.println(map);

        Map map2 = new ConcurrentHashMap();
        map2.get(null);
    }
}
