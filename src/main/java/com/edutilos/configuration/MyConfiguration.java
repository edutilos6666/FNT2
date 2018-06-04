package com.edutilos.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

import java.util.List;

/**
 * Created by edutilos on 02.06.18.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.edutilos"})
public class MyConfiguration implements WebMvcConfigurer{

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
        builder.indentOutput(true);
      //  converters.add(new MappingJackson2XmlHttpMessageConverter(builder.build()));
    }

    @Bean
    public ViewResolver beanNameViewResolver()  {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        return resolver;
    }


    @Bean("jsonView")
    public View jsonView() {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        return view;
    }


    @Bean("xmlView")
    public View xmlView() {
        MappingJackson2XmlView view = new MappingJackson2XmlView();
        return view;
    }




    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }




}