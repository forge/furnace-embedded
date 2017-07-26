/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonDependency;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.addons.AddonStatus;
import org.jboss.forge.furnace.event.EventManager;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.furnace.repositories.AddonRepository;
import org.jboss.forge.furnace.spi.ServiceRegistry;
import org.jboss.forge.furnace.versions.Version;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class EmbeddedAddon implements Addon
{
   @Override
   public AddonId getId()
   {
      return AddonId.from("org.jboss.forge.furnace.container:embedded", "1.0.0.Final");
   }

   @Override
   public ClassLoader getClassLoader()
   {
      return getClass().getClassLoader();
   }

   @Override
   public EventManager getEventManager()
   {
      return null;
   }

   @Override
   public ServiceRegistry getServiceRegistry()
   {
      return null;
   }

   @Override
   public AddonRepository getRepository()
   {
      return new AddonRepository()
      {

         @Override
         public List<AddonId> listEnabledCompatibleWithVersion(Version version)
         {
            return null;
         }

         @Override
         public List<AddonId> listEnabled()
         {
            return null;
         }

         @Override
         public List<AddonId> listAll()
         {
            return null;
         }

         @Override
         public boolean isEnabled(AddonId addon)
         {
            return false;
         }

         @Override
         public boolean isDeployed(AddonId addon)
         {
            return false;
         }

         @Override
         public int getVersion()
         {
            return 0;
         }

         @Override
         public File getRootDirectory()
         {
            return null;
         }

         @Override
         public Date getLastModified()
         {
            return null;
         }

         @Override
         public List<File> getAddonResources(AddonId addon)
         {
            return null;
         }

         @Override
         public File getAddonDescriptor(AddonId addon)
         {
            return null;
         }

         @Override
         public Set<AddonDependencyEntry> getAddonDependencies(AddonId addon)
         {
            return null;
         }

         @Override
         public File getAddonBaseDir(AddonId addon)
         {
            return null;
         }
      };
   }

   @Override
   public AddonStatus getStatus()
   {
      return AddonStatus.STARTED;
   }

   @Override
   public Set<AddonDependency> getDependencies()
   {
      return Collections.emptySet();
   }

   @Override
   public Future<Void> getFuture()
   {
      return CompletableFuture.<Void> completedFuture(null);
   }

}
