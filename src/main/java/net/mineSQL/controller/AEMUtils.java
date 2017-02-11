/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.controller;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.commons.JcrUtils;

import java.security.*; 
import javax.jcr.security.*; 
import org.apache.jackrabbit.api.security.*; 
import org.apache.jackrabbit.api.security.principal.*; 
import org.apache.jackrabbit.commons.jackrabbit.authorization.*; 
import org.apache.jackrabbit.core.security.principal.EveryonePrincipal;


/**
 *
 * @author ax.finamore
 */
public class AEMUtils {
    public Session getAdminSession(String url) throws RepositoryException{

		System.out.println("Try Connecting to " + url);
		Repository repository = JcrUtils.getRepository(url);

		SimpleCredentials creds = new SimpleCredentials(
			"admin",
			"admin".toCharArray());

		System.out.println("Ok Connecting to " + url);
		Session session = repository.login(creds, "crx.default");
		System.out.println("Login successful, workspace: " + session.getWorkspace());

        return session;
    }



	public void addPrivileges(String path) throws RepositoryException {
		
		String url = "http://localhost:4502/crx/server";
        Session session = getAdminSession(url);

		Privilege[] privileges;
		privileges = AccessControlUtils.privilegesFromNames(
			session,
			new String[]{Privilege.JCR_ALL}
		);
		boolean isAllowRule = true; // set to false for deny rule
// get the access control list
		AccessControlList acl = AccessControlUtils.getAccessControlList(session, path);

		boolean success  = AccessControlUtils.addAccessControlEntry(session, path, EveryonePrincipal.getInstance(), privileges, isAllowRule);
		if ( success ) {
			//failure
			System.out.println("net.mineSQL.controller.AEMUtils.main()  fail ");
		} else {
			//success
			System.out.println("net.mineSQL.controller.AEMUtils.main()  succes ");
		}
	}
	
}
