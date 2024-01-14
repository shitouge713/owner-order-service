package com.owner.order.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.owner.order.model.request.base.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商户订单列表请求实体
 * </p>
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "MerchantOrderReqVO实体", description = "商户订单列表请求实体")
public class MerchantOrderReq extends BasePageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "商户code不能为空")
    @ApiModelProperty(value = "商户code")
    private String merchantCode;

    @ApiModelProperty(value = "订单状态集合")
    private List<Integer> orderStatusList;

    @ApiModelProperty(value = "设备编号集合")
    private List<String> deviceNoList;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "搜索字段")
    private String searchKey;

    @ApiModelProperty(value = "代理人id(代运营商Id)")
    private String merchantSubId;

    @ApiModelProperty(value = "代理人名称(代运营商名称)")
    private String merchantSubName;

    @ApiModelProperty(value = "业务状态code 0-开门中  10-已开门  20-识别中（已关门）  30-识别中 （已上传）  45-异常 70-无购物 " +
            "100待付款 、200备餐中、300待配送、400待入柜 、500出货中、600已完成、700 退款成功、701 退款处理中、702 退款失败、800已取消 、900异常订单")
    private List<Integer> processStatusList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "申请开门时间起")
    private LocalDateTime applyOpenTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "申请开门时间止")
    private LocalDateTime applyOpenTimeEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "货柜开门起")
    private LocalDateTime openTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "货柜开门止")
    private LocalDateTime openTimeEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "订单支付时间开始范围")
    private LocalDateTime payTimeBegin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "订单支付时间结束范围")
    private LocalDateTime payTimeEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "订单结束时间开始范围")
    private LocalDateTime endTimeBegin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "订单结束时间结束范围")
    private LocalDateTime endTimeEnd;

    @ApiModelProperty(value = "退款标识")
    private Integer refundFlag = 0;

    @ApiModelProperty(value = "查询表示 默认商家端")
    private Integer querySourceType = 2;

    @ApiModelProperty(value = "场地id列表")
    private List<String> siteIdList;
}
