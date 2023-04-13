package com.owner.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 远程调用响应结果
 *
 * @author JiaoJinxin
 * @date 2023/1/5 14:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoteResult<T> implements Serializable {

    private static final long serialVersionUID = 4977414568461601619L;

    /**
     * 响应编号
     */
    private String code;
    /**
     * 返回文字描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 业务内容
     */
    private T content;

    /**
     * 数据条数，非查询为-1
     */
    private Integer count;

    /**
     * 耗时
     */
    private Long timeCost;

    /**
     * 是否成功响应
     */
    private Boolean success;
}
