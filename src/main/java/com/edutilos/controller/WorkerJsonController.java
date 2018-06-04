package com.edutilos.controller;

import com.edutilos.dao.WorkerDAOManualImpl;
import com.edutilos.model.Worker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 04.06.18.
 */
@Controller
@RequestMapping("/worker/json")
public class WorkerJsonController {
    private WorkerDAOManualImpl daoManualImpl;

    public WorkerJsonController() {
        daoManualImpl = new WorkerDAOManualImpl();
    }



    @GetMapping("/save_manual/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView saveManual(@PathVariable long id, @PathVariable String name,
                                   @PathVariable int age, @PathVariable double wage,
                                   @PathVariable boolean active) {
        Worker w = new Worker(id, name, age, wage, active);
        daoManualImpl.save(w);
        return new ModelAndView("redirect:/worker/json/findAll_manual");
    }


    @GetMapping("/update_manual/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView updateManual(@PathVariable long id, @PathVariable String name,
                                     @PathVariable int age, @PathVariable double wage,
                                     @PathVariable boolean active) {
        daoManualImpl.update(id, new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/json/findAll_manual");
    }

    @GetMapping("/remove_manual/{id}")
    public ModelAndView removeManual(@PathVariable long id) {
        daoManualImpl.remove(id);
        return new ModelAndView("redirect:/worker/json/findAll_manual");
    }

    @GetMapping("/findById_manual/{id}")
    public ModelAndView findByIdManual(@PathVariable long id) {
        Worker w= daoManualImpl.findById(id);
        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("worker", w);
        return mav;
    }

    @GetMapping("/findAll_manual")
    public ModelAndView findAllManual() {
        List<Worker> all = daoManualImpl.findAll();
        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("workers", all);
        return mav;
    }

    //headers and consumes in @PostMapping just narrows , does not do anything
    //@PostMapping(value = "/save_manual", headers="content-type=application/json")
    @PostMapping(value = "/save_manual")
    public ModelAndView saveManual(@RequestBody @Validated Worker worker,
                                   BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/json/findAll_manual");
        if(result.hasErrors())
            return mav;
        daoManualImpl.save(worker);
        return mav;
    }

    @PostMapping(value="/update_manual")
    public ModelAndView updateManual(@RequestBody @Validated Worker worker,
                                     BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/json/findAll_manual");
        if(result.hasErrors())
            return mav;
        daoManualImpl.update(worker.getId(), worker);
        return mav;
    }


    //with response entity
    @GetMapping("/findAll_manual/re")
    public ResponseEntity<List<Worker>> findAllManualRE() {
        List<Worker> workers = daoManualImpl.findAll();
        return new ResponseEntity<List<Worker>>(workers, HttpStatus.OK);
    }


    @PostMapping("/save_manual/re")
    public ResponseEntity<List<Worker>> saveManualRE(@RequestBody @Validated Worker worker,
                                             BindingResult result,
                                             UriComponentsBuilder ucBuilder) {
        if(result.hasErrors()) {
//            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            return findAllManualRE();
        }
        daoManualImpl.save(worker);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(ucBuilder.path("redirect:/worker/json/findAll_manual/re").build().toUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return new ResponseEntity<Void>(headers, HttpStatus.OK);
        return findAllManualRE();
    }


}
