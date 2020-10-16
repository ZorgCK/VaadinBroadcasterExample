
package com.company.broadcastexample.core;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.vaadin.flow.shared.Registration;


public class Broadcaster
{
	static Executor executor = Executors.newSingleThreadExecutor();
	
	static LinkedList<Consumer<String>> listeners = new LinkedList<>();
	
	public static synchronized Registration register(
		final Consumer<String> listener)
	{
		Broadcaster.listeners.add(listener);
		
		return () -> {
			synchronized(Broadcaster.class)
			{
				Broadcaster.listeners.remove(listener);
			}
		};
	}
	
	public static synchronized void broadcast(final String notification)
	{
		for(final Consumer<String> listener : Broadcaster.listeners)
		{
			Broadcaster.executor.execute(() -> listener.accept(notification));
		}
	}
}
