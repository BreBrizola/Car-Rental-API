package com.dentsu.bootcamp.config;

import org.hibernate.type.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisPrefixSerializer implements RedisSerializer<String> {
    private final String prefix;

    public RedisPrefixSerializer(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public byte[] serialize(String value) throws SerializationException {
        if (value == null) {
            return null;
        }
        // Add the prefix to the value before serialization
        return (prefix + value).getBytes();
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }
}
