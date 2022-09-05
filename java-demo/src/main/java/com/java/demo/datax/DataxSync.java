package com.java.demo.datax;

import java.io.File;

//import com.alibaba.datax.core.Engine;

public class DataxSync {


    /**
     * mvn -U clean package assembly:assembly -Dmaven.test.skip=true
     * 下载或者编译 datax.tar.gz 解压到本地
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        String runHome = "E:\\datax_home\\datax";//getCurrentClasspath();
        System.setProperty("datax.home", runHome);
        String jsonFullPath = runHome + File.separator + "script" + File.separator + "test.json";
        String[] params = {"-job", jsonFullPath, "-mode", "standalone", "-jobid", "-1"};
//        Engine.entry(params);
    }

    public static String getCurrentClasspath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String currentClasspath = classLoader.getResource("").getPath();
        // 当前操作系统
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            // 删除path中最前面的/
            currentClasspath = currentClasspath.substring(1);
        }
        return currentClasspath;
    }
}
