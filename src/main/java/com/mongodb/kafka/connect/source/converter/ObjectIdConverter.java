package com.mongodb.kafka.connect.source.converter ;

import org.bson.json.Converter ;
import org.bson.json.StrictJsonWriter ;
import org.bson.types.ObjectId ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;


public class ObjectIdConverter implements Converter<ObjectId> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectIdConverter.class) ;

    @Override
    public void convert(ObjectId value, StrictJsonWriter writer) {
        try {
            writer.writeString(value.toHexString()) ;
        } catch (Exception e) {
            LOGGER.error(String.format("Fail to convert %s", value), e) ;
        }
    }
}
