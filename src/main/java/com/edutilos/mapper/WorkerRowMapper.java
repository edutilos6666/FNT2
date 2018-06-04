package com.edutilos.mapper;

import com.edutilos.model.Worker;

import java.sql.ResultSet;

/**
 * Created by edutilos on 02.06.18.
 */
public class WorkerRowMapper {
    public Worker mapFromRSToWorker(ResultSet rs) {
        Worker w = null;
        try {
            w = new Worker();
            w.setId(rs.getLong(1));
            w.setName(rs.getString(2));
            w.setAge(rs.getInt("age"));
            w.setWage(rs.getDouble("wage"));
            w.setActive(rs.getBoolean("active"));
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            return w;
        }
    }
}
