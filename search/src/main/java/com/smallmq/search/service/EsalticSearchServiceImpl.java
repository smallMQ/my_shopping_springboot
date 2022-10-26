package com.smallmq.search.service;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.smallmq.search.config.SearchConfig;
import com.smallmq.to.es.SkuEsModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
@Slf4j
public class EsalticSearchServiceImpl implements EsalticSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存到es
        // 1. 给es中建立一个索引，product，建立好映射关系

        // 2. 给es中保存这些数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            // 构造保存请求
            IndexRequest indexRequest = new IndexRequest("product");
            indexRequest.id(skuEsModel.getSkuId().toString());
            indexRequest.source(JacksonUtils.toJson(skuEsModel), org.elasticsearch.common.xcontent.XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, SearchConfig.COMMON_OPTIONS);
        boolean b = bulk.hasFailures();
        log.error("商品上架错误：{}", bulk.buildFailureMessage());
        return b;
    }
}
