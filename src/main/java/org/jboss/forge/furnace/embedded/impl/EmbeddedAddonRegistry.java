/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonFilter;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.repositories.AddonRepository;
import org.jboss.forge.furnace.services.Imported;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ApplicationScoped
public class EmbeddedAddonRegistry implements AddonRegistry
{
   @Inject
   private EmbeddedEventManager eventManager;

   @Inject
   Instance<Object> instances;

   @Override
   public String getName()
   {
      return "embedded-addon-registry";
   }

   @Override
   public void dispose()
   {
   }

   @Override
   public Addon getAddon(AddonId id)
   {
      return null;
   }

   @Override
   public Set<Addon> getAddons()
   {
      return Collections.emptySet();
   }

   @Override
   public Set<Addon> getAddons(AddonFilter filter)
   {
      return Collections.emptySet();
   }

   @Override
   public Set<AddonRepository> getRepositories()
   {
      return Collections.emptySet();
   }

   @Override
   public <T> Imported<T> getServices(Class<T> clazz)
   {
      Instance<T> instance = instances.select(clazz);
      return new EmbeddedImported<>(instance);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> Imported<T> getServices(String clazz)
   {
      Instance<T> instance;
      try
      {
         instance = (Instance<T>) instances.select(Class.forName(clazz));
         return new EmbeddedImported<>(instance);
      }
      catch (ClassNotFoundException e)
      {
         return EmbeddedImported.empty();
      }
   }

   @Override
   public Set<Class<?>> getExportedTypes()
   {
      return null;
   }

   @Override
   public <T> Set<Class<T>> getExportedTypes(Class<T> type)
   {
      return null;
   }

   @Override
   public long getVersion()
   {
      return 1L;
   }

   @Override
   public EventManager getEventManager()
   {
      return eventManager;
   }

   @Produces
   @SuppressWarnings("unchecked")
   public <T> Imported<T> produceImported(InjectionPoint injectionPoint)
   {
      Type type = injectionPoint.getAnnotated().getBaseType();

      if (type instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = (ParameterizedType) type;

         Type[] typeArguments = parameterizedType.getActualTypeArguments();
         Class<T> importedType = null;
         Type argument = typeArguments[0];
         if (argument instanceof Class)
         {
            importedType = (Class<T>) argument;
         }
         else if (argument instanceof ParameterizedType)
         {
            Type rawType = ((ParameterizedType) argument).getRawType();
            if (rawType instanceof Class)
               importedType = (Class<T>) rawType;
         }
         else
         {
            throw new IllegalStateException("Cannot inject a generic instance of type " + Imported.class.getName()
                     + "<?> without specifying concrete generic types at injection point " + injectionPoint + ".");
         }
         return getServices(importedType);
      }
      else
      {
         throw new IllegalStateException("Cannot inject a generic instance of type " + Imported.class.getName()
                  + "<?> without specifying concrete generic types at injection point " + injectionPoint + ".");
      }
   }

}
