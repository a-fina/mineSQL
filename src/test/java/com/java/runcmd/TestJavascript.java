package com.java.runcmd;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.script.*;

/**
 *
 * @author alessio.finamore
 */
public class TestJavascript {

    public TestJavascript() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testJavascript() throws ScriptException {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        // evaluate JavaScript code from String
        engine.eval("print('Hello JS')");
    }
    
    @Test
    public void testNashorn() throws ScriptException {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("nashorn");
        // evaluate JavaScript code from String
        engine.eval("print('Hello Nashorn')");

        engine.eval("function sum(a, b) { return a + b; }");
        System.out.println(engine.eval("sum(1, 2);"));

        Object json = engine.eval("JSON");
        System.out.println(json); 
    }

    @Test
    public void parseJSON() throws ScriptException, NoSuchMethodException{
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        
        // Si può fare anche direttamente in Java, fa niente
        String contactJson = "{" +
        "\"contact\": {" +
        "\"name\": \"Mr A\", \"emails\": [" +
        "\"contact@some.tld\", \"sales@some.tld\"" +
        "]}}";
        Invocable invocable = (Invocable) engine;
        
        Object json = engine.eval("JSON");
        Object data = invocable.invokeMethod(json, "parse", contactJson);

        System.out.println(data.getClass());  
    }
}
