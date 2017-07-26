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

import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.forge.furnace.repositories.AddonRepository;
import org.jboss.forge.furnace.versions.Version;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ApplicationScoped
public class EmbeddedAddonRepository implements AddonRepository
{
   @Override
   public File getAddonBaseDir(AddonId addon)
   {
      return null;
   }

   @Override
   public Set<AddonDependencyEntry> getAddonDependencies(AddonId addon)
   {
      return Collections.emptySet();
   }

   @Override
   public File getAddonDescriptor(AddonId addon)
   {
      return null;
   }

   @Override
   public List<File> getAddonResources(AddonId addon)
   {
      return Collections.emptyList();
   }

   @Override
   public File getRootDirectory()
   {
      return null;
   }

   @Override
   public boolean isDeployed(AddonId addon)
   {
      return true;
   }

   @Override
   public boolean isEnabled(AddonId addon)
   {
      return true;
   }

   @Override
   public List<AddonId> listAll()
   {
      return Collections.emptyList();
   }

   @Override
   public List<AddonId> listEnabled()
   {
      return Collections.emptyList();
   }

   @Override
   public List<AddonId> listEnabledCompatibleWithVersion(Version version)
   {
      return Collections.emptyList();
   }

   @Override
   public Date getLastModified()
   {
      return new Date();
   }

   @Override
   public int getVersion()
   {
      return 0;
   }

}
