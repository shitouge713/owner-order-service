package com.owner.order.dao.es.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * <p>
 * 基础的ES索引传输对象
 * </p>
 *
 * @author sxl
 * @version 1.0
 * @date 7/24/22 5:28 PM
 */
@Getter
@Setter
@ToString(callSuper = true)
public class BaseEsIndexDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 索引创建时间
     */
    @Field(type = FieldType.Long)
    private long esCreateTime = System.currentTimeMillis();
}
