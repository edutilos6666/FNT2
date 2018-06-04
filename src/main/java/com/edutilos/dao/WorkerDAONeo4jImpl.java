package com.edutilos.dao;

import com.edutilos.mapper.WorkerNodeMapper;
import com.edutilos.model.Worker;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
//@Component
public class WorkerDAONeo4jImpl implements WorkerDAO {
    private static final String URL = "/home/edutilos/Applications/neo4j/neo4j-community-3.4.0/data/databases/graph.db";
    private static WorkerNodeMapper mapper;
    private GraphDatabaseFactory factory;
    private GraphDatabaseService db;
    public void lock() {
        factory = new GraphDatabaseFactory();
        db = factory.newEmbeddedDatabase(new File(URL));
    }

    //@PreDestroy
    public void preDestroy() {
        shutdown();
    }

    public void shutdown() {
        db.shutdown();
    }

    public WorkerDAONeo4jImpl() {
        mapper = new WorkerNodeMapper();
        lock();
    }

    public void removeAll() {
        try(Transaction tx = db.beginTx()) {
            ResourceIterator<Node> all = db.findNodes(NodeType.WORKER);
            while(all.hasNext()) {
                Node one = all.next();
                remove(Long.parseLong(one.getProperty("id").toString()));
            }
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkIfDuplicateId(Worker w) {
        List<Worker> all = findAll();
        for(Worker _w: all) {
            if(_w.getId() == w.getId()) return true;
        }
        return false;
    }

    @Override
    public void save(Worker w) {
        if(checkIfDuplicateId(w)) return;
        try(Transaction tx = db.beginTx()) {
            Node workerNode = db.createNode(NodeType.WORKER);
            mapper.workerToNode(w, workerNode );
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(long id, Worker w) {
//        GraphDatabaseFactory factory = new GraphDatabaseFactory();
//        GraphDatabaseService db = factory.newEmbeddedDatabase(new File(URL));
        try(Transaction tx = db.beginTx()) {
            Node workerNode = db.findNode(NodeType.WORKER, "id", id);
            mapper.workerToNode(w, workerNode);
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void remove(long id) {
//        GraphDatabaseFactory factory = new GraphDatabaseFactory();
//        GraphDatabaseService db = factory.newEmbeddedDatabase(new File(URL));
        try(Transaction tx = db.beginTx()) {
            Node workerNode = db.findNode(NodeType.WORKER, "id", id);
            workerNode.delete();
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Worker findById(long id) {
//        GraphDatabaseFactory factory = new GraphDatabaseFactory();
//        GraphDatabaseService db = factory.newEmbeddedDatabase(new File(URL));
        Worker ret = null;
        try(Transaction tx = db.beginTx()) {
            Node workerNode = db.findNode(NodeType.WORKER, "id", id);
            ret = mapper.nodeToWorker(workerNode);
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Worker> findAll() {
//        GraphDatabaseFactory factory = new GraphDatabaseFactory();
//        GraphDatabaseService db = factory.newEmbeddedDatabase(new File(URL));
        List<Worker> ret = new ArrayList<>();
        try(Transaction tx = db.beginTx()) {
            ResourceIterator<Node> allWorkerNodes = db.findNodes(NodeType.WORKER);
            while(allWorkerNodes.hasNext()) {
                Node workerNode = allWorkerNodes.next();
                ret.add(mapper.nodeToWorker(workerNode));
            }
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    enum NodeType implements Label {
        PERSON, WORKER, STUDENT;
    }
}
