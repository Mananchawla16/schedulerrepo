package com.test.pavedroad.intcollabsnotification.core;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.test.pavedroad.intcollabsnotification.core.config.DocDbSslContextBuilder;
import com.test.pavedroad.intcollabsnotification.core.config.IksConfig;
import com.test.pavedroad.intcollabsnotification.core.config.LocalConfig;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SslSettings;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.spring.autoconfigure.JobRunrProperties;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.StorageProviderUtils;
import org.jobrunr.storage.nosql.documentdb.AmazonDocumentDBStorageProvider;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Import({LocalConfig.class, IksConfig.class})
public class JobRunrStorageConfiguration {
    private static final Integer DEFAULT_MIN_POOL_SIZE = 10;
    private static final Integer DEFAULT_MAX_POOL_SIZE = 50;
    private static final Integer MAX_WAIT_TIME = 5;
    private static final Integer MAX_CONNECTION_LIFE_TIME = 15;
    private static final Integer MAX_CONNECTION_IDLE_TIME = 2;

    private final MongoProperties mongoProperties;
    private final Environment environment;
    private final MongoCustomConversions mongoCustomConversions;

    /***
     * Method to apply SSL settings if not in local environment or if sslSettings are explicitly requested
     * @return
     */
    private void applySSLSettings(MongoClientSettings.Builder settingsBuilder) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (Arrays.stream(activeProfiles)
                .noneMatch(x -> StringUtils.equalsIgnoreCase("LOCAL", x))) {
            SslSettings sslSettings = SslSettings.builder().context(new DocDbSslContextBuilder().buildSSLContext())
                    .enabled(true).build();
            settingsBuilder.applyToSslSettings(builder -> builder.applySettings(sslSettings));
        }
    }

    /***
     * Method to apply connectionPool settings if present
     * @return
     */
    private void applyConnectionPoolSettings(MongoClientSettings.Builder settingsBuilder) {
        Integer minConnectionPoolSize = ObjectUtils.defaultIfNull(
                mongoProperties.getMinPoolSize(), DEFAULT_MIN_POOL_SIZE);
        Integer maxConnectionPoolSize = ObjectUtils.defaultIfNull(
                mongoProperties.getMaxPoolSize(), DEFAULT_MAX_POOL_SIZE);
        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder().minSize(minConnectionPoolSize)
                .maxSize(maxConnectionPoolSize).maxWaitTime(MAX_WAIT_TIME, TimeUnit.MINUTES)
                .maxConnectionLifeTime(MAX_CONNECTION_LIFE_TIME, TimeUnit.MINUTES)
                .maxConnectionIdleTime(MAX_CONNECTION_IDLE_TIME, TimeUnit.MINUTES).build();
        settingsBuilder.applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings));
    }

    /***
     * Method for creating mongoClient bean
     * @return
     */
    @Bean
    public MongoClient createMongoClient(MongoClientSettings.Builder createMongoSettingsBuilder) {
        MongoClientSettings.Builder settingsBuilder = createMongoSettingsBuilder;
        applySSLSettings(settingsBuilder);
        applyConnectionPoolSettings(settingsBuilder);
        MongoClientSettings settings = settingsBuilder.build();
        return MongoClients.create(settings);
    }

    /***
     * Method for creating mongoTemplate bean with specified mongoConversions
     * @return
     */
    @Bean
    public MongoTemplate createMongoTemplate(MongoClientSettings.Builder createMongoSettingsBuilder) {
        MongoTemplate mongoTemplate = new MongoTemplate(
                createMongoClient(createMongoSettingsBuilder), mongoProperties.getDatabase());
        MappingMongoConverter mappingMongoConverter = (MappingMongoConverter) mongoTemplate.getConverter();
        /* The default constructor adds _class field by default. So we need to explicitly pass null so that no mapper
        is set and _class field is not added */
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingMongoConverter.setCustomConversions(mongoCustomConversions);
        mappingMongoConverter.afterPropertiesSet();
        return mongoTemplate;
    }

    /***
     * Method for creating documentDBStorageProvider bean for JobRunr Storage Provider
     * @return
     */
    @Bean
    @Primary
    public StorageProvider createDocumentDBStorageProvider(
            MongoClient mongoClient, JobMapper jobMapper, JobRunrProperties properties) {
        String databaseName = properties.getDatabase().getDatabaseName();
        String tablePrefix = properties.getDatabase().getTablePrefix();
        StorageProviderUtils.DatabaseOptions databaseOptions = properties.getDatabase()
                .isSkipCreate() ? StorageProviderUtils.DatabaseOptions.SKIP_CREATE :
                StorageProviderUtils.DatabaseOptions.CREATE;
        AmazonDocumentDBStorageProvider amazonDocumentDBStorageProvider = new AmazonDocumentDBStorageProvider(
                mongoClient, databaseName, tablePrefix, databaseOptions);
        amazonDocumentDBStorageProvider.setJobMapper(jobMapper);
        return amazonDocumentDBStorageProvider;
    }
}
