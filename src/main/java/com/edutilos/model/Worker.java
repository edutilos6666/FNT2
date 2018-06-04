package com.edutilos.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by edutilos on 02.06.18.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="Worker")
//@JacksonXmlRootElement
public class Worker {
    @Id
    @Column
    @XmlAttribute
//    @JacksonXmlProperty
    private long id;
    @Column
    @XmlAttribute
//    @JacksonXmlProperty
    private String name;
    @Column
    @XmlAttribute
//    @JacksonXmlProperty
    private int age;
    @Column
    @XmlAttribute
//    @JacksonXmlProperty
    private double wage;
    @Column
    @XmlAttribute
//    @JacksonXmlProperty
    private boolean active;

    public Worker(long id, String name, int age, double wage, boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.wage = wage;
        this.active = active;
    }

    public Worker() {
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

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", wage=" + wage +
                ", active=" + active +
                '}';
    }
}
