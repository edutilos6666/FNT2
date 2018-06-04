package com.edutilos.test;

import com.edutilos.model.Worker;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class XmlMarshallingExample {
    public static void main(String[] args) {
        example1();
    }

    private static void example1() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.edutilos.model", "com.edutilos.test");

        _Example1 ex = new _Example1(marshaller, marshaller);
        List<Worker> workers = Arrays.asList(
           new Worker(1L, "foo", 10, 100.0, true),
                new Worker(2L, "bar", 20, 200.0, false),
                new Worker(3L, "bim", 30, 300.0, true)
        );
        String jaxb2_file = "worker_jaxb2.xml";
        ex.convertObjectToXml(workers.get(0), jaxb2_file);
        Worker ret1 = (Worker)ex.convertXmlToObject(jaxb2_file);
        System.out.printf("ret1 = %s\n", ret1.toString());

        String jaxb2_file2 = "workers_jaxb2.xml";
        WorkerListForMarhsalling workerList = new WorkerListForMarhsalling();
        workerList.setWorkers(workers);
        ex.convertObjectToXml(workerList, jaxb2_file2);
        WorkerListForMarhsalling ret2 = (WorkerListForMarhsalling)ex.convertXmlToObject(jaxb2_file2);
        System.out.println("<<workers>>");
        for(Worker w: ret2.getWorkers()) {
            System.out.println(w.toString());
        }
    }

    private static class _Example1 {
        private Marshaller marshaller;
        private Unmarshaller unmarshaller;

        public _Example1(Marshaller marshaller, Unmarshaller unmarshaller) {
            this.marshaller = marshaller;
            this.unmarshaller = unmarshaller;
        }

        public void convertObjectToXml(Object obj, String xmlFile) {
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(xmlFile);
                marshaller.marshal(obj, new StreamResult(os));
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally  {
                if(os != null) try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public Object convertXmlToObject(String xmlFile) {
            FileInputStream is = null;
            try {
                is = new FileInputStream(xmlFile);
                return unmarshaller.unmarshal(new StreamSource(is));
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                if(is != null) try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
