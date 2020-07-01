package com.mongodb.kafka.connect ;

import com.mongodb.ConnectionString ;
import com.mongodb.MongoClientSettings ;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient ;
import com.mongodb.client.MongoClients ;
import com.mongodb.kafka.connect.source.codec.JsonUuidCodec;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


//@RunWith(JUnitPlatform.class)
public class UuidTestE {
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
                    fromCodecs(new JsonUuidCodec(true)),
                    MongoClientSettings.getDefaultCodecRegistry()
                )
            ) ;
        }
    }

    //@Test
    public void xtest() {
        DocumentCodec cod =  new JsonDocumentCodec() ;
        Document doc = new Document("product_id", UUID.fromString("74d8f370-17b9-4a9d-8b14-cf82f7497abd")) ;
        assert doc.toJson(cod).equals(
            "{\"product_id\": \"74d8f370-17b9-4a9d-8b14-cf82f7497abd\"}"
        ) ;
    }

    public static void main(String[] args) {
        final DocumentCodec codecs = new JsonDocumentCodec() ;
        MongoClientSettings s = MongoClientSettings
            .builder()
            .codecRegistry(fromCodecs(codecs))
            // .codecRegistry(fromRegistries(
            //     fromCodecs(new JsonUuidCodec(true)),
            //     MongoClientSettings.getDefaultCodecRegistry()
            // ))
            .applyConnectionString(new ConnectionString("mongodb://root:secret@localhost/admin"))
            .build() ;
        MongoClient cli = MongoClients.create(s) ;
        FindIterable<Document> f = cli.getDatabase("ty").getCollection("keepa.com").find() ;
        String strDoc = f.cursor().next().toJson(codecs) ;
        System.out.println(strDoc) ;
    }
}
