package com.owner.order.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Configuration
@EnableConfigurationProperties(EsIndexProperties.class)
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchConfig.class);

    private final EsIndexProperties esIndexProperties;

    public ElasticsearchConfig(EsIndexProperties esIndexProperties) {
        this.esIndexProperties = esIndexProperties;
    }

    @Bean
    public String esOrderGlobalIndexName() {
        return esIndexProperties.getOrderGlobalIndexName();
    }


    @Bean
    public String esUrl() {
        return esIndexProperties.getEsUrl();
    }

    @Bean
    public String esUserName() {
        return esIndexProperties.getEsUserName();
    }

    @Bean
    public String esUserPassword() {
        return esIndexProperties.getEsUserPassword();
    }

    @Bean
    RestHighLevelClient client() {
        log.info("Start init elasticsearch client : " + esIndexProperties.toString());
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esUrl())
                .withBasicAuth(esUserName(), esUserPassword())
                .withConnectTimeout(Duration.ofSeconds(5)) // 连接超时时间设置为5秒
                .withSocketTimeout(Duration.ofSeconds(3))  // 套接字超时时间设置为3秒
                .build();
        RestHighLevelClient restHighLevelClient = RestClients.create(clientConfiguration).rest();
        log.info("End init elasticsearch client");
        return restHighLevelClient;
    }

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        List<Converter> converters = new ArrayList<>();
        converters.add(DateToLocalDateTimeConverter.INSTANCE);
        converters.add(StringToLocalDateTimeConverter.INSTANCE);
        converters.add(LongToLocalDateTimeConverter.INSTANCE);
        return new ElasticsearchCustomConversions(converters);
    }

    //保存类型为long类型
    @ReadingConverter
    enum LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {

        INSTANCE;

        @Override
        public LocalDateTime convert(Long source) {
            return Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

    }

    //格式化后保存结果为String类型
    @ReadingConverter
    enum StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

        INSTANCE;

        @Override
        public LocalDateTime convert(String source) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(source, df);
        }

    }

    @WritingConverter
    enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        INSTANCE;

        @Override
        public LocalDateTime convert(Date date) {
            Instant instant = date.toInstant();
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }
}