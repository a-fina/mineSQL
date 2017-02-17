package com.aem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.minesql.orm.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import junit.framework.TestCase;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.core.TransientRepository;
import org.junit.Test;

/**
 *
 * @author alessio.finamore
 */
public class TestLiveCopy extends TestCase {

    public TestLiveCopy(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    @Test
    public void testHello() {
        try {

            String url = "http://localhost:4502/crx/server";
            System.out.println("Try Connecting to " + url);
            Repository repository = JcrUtils.getRepository(url);
            SimpleCredentials creds = new SimpleCredentials("admin",
                    "admin".toCharArray());
            System.out.println("Ok Connecting to " + url);
            Session session = repository.login(creds, "crx.default");

            //Create a node that represents the root node
            Node root = session.getRootNode(); 
            // Retrieve content 
            Node node = root.getNode("content/hmonline/it_it");
            System.out.println(node.getPath());


            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(
                   "{QUERY}",
                    Query.JCR_SQL2);
            QueryResult result = query.execute();

            //Iterate over the nodes in the results ...
            NodeIterator nodeIter = result.getNodes();
             
            while ( nodeIter.hasNext() ) {
                Node n;
                n = nodeIter.nextNode();
                System.out.println(n.getPath());
                
                n.getProperties();
                for (Iterator p = n.getProperties(); p.hasNext();) {
                    Property prop = (Property)p.next();
                    if ( prop.isMultiple() )
                        System.out.println("    " + prop.getName() + " -> " + prop.getValues().toString());
                    else
                        System.out.println("    " + prop.getName() + " -> " + prop.getValue().getString());
                }
 
            }

            session.logout();

        } catch (RepositoryException ex) {
            Logger.getLogger(TestLiveCopy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
