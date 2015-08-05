mineSQL
=======
DB, Web Services and Graph playground

What:
-----
 - DB: H2, Jackrabbit, DB2, MySQL, Oracle, SQLServer, SQLJet, ORMLite
 - PHP with Quercus
 - ExtJs
 - GoogleAPI
 - Map-Hilight
 - Browser: Webview
 - TomCat & NetBeans

Run:
----
git co git@github.com:bayois/mineSQL.git

edit mineSQL/src/main/webapp/config.jsp 

mvn clean package

firefox http://localhost:8080/mineSQL/


TODO:
----
- Include REST API DOCS 
    https://github.com/swagger-api/swagger-core/wiki/Servlet-Quickstart
    <dependency>
      <groupId>com.wordnik</groupId>
      <artifactId>swagger-servlet_2.10</artifactId>
      <version>1.3.10</version>
    </dependency>

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