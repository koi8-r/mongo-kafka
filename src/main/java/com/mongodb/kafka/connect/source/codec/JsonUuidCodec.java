package com.mongodb.kafka.connect.source.codec ;

import org.bson.BsonReader ;
import org.bson.BsonType ;
import org.bson.BsonWriter ;

import org.bson.UuidRepresentation ;
import org.bson.codecs.Codec ;
import org.bson.codecs.DecoderContext ;
import org.bson.codecs.EncoderContext ;

import org.bson.codecs.UuidCodec ;

import java.util.UUID ;


public class JsonUuidCodec extends UuidCodec implements Codec<UUID> {
    private final boolean forceStringRepresentation ;

    public JsonUuidCodec() {
        this(false) ;
    }

    public JsonUuidCodec(final boolean forceStringRepresentation) {
        super(UuidRepresentation.JAVA_LEGACY) ;
        this.forceStringRepresentation = forceStringRepresentation ;
    }

    @Override
    public void encode(final BsonWriter writer, final UUID value, final EncoderContext ctx) {
        if (forceStringRepresentation)
            writer.writeString(value.toString()) ;
        else
            super.encode(writer, value, ctx) ;
    }

    @Override
    public UUID decode(final BsonReader reader, final DecoderContext ctx) {
        if (reader.getCurrentBsonType() == BsonType.STRING)
            return UUID.fromString(reader.readString()) ;
        else
            return super.decode(reader, ctx) ;
    }

    @Override
    public Class<UUID> getEncoderClass() {
        return UUID.class ;
    }
}
