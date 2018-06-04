package com.edutilos.dao;

import com.edutilos.model.Worker;

import java.util.List;

/**
 * Created by edutilos on 02.06.18.
 */
public interface WorkerDAO {
    void save(Worker w);
    void update(long id, Worker w);
    void remove(long id);
    Worker findById(long id);
    List<Worker> findAll();
}
