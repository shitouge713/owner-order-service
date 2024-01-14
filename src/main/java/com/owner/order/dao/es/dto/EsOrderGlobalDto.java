package com.owner.order.dao.es.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Version 1.0.0
 * @Date 2023/09/15
 */
@Getter
@Setter
@ToString(callSuper = true)
@Document(indexName = "#{esOrderGlobalIndexName}", replicas = 2)
public class EsOrderGlobalDto extends BaseEsIndexDto {
    private static final long serialVersionUID = 1L;

    @Field(type = FieldType.Keyword, analyzer = "ik_max_word")
    private String userName;

    @Field(type = FieldType.Integer)
    private Integer userAge;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String userAdress;

    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date bir;

}
