package com.edutilos.mapper;

import com.edutilos.model.Worker;
import org.neo4j.graphdb.Node;

/**
 * Created by edutilos on 03.06.18.
 */
public class WorkerNodeMapper {
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
