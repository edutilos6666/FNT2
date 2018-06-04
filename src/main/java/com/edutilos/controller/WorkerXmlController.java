package com.edutilos.controller;

import com.edutilos.dao.WorkerDAOManualImpl;
import com.edutilos.model.Worker;
import com.edutilos.model.WorkerList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by edutilos on 04.06.18.
 */
@Controller
@RequestMapping("/worker/xml")
public class WorkerXmlController {
    private WorkerDAOManualImpl daoManualImpl;
    public WorkerXmlController() {
        daoManualImpl = new WorkerDAOManualImpl();
//        temp();
    }

    private void temp() {
        List<Worker> workers = Arrays.asList(
           new Worker(1L, "foo", 10, 100.0, true),
                new Worker(2L, "bar", 20, 200.0, false),
                new Worker(3L, "bim", 30, 300.0, true)
        );
        for(Worker w: workers) {
            daoManualImpl.save(w);
        }
    }


    @GetMapping(value = "/save_manual/{id}/{name}/{age}/{wage}/{active}",
            produces = MediaType.APPLICATION_XML_VALUE)
    public ModelAndView saveManual(@PathVariable long id, @PathVariable String name,
                                   @PathVariable int age, @PathVariable double wage,
                                   @PathVariable boolean active) {
        daoManualImpl.save(new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/xml/findAll_manual");
    }


    @GetMapping(value="/update_manual/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView updateManual(@PathVariable long id, @PathVariable String name,
                                     @PathVariable int age, @PathVariable double wage,
                                     @PathVariable boolean active) {
        daoManualImpl.update(id, new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/xml/findAll_manual");
    }

    @GetMapping("/remove_manual/{id}")
    public ModelAndView removeManual(@PathVariable long id) {
        daoManualImpl.remove(id);
        return new ModelAndView("redirect:/worker/xml/findAll_manual");
    }



    @GetMapping(value = "/findById_manual/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ModelAndView findByIdManual(@PathVariable long id) {
        Worker w = daoManualImpl.findById(id);
        ModelAndView mav = new ModelAndView("xmlView");
        mav.addObject("worker", w);
        return mav;
    }



    @GetMapping(value = "/findAll_manual", produces = MediaType.APPLICATION_XML_VALUE)
    public ModelAndView findAllManual() {
        List<Worker> workers = daoManualImpl.findAll();
        WorkerList workerList = new WorkerList();
        workerList.setWorkers(workers);
        ModelAndView mav = new ModelAndView("xmlView");
        mav.addObject("workers", workerList);
        return mav;
    }



    @PostMapping("/save_manual")
    public ModelAndView saveManual(@RequestBody @Validated Worker worker,
                                   BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/xml/findAll_manual");
        if(result.hasErrors()) return mav;
        daoManualImpl.save(worker);
        return mav;
    }

    @PostMapping("/update_manual")
    public ModelAndView updateManual(@RequestBody @Validated Worker worker,
                                     BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/xml/findAll_manual");
        if(result.hasErrors()) return mav;
        daoManualImpl.update(worker.getId(), worker);
        return mav;
    }


    @GetMapping(value="/findById_manual/re/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Worker> findByIdManualRE(@PathVariable long id) {
        Worker w = daoManualImpl.findById(id);
        return new ResponseEntity<Worker>(w, HttpStatus.OK);
    }

    @GetMapping(value="/findAll_manual/re", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<WorkerList> findAllManualRE() {
        WorkerList workerList = new WorkerList();
        workerList.setWorkers(daoManualImpl.findAll());
        return new ResponseEntity<WorkerList>(workerList , HttpStatus.OK);
    }

    @PostMapping(value="/save_manual/re")
    public ResponseEntity<WorkerList> saveManualRE(@RequestBody @Validated Worker worker,
                                                   BindingResult result) {
        if(result.hasErrors()) return findAllManualRE();
        daoManualImpl.save(worker);
        return findAllManualRE();
    }

    @PostMapping(value="/update_manual/re")
    public ResponseEntity<WorkerList> updateManualRE(@RequestBody @Validated Worker worker,
                                                     BindingResult result) {
        if(result.hasErrors()) return findAllManualRE();
        daoManualImpl.update(worker.getId(), worker);
        return findAllManualRE();
    }

    @PostMapping(value="/remove_manual/re")
    public ResponseEntity<WorkerList> removeManualRE(@RequestBody long id) {
        daoManualImpl.remove(id);
        return findAllManualRE();
    }


}
