/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.test.felix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 *
 * @author ax.finamore
 */

public class HostApplication
{
    private HostActivator m_activator = null;
    private Felix m_felix = null;
 
    public HostApplication()
    {
        // Create a configuration property map.
        Map config = new HashMap();
        // Create host activator;
        m_activator = new HostActivator();
        List list = new ArrayList();
        list.add(m_activator);
        config.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);
 
        try
        {
            // Now create an instance of the framework with
            // our configuration properties.
            m_felix = new Felix(config);
            // Now start Felix instance.
            m_felix.start();
        }
        catch (Exception ex)
        {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
        }
    }
 
    public Bundle[] getInstalledBundles()
    {
        // Use the system bundle activator to gain external
        // access to the set of installed bundles.
        return m_activator.getBundles();
    }
 
    public void shutdownApplication() throws BundleException, InterruptedException
    {
        // Shut down the felix framework when stopping the
        // host application.
        m_felix.stop();
        m_felix.waitForStop(0);
    }

	public static void main(String[] args) throws Exception {
        HostApplication happ = new HostApplication();
        
    }
    
}