package net.mineSQL.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class MineScript {

    private static final Logger log = Logger.getLogger(MineScript.class);

    static String paramPrefix = "$";

    public MineScript() {
    }

    /*
    * Sovrascrive il prefisso delle variabili
    */
    public MineScript(String prefix) {
    }

    public String mergeScriptParameters(HashMap params, String statement) {
        String newQuery = statement;

        try {


        Iterator it = params.keySet().iterator();
        Object key = null;
        while (it.hasNext()) {
            key = it.next();
            newQuery = newQuery.replaceAll("\\"+paramPrefix + key, (String) params.get(key));
        }

        } catch (Exception e) {
            log.error(e);
        }

        return newQuery;
    }

}
