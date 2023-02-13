package com.hoily.service.whale.infrastructure.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Camel-Case Json Convert Utils
 * <p>
 * Deserialization features:
 * 1. accept unknown properties
 * 2. accept empty string as null object
 *
 * @author vyckey
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

    private JsonUtils() {
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, JavaType valueType) {
        try {
            return (T) OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(byte[] json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T fromJson(byte[] json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T convert(Object object, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(object, clazz);
    }

    public static <T> T convert(Object object, TypeReference<T> type) {
        return OBJECT_MAPPER.convertValue(object, type);
    }
}