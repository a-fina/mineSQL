mineSQL 0.9.9a
=============
DB, Web Services and Slide 

Requisite:
----------
Apache Maven 3
Oracle Java 1.7

What:
-----
 - DB: H2, Jackrabbit, DB2, MySQL, Oracle, SQLServer, SQLJet, ORMLite
 - PHP with Quercus
 - ExtJs
 - GoogleAPI
 - Map-Hilight
 - TomCat & NetBeans
 - Reveal.js

Stand-alone Run:
----
git co https://github.com/bayois/mineSQL/tree/boma

mvn package

java -jar target/mineSQL-0.9.9a-full.jar [browser]

firefox http://localhost:8080/


Heroku:
-------
heroku local


TODO:
----
- https://github.com/jingweno/gaffer
  mvn gaffer:start

- OSGI: Felix Embed

- PDF: custom skin for Saved Report

- OSGI: Bundle Conversion Tool
  https://access.redhat.com/documentation/en-US/Fuse_ESB_Enterprise/7.0/html/Deploying_into_the_Container/files/DeployJar-Convert.html

- Include REST API DOCS 
    https://github.com/swagger-api/swagger-core/wiki/Servlet-Quickstart
    <dependency>
      <groupId>com.wordnik</groupId>
      <artifactId>swagger-servlet_2.10</artifactId>
      <version>1.3.10</version>
    </dependency>

- Kanbas Dashboard

DOC:
---

- Kanbas Guide
    http://leankanban.com/guide/
    http://www.mokabyte.it/2013/12/AgileKanban/#ref
        

- REST Server / Heroku deploy
    https://jersey.java.net/documentation/latest/deployment.html#deployment.servlet
    https://jersey.java.net/documentation/latest/getting-started.html#heroku-webapp

- HADOOP
    http://azure.microsoft.com/it-it/documentation/articles/hdinsight-hbase-build-java-maven/
    http://hbase.apache.org/book.html#quickstart

- Interactive GRAPH
    http://www.gojs.net/latest/learn/index.html
    http://sigmajs.org/: more closer to node stucture ( JSON )

- Others
    http://www.datamartist.com/

- Jersey / Grizzly JSP
    http://blog.usul.org/using-jsp-in-a-jersey-jax-rs-restful-application/


