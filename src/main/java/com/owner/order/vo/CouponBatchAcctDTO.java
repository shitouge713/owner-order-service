package com.owner.order.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 券账户承担比例信息
 * </p>
 *
 * @author zhaojignlei
 * @version V1.0
 * @date 2024-03-21
 */
@Data
public class CouponBatchAcctDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 券批次分组编号
     */
    private String groupCode;

    /**
     * 券批次分组名称
     */
    private String groupName;

    /**
     * 成本账户id
     */
    private String acctId;
    /**
     * 账户子类型 1 平台 2 商家
     */
    private Integer acctSubType;

    /**
     * 成本账户名称
     */
    private String acctName;

    /**
     * 成本账户预算id
     */
    private String budgetId;

    /**
     * 承担比例
     */
    private BigDecimal assumeRatio;

    
}
