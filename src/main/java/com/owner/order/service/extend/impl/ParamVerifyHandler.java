package com.owner.order.service.extend.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.owner.order.service.extend.CreateOrderHandler;
import com.owner.order.vo.OrderReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 参数校验规则
 */
@Component
@Slf4j
@Order(6)
public class ParamVerifyHandler implements CreateOrderHandler {
    @Override
    public boolean canHandle() {
        return true;
    }
    @Override
    public void beforeToDB(OrderReqVO vo) {
        log.info("ParamVerifyHandler,参数校验逻辑");
        /*boolean haha = SourceTypeEnum.HAHA.getValue().equals(vo.getSourceType());
        boolean jpgk = SourceTypeEnum.JPGK.getValue().equals(vo.getSourceType());
        if ((haha || jpgk) && DeductFlagEnum.DEDUCT.getType().equals(vo.getDeductFlag())) {
            throw new NoWarnException("201", "哈哈渠道订单不支持发起补扣");
        }
        if (SourceTypeEnum.ZY_SOURCING.getValue().equals(vo.getSourceType())) {
            if (CollectionUtils.isEmpty(vo.getGoodsList())) {
                throw new OrderException(ReturnStatusEnum.GOODS_NULL_ERROR);
            }
            for (OrderGoods orderGoods : vo.getGoodsList()) {
                if (orderGoods.getQuantity() == null || orderGoods.getQuantity() <= 0) {
                    throw new OrderException(ReturnStatusEnum.GOODS_QUANTITY_ERROR);
                }
                if (orderGoods.getRealPrice() == null || orderGoods.getRealPrice().longValue() <= 0L) {
                    throw new OrderException(ReturnStatusEnum.GOODS_PRICE_ERROR);
                }
            }
        }*/
    }
}
