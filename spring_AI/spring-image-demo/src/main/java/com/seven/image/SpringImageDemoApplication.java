package com.seven.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringImageDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringImageDemoApplication.class, args);
        System.setProperty("http.proxyHost",System.getenv("127.0.0.1")); //修改为你代理服务器的IP
        System.setProperty("https.proxyHost",System.getenv("127.0.0.1"));
        System.setProperty("http.proxyPort","7890"); // 修改为你代理软件的端⼝
        System.setProperty("https.proxyPort","7890"); // 同理
    }
}
