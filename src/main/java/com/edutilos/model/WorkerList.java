package com.edutilos.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by edutilos on 04.06.18.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkerList {
    @XmlElement
    private List<Worker> workers;

    public List<Worker> getWorkers() {
        return workers;
    }

    public void addAll(List<Worker> other) {
        workers.addAll(other);
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
