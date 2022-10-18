package com.zonaut.playground.reactive.config.converters.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zonaut.playground.reactive.entities.ProductEntityFeaturesJson;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.io.IOException;

import static com.zonaut.playground.reactive.config.JacksonConfig.OBJECT_MAPPER;

@Slf4j
public class ProductEntityFeaturesConverter {

    public static class JsonToProductEntityFeaturesConverter implements Converter<Json, ProductEntityFeaturesJson> {

        @Override
        public ProductEntityFeaturesJson convert(Json source) {
            try {
                return OBJECT_MAPPER.readValue(source.asString(), new TypeReference<>() {
                });
            } catch (IOException e) {
                log.error("Problem while parsing JSON: {}", source, e);
                throw new RuntimeException(e);
            }
        }
    }

    public static class ProductEntityFeaturesToJsonConverter implements Converter<ProductEntityFeaturesJson, Json> {

        @Override
        public Json convert(@NonNull ProductEntityFeaturesJson source) {
            try {
                return Json.of(OBJECT_MAPPER.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                log.error("Error occurred while serializing map to JSON: {}", source, e);
                throw new RuntimeException(e);
            }
        }
    }


}
