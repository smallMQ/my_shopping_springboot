package com.smallmq.ware.vo;

import java.util.List;

public class PurchaseVo {
    private Long id;
    private List<Long> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }
}
