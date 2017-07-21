/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.forge.furnace.event.EventException;
import org.jboss.forge.furnace.event.EventManager;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ApplicationScoped
public class EmbeddedEventManager implements EventManager
{
   @Inject
   private BeanManager beanManager;

   @Override
   public void fireEvent(Object event, Annotation... qualifiers) throws EventException
   {
      beanManager.fireEvent(event, qualifiers);
   }
}
