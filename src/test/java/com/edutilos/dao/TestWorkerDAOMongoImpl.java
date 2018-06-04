package com.edutilos.dao;

import com.edutilos.model.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by edutilos on 04.06.18.
 */
public class TestWorkerDAOMongoImpl {

    private WorkerDAOMongoImpl dao;

    public TestWorkerDAOMongoImpl() {
        dao = new WorkerDAOMongoImpl();
    }

    @Before
    public void setUp() {
        List<Worker> workers = Arrays.asList(
           new Worker(1l, "foo", 10, 100.0, true),
                new Worker(2l, "bar", 20, 200.0, false),
                new Worker(3l, "bim", 30, 300.0, true)
        );

        for(Worker w: workers) dao.save(w);
    }


    @After
    public void tearDown() {
        List<Worker> all = dao.findAll();
        for(Worker w: all) dao.remove(w.getId());
        dao.disconnect();
    }

    @Test
    public void testSave() {
        List<Worker> all = dao.findAll();
        assertEquals(3, all.size());
    }

    @Test
    public void testUpdate() {
        dao.update(1L, new Worker(1L, "new_foo", 66, 666.6, false));
        Worker one = dao.findById(1L);
        assertEquals(1L, one.getId());
        assertEquals("new_foo", one.getName());
        assertEquals(66, one.getAge());
        assertEquals(666.6, one.getWage(), 0.001);
        assertEquals(false, one.isActive());
    }

    @Test
    public void testRemove() {
        dao.remove(1L);
        List<Worker> all = dao.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testFindById() {
        Worker one = dao.findById(3L);
        assertEquals(3L, one.getId());
        assertEquals("bim", one.getName());
        assertEquals(30, one.getAge());
        assertEquals(300.0, one.getWage(), 0.001);
        assertEquals(true, one.isActive());
    }

}
