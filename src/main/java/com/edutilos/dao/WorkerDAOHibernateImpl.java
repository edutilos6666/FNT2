package com.edutilos.dao;

import com.edutilos.model.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class WorkerDAOHibernateImpl implements WorkerDAO {

    private SessionFactory factory ;

    public WorkerDAOHibernateImpl() {
        configure();
    }

    public void configure() {
        try {
            factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Worker.class)
                    .buildSessionFactory();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closeSessionFactory() {
        if(factory!= null && !factory.isClosed()) factory.close();
    }

    @Override
    public void save(Worker w) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(w);
            tx.commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            if(tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void update(long id, Worker w) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Worker oldWorker = session.get(Worker.class, id);
            oldWorker.setName(w.getName());
            oldWorker.setAge(w.getAge());
            oldWorker.setWage(w.getWage());
            oldWorker.setActive(w.isActive());
            session.update(oldWorker);
            tx.commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            if(tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(long id) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Worker w = session.get(Worker.class, id);
            session.remove(w);
            tx.commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            if(tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Worker findById(long id) {
        Session session = factory.openSession();
        Transaction tx = null;
        Worker ret = null;
        try {
            tx = session.beginTransaction();
            ret = session.get(Worker.class, id);
            tx.commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            if(tx != null) tx.rollback();
        } finally {
            session.close();
        }
        return ret;
    }

    @Override
    public List<Worker> findAll() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Worker> ret = null;
        try {
            tx = session.beginTransaction();
            ret = session.createQuery("select w from Worker w").getResultList();
            tx.commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            if(tx != null) tx.rollback();
        } finally {
            session.close();
        }
        return ret;
    }
}
