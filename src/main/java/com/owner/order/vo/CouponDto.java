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
 * 订单优惠券表 页面展示实体
 * </p>
 *
 * @author pengdonglan
 * @version V1.0
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode
@ApiModel(value="Coupon页面展示实体", description="订单优惠券表页面展示实体")
public class CouponDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "券编号")
    private String couponCode;

    @ApiModelProperty(value = "券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponAmount;

/*    @ApiModelProperty(value = "已使用优惠券金额")
    private BigDecimal usedAmount;*/

    @ApiModelProperty(value = "券类型 1立减 2满减  3 折扣")
    private Integer couponType;

    @ApiModelProperty(value = "券批次号")
    private String couponBatchNumber;

    @ApiModelProperty(value = "券批次号名称")
    private String couponBatchName;

    /**
     * 券类型：1:优惠券,2:运费券
     */
    @ApiModelProperty(value = "券类型：1:优惠券,2:运费券")
    private Integer voucherType;

    @ApiModelProperty(value = "券包类型: 1-平台券, 2-商户券")
    private Integer groupType;

    @ApiModelProperty(value = "优惠券账户分摊信息")
    private List<CouponBatchAcctDTO> couponAcctList;
}
