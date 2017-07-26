/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.forge.furnace.ContainerStatus;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.AddonCompatibilityStrategy;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.lock.LockManager;
import org.jboss.forge.furnace.lock.LockMode;
import org.jboss.forge.furnace.repositories.AddonRepository;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.spi.ContainerLifecycleListener;
import org.jboss.forge.furnace.spi.ListenerRegistration;
import org.jboss.forge.furnace.util.AddonCompatibilityStrategies;
import org.jboss.forge.furnace.versions.Version;
import org.jboss.forge.furnace.versions.Versions;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ApplicationScoped
public class EmbeddedFurnace implements Furnace
{

   @Inject
   private EmbeddedAddonRegistry addonRegistry;

   @Override
   public Future<Furnace> startAsync()
   {
      return CompletableFuture.<Furnace> completedFuture(this);
   }

   @Override
   public Future<Furnace> startAsync(ClassLoader loader)
   {
      return CompletableFuture.<Furnace> completedFuture(this);
   }

   @Override
   public void start()
   {
   }

   @Override
   public void start(ClassLoader loader)
   {
   }

   @Override
   public Furnace stop()
   {
      return this;
   }

   @Override
   public boolean isServerMode()
   {
      return false;
   }

   @Override
   public Furnace setServerMode(boolean server)
   {
      return this;
   }

   @Override
   public AddonRegistry getAddonRegistry(AddonRepository... repositories)
   {
      return addonRegistry;
   }

   @Override
   public List<AddonRepository> getRepositories()
   {
      return Collections.emptyList();
   }

   @Override
   public AddonRepository addRepository(AddonRepositoryMode mode, File repository)
   {
      return null;
   }

   @Override
   public AddonRepository addRepository(AddonRepository repository)
   {
      return repository;
   }

   @Override
   public Version getVersion()
   {
      return Versions.getImplementationVersionFor(getClass());
   }

   @Override
   public ListenerRegistration<ContainerLifecycleListener> addContainerLifecycleListener(
            ContainerLifecycleListener listener)
   {
      return null;
   }

   @Override
   public ClassLoader getRuntimeClassLoader()
   {
      return getClass().getClassLoader();
   }

   @Override
   public LockManager getLockManager()
   {
      return new LockManager()
      {
         @Override
         public <T> T performLocked(LockMode mode, Callable<T> task)
         {
            try
            {
               return task.call();
            }
            catch (Exception e)
            {
               throw new RuntimeException(e);
            }
         }
      };
   }

   @Override
   public ContainerStatus getStatus()
   {
      return ContainerStatus.STARTED;
   }

   @Override
   public void setArgs(String[] args)
   {
   }

   @Override
   public String[] getArgs()
   {
      return new String[0];
   }

   @Override
   public boolean isTestMode()
   {
      return false;
   }

   @Override
   public void setAddonCompatibilityStrategy(AddonCompatibilityStrategy policy)
   {
   }

   @Override
   public AddonCompatibilityStrategy getAddonCompatibilityStrategy()
   {
      return AddonCompatibilityStrategies.LENIENT;
   }

}
