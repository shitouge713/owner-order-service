package com.owner.order.dao.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private Integer userAge;
}
