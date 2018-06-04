package com.edutilos.webclient;

import com.edutilos.model.Worker;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 03.06.18.
 */
public class SpringRestClientExample {
    public static void main(String[] args) {
        example1();
    }

    private static void example1() {
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        StringHttpMessageConverter converter1 = new StringHttpMessageConverter();
        restTemplate.getMessageConverters().add(converter1);
        MarshallingHttpMessageConverter converter2 = new MarshallingHttpMessageConverter();
        //converter2.setMarshaller();
        List<Worker> workers = Arrays.asList(
            new Worker(1L, "foo", 15, 110.0, true),
                new Worker(2L, "bar", 20, 200.0, false),
                new Worker(3L, "bim", 30, 300.0, true)
        );

        HttpEntity<Worker> request = null;
        for(Worker w: workers) {
            request = new HttpEntity<>(w);
            String url = "http://localhost:8080/worker/save_hibernate_post";
            restTemplate.postForObject(url, request, String.class);
            //System.out.println(worker.toString());
        }

        List<Worker> all = restTemplate.getForObject("http://localhost:8080/findAll_hibernate", List.class);
        System.out.println("<<all>>");
        for(Worker w: all) {
            System.out.println(w.toString());
        }
    }
}
