/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessProducer;

import org.jboss.forge.furnace.container.simple.Service;
import org.jboss.forge.furnace.container.simple.SingletonService;
import org.jboss.forge.furnace.container.simple.lifecycle.SimpleServiceBean;
import org.jboss.forge.furnace.container.simple.lifecycle.SimpleSingletonServiceBean;
import org.jboss.forge.furnace.embedded.impl.EmbeddedAddonRegistry;
import org.jboss.forge.furnace.embedded.util.BeanManagerUtils;
import org.jboss.forge.furnace.embedded.util.Types;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class EmbeddedFurnaceExtension implements Extension
{
   private static final Logger log = Logger.getLogger(EmbeddedFurnaceExtension.class.getName());

   private String threadName;
   private final Set<Class<?>> services = new HashSet<>();

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

   public void registerSimpleServices(@Observes AfterBeanDiscovery event, BeanManager beanManager) throws Exception
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
                  log.log(Level.FINE, "Could not load class " + line + ". Ignoring", ncde);
               }
            }
         }
      }
   }

   public void registerSimpleSingletonServices(@Observes AfterBeanDiscovery event, BeanManager beanManager)
            throws Exception
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
                  log.log(Level.FINE, "Could not load class " + line + ". Ignoring", ncde);
               }
            }
         }
      }
   }

   // Needed for EmbeddedAddonRegistry.getExportedTypes
   public void processExportedService(@Observes ProcessAnnotatedType<?> event) throws InstantiationException,
            IllegalAccessException
   {
      Class<?> type = event.getAnnotatedType().getJavaClass();
      if (!(Modifier.isAbstract(type.getModifiers()) || Modifier.isInterface(type.getModifiers())))
      {
         services.add(event.getAnnotatedType().getJavaClass());
      }
   }

   // Needed for EmbeddedAddonRegistry.getExportedTypes
   public void processProducerHooks(@Observes ProcessProducer<?, ?> event, BeanManager manager)
   {
      Class<?> type = Types.toClass(event.getAnnotatedMember().getJavaMember());
      services.add(type);
   }

   public void registerServicesInEmbeddedAddonRegistry(@Observes AfterDeploymentValidation event, BeanManager manager)
   {
      EmbeddedAddonRegistry addonRegistry = BeanManagerUtils.getContextualInstance(manager,
               EmbeddedAddonRegistry.class);
      addonRegistry.registerServiceTypes(services);
      services.clear();
   }
}