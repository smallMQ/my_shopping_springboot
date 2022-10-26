package com.smallmq.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smallmq.constant.ProductConstant;
import com.smallmq.product.dao.SpuInfoDao;
import com.smallmq.product.dao.SpuInfoDescDao;
import com.smallmq.product.entity.*;
import com.smallmq.product.feign.CouponFeignService;
import com.smallmq.product.feign.SearchFeignService;
import com.smallmq.product.feign.WareFeignService;
import com.smallmq.product.service.*;
import com.smallmq.product.vo.*;
import com.smallmq.to.SkuReductionTo;
import com.smallmq.to.SpuBoundTo;
import com.smallmq.to.es.SkuEsModel;
import com.smallmq.utils.PageUtils;
import com.smallmq.utils.Query;
import com.smallmq.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
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

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;

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
                    }).filter(entity -> {
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

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);

    }

    @Override
    public void up(Long spuId) {
        List<SkuEsModel> list = new ArrayList<>();
        SkuEsModel skuEsModel = new SkuEsModel();
        // 查出当前spuId对应的所有sku信息，品牌的名字
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // TODO 4、查询当前sku的所有属性信息
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);

        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());


        // TODO 1、发送远程调用，库存系统查询是否有库存
        Map<Long, Boolean> stockMap = null;
        try {
            List<SkuHasStockVo> skuHasStock = wareFeignService.getSkuHasStock(skuIds);
            stockMap = skuHasStock.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        } catch (Exception e) {
            log.error("库存服务查询异常：原因{}", e);
        }

        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> list1 = skuInfoEntities.stream().map(skuInfoEntity -> {
            SkuEsModel skuEsModel1 = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel1);
            skuEsModel1.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel1.setSkuImg(skuInfoEntity.getSkuDefaultImg());

            // TODO 2、热度评分。0

            if (finalStockMap == null) {
                skuEsModel1.setHasStock(true);
            } else {
                skuEsModel1.setHasStock(finalStockMap.get(skuInfoEntity.getSkuId()));
            }
            skuEsModel1.setHotScore(0L);
            // TODO 3、查询品牌和分类的名字信息
            BrandEntity brandEntity = brandService.getById(skuInfoEntity.getBrandId());
            skuEsModel1.setBrandName(brandEntity.getName());
            skuEsModel1.setBrandImg(brandEntity.getLogo());

            CategoryEntity categoryEntity = categoryService.getById(skuInfoEntity.getCatalogId());
            skuEsModel1.setCatalogName(categoryEntity.getName());

            skuEsModel1.setAttrs(attrsList);

            return skuEsModel1;
        }).collect(Collectors.toList());
        // TODO 5、将数据发送给es进行保存：gulimall-search
        R r = searchFeignService.productStatusUp(list1);
        if (r.getCode() == 0) {
            // 远程调用成功
            // TODO 6、修改当前spu的状态
            this.baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            // 远程调用失败
            // TODO 7、重复调用？接口幂等性：重试机制
        }

    }
}