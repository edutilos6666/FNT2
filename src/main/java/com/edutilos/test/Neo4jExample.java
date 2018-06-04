package com.edutilos.test;

import com.edutilos.dao.WorkerDAO;
import com.edutilos.model.Worker;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class Neo4jExample {
    public static void main(String[] args) {
//        example1();
        testDao();
//        example3();
    }


    private static void example3() {
        GraphDatabaseFactory factory = new GraphDatabaseFactory();
        GraphDatabaseService db = factory.newEmbeddedDatabase(new File("/home/edutilos/Applications/neo4j/neo4j-community-3.4.0/data/databases/graph.db"));
        try(Transaction tx = db.beginTx()) {
            Node fooNode = db.createNode(_NodeType.PERSON);
            Node barNode = db.createNode(_NodeType.PERSON);
            Relationship foo_rel_bar = fooNode.createRelationshipTo(barNode, RelationType.IS_HUSBAND_OF);
            Relationship bar_rel_foo = barNode.createRelationshipTo(fooNode, RelationType.IS_WIFE_OF);

            fooNode.setProperty("fname", "foo");
            fooNode.setProperty("lname", "leonel");
            fooNode.setProperty("wage", 100.0);

            barNode.setProperty("fname", "bar");
            barNode.setProperty("lname", "cristiano");
            barNode.setProperty("dependency", 82.123);

            foo_rel_bar.setProperty("date", LocalDate.now());
            foo_rel_bar.setProperty("initiator", false);

            bar_rel_foo.setProperty("date", LocalDate.now());
            bar_rel_foo.setProperty("initiator", true);
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        try(Transaction tx = db.beginTx()) {
            ResourceIterable<Relationship> allRels = db.getAllRelationships();
            for(Relationship rel: allRels) {
                System.out.printf("date = %s, initiator = %s\n",
                        rel.getProperty("date", rel.getProperty("date")),
                        rel.getProperty("initiator", rel.getProperty("initiator")));
                Node startNode = rel.getStartNode();
                Node endNode = rel.getEndNode();
                System.out.printf("startNode fname = %s, endNode fname = %s\n",
                        startNode.getProperty("fname"), endNode.getProperty("fname"));
            }
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        db.shutdown();
    }


    private static void testDao() {
        _WorkerDAOImplNeo4j dao = new _WorkerDAOImplNeo4j();
        dao.lock();
        List<Worker> workers = Arrays.asList(
           new Worker(1, "foo", 10, 100.0,true),
                new Worker(2, "bar", 20, 200.0, false),
                new Worker(3, "bim", 30, 300.0, true),
                new Worker(1, "old_foo", 100, 1000.0, false)
        );
        for(Worker w: workers) {
            dao.save(w);
        }

        System.out.println("<<all workers>>");
        List<Worker> all = dao.findAll();
        for(Worker w: all) {
            System.out.println(w.toString());
        }

        Worker one = dao.findById(1L);
        System.out.printf("one = %s\n", one.toString());

        //update
        dao.update(1L, new Worker(1L, "new_foo", 66, 666.6, false));
        one = dao.findById(1L);
        System.out.printf("one (after update id = 1L) = %s\n", one.toString());

        //remove
        dao.remove(2L);
        all = dao.findAll();
        System.out.println("<<all after remove id= 2L>>");
        for(Worker w: all) {
            System.out.println(w.toString());
        }


        dao.removeAll();
        dao.shutdown();

    }
    private static void example1() {
        GraphDatabaseFactory gdf = new GraphDatabaseFactory();
        GraphDatabaseService db = gdf.newEmbeddedDatabase(new File("/home/edutilos/Applications/neo4j/neo4j-community-3.4.0/data/databases/graph.db"));


        try(Transaction tx = db.beginTx()) {
            Node fooNode = db.createNode(NodeType.PERSON);
            fooNode.setProperty("id", 1);
            fooNode.setProperty("name", "foo");
            fooNode.setProperty("age", 10);

            Node barNode = db.createNode(NodeType.PERSON);
            barNode.setProperty("id", 2);
            barNode.setProperty("name", "bar");
            barNode.setProperty("age", 20);

            Node bimNode = db.createNode(NodeType.PERSON);
            bimNode.setProperty("id", 3);
            bimNode.setProperty("name", "bim");
            bimNode.setProperty("age", 30);


            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }


        try(Transaction tx = db.beginTx()) {
            ResourceIterator<Node> people = db.findNodes(NodeType.PERSON);
            while(people.hasNext()) {
                Node p = people.next();
                System.out.println(personNodeToString(p));
            }
            tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }


        try(Transaction tx = db.beginTx()) {
             ResourceIterator<Node> people = db.findNodes(NodeType.PERSON);
             while(people.hasNext()) {
                 people.next().delete();
             }

             tx.success();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        db.shutdown();
    }

    public static enum NodeType implements Label  {
        PERSON, WORKER, STUDENT;
    }

    public static enum RelationType implements RelationshipType {
        KNOWS, IS_TOGETHER_WITH, IS_MARRIED_TO, IS_HUSBAND_OF, IS_WIFE_OF;
    }

    private static String personNodeToString(Node personNode) {
        StringBuilder sb  = new StringBuilder("Person{")
                .append("id=").append(personNode.getProperty("id")).append(",")
                .append("name=").append(personNode.getProperty("name")).append(",")
                .append("age=").append(personNode.getProperty("age")).append("}");
        return sb.toString();
    }
}


class _WorkerDAOImplNeo4j implements WorkerDAO {

    private static final String URL = "/home/edutilos/Applications/neo4j/neo4j-community-3.4.0/data/databases/graph.db";
    private static _WorkerNodeMapper mapper;
    private GraphDatabaseFactory factory;
    private GraphDatabaseService db;
    public void lock() {
        factory = new GraphDatabaseFactory();
        db = factory.newEmbeddedDatabase(new File(URL));
    }

    public void shutdown() {
        db.shutdown();
    }

    public _WorkerDAOImplNeo4j() {
        mapper = new _WorkerNodeMapper();
    }

    public void removeAll() {
        try(Transaction tx = db.beginTx()) {
            ResourceIterator<Node> all = db.findNodes(_NodeType.WORKER);
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
            Node workerNode = db.createNode(_NodeType.WORKER);
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
            Node workerNode = db.findNode(_NodeType.WORKER, "id", id);
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
            Node workerNode = db.findNode(_NodeType.WORKER, "id", id);
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
            Node workerNode = db.findNode(_NodeType.WORKER, "id", id);
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
            ResourceIterator<Node> allWorkerNodes = db.findNodes(_NodeType.WORKER);
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
}

class _WorkerNodeMapper {
    public Worker nodeToWorker(Node workerNode) {
        Worker w = new Worker();
        w.setId(Long.parseLong(workerNode.getProperty("id").toString()));
        w.setName(workerNode.getProperty("name").toString());
        w.setAge(Integer.parseInt(workerNode.getProperty("age").toString()));
        w.setWage(Double.parseDouble(workerNode.getProperty("wage").toString()));
        w.setActive(Boolean.parseBoolean(workerNode.getProperty("active").toString()));
        return w;
    }
    public Node workerToNode(Worker worker,Node ret) {
        ret.setProperty("id", worker.getId());
        ret.setProperty("name", worker.getName());
        ret.setProperty("age",worker.getAge());
        ret.setProperty("wage", worker.getWage());
        ret.setProperty("active", worker.isActive());
        return ret ;
    }
}

enum _NodeType implements Label  {
    PERSON, WORKER, STUDENT;
}