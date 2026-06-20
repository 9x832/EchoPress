package com.ruoyiblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ruoyiblog.mapper")
public class BlogApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BlogApplication.class, args);
    }
}
