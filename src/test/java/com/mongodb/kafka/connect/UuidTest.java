package com.mongodb.kafka.connect ;

import com.mongodb.MongoClientSettings ;
import com.mongodb.kafka.connect.source.codec.JsonUuidCodec ;

import org.bson.* ;
import org.bson.codecs.* ;
import org.bson.codecs.configuration.CodecProvider ;
import org.bson.codecs.configuration.CodecRegistry ;

import java.util.HashMap ;
import java.util.Map ;
import java.util.UUID ;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs ;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries ;

import org.junit.Test ;
import org.junit.platform.runner.JUnitPlatform ;
import org.junit.runner.RunWith ;


@RunWith(JUnitPlatform.class)
public class UuidTest {
    public static class JsonCodecProvider implements CodecProvider {
        private final Map<Class<?>, Codec<?>> codecs = new HashMap<Class<?>, Codec<?>>() ;

        public JsonCodecProvider() {
            codecs.put(UUID.class, new JsonUuidCodec()) ;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
            return (Codec<T>) codecs.get(clazz) ;
        }
    }

    public static class JsonDocumentCodec extends DocumentCodec {
        /*
        public JsonDocumentCodec() {
            super(fromProviders(
                new JsonCodecProvider(),
                new ValueCodecProvider(),
                new BsonValueCodecProvider(),
                new DocumentCodecProvider()
            )) ;
        }
        */
        public JsonDocumentCodec() {
            super(
                fromRegistries(
                    fromCodecs(new JsonUuidCodec()),
                    MongoClientSettings.getDefaultCodecRegistry()
                )
            ) ;
        }
    }

    @Test
    public void test() {
        DocumentCodec cod =  new UuidTest.JsonDocumentCodec() ;
        Document doc = new Document("product_id", UUID.fromString("74d8f370-17b9-4a9d-8b14-cf82f7497abd")) ;
        assert doc.toJson(cod).equals(
            "{\"product_id\": \"74d8f370-17b9-4a9d-8b14-cf82f7497abd\"}"
        ) ;
    }
}
