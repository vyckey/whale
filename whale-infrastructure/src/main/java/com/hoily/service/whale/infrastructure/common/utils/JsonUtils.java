package com.hoily.service.whale.infrastructure.common.utils;

import lombok.extern.slf4j.Slf4j;

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
    private JsonUtils() {
    }

    public static String toJson(Object object) {
        return JsonObjectMapper.CAMEL_CASE.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        return JsonObjectMapper.CAMEL_CASE.fromJson(json, valueType);
    }

    public static <T> T convert(Object object, Class<T> clazz) {
        return JsonObjectMapper.CAMEL_CASE.convert(object, clazz);
    }

}