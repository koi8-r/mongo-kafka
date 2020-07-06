package com.mongodb.kafka.connect.source.codec ;

import com.mongodb.MongoClientSettings ;

import com.mongodb.kafka.connect.source.converter.DateTimeConverter ;
import com.mongodb.kafka.connect.source.converter.ObjectIdConverter ;

import org.bson.BsonValue ;
import org.bson.codecs.*;

import org.bson.codecs.configuration.CodecRegistry ;
import org.bson.json.JsonMode ;
import org.bson.json.JsonWriter ;
import org.bson.json.JsonWriterSettings ;

import java.io.StringWriter ;
import java.util.Map ;

import static org.bson.codecs.configuration.CodecRegistries.* ;


public class JsonDocumentCodec extends DocumentCodec {

    protected static final CodecRegistry DEFAULT_CODE_REGISTRY = fromRegistries(
        fromCodecs(new JsonUuidCodec(true)),
        MongoClientSettings.getDefaultCodecRegistry()
    ) ;

    private final CodecRegistry codecRegistry ;

    private static final JsonWriterSettings jsonWriterSettings = JsonWriterSettings
        .builder()
        .outputMode(JsonMode.RELAXED)
        .objectIdConverter(new ObjectIdConverter())
        .dateTimeConverter(new DateTimeConverter())
        .build() ;

    public JsonDocumentCodec() {
        super(DEFAULT_CODE_REGISTRY) ;
        this.codecRegistry = DEFAULT_CODE_REGISTRY ;
    }

    public CodecRegistry getCodecRegistry() {
        return codecRegistry ;
    }

    public JsonWriterSettings getJsonWriterSettings() {
        return jsonWriterSettings ;
    }

    /**
     * Workaround for MongoCopyDataManager.copyDataFrom returns BsonDocument that mimic:
     * - {@link org.bson.codecs.DocumentCodec #encode}
     * - {@link org.bson.codecs.BsonDocumentCodec #encode}
     *
     * <pre>{@code Document doc = codec.decode(bsonDoc.asBsonReader(), DecoderContext.builder().build())}</pre>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String toJson(final Map<String, BsonValue> bsonDoc) {
        JsonWriter writer = new JsonWriter(new StringWriter(), jsonWriterSettings) ;
        EncoderContext ctx = EncoderContext.builder().build() ;

        writer.writeStartDocument() ;

        for (final Map.Entry<String, BsonValue> entry : bsonDoc.entrySet()) {
            writer.writeName(entry.getKey()) ;
            Codec codec = codecRegistry.get(entry.getValue().getClass()) ;
            if (entry.getKey().equals("product_id")) {
                System.out.println("> " + codec) ;
                System.out.println(entry.getValue().getBsonType()) ;
            }
            ctx.encodeWithChildContext(codec, writer, entry.getValue()) ;
        }

        writer.writeEndDocument() ;
        return writer.getWriter().toString() ;
    }
}
