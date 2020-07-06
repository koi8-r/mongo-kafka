package com.mongodb.kafka.connect.source.codec ;

import org.bson.codecs.Codec ;
import org.bson.codecs.configuration.CodecProvider ;
import org.bson.codecs.configuration.CodecRegistry ;

import java.util.HashMap ;
import java.util.Map ;
import java.util.UUID ;


public class JsonCodecProvider implements CodecProvider {
    private final Map<Class<?>, Codec<?>> codecs = new HashMap<Class<?>, Codec<?>>() ;

    public JsonCodecProvider() {
        codecs.put(UUID.class, new JsonUuidCodec(true)) ;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        return (Codec<T>) codecs.get(clazz) ;
    }
}
