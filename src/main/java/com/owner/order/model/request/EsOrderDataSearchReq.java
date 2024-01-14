package com.owner.order.model.request;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 *
 */
public class EsOrderDataSearchReq implements Serializable {

    @NotEmpty(message = "id不能为空")
    private String id;
}
