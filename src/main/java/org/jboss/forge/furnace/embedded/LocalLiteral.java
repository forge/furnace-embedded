/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.furnace.embedded;

import java.lang.annotation.Annotation;

import org.jboss.forge.furnace.container.cdi.events.Local;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@SuppressWarnings("all")
public class LocalLiteral implements Local
{
   @Override
   public Class<? extends Annotation> annotationType()
   {
      return Local.class;
   }
}
