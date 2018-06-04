package com.edutilos.test;

import com.edutilos.dao.WorkerDAO;
import com.edutilos.model.Worker;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class HibernateExample {
    public static void main(String[] args) {
//        example1();
        testDao();
    }

    private static void testDao() {
        _WorkerDAOImplHibernate dao = new _WorkerDAOImplHibernate();
        dao.configure();
        List<Worker> workers = Arrays.asList(
           new Worker(1L, "foo", 10, 100.0, true),
                new Worker(2L, "bar", 20, 200.0, false),
                new Worker(3L, "bim", 30, 300.0, true)
        );
        for(Worker w: workers)
            dao.save(w);

        List<Worker> all = dao.findAll();
        System.out.println("<<all workers>>");
        for(Worker w:all) System.out.println(w.toString());
        System.out.println("\n");

        //findById
        Worker one = dao.findById(1L);
        System.out.printf("one with id = 1L = %s\n", one.toString());

        //update
        dao.update(1L, new Worker(1L, "new_foo", 66, 666.6 , false));
        one = dao.findById(1L);
        System.out.printf("one with id = 1L = %s\n", one.toString());

        //remove
        dao.remove(1L);
        all = dao.findAll();
        System.out.println("<<all after remove id=1L>>");
        for(Worker w: all)
            System.out.println(w.toString());


        all = dao.findAll();
        for(Worker w:all) dao.remove(w.getId());

        dao.closeSessionFactory();
    }
    private static void example1() {
        _Example1 ex = new _Example1();
        List<HibernateWorker> workers = Arrays.asList(
           new HibernateWorker(1, "foo", 10, 100.0, true, LocalDate.now()),
                new HibernateWorker(2, "bar", 20, 200.0, false, LocalDate.now()),
                new HibernateWorker(3, "bim", 30, 300.0, true, LocalDate.now())
        );

        for(HibernateWorker w: workers) {
            ex.addWorker(w);
        }

        List<HibernateWorker> all = ex.getAllWorkers();
        System.out.println("<<all workers>>");
        for(HibernateWorker w: all) {
            System.out.println(w.toString());
        }


        System.out.println("\n\n\n");
        ex.someQueries();
        System.out.println("\n\n\n");


        System.out.println("\n\n\n");
        ex.someCriterias();
        System.out.println("\n\n\n");
        System.out.println("\n\n\n");
        ex.someNamedQueries();
        System.out.println("\n\n\n");


        HibernateWorker one = ex.getOneWorker(1L);
        System.out.printf("one with id = 1 = %s\n", one.toString());

        //update
        ex.updateWorker(1L, new HibernateWorker(1L, "new-foo", 66, 666.6, false, LocalDate.now()));
        one = ex.getOneWorker(1L);
        System.out.printf("one with id = 1 = %s\n", one.toString());

        //remove
        ex.removeWorker(1L);
        all = ex.getAllWorkers();
        System.out.println("<<all workers after remove id = 1>>");
        for(HibernateWorker w: all) {
            System.out.println(w.toString());
        }

        all  = ex.getAllWorkers();
        for(HibernateWorker w: all) {
            ex.removeWorker(w.getId());
        }

        ex.close();
    }



    private static class _Example2 {

    }


    private static class _Example1 {
        private SessionFactory factory;


        private void configure() {
            try {
                factory = new Configuration()
                        .configure()
                        .addAnnotatedClass(HibernateWorker.class)
                        .buildSessionFactory();

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        public void close() {
            if(factory != null && !factory.isClosed()) factory.close();
        }


        public _Example1() {
            configure();
        }

        public void someNamedQueries() {
            Session session = factory.openSession();
            Query<HibernateWorker> query =  session.createNamedQuery("select.all", HibernateWorker.class);
            List<HibernateWorker> workers = query.list();
            System.out.println("\n");
            System.out.println("<<select.all>>");
            for(HibernateWorker w: workers) System.out.println(w.toString());
            System.out.println("\n");
            Query<String> strQuery  = session.createNamedQuery("select.names", String.class);
            List<String> some = strQuery.list();
            System.out.println("<<select.names>>");
            for(String s:some) System.out.println(s);
            System.out.println("\n");
            strQuery = session.createNamedQuery("select.some.names", String.class);
            some = strQuery.list();
            System.out.println("<<select.some.names>>");
            for(String s:some) System.out.println(s);
            System.out.println("\n");

            session.close();
        }

        public void someCriterias()  {
            Session session = factory.openSession();
            CriteriaBuilder builder =  session.getCriteriaBuilder();
            CriteriaQuery<HibernateWorker> query =  builder.createQuery(HibernateWorker.class);
            query.from(HibernateWorker.class);
            List<HibernateWorker> all = session.createQuery(query).getResultList();
            System.out.println("<<all>>");
            for(HibernateWorker w: all)
                System.out.println(w.toString());
            System.out.println("\n");


            builder = session.getCriteriaBuilder();
            query = builder.createQuery(HibernateWorker.class);
            Root<HibernateWorker> root = query.from(HibernateWorker.class);
            Predicate idPred =  builder.and(builder.ge(root.get("id"), 1L),
                    builder.le(root.get("id"), 4L));
            Predicate namePred = builder.or(builder.equal(root.get("name"), "foo"),
                    builder.equal(root.get("name"), "bar"),
                    builder.equal(root.get("name"), "bim"));

            Predicate agePred = builder.and(builder.gt(root.get("age"), 5),
                    builder.lt(root.get("age"), 40));

            Predicate wagePred = builder.and(builder.ge(root.get("wage"), 100.0),
                    builder.le(root.get("wage"), 300.0));

            Predicate activePred = builder.or(builder.equal(root.get("active"), true),
                    builder.equal(root.get("active"), true));

            Predicate bdayPred = builder.equal(root.get("bday"), LocalDate.now());

            Predicate compoundPred = builder.and(idPred, namePred, agePred, wagePred, activePred, bdayPred);
            query.select(root).where(compoundPred);
            List<HibernateWorker> some = session.createQuery(query).getResultList();
            System.out.println("\n");
            System.out.println("<<some>>");
            for(HibernateWorker w: some) System.out.println(w.toString());
            System.out.println("\n");
            session.close();
        }

        public void someQueries() {
            Session session = factory.openSession();
            Query<HibernateWorker> query = session.createQuery("SELECT w FROM HibernateWorker w", HibernateWorker.class);
            List<HibernateWorker> all = query.list();
            System.out.println("<<all>>");
            for(HibernateWorker w: all) System.out.println(w.toString());

            System.out.println("\n");
            Query<String> names = session.createQuery("SELECT w.name FROM HibernateWorker w", String.class);
            System.out.println("<<names>>");
            for(String name: names.list()) System.out.println(name);
            System.out.println("\n");

            System.out.println("\n");
            query = session.createQuery("SELECT w FROM HibernateWorker w WHERE w.id >=1 and w.id <= 3", HibernateWorker.class);
            all = query.list();
            for(HibernateWorker w: all) System.out.println(w.toString());

            System.out.println("\n");

            names = session.createQuery("SELECT w.name FROM HibernateWorker w WHERE w.id >=1 and w.id <= 2", String.class);
            for(String name: names.list()) System.out.println(name);


            System.out.println("\n");
            names = session.createQuery("SELECT w.name FROM HibernateWorker w WHERE w.id >= :lower_bound and w.id <= :upper_bound", String.class);
            names.setParameter("lower_bound", 2L);
            names.setParameter("upper_bound", 3L);
            for(String name: names.list()) System.out.println(name);
            System.out.println("\n");

            names = session.createQuery("SELECT w.name FROM HibernateWorker w WHERE w.id >= ?1 and w.id <= ?2", String.class);
            names.setParameter(1, 1L);
            names.setParameter(2, 2L);
            for(String name: names.list()) System.out.println(name);

            System.out.println("\n");

            session.close();
        }

        public void addWorker(HibernateWorker worker) {
            Session session = factory.openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
/*                HibernateWorker w = new HibernateWorker();
                w.setId(worker.getId());
                w.setName(worker.getName());
                w.setAge(worker.getAge());
                w.setWage(worker.getWage());
                w.setActive(worker.isActive());
                session.save(w);*/
                session.save(worker);
                tx.commit();
            } catch(Exception ex) {
                ex.printStackTrace();
                if(tx != null) tx.rollback();
            } finally {
                session.close();
            }
        }


        public void updateWorker(long id, HibernateWorker worker) {
            Session session = factory.openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                HibernateWorker oldWorker = session.get(HibernateWorker.class, id);
                oldWorker.setName(worker.getName());
                oldWorker.setAge(worker.getAge());
                oldWorker.setWage(worker.getWage());
                oldWorker.setActive(worker.isActive());
                session.update(oldWorker);
                tx.commit();
            } catch(Exception ex) {
                ex.printStackTrace();
                if(tx != null) tx.rollback();
            } finally {
                session.close();
            }
        }

        public void removeWorker(long id) {
            Session session = factory.openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                HibernateWorker oldWorker = session.get(HibernateWorker.class, id);
                session.delete(oldWorker);
                tx.commit();
            } catch(Exception ex) {
                ex.printStackTrace();
                if(tx != null) tx.rollback();
            } finally {
                session.close();
            }
        }


        public HibernateWorker getOneWorker(long id) {
            Session session = factory.openSession();
            Transaction tx = null;
            HibernateWorker ret =  null;
            try {
                tx = session.beginTransaction();
                ret = session.get(HibernateWorker.class, id);
                tx.commit();
            } catch(Exception ex) {
                ex.printStackTrace();
                if(tx != null) tx.rollback();
            } finally {
                session.close();
            }
            return ret;
        }

        public List<HibernateWorker> getAllWorkers() {
            Session session = factory.openSession();
            Transaction tx = null;
            List<HibernateWorker> all = null;
            try {
                tx = session.beginTransaction();
                all = session.createQuery("FROM HibernateWorker", HibernateWorker.class).list();
                tx.commit();
            } catch(Exception ex) {
                ex.printStackTrace();
                if(tx != null) tx.rollback();
            } finally {
                session.close();
            }

            return all;
        }


    }
}


class _WorkerDAOImplHibernate implements WorkerDAO {

    private SessionFactory factory ;

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





