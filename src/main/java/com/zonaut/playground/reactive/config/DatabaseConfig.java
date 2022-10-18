package com.zonaut.playground.reactive.config;

import com.zonaut.playground.reactive.config.converters.enums.EnumConverters;
import com.zonaut.playground.reactive.config.converters.json.ProductEntityFeaturesConverter;
import com.zonaut.playground.reactive.domain.types.OrderStatus;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.zonaut.playground.reactive.domain.types.OrderStatus.ORDER_STATUS_DB_TYPE_NAME;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableR2dbcRepositories
@EnableTransactionManagement
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    private final R2dbcProperties r2dbcProperties;

    @Bean
    @Override
    @NonNull
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionFactory connectionFactory = getConnectionFactory();
        ConnectionPoolConfiguration configuration = getConnectionPoolConfiguration(connectionFactory);
        return new ConnectionPool(configuration);
    }

    @Bean
    @Override
    @NonNull
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();

        // enum types
        converters.add(new EnumConverters.OrderStatusConverter());

        // json data types
        converters.add(new ProductEntityFeaturesConverter.JsonToProductEntityFeaturesConverter());
        converters.add(new ProductEntityFeaturesConverter.ProductEntityFeaturesToJsonConverter());

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    private PostgresqlConnectionFactory getConnectionFactory() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(r2dbcProperties.getUrl());

        String host = options.getRequiredValue(ConnectionFactoryOptions.HOST).toString();
        int port = Integer.parseInt(options.getRequiredValue(ConnectionFactoryOptions.PORT).toString());
        String database = options.getRequiredValue(ConnectionFactoryOptions.DATABASE).toString();

        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .database(database)
                        .username(r2dbcProperties.getUsername())
                        .password(r2dbcProperties.getPassword())
                        .codecRegistrar(
                                EnumCodec.builder()
                                        // ! Important, enum db type names need to be written lower case.
                                        // The name must match exactly as found under schema -> object types
                                        .withEnum(ORDER_STATUS_DB_TYPE_NAME, OrderStatus.class)
                                        .build()
                        )
                        .build()
        );
    }

    private ConnectionPoolConfiguration getConnectionPoolConfiguration(PostgresqlConnectionFactory connectionFactory) {
        return ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMinutes(30))
                .initialSize(10)
                .maxSize(10)
                .maxCreateConnectionTime(Duration.ofSeconds(1))
                .build();
    }
}
