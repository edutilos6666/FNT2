package com.edutilos.mapper;

import com.edutilos.model.Worker;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class WorkerDocumentMapper {
    public Document workerToDocument(Worker w) {
        Document doc = new Document();
        doc.append("id", w.getId())
                .append("name", w.getName())
                .append("age", w.getAge())
                .append("wage", w.getWage())
                .append("active", w.isActive());
        return doc;
    }

    public Worker documentToWorker(Document doc) {
        Worker w = new Worker();
        w.setId(doc.getLong("id"));
        w.setName(doc.getString("name"));
        w.setAge(doc.getInteger("age"));
        w.setWage(doc.getDouble("wage"));
        w.setActive(doc.getBoolean("active"));
        return w;
    }

    public List<Document> workersToDocuments(List<Worker> workers) {
        List<Document> ret = new ArrayList<>();
        for(Worker w: workers)  {
            ret.add(workerToDocument(w));
        }
        return ret;
    }

    public List<Worker> documentsToWorkers(List<Document> docs) {
        List<Worker> workers = new ArrayList<>();
        for(Document doc: docs) {
            workers.add(documentToWorker(doc));
        }
        return workers;
    }

    public List<Worker> documentsToWorkers(FindIterable<Document> docs) {
        List<Worker> workers = new ArrayList<>();
        for(Document doc: docs) {
            workers.add(documentToWorker(doc));
        }
        return workers;
    }
}
