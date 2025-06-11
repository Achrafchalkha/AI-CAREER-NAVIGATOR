package com.example.aicareernavigator.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.aicareernavigator.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSslSettings(builder ->
                builder.enabled(true)
                       .invalidHostNameAllowed(true)) // Allow for development - MongoDB Atlas SSL issue
            .applyToSocketSettings(builder ->
                builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
                       .readTimeout(30000, TimeUnit.MILLISECONDS))
            .applyToConnectionPoolSettings(builder ->
                builder.maxSize(50)
                       .minSize(5)
                       .maxWaitTime(30000, TimeUnit.MILLISECONDS)
                       .maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS))
            .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
} 