/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import junit.framework.TestCase;
import org.apache.jackrabbit.core.TransientRepository;

/**
 *
 * @author alessio.finamore
 */
public class TestJackrabbit extends TestCase {

    public TestJackrabbit(String testName) {
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
    public void testHello() {
        try {
            Repository repository = new TransientRepository();
            SimpleCredentials sc =   new SimpleCredentials("username", "password".toCharArray());
            Session session = repository.login(sc);
            try {
                Node root = session.getRootNode();

// Store content
                Node hello = root.addNode("hello");
                Node world = hello.addNode("world");
                world.setProperty("message", "Hello, World!");
                session.save();

// Retrieve content
                Node node = root.getNode("hello/world");
                System.out.println(node.getPath());
                System.out.println(node.getProperty("message").getString());

// Remove content
                root.getNode("hello").remove();
                session.save();
            } finally {
                session.logout();
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(TestJackrabbit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
