package com.shopstream.marketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.lang.NonNull;

@Configuration
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Override
    @NonNull
    protected String getDatabaseName() {
        return "shopstream";
    }
}
