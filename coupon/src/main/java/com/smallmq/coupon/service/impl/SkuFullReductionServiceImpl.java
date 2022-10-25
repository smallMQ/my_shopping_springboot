package com.smallmq.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.coupon.dao.SkuFullReductionDao;
import com.smallmq.coupon.entity.MemberPriceEntity;
import com.smallmq.coupon.entity.SkuFullReductionEntity;
import com.smallmq.coupon.entity.SkuLadderEntity;
import com.smallmq.coupon.service.MemberPriceService;
import com.smallmq.coupon.service.SkuFullReductionService;
import com.smallmq.coupon.service.SkuLadderService;
import com.smallmq.to.SkuReductionTo;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        // 1、保存满减信息mall_sms->sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
        this.save(skuFullReductionEntity);
        // 2、保存折扣信息mall_sms->sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if(skuLadderEntity.getFullCount() > 0){
            skuLadderService.save(skuLadderEntity);
        }

        // 3、保存满减信息mall_sms->sms_member_price
        List<SkuReductionTo.MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        // 判断是否为空
        if (memberPrice != null && memberPrice.size() > 0) {
            List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                memberPriceEntity.setMemberLevelId(item.getId());
                memberPriceEntity.setMemberLevelName(item.getName());
                memberPriceEntity.setMemberPrice(item.getPrice());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter(item ->{
                return item.getMemberPrice().compareTo(new BigDecimal("0")) == 1;
            }).collect(Collectors.toList());
            memberPriceService.saveBatch(collect);
        }
    }

}