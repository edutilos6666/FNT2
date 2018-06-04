package com.edutilos.dao;

import com.edutilos.model.Worker;
import com.edutilos.mapper.WorkerRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edutilos on 02.06.18.
 */
public class WorkerDAOJDBCImpl implements WorkerDAO {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://localhost/test3?useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Logger logger = LogManager.getLogger(WorkerDAOJDBCImpl.class);
    private void connect() {
        try {
            Class.forName(MYSQL_DRIVER);
            conn = DriverManager.getConnection(MYSQL_URL, USER, PASSWORD);
            stmt = conn.createStatement();
            logger.info("Connection was successfull.");
        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("Connection failed.");
        }
    }

    public void disconnect()  {
        try {
            if(rs != null && !rs.isClosed())  rs.close();
            if(pstmt != null && !pstmt.isClosed()) pstmt.close();

            if(stmt != null && !stmt.isClosed()) stmt.close();

            if(conn != null && !conn.isClosed()) conn.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    public WorkerDAOJDBCImpl() {
        connect();
        createTable("Worker");
    }

    public void createTable(String name) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(name).append("(")
                    .append("id BIGINT PRIMARY KEY, ")
                    .append("name VARCHAR(50) NOT NULL, ")
                    .append("age INT NOT NULL, ")
                    .append("wage DOUBLE NOT NULL, ")
                    .append("active boolean NOT NULL").append(")");
            if(stmt.execute(sb.toString())) {
                logger.info(String.format("successfully created table %s.", name));
            } else {
                logger.info(String.format("could not create table %s.", name));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dropTable(String name)  {
        try {
            String sql = String.format("DROP TABLE IF EXISTS %s", name);
            if(stmt.execute(sql)) {
                logger.info(String.format("successfully dropped table %s.", name));
            } else {
                logger.info(String.format("could not drop table %s.", name));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void save(Worker w) {
        try {
            String sql = "INSERT INTO Worker VALUES(?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, w.getId());
            pstmt.setString(2, w.getName());
            pstmt.setInt(3, w.getAge());
            pstmt.setDouble(4, w.getWage());
            pstmt.setBoolean(5, w.isActive());
            if(pstmt.execute()) {
                logger.info("Successfully add new entry.");
            } else {
                logger.info("Could not add entry");
            }
            pstmt.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(long id, Worker w) {
        try {
            String sql = "UPDATE Worker set name = ? , age = ?, wage = ?, active = ? where id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, w.getName());
            pstmt.setInt(2, w.getAge());
            pstmt.setDouble(3, w.getWage());
            pstmt.setBoolean(4, w.isActive());
            pstmt.setLong(5, w.getId());
            if(pstmt.execute()) {
                logger.info("Successfully updated entry.");
            } else {
                logger.info("Could not update entry.");
            }
            pstmt.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void remove(long id) {
        try {
            String sql = "DELETE FROM Worker where id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            if(pstmt.execute()) {
                logger.info("Successfully deleted entry.");
            } else {
                logger.info("Could not delete entry.");
            }
            pstmt.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Worker findById(long id) {
        try {
            String sql = "SELECT * FROM Worker WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            Worker res = new WorkerRowMapper().mapFromRSToWorker(rs);
            rs.close();
            return res;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Worker> findAll() {
        List<Worker> ret = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Worker";
            rs = stmt.executeQuery(sql);
            WorkerRowMapper mapper = new WorkerRowMapper();
            while(rs.next()) {
                ret.add(mapper.mapFromRSToWorker(rs));
            }
            rs.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            return ret;
        }

    }
}
