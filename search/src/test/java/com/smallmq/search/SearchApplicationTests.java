package com.smallmq.search;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.smallmq.search.config.SearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class SearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Data
    class User{
        private String name;
        private String age;
        private String sex;
    }

    @Test
    void contextLoads() throws IOException {
        // 测试es是否启动
        System.out.println(client);
        // 测试存储数据
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setName("张三");
        user.setAge("18");
        user.setSex("男");
        // 将user转为json
        String json = JacksonUtils.toJson(user);

        indexRequest.source(json, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, SearchConfig.COMMON_OPTIONS);

        // 测试获取数据
        System.out.println(index);

    }

}
