package com.minesql.orm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

/**
 *
 * @author alessio.finamore
 */
public class TestSQLJet extends TestCase {

    public TestSQLJet(String testName) {
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
            File dbFile = new File("z:\\Fina\\db");
            dbFile.delete();
            
            SqlJetDb db = SqlJetDb.open(dbFile, true);
            db.getOptions().setAutovacuum(true);
            db.beginTransaction(SqlJetTransactionMode.WRITE);
            try {
                db.getOptions().setUserVersion(1);
            } finally {
                db.commit();
            }
        }   catch (SqlJetException ex) {
            Logger.getLogger(TestSQLJet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
