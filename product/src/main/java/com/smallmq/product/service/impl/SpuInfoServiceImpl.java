package com.smallmq.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.product.dao.SpuInfoDao;
import com.smallmq.product.dao.SpuInfoDescDao;
import com.smallmq.product.entity.*;
import com.smallmq.product.feign.CouponFeignService;
import com.smallmq.product.service.*;
import com.smallmq.product.vo.*;
import com.smallmq.to.SkuReductionTo;
import com.smallmq.to.SpuBoundTo;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import com.smallmq.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescDao spuInfoDescDao;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        // 2.保存spu的描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        // 没有图片
        if (decript != null && decript.size() > 0) {
            spuInfoDescEntity.setDecript(String.join(",", decript));
        }
        spuInfoDescDao.insert(spuInfoDescEntity);
        // 3.保存spu的图片集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        // 没有图片
        if (images != null && images.size() > 0) {
            spuImagesService.insertImages(spuInfoEntity.getId(), images);
        }
        // 4.保存spu的规格参数 pms_product_attr_value
        List<Baseattrs> baseattrs = spuSaveVo.getBaseAttrs();
        // 没有规格参数
        if (baseattrs != null && baseattrs.size() > 0) {
            List<ProductAttrValueEntity> productAttrValueEntities = baseattrs.stream().map(baseattr -> {
                ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                productAttrValueEntity.setAttrId(baseattr.getAttrId());
                productAttrValueEntity.setAttrName(attrService.getById(baseattr.getAttrId()).getAttrName());
                productAttrValueEntity.setAttrValue(baseattr.getAttrValues());
                productAttrValueEntity.setQuickShow(baseattr.getShowDesc());
                productAttrValueEntity.setSpuId(spuInfoEntity.getId());
                return productAttrValueEntity;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(productAttrValueEntities);

        }
        // 5.保存spu的积分信息 sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        if (bounds != null) {
            SpuBoundTo spuBoundTo = new SpuBoundTo();
            BeanUtils.copyProperties(bounds, spuBoundTo);
            spuBoundTo.setSpuId(spuInfoEntity.getId());
            if (bounds.getBuyBounds().compareTo(new BigDecimal("0")) == 1) {
                R r = couponFeignService.saveSpuBounds(spuBoundTo);
                if (r.getCode() != 0) {
                    log.error("远程保存spu积分信息失败");
                }
            }
        }
        // 5.保存spu的sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        // 没有sku
        if (skus != null && skus.size() > 0) {
            skus.forEach(sku -> {
                List<Images> images1 = sku.getImages();
                String defaultImg = "";
                if (images1 != null && images1.size() > 0) {
                    for (Images image : images1) {
                        if (image.getDefaultImg() == 1) {
                            defaultImg = image.getImgUrl();
                        }
                    }
                }
                // 5.1 保存sku的基本信息 pms_sku_info
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.save(skuInfoEntity);
                if (images1 != null && images1.size() > 0) {
                    List<SkuImagesEntity> skuImagesEntities = images1.stream().map((img) -> {
                        SkuImagesEntity imagesEntity = new SkuImagesEntity();
                        imagesEntity.setSkuId(skuInfoEntity.getSkuId());
                        imagesEntity.setImgUrl(img.getImgUrl());
                        imagesEntity.setDefaultImg(img.getDefaultImg());

                        return imagesEntity;
                    }).filter(entity ->{
                        return !StringUtils.isEmpty(entity.getImgUrl());
                    }).collect(Collectors.toList());
                    // 5.2 保存sku的图片信息 pms_sku_images
                    skuImagesService.saveBatch(skuImagesEntities);

                    // 5.3 保存sku的销售属性信息 pms_sku_sale_attr_value
                    List<Attr> attr = sku.getAttr();
                    List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map((a) -> {
                        SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                        BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                        skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                        return skuSaleAttrValueEntity;
                    }).collect(Collectors.toList());
                    skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                    // 5.4 保存sku的优惠、满减等信息 sms_sku_ladder、sms_sku_full_reduction、sms_member_price
                    SkuReductionTo skuReductionTo = new SkuReductionTo();
                    BeanUtils.copyProperties(sku, skuReductionTo);
                    skuReductionTo.setSkuId(skuInfoEntity.getSkuId());
                    if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                        R r = couponFeignService.saveSkuReduction(skuReductionTo);
                        if (r.getCode() != 0) {
                            log.error("远程保存sku优惠信息失败");
                        }
                    }
                }

            });

        }

    }
}