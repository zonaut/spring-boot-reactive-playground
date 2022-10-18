package com.zonaut.playground.reactive.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

// This isn't needed as Spring Boot will register all Jackson modules it finds on it's classpath.
// I want to reuse the same static OBJECT_MAPPER in other places without the need to inject it all over the place.
// It is thread safe, so it shouldn't be a problem, read the next link for more information and possible pitfalls
// https://stackoverflow.com/questions/3907929/should-i-declare-jacksons-objectmapper-as-a-static-field
@Slf4j
@Configuration
public class JacksonConfig {

    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // Needed for what ?
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();

        // https://github.com/FasterXML/jackson-modules-java8
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        // TODO This can be removed starting with version 3.x
        // https://github.com/FasterXML/jackson-modules-java8/tree/master/parameter-names
        OBJECT_MAPPER.registerModule(new ParameterNamesModule());

        // TODO Should we even use this as it allows the use of Optionals in Class variables.
        //      - optionals should be used in getters only and this is not needed if we are doing this
        //OBJECT_MAPPER.registerModule(new Jdk8Module());

        // TODO Look at https://github.com/FasterXML/jackson-modules-base/tree/2.14/blackbird

        // Example of adding custom serializers
        // TODO Remove io.r2dbc.postgresql.codec.Json and add a realistic example, this type should only be used from db to object once
        SimpleModule customSerializersModule = new SimpleModule();
        SimpleSerializers customSerializers = new SimpleSerializers(List.of(new Serializer(Json.class)));
        customSerializersModule.setSerializers(customSerializers);
        OBJECT_MAPPER.registerModule(customSerializersModule);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    static class Serializer extends StdSerializer<Json> {
        protected Serializer(Class<Json> t) {
            super(t);
        }

        @Override
        public void serialize(Json value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            var text = value.asString();
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(text);
            var node = gen.getCodec().readTree(parser);
            serializers.defaultSerializeValue(node, gen);
        }
    }

    public static byte[] writeAsBytes(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
