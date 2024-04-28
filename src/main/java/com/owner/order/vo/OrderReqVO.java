package com.owner.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


/**
 * <p>
 * 订单下单请求展示实体
 * </p>
 *
 * @author pengdonglan
 * @version V1.0
 * @date 2022-04-21
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "OrderReqVO实体", description = "订单下单请求实体")
public class OrderReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "原订单编码 补扣工单创建的补扣单该字段不为空")
    private String originOrderNoSub;

    @NotEmpty(message = "用户Id非空")
    @ApiModelProperty(value = "用户Iid", required = true)
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userMobile;

    @ApiModelProperty(value = "订单商品总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "订单实付金额")
    private BigDecimal payAmount;

    //默认给个0做兼容
    @ApiModelProperty(value = "订单优惠金额")
    private BigDecimal disctAmount = new BigDecimal(0);

    @NotNull(message = "订单来源非空")
    @ApiModelProperty(value = "来源，1：预制菜；2：供应链；3:619；4：众包；5：易触；6：微米；7:美智；8:到店；9:到家；10:天波；11:自研")
    private Integer sourceType;

 /*   @ApiModelProperty(value = "拆单规则，1：店铺；2：仓库；3：物流公司")
    private Integer splitType;*/

    @ApiModelProperty(value = "支付流水号")
    private String payNo;

    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;

    @ApiModelProperty(value = "支付方式:1:微信h5支付,6:免密支付")
    private Integer payType;

    @ApiModelProperty(value = "支付渠道")
    private Integer payChannel;

    @ApiModelProperty(value = "支付状态: 1:待支付,2:支付中,3:支付失败,4:支付成功")
    private Integer payStatus;

    @ApiModelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "渠道,1、自营渠道：商城主站,2、三方渠道：天猫/抖音/京东旗舰店,3、分销渠道：个人分销/机构分销/其他")
    private Integer channel;

    @ApiModelProperty(value = "外部订单id	目前是微信支付分支付返回的订单号")
    private String outOrderId;

    //@NotEmpty(message = "创建人非空")
    @ApiModelProperty(value = "创建人id", required = true)
    private String createId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shipTime;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "订单状态：100-待付款 200-备餐中 300-待配送 400-待入柜 500-待取货 600-已完成 700-已退款 701-部分退款 800-已取消  900异常订单")
    private Integer orderStatus;

    @ApiModelProperty(value = "取消类型，1：超时取消；2：用户取消")
    private Integer cancelType;

    @ApiModelProperty(value = "取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime cancelTime;

    @ApiModelProperty(value = "用户地址id")
    private String clientAddrId;

    @ApiModelProperty(value = "用户地址(冗余字段)，临时为预制菜所加")
    private String clientAddr;

    @ApiModelProperty(value = "优惠券编号")
    private String couponCode;

    @ApiModelProperty(value = "取货码")
    private String takeFoodsCode;

    @ApiModelProperty(value = "取货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime takeFoodsTime;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @ApiModelProperty(value = "商户截单时间")
    private LocalTime cutOrderTime;

    @ApiModelProperty(value = "商家编号")
    private String merchantCode;

    @ApiModelProperty(value = "商家类型,1、货柜：直营，柜主（购买/租赁），点位主（代运营）2、门店：直营/加盟")
    private Integer merchantType;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "商户是否在白名单中 0否 1是")
    private Integer onWhite;

    @ApiModelProperty(value = "POI类型  1智能货柜 2门店")
    private Integer poiType;

    @ApiModelProperty(value = "POI编号")
    private String poiCode;

    @ApiModelProperty(value = "POI名称")
    private String poiName;

    @ApiModelProperty(value = "开柜记录号")
    private String applyCode;

    @ApiModelProperty(value = "点位Id")
    private String poiId;

    @ApiModelProperty(value = "点位地址")
    private String poiAddress;

    //@NotNull(message = "商品列表非空")
    @ApiModelProperty(value = "商品列表")
    private List<OrderGoods> goodsList;

    @ApiModelProperty(value = "开柜记录视频url")
    private List<String> videoUrls;

    @ApiModelProperty(value = "申请开门时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applyOpenTime;

    @ApiModelProperty(value = "设备开门成功时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deviceOpenTime;

    @ApiModelProperty(value = "外部订单号（易触）")
    private String outOrderNo;

    @ApiModelProperty(value = "易触支付成功通知地址")
    private String outNotifyUrl;

    @ApiModelProperty(value = "营销信息")
    private CouponDto couponInfo;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备品牌")
    private String deviceBrand;

    @ApiModelProperty(value = "厂商设备id")
    private String productDeviceId;

    @ApiModelProperty(value = "一级设备类型code")
    private String productType;

    @ApiModelProperty(value = "一级设备类型code")
    private String deviceType;

    @ApiModelProperty(value = "三级型号code")
    private String modelCode;

    @ApiModelProperty(value = "三级型号名称")
    private String deviceModelName;

    @ApiModelProperty(value = "运营模式 1自营 2代运营")
    private Integer operateType;

    @ApiModelProperty(value = "签约合同编码")
    private String contractCode;

    @ApiModelProperty(value = "省份code")
    private String provinceCode;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "市名")
    private String cityName;

    @ApiModelProperty(value = "县code")
    private String countyCode;

    @ApiModelProperty(value = "县名")
    private String countyName;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "一级场景code")
    private String firstScenesCode;

    @ApiModelProperty(value = "一级场景名称")
    private String firstScenesName;

    @ApiModelProperty(value = "二级场景code")
    private String secondScenesCode;

    @ApiModelProperty(value = "二级场景名称")
    private String secondScenesName;

    @ApiModelProperty(value = "是否为补扣单 0 否 1异常工单补扣单 2补扣工单补扣单")
    private Integer deductFlag;

    @ApiModelProperty(value = "设备白名单 0否1是（1表示走自己的IOT和算法）,2 表示收付通白名单（临时方案）")
    private Integer deviceOnWhite;

    @ApiModelProperty(value = "购物行为id")
    private String buyApplyId;

    @ApiModelProperty(value = "代理人id")
    private String merchantSubId;

    @ApiModelProperty(value = "代理人名称")
    private String merchantSubName;

    @ApiModelProperty(value = "参考枚举PlatformEnum")
    private Integer platform;

    @ApiModelProperty(value = "platform版本")
    private String platformVersion;

    @ApiModelProperty(value = "经营属性 1-自营 2-加盟")
    private Integer businessProperty;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "免密支付域值金额,默认20000分")
    private Integer scorePayLimit = 20000;

    @ApiModelProperty(value = "免密支付商品数量 默认100")
    private Integer goodAmountLimit = 100;

    @ApiModelProperty(value = "是否为免密付款")
    private Boolean nonSecretPay = true;

    @ApiModelProperty(value = "是否走收付通")
    private Boolean onSft;

    @ApiModelProperty(value = "是否刷脸支付")
    private Boolean facePay;

    @ApiModelProperty(value = "全局唯一交易流水号 开门柜业务需要（sourceType=3/11）")
    private String globalNumber;

    /**
     * com.hsh.order.model.enums.SettlementChannelEnum
     * 分账渠道，1：微信，3：小恒钱包  6:收付通
     */
    @ApiModelProperty(value = "是否开通钱包")
    private Integer settlementChannel;

    @ApiModelProperty(value = "是否是KA标签 0:其他 ，1:中邮")
    private Integer kaType;

    @ApiModelProperty(value = "金额和积分的换算比例")
    private BigDecimal convertRate;

    @ApiModelProperty(value = "设备分类类型")
    private Integer deviceKindType;

    @ApiModelProperty(value = "异常标签")
    private String errorCode;

    @ApiModelProperty(value = "货柜点位")
    private String locationCode;

    @ApiModelProperty(value = "场地id")
    private String siteId;

    @ApiModelProperty(value = "代理商id")
    private String agentId;

    @ApiModelProperty(value = "货道号")
    private String aisleCode;

    @ApiModelProperty(value = "是否命中活动")
    private Boolean hitOffer;
}
