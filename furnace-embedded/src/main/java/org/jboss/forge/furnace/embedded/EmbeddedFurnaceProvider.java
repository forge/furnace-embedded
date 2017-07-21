/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded;

import javax.enterprise.inject.spi.CDI;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.embedded.impl.EmbeddedFurnace;
import org.jboss.forge.furnace.spi.FurnaceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class EmbeddedFurnaceProvider implements FurnaceProvider
{
   @Override
   public Furnace getFurnace(ClassLoader loader)
   {
      return CDI.current().select(EmbeddedFurnace.class).get();
   }
}
