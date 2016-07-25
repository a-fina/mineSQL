/*
	SessionWatcher.java
*/
package net.mineSQL.util;

import java.util.HashMap;

import net.mineSQL.util.ApplicationWatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

import org.apache.log4j.Logger;
import org.apache.commons.lang.time.DateFormatUtils;


public class SessionWatcher implements HttpSessionListener {

    private static final Logger log = Logger.getLogger(SessionWatcher.class);

	private static int activeSessions = 0;
	private static HashMap sessions = null;

	public void sessionCreated(HttpSessionEvent se) {
        getSessions();

        String id = se.getSession().getId();
        String creation = DateFormatUtils.format(
                se.getSession().getCreationTime(), ApplicationWatcher.dateformat);

        //log.info(" -----------> sessionCreated: " + id +" date: "+creation);
        addSession(id, "creationTime", creation);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		String id = se.getSession().getId();
        if ( sessions.containsKey(id) )
		    sessions.remove(id);
		
		if(activeSessions > 0)
			activeSessions--;
        //log.info(" -----------> removeSession: " + id );
	}
	public static HashMap getSessions() {
		//log.debug("initializing getSessions() ");
		if (sessions == null)
			sessions = new HashMap();
		
        return sessions;
	}
	public static void addSession(String id, String key, String value){
        HashMap sessionDetails = null;

        getSessions();

        //log.info(" -----------> id: " + id );
        //7log.info(" -----------> sessions: " + sessions );
        if (sessions.get(id) != null){
            sessionDetails = (HashMap)sessions.get(id);
		 //   log.info(" -----------> addSession: " + id +","+key+","+value);
        }else{
            sessionDetails = new HashMap();
		    activeSessions++;
        }

		sessionDetails.put(key,value);
		sessions.put(id, sessionDetails.clone());
	}
	
	public static HashMap getSession(String id) {
		return (HashMap)sessions.get(id);
	}
	public static int getActiveSessions() {
		return activeSessions;
	}
}
