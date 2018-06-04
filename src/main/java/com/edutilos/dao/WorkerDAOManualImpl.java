package com.edutilos.dao;

import com.edutilos.model.Worker;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by edutilos on 02.06.18.
 */
public class WorkerDAOManualImpl implements WorkerDAO{
    private List<Worker> container = new ArrayList<>();
    private final Logger logger = LogManager.getLogger(WorkerDAOManualImpl.class);
    @Override
    public void save(Worker w) {
        if(checkIfIdExists(w.getId())) {
            logger.error("Worker with this id already exists.");
        } else {
            container.add(w);
            logger.info("Added new Worker");
        }
    }

    private boolean checkIfIdExists(long id) {
        for(Worker w: container) {
            if(w.getId() == id) return true;
        }
        return false;
    }

    @Override
    public void update(long id, Worker w) {
        if(checkIfIdExists(id)) {
            for(Worker _w: container) {
                if(_w.getId() == id) {
                    container.set(container.indexOf(_w), w);
                    logger.info("Updated worker successfully.");
                    return ;
                }
            }
        } else {
            logger.error("Worker with this id does not exist.");
        }
    }

    @Override
    public void remove(long id) {
        if(checkIfIdExists(id)) {
            for(Worker w: container) {
                if(w.getId() == id) {
                    container.remove(w);
                    logger.info("Removed worker successfully.");
                    return ;
                }
            }
        } else {
            logger.error("Worker with id does not exist.");
        }
    }

    @Override
    public Worker findById(long id) {
        for(Worker w: container) {
            if(w.getId() == id) return w;
        }
        return null;
    }

    @Override
    public List<Worker> findAll() {
        return container;
    }
}
