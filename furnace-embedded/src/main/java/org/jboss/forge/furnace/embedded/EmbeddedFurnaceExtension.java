/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.jboss.forge.furnace.container.simple.Service;
import org.jboss.forge.furnace.container.simple.SingletonService;
import org.jboss.forge.furnace.container.simple.lifecycle.SimpleServiceBean;
import org.jboss.forge.furnace.container.simple.lifecycle.SimpleSingletonServiceBean;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class EmbeddedFurnaceExtension implements Extension
{
   private static final Logger log = Logger.getLogger(EmbeddedFurnaceExtension.class.getName());

   private String threadName;

   /**
    * @see org.jboss.forge.addon.ui.impl.extension.AnnotatedCommandExtension.observeAnnotationMethods(ProcessAnnotatedType<T>,
    *      BeanManager)
    */
   public void changeThreadName(@Observes BeforeBeanDiscovery event)
   {
      this.threadName = Thread.currentThread().getName();
      Thread.currentThread().setName("org.jboss.forge.furnace.container:embedded,1.0.0.Final");
   }

   public void revertThreadNameChange(@Observes AfterBeanDiscovery event)
   {
      Thread.currentThread().setName(threadName);
   }

   public void registerSimpleServices(@Observes AfterBeanDiscovery event, BeanManager beanManager)
   {
      try
      {
         Enumeration<URL> resources = getClass().getClassLoader()
                  .getResources("/META-INF/services/" + Service.class.getName());
         while (resources.hasMoreElements())
         {
            URL serviceUrl = resources.nextElement();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(serviceUrl.openStream())))
            {
               String line;
               while ((line = br.readLine()) != null)
               {
                  log.fine("Registering service: " + line);
                  try
                  {
                     Class<?> type = Class.forName(line);
                     event.addBean(new SimpleServiceBean<>(type));
                  }
                  catch (NoClassDefFoundError ncde)
                  {
                     // ignore
                  }
               }
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public void registerSimpleSingletonServices(@Observes AfterBeanDiscovery event, BeanManager beanManager)
   {
      try
      {
         Enumeration<URL> resources = getClass().getClassLoader()
                  .getResources("/META-INF/services/" + SingletonService.class.getName());
         while (resources.hasMoreElements())
         {
            URL serviceUrl = resources.nextElement();
            try (BufferedReader br = new BufferedReader(
                     new InputStreamReader(serviceUrl.openStream())))
            {
               String line;
               while ((line = br.readLine()) != null)
               {
                  log.fine("Registering singleton service: " + line);
                  try
                  {
                     Class<?> type = Class.forName(line);
                     event.addBean(new SimpleSingletonServiceBean<>(type));
                  }
                  catch (NoClassDefFoundError ncde)
                  {
                     // ignore
                  }
               }
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
