package com.edutilos.test;

import com.edutilos.dao.WorkerDAO;
import com.edutilos.model.Worker;
import com.mongodb.Block;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by edutilos on 03.06.18.
 */
public class MongodbExample {
    public static void main(String[] args) {
//        example1();
//        example2();
        test_WorkerDAO();
    }


    private static void test_WorkerDAO() {
        _WorkerDAOImplMongodb dao = new _WorkerDAOImplMongodb();
        dao.save(new Worker(1, "foo", 10, 100.0, true));
        dao.save(new Worker(2, "bar", 20, 200.0, false));
        dao.save(new Worker(3, "bim", 30, 300.0, true));
        List<Worker> all = dao.findAll();
        System.out.println("<<all>>");
        for(Worker w: all) {
            System.out.println(w.toString());
        }

        Worker one = dao.findById(1L);
        System.out.printf("one = %s\n", one.toString());

        dao.update(2L, new Worker(2, "new_bar", 123, 123.0, true));
        one = dao.findById(2L);
        System.out.printf("one with id=2 after update = %s\n", one.toString());

        dao.remove(1L);
        all = dao.findAll();
        System.out.println("<<all after delete id = 1>>");
        for(Worker w: all) {
            System.out.println(w.toString());
        }
    }

    private static void example1() {
        MongoClient client = MongoClients.create("mongodb://localhost");
        MongoDatabase db= client.getDatabase("test");
        MongoCollection<Document> collPerson = db.getCollection("Person");
        FindIterable<Document> iterPerson = collPerson.find();
        for(Document doc: iterPerson) {
            System.out.printf("%s, %d, %d\n", doc.getString("name"),
                    doc.getInteger("age"), doc.getInteger("wage"));
        }


        client.close();
    }


    private static void example2() {
        MongoClient client = MongoClients.create("mongodb://localhost");
        MongoDatabase dbTest = client.getDatabase("test");
        MongoCollection<Document> collWorker = dbTest.getCollection("Worker");
        List<Worker> workers = Arrays.asList(
           new Worker(1, "foo", 10, 100.0, true),
                new Worker(2, "bar", 20, 200.0, false),
                new Worker(3, "bim", 30, 300.0, true)
        );

        for(Worker w: workers) {
            collWorker.insertOne(workerToDocument(w));
        }

        List<Worker> workers2 = Arrays.asList(
           new Worker(4, "pako", 40, 400.0, false),
                new Worker(5, "deko", 50, 500.0, true)
        );
        collWorker.insertMany(workersToDocuments(workers2));

        FindIterable<Document> docs = collWorker.find();
        for(Document doc: docs) {
            System.out.println(documentToWorker(doc));
        }


        System.out.println("<<name == foo>>");
        docs = collWorker.find(new Document().append("name", "foo"));
        for(Document doc: docs) {
            System.out.println(documentToWorker(doc));
        }

        MongoCursor<Document> cursor = collWorker.find().iterator();
        System.out.println("<<cursor>>");
        while(cursor.hasNext()) {
            System.out.println(documentToWorker(cursor.next()));
        }
        cursor.close();


        Bson filter = Filters.and(Filters.gte("age", 10), Filters.lte("age", 40));
        cursor = collWorker.find(filter).iterator();
        System.out.println("<<age>=10 && age <=40");
        while(cursor.hasNext()) {
            System.out.println(documentToWorker(cursor.next()));
        }
        cursor.close();

        Block<Document> block = new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(documentToWorker(document).toString());
            }
        };
        System.out.println("<<using block>>");
        collWorker.find(filter).forEach(block);

        Consumer<Document> consumer = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(documentToWorker(document));
            }
        };
        System.out.println("<<using consumer>>");
        collWorker.find(filter).forEach(consumer);


        collWorker.replaceOne(Filters.eq("id", 1),
                new Document(workerToDocument(new Worker(1, "new_foo", 123, 123.0, false))));

        Document first = collWorker.find(Filters.eq("id", 1)).first();
        System.out.printf("first = %s\n", documentToWorker(first).toString());

        collWorker.deleteOne(Filters.and(Filters.eq("id", 1),
                Filters.eq("name", "new_foo"),
                Filters.eq("age", 123),
                Filters.lte("wage", 123.0),
                Filters.eq("active", false)));

        System.out.println("<<all workers after delete>>");
        collWorker.find().forEach(block );

        collWorker.drop();
        client.close();
    }

    private static Document workerToDocument(Worker w) {
        Document doc = new Document();
        doc.append("id", w.getId())
                .append("name", w.getName())
                .append("age", w.getAge())
                .append("wage", w.getWage())
                .append("active", w.isActive());
        return doc;
    }

    private static Worker documentToWorker(Document doc) {
        Worker w = new Worker();
        w.setId(doc.getLong("id"));
        w.setName(doc.getString("name"));
        w.setAge(doc.getInteger("age"));
        w.setWage(doc.getDouble("wage"));
        w.setActive(doc.getBoolean("active"));
        return w;
    }

    public static List<Document> workersToDocuments(List<Worker> workers) {
        List<Document> ret = new ArrayList<>();
        for(Worker w: workers)  {
            ret.add(workerToDocument(w));
        }
        return ret;
    }

    public static List<Worker> documentsToWorkers(List<Document> docs) {
        List<Worker> workers = new ArrayList<>();
        for(Document doc: docs) {
            workers.add(documentToWorker(doc));
        }
        return workers;
    }
}

class _WorkerDAOImplMongodb implements WorkerDAO {
    private MongoClient client ;
    private MongoDatabase db;
    private MongoCollection<Document> collWorker;
    private _WorkerDocumentMapperMongodb mapper;

    public _WorkerDAOImplMongodb() {
        mapper = new _WorkerDocumentMapperMongodb();
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

class _WorkerDocumentMapperMongodb {
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
