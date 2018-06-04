package com.edutilos.controller;

import com.edutilos.dao.*;
import com.edutilos.model.Worker;
import com.edutilos.validator.WorkerValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Created by edutilos on 02.06.18.
 */
//@Controller
@RequestMapping("/worker")
public class WorkerController {
    private Logger logger = LogManager.getLogger(WorkerController.class);
    private WorkerDAO daoManualImpl = new WorkerDAOManualImpl();
    private WorkerDAOJDBCImpl daoJdbcImpl = new WorkerDAOJDBCImpl();
    private WorkerDAOMongoImpl daoMongoImpl = new WorkerDAOMongoImpl();
    private WorkerDAONeo4jImpl daoNeo4jImpl = new WorkerDAONeo4jImpl();
    private WorkerDAOHibernateImpl daoHibernateImpl = new WorkerDAOHibernateImpl();

    @Autowired
    private WorkerValidator workerValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(workerValidator);
    }

    @PreDestroy
    public void cleanUp() {
        daoNeo4jImpl.shutdown();
    }


    @RequestMapping(value="/save_manual/{id}/{name}/{age}/{wage}/{active}", method = RequestMethod.GET)
    public ModelAndView saveManual(@PathVariable long id, @PathVariable String name,
                                   @PathVariable int age, @PathVariable double wage,
                                   @PathVariable boolean active) {
        daoManualImpl.save(new Worker(id, name, age, wage, active));
        logger.info("saved worker successfully.");
        return new ModelAndView("redirect:/worker/findAll_manual");
    }

    @RequestMapping(value= "/update_manual/{id}/{name}/{age}/{wage}/{active}", method=RequestMethod.GET)
    public ModelAndView updateManual(@PathVariable long id, @PathVariable String name,
                               @PathVariable int age, @PathVariable double wage,
                               @PathVariable boolean active) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_manual");
        daoManualImpl.update(id, new Worker(id, name, age, wage, active));
        return mav;
    }

    @RequestMapping(value = "/remove_manual/{id}", method = RequestMethod.GET)
    public ModelAndView removeManual(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_manual");
        daoManualImpl.remove(id);
        return mav;
    }

    @RequestMapping(value = "/findById_manual/{id}", method = RequestMethod.GET)
    public ModelAndView findByIdManual(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("worker/findById");
        Worker w = daoManualImpl.findById(id);
        mav.addObject("worker", w);
        return mav;
    }

    @RequestMapping(value = "/findAll_manual", method = RequestMethod.GET)
    public ModelAndView findAllManual() {
        ModelAndView mav = new ModelAndView("worker/findAll");
        mav.addObject("workers", daoManualImpl.findAll());
        return mav;
    }



    @RequestMapping(value = "/save_jdbc/{id}/{name}/{age}/{wage}/{active}", method = RequestMethod.GET)
    public ModelAndView saveJdbc(@PathVariable long id , @PathVariable String name,
                                 @PathVariable int age, @PathVariable double wage,
                                 @PathVariable boolean active) {
        daoJdbcImpl.save(new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_jdbc");
    }

    @RequestMapping(value="/update_jdbc/{id}/{name}/{age}/{wage}/{active}", method = RequestMethod.GET)
    public ModelAndView updateJdbc(@PathVariable long id, @PathVariable String name,
                                   @PathVariable int age, @PathVariable double wage,
                                   @PathVariable boolean active) {
        daoJdbcImpl.update(id, new Worker(id, name, age, wage,active));
        return new ModelAndView("redirect:/worker/findAll_jdbc");
    }

    @RequestMapping(value="/remove_jdbc/{id}", method=RequestMethod.GET)
    public ModelAndView removeJdbc(@PathVariable long id) {
        daoJdbcImpl.remove(id);
        return new ModelAndView("redirect:/worker/findAll_jdbc");
    }

    @RequestMapping(value="/findById_jdbc/{id}", method = RequestMethod.GET)
    public ModelAndView findByIdJdbc(@PathVariable long id) {
        Worker w = daoJdbcImpl.findById(id);
        ModelAndView mav = new ModelAndView("worker/findById");
        mav.addObject("worker", w);
        return mav;
    }

    @RequestMapping(value="/findAll_jdbc", method = RequestMethod.GET)
    public ModelAndView findAllJdbc() {
        List<Worker> workers = daoJdbcImpl.findAll();
        ModelAndView mav = new ModelAndView("worker/findAll");
        mav.addObject("workers", workers);
        return mav;
    }

    @RequestMapping(value="/disconnect_jdbc", method =RequestMethod.GET)
    public ModelAndView disconnectJdbc() {
        daoJdbcImpl.disconnect();
        return new ModelAndView("redirect:/worker/findAll_jdbc");
    }

    @RequestMapping(value="/connect_jdbc", method =RequestMethod.GET)
    public ModelAndView connectJdbc() {
        daoJdbcImpl = new WorkerDAOJDBCImpl();
        return new ModelAndView("redirect:/worker/findAll_jdbc");
    }


    @GetMapping("/save_jdbc_post_form")
    public ModelAndView saveJdbcPostForm() {
        ModelAndView mav =  new ModelAndView("worker/save_post_form");
        mav.addObject("worker", new Worker());
        return mav;
    }

    @PostMapping("/save_jdbc_post")
    public ModelAndView saveJdbcPost(@ModelAttribute @Validated Worker worker, BindingResult result) {
        if(result.hasErrors()) {
            logger.error("Error count = "+ result.getErrorCount());
            List<FieldError> errors = result.getFieldErrors();
            for(FieldError err: errors) {
                logger.error(result.resolveMessageCodes(err.getCode())[0]);
            }
            return new ModelAndView("redirect:/worker/findAll_jdbc");
        }
        daoJdbcImpl.save(worker);
        return new ModelAndView("redirect:/worker/findAll_jdbc");
    }


    @GetMapping("/update_jdbc_post_form/{id}")
    public ModelAndView updateJdbcPostForm(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("worker/update_post_form");
        mav.addObject("worker", new Worker(id, null, 0, 0.0, false));
        return mav;
    }

    @PostMapping("/update_jdbc_post")
    public ModelAndView updateJdbcPost(@ModelAttribute @Validated Worker worker,
                                       BindingResult result) {
        ModelAndView mov = new ModelAndView("redirect:/worker/findAll_jdbc");
        if(result.hasErrors()) {
            return mov;
        }

        daoJdbcImpl.update(worker.getId(), worker);
        return mov;

    }



    @GetMapping("/save_mongo/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView saveMongo(@PathVariable long id, @PathVariable String name,
                                  @PathVariable int age, @PathVariable double wage,
                                  @PathVariable boolean active) {
        daoMongoImpl.save(new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_mongo");
    }

    @GetMapping("/update_mongo/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView updateMongo(@PathVariable long id, @PathVariable String name,
                                    @PathVariable int age, @PathVariable double wage,
                                    @PathVariable boolean active) {
        daoMongoImpl.update(id, new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_mongo");
    }

    @GetMapping("/remove_mongo/{id}")
    public ModelAndView removeMongo(@PathVariable long id) {
        daoMongoImpl.remove(id);
        return new ModelAndView("redirect:/worker/findAll_mongo");
    }

    @GetMapping("/findById_mongo/{id}")
    public ModelAndView findByIdMongo(@PathVariable long id) {
        Worker w = daoMongoImpl.findById(id);
        ModelAndView mav = new ModelAndView("worker/findById");
        mav.addObject("worker", w);
        return mav;
    }

    @GetMapping("/findAll_mongo")
    public ModelAndView findAllMongo() {
        List<Worker> workers = daoMongoImpl.findAll();
        ModelAndView mav = new ModelAndView("worker/findAll");
        mav.addObject("workers", workers);
        return mav;
    }

    @GetMapping("/save_mongo_post_form")
    public ModelAndView saveMongoPostForm() {
        ModelAndView mav = new ModelAndView("worker/save_post_form");
        mav.addObject("worker", new Worker());
        return mav;
    }

    @PostMapping("/save_mongo_post")
    public ModelAndView saveMongoPost(@ModelAttribute @Validated Worker worker,
                                      BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_mongo");
        if(result.hasErrors()) {
            return mav;
        }

        daoMongoImpl.save(worker);
        return mav;
    }

    @GetMapping("/update_mongo_post_form/{id}")
    public ModelAndView updateMongoPostForm(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("worker/update_post_form");
        Worker w = daoMongoImpl.findById(id);
        if(w == null) {
            return new ModelAndView("redirect:/worker/findAll_mongo");
        }
        mav.addObject("worker", w);
        return mav;
    }

    @PostMapping("/update_mongo_post")
    public ModelAndView updateMongoPost(@ModelAttribute @Validated Worker worker,
                                 BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_mongo");
        if(result.hasErrors()) {
            return mav;
        }
        daoMongoImpl.update(worker.getId(), worker);
        return mav;
    }

    @GetMapping("/connect_mongo")
    public ModelAndView connectMongo() {
        daoMongoImpl = new WorkerDAOMongoImpl();
        return new ModelAndView("redirect:/worker/findAll_mongo");
    }

    @GetMapping("/disconnect_mongo")
    public ModelAndView disconnectMongo() {
        if(daoMongoImpl !=  null) daoMongoImpl.disconnect();
        return new ModelAndView("redirect:/worker/findAll_mongo");
    }



    @GetMapping("/save_neo4j/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView saveNeo4j(@PathVariable long id, @PathVariable String name,
                                  @PathVariable int age, @PathVariable double wage,
                                  @PathVariable boolean active) {
        daoNeo4jImpl.save(new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_neo4j");
    }


    @GetMapping("/update_neo4j/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView updateNeo4j(@PathVariable long id, @PathVariable String name,
                                    @PathVariable int age, @PathVariable double wage,
                                    @PathVariable boolean active) {
        daoNeo4jImpl.update(id, new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_neo4j");
    }

    @GetMapping("/remove_neo4j/{id}")
    public ModelAndView removeNeo4j(@PathVariable long id) {
        daoNeo4jImpl.remove(id);
        return new ModelAndView("redirect:/worker/findAll_neo4j");
    }

    @GetMapping("/findById_neo4j/{id}")
    public ModelAndView findByIdNeo4j(@PathVariable long id) {
        Worker w = daoNeo4jImpl.findById(id);
        ModelAndView mav = new ModelAndView("worker/findById");
        mav.addObject("worker", w);
        return mav;
    }

    @GetMapping("/findAll_neo4j")
    public ModelAndView findAllNeo4j() {
        List<Worker> all = daoNeo4jImpl.findAll();
        ModelAndView mav = new ModelAndView("worker/findAll");
        mav.addObject("workers", all);
        return mav;
    }

    @GetMapping("/connect_neo4j")
    public ModelAndView connectNeo4j() {
        daoNeo4jImpl.lock();
        return new ModelAndView("redirect:/worker/findAll_neo4j");
    }

    @GetMapping("/disconnect_neo4j")
    public ModelAndView disconnectNeo4j() {
        daoNeo4jImpl.shutdown();
        return new ModelAndView("redirect:/worker/findAll_neo4j");
    }

    @GetMapping("/save_neo4j_post_form")
    public ModelAndView saveNeo4jPostForm() {
        Worker w = new Worker();
        ModelAndView mav = new ModelAndView("worker/save_post_form");
        mav.addObject("worker", w);
        return mav;
    }

    @PostMapping("/save_neo4j_post")
    public ModelAndView saveNeo4jPost(@ModelAttribute @Validated Worker worker,
                                      BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_neo4j");
        if(result.hasErrors()){
            return mav ;
        }
        daoNeo4jImpl.save(worker);
        return mav ;
    }

    @GetMapping("/update_neo4j_post_form/{id}")
    public ModelAndView updateNeo4jPostForm(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("worker/update_post_form");
        Worker w = daoNeo4jImpl.findById(id);
        if(w == null) return new ModelAndView("redirect:/worker/findAll_neo4j"); ;
        mav.addObject("worker", w);
        return mav;
    }

    @PostMapping("/update_neo4j_post")
    public ModelAndView updateNeo4jPost(@ModelAttribute @Validated Worker worker,
                                        BindingResult result) {
        ModelAndView mav=  new ModelAndView("redirect:/worker/findAll_neo4j");
        if(result.hasErrors()) return mav;
        daoNeo4jImpl.update(worker.getId(), worker);
        return mav;
    }


    @GetMapping("/save_hibernate/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView saveHibernate(@PathVariable long id,@PathVariable String name,
                                      @PathVariable int age, @PathVariable double wage,
                                      @PathVariable boolean active) {
        daoHibernateImpl.save(new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_hibernate");
    }

    @GetMapping("/update_hibernate/{id}/{name}/{age}/{wage}/{active}")
    public ModelAndView updateHibernate(@PathVariable long id,@PathVariable String name,
                                      @PathVariable int age, @PathVariable double wage,
                                      @PathVariable boolean active) {
        daoHibernateImpl.update(id, new Worker(id, name, age, wage, active));
        return new ModelAndView("redirect:/worker/findAll_hibernate");
    }

    @GetMapping("/remove_hibernate/{id}")
    public ModelAndView removeHibernate(@PathVariable long id) {
        daoHibernateImpl.remove(id);
        return new ModelAndView("redirect:/worker/findAll_hibernate");
    }


    @GetMapping("/findById_hibernate/{id}")
    public ModelAndView findByIdHibernate(@PathVariable long id) {
        Worker w = daoHibernateImpl.findById(id);
        ModelAndView mav = new ModelAndView("worker/findById");
        mav.addObject("worker", w);
        return mav;
    }

    @GetMapping("/findAll_hibernate")
    public ModelAndView findAllHibernate() {
        List<Worker> all = daoHibernateImpl.findAll();
        ModelAndView mav = new ModelAndView("worker/findAll");
        mav.addObject("workers", all);
        return mav;
    }


    @GetMapping("/connect_hibernate")
    public ModelAndView connectHibernate() {
        daoHibernateImpl.configure();
        return new ModelAndView("redirect:/worker/findAll_hibernate");
    }

    @GetMapping("/disconnect_hibernate")
    public ModelAndView disconnectHibernate() {
        daoHibernateImpl.closeSessionFactory();
        return new ModelAndView("redirect:/worker/findAll_hibernate");
    }

    @GetMapping("/save_hibernate_post_form")
    public ModelAndView saveHibernatePostForm() {
        Worker w = new Worker();
        ModelAndView mav = new ModelAndView("worker/save_post_form");
        mav.addObject("worker", w);
        return mav;
    }
    @PostMapping("/save_hibernate_post")
    public ModelAndView saveHibernatePost(@ModelAttribute @Validated Worker worker,
                                          BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_hibernate");
        if(result.hasErrors()) return mav;
        daoHibernateImpl.save(worker);
        return mav;
    }

    @GetMapping("/update_hibernate_post_form/{id}")
    public ModelAndView updateHibernatePostForm(@PathVariable long id) {
        Worker w = daoHibernateImpl.findById(id);
        ModelAndView mav = new ModelAndView("worker/update_post_form");
        mav.addObject("worker", w);
        return mav;
    }
    @PostMapping("/update_hibernate_post")
    public ModelAndView updateHibernatePost(@ModelAttribute @Validated Worker worker,
                                            BindingResult result) {
        ModelAndView mav = new ModelAndView("redirect:/worker/findAll_hibernate");
        if(result.hasErrors()) return mav;
        daoHibernateImpl.update(worker.getId(), worker);
        return mav;
    }



}
