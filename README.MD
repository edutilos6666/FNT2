**In this project, i have tried to used  
spring rest, jax-rs , jms , databases with spring 
or separately(mysql, mongodb, neo4j), some jdbc frameworks
(jdbc, spring JdbcTemplate, hibernate).**
## Tools , that were used ## 
<ul> 
<li>Intellij IDEA 2017.1.3</li>
<li>jdk 1.8</li>
<li>spring 5.0.6</li>
<li>ubuntu 16.04 LTS</li>
<li>Tomcat 8.5.15</li>
<li>Jetty 9.4.10</li>
</ul>


### REST URLs for WorkerController ###
**URL-base for WorkerController:** http://localhost:8080/worker
<ul>
<li>/save_manual/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_manual/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_manual/{id}</li>
<li>/findById_manual/{id}</li>
<li>/findAll_manual</li>

<li>/save_jdbc/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_jdbc/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_jdbc/{id}</li>
<li>/findById_jdbc/{id}</li>
<li>/findAll_jdbc</li>
<li>/connect_jdbc</li>
<li>/disconnect_jdbc</li>
<li>/save_jdbc_post_form</li>
<li>/save_jdbc_post(**POST request**)</li>
<li>/update_jdbc_post_form</li>
<li>/update_jdbc_post(**POST request**)</li>


<li>/save_mongo/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_mongo/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_mongo/{id}</li>
<li>/findById_mongo/{id}</li>
<li>/findAll_mongo</li>
<li>/connect_mongo</li>
<li>/disconnect_mongo</li>
<li>/save_mongo_post_form</li>
<li>/save_mongo_post(**POST request**)</li>
<li>/update_mongo_post_form</li>
<li>/update_mongo_post(**POST request**)</li>

<li>/save_neo4j/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_neo4j/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_neo4j/{id}</li>
<li>/findById_neo4j/{id}</li>
<li>/findAll_neo4j</li>
<li>/connect_neo4j</li>
<li>/disconnect_neo4j</li>
<li>/save_neo4j_post_form</li>
<li>/save_neo4j_post(**POST request**)</li>
<li>/update_neo4j_post_form</li>
<li>/update_neo4j_post(**POST request**)</li>

<li>/save_hibernate/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_hibernate/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_hibernate/{id}</li>
<li>/findById_hibernate/{id}</li>
<li>/findAll_hibernate</li>
<li>/connect_hibernate</li>
<li>/disconnect_hibernate</li>
<li>/save_hibernate_post_form</li>
<li>/save_hibernate_post(**POST request**)</li>
<li>/update_hibernate_post_form</li>
<li>/update_hibernate_post(**POST request**)</li>
</ul>


### REST URLs for WorkerJsonController ###
**URL-base for WorkerJsonController:** http://localhost:8080/worker/json
<ul>
<li>/save_manual/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_manual/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_manual/{id}</li>
<li>/findById_manual/{id}</li>
<li>/findAll_manual</li>
<li>/findAll_manual/re</li>
<li>/save_manual/re(**POST request**)</li>
</ul>

### REST URLs for WorkerXmlController ###
**URL-base for WorkerJsonController:** http://localhost:8080/worker/xml
<ul>
<li>/save_manual/{id}/{name}/{age}/{wage}/{active}</li>
<li>/update_manual/{id}/{name}/{age}/{wage}/{active}</li>
<li>/remove_manual/{id}</li>
<li>/findById_manual/{id}</li>
<li>/findAll_manual</li>
<li>/save_manual/re(**POST request**)</li>
<li>/update_manual/re(**POST request**)</li>
<li>/remove_manual/re(**POST request**)</li>
<li>/findById_manual/re/{id}</li>
<li>/findAll_manual/re</li>
</ul>

## curl examples ##
**save new worker** <br/>
curl -X POST -H "Content-Type: application/json" --data '{"id":2,"name": "bar","age":20, "wage": 200.0, "active":false}' http://localhost:8080/worker/json/save_manual<br/>
curl  -i -H "Accept: application/json" -X POST -H "Content-Type: application/json" --data '{"id":1,"name": "foo","age":10, "wage": 100.0, "active":true}' http://localhost:8080/worker/json/save_manual  <br/>
curl  -i -H "Accept: application/json" -X POST -H "Content-Type: application/json" --data '{"id":2,"name": "bar","age":20, "wage": 200.0, "active":false}' http://localhost:8080/worker/json/save_manual  <br/>
curl  -i -H "Accept: application/json" -X POST -H "Content-Type: application/json" --data '{"id":3,"name": "bim","age":30, "wage": 300.0, "active":true}' http://localhost:8080/worker/json/save_manual  <br/>
**find all workers** <br/>
curl http://localhost:8080/worker/json/findAll_manual <br/>
**find worker by id**<br/>
curl http://localhost:8080/worker/json/findById_manual/1<br/>
curl http://localhost:8080/worker/json/findById_manual/2<br/>
curl http://localhost:8080/worker/json/findById_manual/3<br/>
**update worker by id**<br/>
curl  -i -H "Accept: application/json" -X POST -H "Content-Type: application/json" --data '{"id":2,"name": "new_bar","age":66, "wage": 300.0, "active":true}' http://localhost:8080/worker/json/update_manual<br/>
**remover worker by id**<br/>
curl http://localhost:8080/worker/json/remove_manual/3<br/>
