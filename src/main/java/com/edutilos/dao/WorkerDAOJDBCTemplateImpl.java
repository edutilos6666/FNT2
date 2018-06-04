package com.edutilos.dao;


import com.edutilos.mapper.WorkerRowMapper_JdbcTemplate;
import com.edutilos.model.Worker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class WorkerDAOJDBCTemplateImpl implements WorkerDAO {
    private JdbcTemplate template;
    public WorkerDAOJDBCTemplateImpl() {
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
        return template.queryForObject(sql, new Object[]{id}, new WorkerRowMapper_JdbcTemplate());
    }

    @Override
    public List<Worker> findAll() {
        String sql = "select * from Worker";
        return template.query(sql, new WorkerRowMapper_JdbcTemplate());
    }
}
