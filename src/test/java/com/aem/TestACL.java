package com.aem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.jcr.RepositoryException;
import junit.framework.TestCase;
import net.mineSQL.controller.AEMUtils;

/**
 *
 * @author alessio.finamore
 */
public class TestACL extends TestCase {

	public TestACL(String testName) {
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
	//@ Test
	public void testHello() throws RepositoryException {
		AEMUtils aeu = new AEMUtils();

		String path = "/content";
		aeu.addPrivileges(path);

	}

}
