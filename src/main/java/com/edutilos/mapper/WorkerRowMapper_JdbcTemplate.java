package com.edutilos.mapper;

import com.edutilos.model.Worker;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by edutilos on 03.06.18.
 */
public class WorkerRowMapper_JdbcTemplate implements RowMapper<Worker> {

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
