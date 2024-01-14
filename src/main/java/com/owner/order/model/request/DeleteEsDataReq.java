package com.owner.order.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class DeleteEsDataReq implements Serializable {

    @NotEmpty(message = "id不能为空")
    private String id;
}
