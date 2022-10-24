package com.smallmq.product.vo;

import com.smallmq.product.entity.AttrEntity;
import com.smallmq.product.entity.AttrGroupEntity;

import java.util.List;

public class AttrGroupVo extends AttrGroupEntity {
    private List<AttrEntity> Attrs;

    public List<AttrEntity> getAttrs() {
        return Attrs;
    }

    public void setAttrs(List<AttrEntity> attrs) {
        Attrs = attrs;
    }
}
