package com.owner.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单详情表 页面展示实体
 * </p>
 *
 * @author pengdonglan
 * @version V1.0
 * @date 2022-04-22
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "OrderGoods页面展示实体", description = "订单详情表页面展示实体")
public class OrderGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "商品编码")
    private String skuCode;

    @ApiModelProperty(value = "品牌")
    private String skuBrand;

    @ApiModelProperty(value = "三级分类id")
    private String skuThirdCid;

    @ApiModelProperty(value = "商品缩略图")
    private String skuUrl;

    @ApiModelProperty(value = "商品原价")
    private BigDecimal oriPrice;

    @ApiModelProperty(value = "商品现价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "商品原价积分")
    private BigDecimal oriScore;

    @ApiModelProperty(value = "商品现价积分")
    private BigDecimal realScore;

    @ApiModelProperty(value = "购买商品数量")
    private Integer quantity;

    @ApiModelProperty(value = "当前订单此商品总金额")
    private BigDecimal skuAmount;

    @ApiModelProperty(value = "当前订单此商品总积分")
    private BigDecimal skuScore;

    @ApiModelProperty(value = "货道编号，微米专用,0-A00,第一个柜，第一层，第一个货道")
    private String aisleCode;

    @ApiModelProperty(value = "货道排序，微米专用")
    private Integer aisleOrder;

    @ApiModelProperty(value = "货道状态，微米专用")
    private Integer aisleStatus;

    @ApiModelProperty(value = "商品规格")
    private String productSpec;

   @ApiModelProperty(value = "spu编码")
   private String spuId;

   @ApiModelProperty(value = "商品储存方式编码:常温:normal、冷藏:preserve、冷冻:freeze")
   private String storageStyleCode;

    @ApiModelProperty(value = "门类型 默认0-单门  1-左门 2-右门")
    private Integer doorType;

    @ApiModelProperty(value = "标品条码")
    private String standardBarCode;

    @ApiModelProperty("重量")
    private String weight;

    @ApiModelProperty("长度")
    private String length;

    @ApiModelProperty("宽度")
    private String width;

    @ApiModelProperty("高度")
    private String height;

    @ApiModelProperty(value = "goods编码")
    private String goodsCode;

    @ApiModelProperty(value = "活动信息列表")
    private List<String> promoList;
}
