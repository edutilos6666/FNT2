package com.edutilos.dao;

import com.edutilos.mapper.WorkerDocumentMapper;
import com.edutilos.model.Worker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class WorkerDAOMongoImpl implements WorkerDAO {
    private MongoClient client ;
    private MongoDatabase db;
    private MongoCollection<Document> collWorker;
    private WorkerDocumentMapper mapper;

    public WorkerDAOMongoImpl() {
        mapper = new WorkerDocumentMapper();
        connect();
        createCollection();
    }

    public void connect() {
        client =  MongoClients.create("mongodb://localhost");
        db = client.getDatabase("test");
    }
    public void disconnect() {
        client.close();
    }

    public void dropCollection() {
        if(collWorker != null) collWorker.drop();
    }

    public void createCollection()  {
        collWorker = db.getCollection("Worker");
    }

    @Override
    public void save(Worker w) {
        collWorker.insertOne(mapper.workerToDocument(w));
    }

    @Override
    public void update(long id, Worker w) {
        collWorker.replaceOne(Filters.eq("id", id),
                mapper.workerToDocument(w));
    }

    @Override
    public void remove(long id) {
        collWorker.deleteOne(Filters.eq("id", id));
    }

    @Override
    public Worker findById(long id) {
        try {
            return mapper.documentToWorker(collWorker.find(Filters.eq("id", id)).first());
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Worker> findAll() {
        try {
            return mapper.documentsToWorkers(collWorker.find());
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
