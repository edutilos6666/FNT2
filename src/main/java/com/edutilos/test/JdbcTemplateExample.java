package com.edutilos.test;

import com.edutilos.dao.WorkerDAO;
import com.edutilos.model.Worker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class JdbcTemplateExample {
    public static void main(String[] args) {
        testDao();
    }

    private static void testDao() {
        _WorkerDAOJdbcTemplateImpl dao = new _WorkerDAOJdbcTemplateImpl();
        dao.dropTable();
        dao.createTable();
        List<Worker> workers = Arrays.asList(
                new Worker(1L, "foo", 10, 100.0, true),
                new Worker(2L, "bar", 20, 200.0, false),
                new Worker(3L, "bim", 30, 300.0, true)
        );

        for(Worker w: workers) dao.save(w);

        List<Worker> all = dao.findAll();
        System.out.println("<<all workers>>");
        for(Worker w:all) System.out.println(w.toString());

        //update and findById
        dao.update(1L, new Worker(1L, "new-foo", 66, 666.6 , false));
        Worker one = dao.findById(1L);
        System.out.printf("one with id=1L= %s\n", one.toString());

        //remove
        dao.remove(1L);
        all= dao.findAll();
        System.out.println("<<all after remove id=1L>>");
        for(Worker w: all)
            System.out.println(w.toString());


        //truncate
        all= dao.findAll();
        for(Worker w: all) dao.remove(w.getId());
    }


    private static class _WorkerDAOJdbcTemplateImpl  implements WorkerDAO{
        private JdbcTemplate template;
        public _WorkerDAOJdbcTemplateImpl() {
            template = new JdbcTemplate(generateDataSource());
        }

        private DataSource generateDataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=Europe/Berlin");
            ds.setUsername("root");
            ds.setPassword("root");
            return ds;
        }

        public void dropTable() {
            String sql = "drop table if exists Worker";
            template.execute(sql);
        }


        public void createTable() {
            StringBuilder sb = new StringBuilder();
            sb.append("create table if not exists Worker(")
                    .append("id bigint primary key, ")
                    .append("name varchar(50) not null, ")
                    .append("age int not null, ")
                    .append("wage double not null, ")
                    .append("active boolean not null)");
            template.update(sb.toString());
        }

        @Override
        public void save(Worker w) {
            String sql = "insert into Worker VALUES(?,?,?,?,?)";
            template.update(sql, w.getId(), w.getName(), w.getAge(), w.getWage(), w.isActive());
        }

        @Override
        public void update(long id, Worker w) {
            String sql = "update Worker set name=?, age=?,wage=?, active=? where id=?";
            template.update(sql, w.getName(), w.getAge(), w.getWage(), w.isActive(), w.getId());
        }

        @Override
        public void remove(long id) {
            String sql = "delete from Worker where id=?";
            template.update(sql, id);
        }

        @Override
        public Worker findById(long id) {
            String sql = "select * from Worker where id=?";
            return template.queryForObject(sql, new Object[]{id}, new _WorkerRowMapper());
        }

        @Override
        public List<Worker> findAll() {
            String sql = "select * from Worker";
            return template.query(sql, new _WorkerRowMapper());
        }
    }

    private static class _WorkerRowMapper implements RowMapper<Worker> {

        @Override
        public Worker mapRow(ResultSet rs, int rowNum) throws SQLException {
            Worker w = new Worker();
            w.setId(rs.getLong(1));
            w.setName(rs.getString(2));
            w.setAge(rs.getInt(3));
            w.setWage(rs.getDouble(4));
            w.setActive(rs.getBoolean(5));
            return w;
        }
    }
}

