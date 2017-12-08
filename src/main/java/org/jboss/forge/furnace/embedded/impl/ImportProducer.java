/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.forge.furnace.services.Imported;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class ImportProducer
{
   @Produces
   @ApplicationScoped
   @SuppressWarnings("unchecked")
   public <T> Imported<T> produceImported(InjectionPoint injectionPoint, EmbeddedAddonRegistry registry)
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
         return registry.getServices(importedType);
      }
      else
      {
         throw new IllegalStateException("Cannot inject a generic instance of type " + Imported.class.getName()
                  + "<?> without specifying concrete generic types at injection point " + injectionPoint + ".");
      }
   }
}
