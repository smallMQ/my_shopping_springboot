package com.smallmq.third;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class ThirdApplicationTests {
    @Autowired
    private OSSClient ossClient;

    @Test
    void contextLoads() throws FileNotFoundException {
        // 读取文件
        InputStream inputStream = new FileInputStream("D:\\desktop\\微信图片_20221005125010.jpg");
        // 上传文件
        ossClient.putObject("smallmq", "haha.jpg", inputStream);
    }

}
