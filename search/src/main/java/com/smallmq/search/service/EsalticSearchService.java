package com.smallmq.search.service;

import com.smallmq.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;


public interface EsalticSearchService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
