package com.owner.order.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 商品索引配置
 * </p>
 *
 * @author sxl
 * @version 1.0
 * @date 9/15/23 7:53 PM
 */
@Data
@ToString(exclude = "esUserPassword")
@ConfigurationProperties(prefix = "hsh.search.index")
public class EsIndexProperties {

    private String esUrl;

    private String esUserName;

    private String esUserPassword;

    private String orderGlobalIndexName;

}
