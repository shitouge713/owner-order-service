package com.owner.order.dao.es.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hsh.common.dto.PageResultV2;
import com.hsh.common.exception.SearchParentException;
import com.hsh.common.utils.DateUtils;
import com.hsh.search.domain.util.LocalBeanUtils;
import com.hsh.search.model.dto.search.SearchSkuDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ElasticsearchRestTemplate 通用模板方法
 * 使用实体必须实现
 * @see org.springframework.data.elasticsearch.annotations.Document
 * @see org.springframework.data.annotation.Id
 * 两个注解
 * ex:
 * @see com.hsh.search.model.dto.search.SearchSkuDto
 * @author bufan
 * @date 2023/7/17 上午11:39
 **/
@Slf4j
@Service
public class GeneralEsTemplate {

    @Resource
    private ElasticsearchRestTemplate esTemplate;
    @Resource
    private RestHighLevelClient client;
    /**
     * 保存每个索引的operations
     */
    private static final Map<String, IndexOperations> OPS_MAP = new ConcurrentHashMap<>();
    private static final FastJsonForEsDateAnnotationFilter esDateFilter = new FastJsonForEsDateAnnotationFilter();

    /**
     * 根据文档ID获取文档
     *
     * @param id     文档ID
     * @param tClass 索引dto class
     * @param <T>    泛型
     * @return
     */
    public <T> T get(String id, Class<T> tClass) {
        return esTemplate.get(id, tClass);
    }

    /**
     * 分页查询
     *
     * @param queryBuilder 查询条件
     * @param tClass       查询对象class
     * @param returnClass  返回对象class
     * @param pageNo       页码
     * @param pageSize     每页数量
     * @param <T>          查询对象
     * @param <R>          返回对象
     * @return
     */
    public <T, R> PageResultV2<R> queryPage(NativeSearchQueryBuilder queryBuilder, Class<T> tClass, Class<R> returnClass, int pageNo, int pageSize) {
        if (pageNo < 0 || pageSize < 0) {
            throw new SearchParentException("分页数据不能为负数");
        }
        return this.queryPageWithFields(queryBuilder, tClass, returnClass, null, pageNo, pageSize);
    }

    /**
     * 分页查询，返回所有字段
     *
     * @param queryBuilder 查询条件
     * @param tClass       查询对象class
     * @param pageNo       页码
     * @param pageSize     每页数量
     * @param <T>          泛型
     * @return
     */
    public <T> PageResultV2<T> queryPage(NativeSearchQueryBuilder queryBuilder, Class<T> tClass, int pageNo, int pageSize) {
        return this.queryPageWithFields(queryBuilder, tClass, null, pageNo, pageSize);
    }

    /**
     * 分页查询，返回指定字段
     * 包含对象转换
     *
     * @param queryBuilder   查询条件
     * @param tClass         查询对象class
     * @param fieldsNameList 返回的字段名称
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param <T>            泛型
     * @return
     */
    public <T> PageResultV2<T> queryPageWithFields(NativeSearchQueryBuilder queryBuilder, Class<T> tClass, List<String> fieldsNameList, int pageNo, int pageSize) {
        SearchHits<T> res = queryPageBase(queryBuilder, tClass, fieldsNameList, pageNo, pageSize);
        if (Objects.isNull(res)) {
            return new PageResultV2<>();
        }
        List<T> list = res.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return buildPageResult(list, res.getTotalHits(), pageNo, pageSize);
    }

    /**
     * 分页查询，返回指定字段
     * 包含对象转换
     *
     * @param queryBuilder   查询条件
     * @param tClass         查询对象class
     * @param returnClass    返回对象class
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param <T>            查询对象
     * @param <R>            返回对象
     * @param fieldsNameList 返回的字段名称
     * @return
     */
    public <T, R> PageResultV2<R> queryPageWithFields(NativeSearchQueryBuilder queryBuilder, Class<T> tClass, Class<R> returnClass, List<String> fieldsNameList, int pageNo, int pageSize) {
        SearchHits<T> res = queryPageBase(queryBuilder, tClass, fieldsNameList, pageNo, pageSize);
        if (Objects.isNull(res)) {
            return new PageResultV2<>();
        }
        List<R> list = LocalBeanUtils.collection2List(res.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()), returnClass);
        return buildPageResult(list, res.getTotalHits(), pageNo, pageSize);
    }

    private <T> SearchHits<T> queryPageBase(NativeSearchQueryBuilder queryBuilder, Class<T> tClass, List<String> fieldsNameList, int pageNo, int pageSize) {
        if (pageNo < 0 || pageSize < 0) {
            throw new SearchParentException("分页数据不能为负数");
        }

        //分页
        queryBuilder.withPageable(PageRequest.of(pageNo - 1, pageSize));
        if (!CollectionUtils.isEmpty(fieldsNameList)) {
            queryBuilder.withFields(fieldsNameList.toArray(new String[0]));
        }
        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        log.info("es queryPage | query {}", JSON.toJSONString(nativeSearchQuery));
        SearchHits<T> res = esTemplate.search(nativeSearchQuery, tClass);
        log.info("es queryPage | hasRes {} , total {} , list.size {}", res.hasSearchHits(), res.getTotalHits(), res.getSearchHits().size());
        if (!res.hasSearchHits()) {
            return null;
        }
        return res;
    }

    private <T> PageResultV2<T> buildPageResult(List<T> list, long totalHits, int pageNo, int pageSize) {
        PageResultV2<T> pageResult = new PageResultV2<>();
        pageResult.setPageNo(pageNo);
        pageResult.setPageSize(pageSize);
        pageResult.setPageTotal(list.size());
        pageResult.setTotal((int) totalHits);
        int max = totalHits == 0 ? 1 : (int) Math.ceil((double) totalHits / (double) pageSize);
        pageResult.setMaxPage(max);
        pageResult.setList(list);
        return pageResult;
    }

    /**
     * 根据条件查询对象
     *
     * @param query  查询条件
     * @param tClass 查询对象class
     * @param <T>    泛型
     * @return
     */
    public <T> SearchHits<T> search(NativeSearchQueryBuilder query, Class<T> tClass) {
        return esTemplate.search(query.build(), tClass);
    }

    /**
     * 添加单个文档
     * 添加后立刻刷新缓存
     * 有可能带来性能损耗
     *
     * @param t   对象实体
     * @param <T> 泛型
     */
    public <T> void add(T t) {
        esTemplate.save(t, esTemplate.getIndexCoordinatesFor(t.getClass()));
        refreshData(t.getClass().toString(), t.getClass());
    }

    private void refreshData(String key, Class tClass) {
        if (OPS_MAP.containsKey(key)) {
            OPS_MAP.get(key).refresh();
        } else {
            synchronized (this) {
                if (OPS_MAP.containsKey(key)) {
                    OPS_MAP.get(key).refresh();
                } else {
                    OPS_MAP.put(key, esTemplate.indexOps(tClass));
                    log.info("refreshData| add indexOps,class {},map size {}", tClass, OPS_MAP.size());
                    OPS_MAP.get(key).refresh();
                }
            }
        }
    }

    /**
     * 添加单个文档
     *
     * @param t   对象实体
     * @param <T> 泛型
     */
    public <T> void addWithoutRefresh(T t) {
        esTemplate.save(t, esTemplate.getIndexCoordinatesFor(t.getClass()));
    }

    /**
     * 根据文档ID更新数据
     *
     * @param id  id
     * @param t   实体
     * @param <T> 泛型
     */
    public <T> void updateById(String id, T t) {
        String json = JSON.toJSONString(t, esDateFilter);
        Document document = Document.parse(json);
        UpdateQuery query = UpdateQuery.builder(id).withDocument(document).withRefresh(UpdateQuery.Refresh.True).build();
        log.info("updateById query: {}", JSON.toJSONString(query));
        UpdateResponse response = esTemplate.update(query, esTemplate.getIndexCoordinatesFor(t.getClass()));
        log.info("updateById resp: {}", JSON.toJSONString(response));
    }

    /**
     * 批量更新
     * 刷新缓冲区
     * @param list
     * @param <T>
     */
    public <T> void bulkUpdate(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        this.bulkUpdateBase(list);
        this.refreshData(list.get(0).getClass().toString(), list.get(0).getClass());
    }

    /**
     * 批量更新
     * 不更新缓冲区
     * ES自动刷新 ，有一定延迟，默认1s左右
     * @param list
     * @param <T>
     */
    public <T> void bulkUpdateWithOutRefresh(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        bulkUpdateBase(list);
    }

    private <T> void bulkUpdateBase(List<T> list) {
        List<UpdateQuery> updateQueries = Lists.newArrayList();
        Field field = getIdAnnotationField(list.get(0).getClass());
        list.forEach(item -> {
            String json = JSON.toJSONString(item, esDateFilter);
            Document document = Document.parse(json);
            document.setId(getIdByEntity(item, field));
            UpdateQuery build = UpdateQuery.builder(document.getId())
                    .withDocument(document)
                    .build();
            updateQueries.add(build);
        });
        log.info("bulkUpdate | class {} ,size {}", list.get(0).getClass(), updateQueries.size());
        esTemplate.bulkUpdate(updateQueries, esTemplate.getIndexCoordinatesFor(list.get(0).getClass()));
        log.info("bulkUpdate | class {} ,size {}  end ", list.get(0).getClass(), updateQueries.size());
    }


    public <T> void deleteById(String id, Class<T> tClass) {
        String str = esTemplate.delete(id, tClass);
        log.info("deleteById resp: {}", JSON.toJSONString(str));
    }

    /**
     * 根据条件更新字段值
     * 不能解决嵌套
     *
     * @param tClass          索引dto
     * @param fieldName       要更改的字段名
     * @param fieldValue      要更改的字段值
     * @param queryFieldName  查询条件字段名
     * @param queryFieldValue 查询条件字段值
     * @param <T>             索引dto
     */
    public <T> void updateFieldByQuery(Class<T> tClass, String fieldName, Object fieldValue, String queryFieldName, String queryFieldValue) {
        try {
            UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(esTemplate.getIndexCoordinatesFor(tClass).getIndexName());
            Map<String, Object> paramsMap = Maps.newHashMap();
            paramsMap.put(fieldName, fieldValue);
            ScriptType type = ScriptType.INLINE;
            String lang = "painless";
            updateByQueryRequest.setScript(new Script(type, lang, "ctx._source." + fieldName + "= params." + fieldName, paramsMap));
            updateByQueryRequest.setQuery(new TermQueryBuilder(queryFieldName, queryFieldValue));
            updateByQueryRequest.setMaxDocs(1000);
            updateByQueryRequest.setBatchSize(1000);
            updateByQueryRequest.setAbortOnVersionConflict(false);
            updateByQueryRequest.setRefresh(true);
            client.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.info("updateFieldByQuery 按单个条件更新单个字段报错 {}", e.getLocalizedMessage());
            throw new SearchParentException(e.getLocalizedMessage());
        }
    }

    /**
     * 根据注解获取实体类关联es属性的id值
     *
     * @return java.lang.String
     * @author Innocence
     * @date 2021/6/16
     */
    private <T> String getIdByEntity(T t, Field field) {
        try {
            String id = (String) field.get(t);
            if (StringUtils.isNotBlank(id)) {
                return id;
            } else {
                log.info("获取对象 ID 失败,id is null");
                throw new SearchParentException("获取对象 ID 失败,id is null");
            }
        } catch (IllegalAccessException e) {
            log.info("获取对象 ID 失败 ", e);
            throw new SearchParentException("获取对象 ID 失败 " + e.getLocalizedMessage());
        }
    }

    private Field getIdAnnotationField(Class clazz) {
        List<Field> fieldList = Lists.newArrayList();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        org.springframework.data.annotation.Id annotation;
        for (Field field : fieldList) {
            annotation = field.getAnnotation(org.springframework.data.annotation.Id.class);
            if (Objects.nonNull(annotation)) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new SearchParentException("获取对象 ID注解字段 失败");
    }

    /**
     * fastjson 转JSON时，处理带有ES Field 注解的过滤器
     */
    static class FastJsonForEsDateAnnotationFilter implements ValueFilter {
        @Override
        public Object process(Object object, String name, Object value) {
            try {
                Field field = object.getClass().getDeclaredField(name);
                // 获取注解
                org.springframework.data.elasticsearch.annotations.Field annotation =
                        field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class);
                if (Objects.nonNull(annotation)
                        && annotation.type().equals(FieldType.Date)
                        && StringUtils.isNotBlank(annotation.pattern())
                        && Objects.nonNull(value)) {
                    //进行转换
                    LocalDateTime localDateTime = (LocalDateTime) value;
                    value = DateUtils.formatDateToString(localDateTime, annotation.pattern());
                } else if (value instanceof LocalDateTime) {
                    value = Timestamp.valueOf((LocalDateTime) value).getTime();
                }
            } catch (NoSuchFieldException e) {
                return value;
            } catch (Exception e) {
                log.error("FastJsonForEsDateAnnotationFilter|process err", e);
                return value;
            }
            return value;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {

//        SearchSkuDto dto = new SearchSkuDto();
//        dto.setGmtCreate(LocalDateTime.now());
//        SkuMedia media = new SkuMedia();
//        media.setMediaName("test");
//        media.setGmtCreate(LocalDateTime.now());
//        List<SkuMedia> list = Lists.newArrayList();
//        list.add(media);
//        dto.setSkuMediaList(list);
//
//        SearchPromotionDto promotionDto = new SearchPromotionDto();
//        promotionDto.setSeckillBeginTime(LocalDateTime.now());
//        promotionDto.setGroupCode("111");
//        List<SearchPromotionDto> list2 = Lists.newArrayList();
//        list2.add(promotionDto);
//        dto.setPromotionList(list2);
//        FastJsonForEsDateAnnotationFilter serializerFeature = new FastJsonForEsDateAnnotationFilter();
//        String json = JSON.toJSONString(dto, serializerFeature);


        System.out.println("end");
    }

}
