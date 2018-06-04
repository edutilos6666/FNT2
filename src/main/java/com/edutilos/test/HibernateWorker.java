package com.edutilos.test;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by edutilos on 03.06.18.
 */
@Entity
@Table(name="HibernateWorker")
@NamedQueries({
        @NamedQuery(name="select.all", query="select w from HibernateWorker w"),
        @NamedQuery(name="select.names", query="select w.name from HibernateWorker w"),
        @NamedQuery(name="select.some.names", query="select w.name from HibernateWorker w where w.id >=1 and w.id <=2")
})
public class HibernateWorker {
    @Id
    @Column
    private long id;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private double wage;
    @Column
    private boolean active;
    @Column
    private LocalDate bday;

    public HibernateWorker() {
    }

    public HibernateWorker(long id, String name, int age, double wage, boolean active, LocalDate bday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.wage = wage;
        this.active = active;
        this.bday = bday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getBday() {
        return bday;
    }

    public void setBday(LocalDate bday) {
        this.bday = bday;
    }

    @Override
    public String toString() {
        return "HibernateWorker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", wage=" + wage +
                ", active=" + active +
                ", bday=" + bday +
                '}';
    }
}
