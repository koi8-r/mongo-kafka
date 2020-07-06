package com.mongodb.kafka.connect ;

import com.mongodb.ConnectionString ;
import com.mongodb.MongoClientSettings ;
import com.mongodb.client.FindIterable ;
import com.mongodb.client.MongoClient ;
import com.mongodb.client.MongoClients ;
import com.mongodb.kafka.connect.source.codec.JsonDocumentCodec ;
import org.bson.BsonDocument ;
import org.bson.Document ;
import org.bson.codecs.DecoderContext ;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries ;


public class UuidTestE {
    public static void main(String[] args) {
        MongoClientSettings s = MongoClientSettings
            .builder()
            .codecRegistry(fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry()
            ))
            .applyConnectionString(new ConnectionString("mongodb://root:secret@localhost/admin"))
            .build() ;
        MongoClient cli = MongoClients.create(s) ;
        FindIterable<Document> f = cli.getDatabase("ty").getCollection("keepa.com").find() ;
        Document doc = f.cursor().next() ;
        JsonDocumentCodec codec = new JsonDocumentCodec() ;
        BsonDocument bsonDoc = doc.toBsonDocument(BsonDocument.class, MongoClientSettings.getDefaultCodecRegistry()) ;
        String strDoc = codec.toJson(bsonDoc) ;
        strDoc = doc.toJson(codec) ;
        Document doc2 = codec.decode(bsonDoc.asBsonReader(), DecoderContext.builder().build()) ;
        strDoc = doc2.toJson(codec.getJsonWriterSettings(), codec) ;
        System.out.println(strDoc) ;
    }
}
