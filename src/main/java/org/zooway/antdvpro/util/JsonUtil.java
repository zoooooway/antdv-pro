package org.zooway.antdvpro.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Customize JSON serialization using Jackson.
 */
@Slf4j
public final class JsonUtil {

    /**
     * Holds ObjectMapper for internal use: NEVER modify!
     */
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapperForInternal();

    private static ObjectMapper createObjectMapperForInternal() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // disabled features:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    public static byte[] writeJsonAsBytes(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String writeJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeJson(OutputStream output, Object obj) throws IOException {
        try {
            OBJECT_MAPPER.writeValue(output, obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String writeJsonWithPrettyPrint(Object obj) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(String str, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            log.warn("cannot read json: " + str, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T readJson(Reader reader, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(reader, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(Reader reader, TypeReference<T> ref) {
        try {
            return OBJECT_MAPPER.readValue(reader, ref);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(InputStream input, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(input, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(InputStream input, TypeReference<T> ref) {
        try {
            return OBJECT_MAPPER.readValue(input, ref);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(String str, TypeReference<T> ref) {
        try {
            return OBJECT_MAPPER.readValue(str, ref);
        } catch (JsonProcessingException e) {
            log.warn("cannot read json: " + str, e);
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(byte[] src, TypeReference<T> ref) {
        try {
            return OBJECT_MAPPER.readValue(src, ref);
        } catch (JsonProcessingException e) {
            log.warn("cannot read json from bytes: " + HexUtils.toHexString(src), e);
            throw new UncheckedIOException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readJson(byte[] src, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(src, clazz);
        } catch (JsonProcessingException e) {
            log.warn("cannot read json from bytes: " + HexUtils.toHexString(src), e);
            throw new UncheckedIOException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Map<String, Object> readJsonAsMap(String str) {
        try {
            return OBJECT_MAPPER.readValue(str, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * A special map to JSON that using string builder.
     *
     * @return
     */
    public static String buildJsonFromJsonMap(Map<String, String> stringJsonMap) {
        if (stringJsonMap.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(1024);
        sb.append("{");
        for (String key : stringJsonMap.keySet()) {
            String jsonString = stringJsonMap.get(key);
            if (jsonString != null) {
                // "key": jsonString
                sb.append('\"').append(key).append("\":").append(jsonString).append(',');
            }
        }
        // set last ',' to '}'
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }
}
