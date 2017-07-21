/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.embedded.impl;

import java.util.Collections;
import java.util.Iterator;

import javax.enterprise.inject.Instance;

import org.jboss.forge.furnace.exception.ContainerException;
import org.jboss.forge.furnace.services.Imported;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class EmbeddedImported<T> implements Imported<T>
{
   private final Instance<T> instance;

   public EmbeddedImported(Instance<T> instance)
   {
      this.instance = instance;
   }

   @Override
   public Iterator<T> iterator()
   {
      return instance.iterator();
   }

   @Override
   public T get() throws ContainerException, IllegalStateException
   {
      return instance.get();
   }

   @Override
   public void release(T obj)
   {
      instance.destroy(obj);
   }

   @Override
   public T selectExact(Class<T> type)
   {
      return instance.select(type).get();
   }

   @Override
   public boolean isUnsatisfied()
   {
      return instance.isUnsatisfied();
   }

   @Override
   public boolean isAmbiguous()
   {
      return instance.isAmbiguous();
   }

   public static <T> Imported<T> empty()
   {
      return new Imported<T>()
      {

         @Override
         public Iterator<T> iterator()
         {
            return Collections.emptyIterator();
         }

         @Override
         public T get() throws ContainerException, IllegalStateException
         {
            return null;
         }

         @Override
         public void release(T instance)
         {
         }

         @Override
         public T selectExact(Class<T> type)
         {
            return null;
         }

         @Override
         public boolean isUnsatisfied()
         {
            return true;
         }

         @Override
         public boolean isAmbiguous()
         {
            return false;
         }
      };
   }
}
