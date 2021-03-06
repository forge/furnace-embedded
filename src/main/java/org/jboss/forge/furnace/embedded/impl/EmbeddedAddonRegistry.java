/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
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

   private final Set<Class<?>> serviceTypes = Collections.newSetFromMap(new ConcurrentHashMap<>());

   public void registerServiceTypes(Set<Class<?>> types)
   {
      serviceTypes.addAll(types);
   }

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
      throw new UnsupportedOperationException("getAddon is not supported in Embedded containers");
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
      return Collections.unmodifiableSet(serviceTypes);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> Set<Class<T>> getExportedTypes(Class<T> type)
   {
      Set<Class<T>> result = new HashSet<>();
      for (Class<?> serviceType : serviceTypes)
      {
         if (type.isAssignableFrom(serviceType))
            result.add((Class<T>) serviceType);
      }
      return result;
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
}
