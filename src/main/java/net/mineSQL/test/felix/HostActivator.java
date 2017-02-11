/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.test.felix;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author ax.finamore
 */

public class HostActivator implements BundleActivator
{
    private BundleContext m_context = null;
 
    @Override
    public void start(BundleContext context)
    {
        m_context = context;
    }
 
    @Override
    public void stop(BundleContext context)
    {
        m_context = null;
    }
 
    public Bundle[] getBundles()
    {
        if (m_context != null)
        {
            return m_context.getBundles();
        }
        return null;
    }

}
