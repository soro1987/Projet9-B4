package com.dummy.myerp.consumer.dao.impl;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;



        import com.dummy.myerp.consumer.db.AbstractDbConsumer;
        import com.dummy.myerp.consumer.db.DataSourcesEnum;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class NamedJdbcConfig extends AbstractDbConsumer {

    @Bean
    public JdbcTemplate namedJdbcTemplate() {
        return new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
    }

}
