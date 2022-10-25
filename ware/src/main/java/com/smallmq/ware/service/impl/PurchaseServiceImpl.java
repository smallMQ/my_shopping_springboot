package com.smallmq.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.constant.WareConstant;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import com.smallmq.ware.dao.PurchaseDao;
import com.smallmq.ware.entity.PurchaseDetailEntity;
import com.smallmq.ware.entity.PurchaseEntity;
import com.smallmq.ware.service.PurchaseDetailService;
import com.smallmq.ware.service.PurchaseService;
import com.smallmq.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseEntity::getStatus, 0).or().eq(PurchaseEntity::getStatus, 1);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo vo) {
        Long purchaseId = vo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PURCHASE_STATUS_CREATE.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        //TODO 确认采购单状态是0,1才可以合并
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = vo.getItems().stream().map(item -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PURCHASE_STATUS_ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(purchaseDetailEntities);
    }

    @Override
    public void receivedPurchase(List<Long> asList) {
        //1.确认当前采购单是新建或已分配状态
        List<PurchaseEntity> collect = asList.stream().map(item -> {
            PurchaseEntity purchaseEntity = this.getById(item);
            return purchaseEntity;
        }).filter(item -> {
            return item.getStatus() == WareConstant.PURCHASE_STATUS_CREATE.getCode() || item.getStatus() == WareConstant.PURCHASE_STATUS_ASSIGNED.getCode();
        }).map(item -> {
            item.setStatus(WareConstant.PURCHASE_STATUS_PURCHASING.getCode());
            return item;
        }).collect(Collectors.toList());

        //2.改变采购单的状态
        this.updateBatchById(collect);

        //3.改变采购项的状态
        collect.forEach((item) -> {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream().map((en) -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(en.getId());
                detailEntity.setStatus(WareConstant.PURCHASE_STATUS_DONE.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

}