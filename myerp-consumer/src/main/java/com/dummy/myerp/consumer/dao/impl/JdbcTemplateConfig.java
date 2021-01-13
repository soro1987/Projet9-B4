package com.dummy.myerp.consumer.dao.impl;


import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig extends AbstractDbConsumer {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
    }

}
