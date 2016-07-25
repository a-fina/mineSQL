/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.test;

/**
 *
 * @author ax.finamore
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.osgi.framework.Constants;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.cache.BundleCache;
import org.apache.felix.framework.util.StringMap;
// import org.apache.felix.framework.main.AutoActivator;

public class FelixMain 
{
    private static Felix m_felix = null;

    public static void main(String[] argv) throws Exception
    {
        // Print welcome banner.
        System.out.println("\nWelcome to Felix.");
        System.out.println("=================\n");
    /************
        Map configMap = new StringMap(false);
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
            "org.osgi.framework; version=1.3.0," +
            "org.osgi.service.packageadmin; version=1.2.0," +
            "org.osgi.service.startlevel; version=1.0.0," +
            "org.osgi.service.url; version=1.0.0");
        configMap.put(AutoActivator.AUTO_START_PROP + ".1",
            "file:bundle/org.apache.felix.shell-1.0.0.jar " +
            "file:bundle/org.apache.felix.shell.tui-1.0.0.jar");
        configMap.put(BundleCache.\CACHE_PROFILE_DIR_PROP, "cache");

        try
        {
            List list = new ArrayList();
            list.add(new AutoActivator(configProps));
            configMap = new StringMap(configProps, false);
            m_felix = new Felix(configMap, list);
            m_felix.start();
        }
        catch (Exception ex)
        {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
            System.exit(-1);
        }
        ***************/
    }
}

