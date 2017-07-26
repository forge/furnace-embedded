/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.simple.lifecycle;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.container.simple.EventListener;
import org.jboss.forge.furnace.embedded.LocalLiteral;
import org.jboss.forge.furnace.embedded.impl.EmbeddedAddon;
import org.jboss.forge.furnace.event.PostStartup;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@Dependent
public class SimpleContainerInitializer
{
   private final Logger log = Logger.getLogger(getClass().getName());

   @Inject
   @Any
   private Instance<EventListener> eventListeners;

   /**
    * Initializes the SimpleContainer
    * 
    * @param postStartup
    * @param furnace
    */
   public void initialize(@Observes @Initialized(ApplicationScoped.class) Object init, BeanManager beanManager,
            Furnace furnace)
   {
      log.info("Initializing Simple Container...");
      PostStartup postStartup = new PostStartup(new EmbeddedAddon());
      SimpleContainer.start(postStartup.getAddon(), furnace);
      beanManager.fireEvent(postStartup);
      LocalLiteral localLiteral = new LocalLiteral();
      beanManager.fireEvent(postStartup, localLiteral);
      eventListeners.forEach(listener -> listener.handleEvent(postStartup, localLiteral));
      log.info("Simple Container succesfully initialized!");
   }
}
