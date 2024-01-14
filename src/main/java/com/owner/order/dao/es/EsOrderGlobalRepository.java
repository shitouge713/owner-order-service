package com.owner.order.dao.es;

import com.owner.order.dao.es.dto.EsOrderGlobalDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface EsOrderGlobalRepository extends ElasticsearchRepository<EsOrderGlobalDto, String> {

    // 根据姓名查询
    List<EsOrderGlobalDto> findByUserName(String userName);
}
