/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.container.simple.lifecycle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class SimpleSingletonServiceBean<T> implements Bean<T>, PassivationCapable
{
   private final Class<T> type;

   public SimpleSingletonServiceBean(Class<T> type)
   {
      this.type = type;
   }

   @Override
   public String getId()
   {
      return type.getName();
   }

   @Override
   public T create(CreationalContext<T> creationalContext)
   {
      try
      {
         return type.newInstance();
      }
      catch (InstantiationException | IllegalAccessException e)
      {
         throw new RuntimeException("Error while creating " + type, e);
      }
   }

   @Override
   public void destroy(T instance, CreationalContext<T> creationalContext)
   {
   }

   @Override
   public Set<Type> getTypes()
   {
      Set<Type> types = new HashSet<>();
      types.add(type);

      Class<?> superType = type;
      types.addAll(Arrays.asList(superType.getInterfaces()));
      while ((superType = superType.getSuperclass()) != null)
      {
         types.add(superType);
         types.addAll(Arrays.asList(superType.getInterfaces()));
      }
      return types;
   }

   @Override
   public Set<Annotation> getQualifiers()
   {
      return Collections.singleton(new Default()
      {
         @Override
         public Class<? extends Annotation> annotationType()
         {
            return Default.class;
         }
      });
   }

   @Override
   public Class<? extends Annotation> getScope()
   {
      return ApplicationScoped.class;
   }

   @Override
   public String getName()
   {
      return type.getName();
   }

   @Override
   public Set<Class<? extends Annotation>> getStereotypes()
   {
      return Collections.emptySet();
   }

   @Override
   public boolean isAlternative()
   {
      return false;
   }

   @Override
   public Class<?> getBeanClass()
   {
      return type;
   }

   @Override
   public Set<InjectionPoint> getInjectionPoints()
   {
      return Collections.emptySet();
   }

   @Override
   public boolean isNullable()
   {
      return false;
   }
}