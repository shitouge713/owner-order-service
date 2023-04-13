package com.owner.order.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.owner.order.dao.pojo.OCart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OCartMapper extends BaseMapper<OCart> {

}