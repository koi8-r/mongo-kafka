package com.mongodb.kafka.connect.source.codec ;

import org.bson.BsonReader ;
import org.bson.BsonWriter ;

import org.bson.codecs.Codec ;
import org.bson.codecs.DecoderContext ;
import org.bson.codecs.EncoderContext ;

import java.util.UUID ;


public class JsonUuidCodec implements Codec<UUID> {
    @Override
    public void encode(final BsonWriter writer, final UUID value, final EncoderContext encoderContext) {
        writer.writeString(value.toString()) ;
    }

    @Override
    public UUID decode(final BsonReader reader, final DecoderContext decoderContext) {
        return UUID.fromString(reader.readString()) ;
    }

    @Override
    public Class<UUID> getEncoderClass() {
        return UUID.class ;
    }
}
