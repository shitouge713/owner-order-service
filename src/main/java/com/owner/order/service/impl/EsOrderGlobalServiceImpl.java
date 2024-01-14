package com.owner.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.owner.order.dao.es.EsOrderGlobalRepository;
import com.owner.order.dao.es.dto.EsOrderGlobalDto;
import com.owner.order.dao.pojo.UserInfo;
import com.owner.order.model.request.MerchantOrderReq;
import com.owner.order.service.IEsOrderGlobalService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class EsOrderGlobalServiceImpl implements IEsOrderGlobalService {
    @Autowired
    private EsOrderGlobalRepository esOrderGlobalRepository;

    @Override
    public Boolean syncToEsByLinkData(UserInfo userInfo) {
        try {
            if (Objects.isNull(userInfo) || StringUtils.isBlank(userInfo.getId())) {
                return Boolean.FALSE;
            }
            EsOrderGlobalDto esOrderGlobalDto = new EsOrderGlobalDto();
            BeanUtil.copyProperties(userInfo, esOrderGlobalDto, Boolean.TRUE);
            //设置主键，唯一
            esOrderGlobalDto.setId(userInfo.getId());
            esOrderGlobalRepository.save(esOrderGlobalDto);
            log.info("success,syncToEsByLinkData,id:{}", userInfo.getId());
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("fatalError,syncToEsByLinkData,id:{},e:", userInfo.getId(), e);
            return Boolean.FALSE;
        }
    }

    @Override
    public void deleteById(String id) {
        esOrderGlobalRepository.deleteById(id);
    }

    @Override
    public void findOne() {
        Optional<EsOrderGlobalDto> optional = esOrderGlobalRepository.findById("1666eb47-0bbf-468b-ab45-07758c741461");
        System.out.println(optional.get());
    }

    @Override
    public void findAll() {
        Iterable<EsOrderGlobalDto> all = esOrderGlobalRepository.findAll();
        all.forEach(user -> System.out.println(user));
    }

    @Override
    public void findAllSort() {
        Iterable<EsOrderGlobalDto> all = esOrderGlobalRepository.findAll(Sort.by(Sort.Order.asc("age")));
        all.forEach(user -> System.out.println(user));
    }

    @Override
    public void findPage() {
        //PageRequest.of 参数1: 当前页-1
        Page<EsOrderGlobalDto> search = esOrderGlobalRepository.search(QueryBuilders.matchAllQuery(), PageRequest.of(1, 1));
        search.forEach(user -> System.out.println(user));
    }

    @Override
    public void findByName() {
        esOrderGlobalRepository.findAll();
    }

    @Override
    public void searchBySql() {
        NativeSearchQueryBuilder queryBuilder = createQueryBuilder(req);
    }

    /**
     * 根据输入条件拼接elasticsearch查询语句
     */
    private NativeSearchQueryBuilder createQueryBuilder(MerchantOrderReq req) {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder mainBoolQuery = QueryBuilders.boolQuery();
        queryBuilder.withFilter(mainBoolQuery);
        //拼接goodsIds过滤条件
        createGoodsIdsFilter(req.getGoodsIds(), mainBoolQuery);
        queryBuilder.withFilter(mainBoolQuery);
        //分页
        createPageable(req, queryBuilder);
        //排序信息后期添加
        createGoodsOrder(req.getOrder(), queryBuilder);
        return queryBuilder;
    }

    /**
     * 拼接goodsIds过滤条件
     */
    protected void createGoodsIdsFilter(Set<String> goodsIds, BoolQueryBuilder boolQueryFilter) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            return;
        }
        BoolQueryBuilder goodsIdsFilter = QueryBuilders.boolQuery();
        for (String goodsId : goodsIds) {
            if (StringUtils.isNotBlank(goodsId)) {
                goodsIdsFilter.should(QueryBuilders.termQuery("goodsId", goodsId));
            }
        }

        boolQueryFilter.filter(goodsIdsFilter);
    }


}