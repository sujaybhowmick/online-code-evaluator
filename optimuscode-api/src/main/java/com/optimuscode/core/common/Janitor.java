package com.optimuscode.core.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Inspired by Groovy's compiler API classes
 */
public class Janitor implements HasCleanup{
   private final Set<HasCleanup> pending = new HashSet<HasCleanup>();

   public void register(HasCleanup o){
       pending.add(o);
   }

   @Override
   public void cleanup() {
       Iterator iterator = pending.iterator();
       while( iterator.hasNext()){
           HasCleanup object = (HasCleanup)iterator.next();

           try {
               object.cleanup();
           }catch(Exception e){
           }
       }

       pending.clear();
   }
}
