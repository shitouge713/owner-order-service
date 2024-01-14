package com.owner.order.model.request.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version V1.0
 * @date 2022-04-20
 */
@Data
@ApiModel(value = "基础分页请求")
public class BasePageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页数", required = true)
    @NotNull(message = "当前页数不能为空")
    @Min(value = 1, message = "当前页数最小为1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页个数", required = true)
    @NotNull(message = "每页个数不能为空")
    @Range(min = 1, max = 1000, message = "每页个数1~1000")
    private Integer pageSize;

    @ApiModelProperty(value = "系统标识", required = true)
    @NotEmpty(message = "系统标识")
    private String appKey;

    @ApiModelProperty(value = "业务线编号")
    private String busCode;

    @ApiModelProperty(value = "场景线编号")
    private String sceneCode;

    @ApiModelProperty(value = "功能线编号")
    private String functionCode;

    @ApiModelProperty(value = "最后Id,导出接口第二页开始必传，pageNum>=2必传")
    private Long lastId;

}
