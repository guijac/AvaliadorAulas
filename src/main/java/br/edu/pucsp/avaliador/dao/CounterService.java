package br.edu.pucsp.avaliador.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Query.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.*;

@Service
public class CounterService {
    private final MongoOperations mongo;

    @Autowired
    public CounterService(MongoOperations mongo) {
        this.mongo = mongo;
    }

    public int getNextSequence(String collectionName) {

        Counter counter = mongo.findAndModify(
                query(where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                Counter.class);

        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}