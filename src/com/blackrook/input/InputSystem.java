/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.Component;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Rumbler;

import com.blackrook.commons.Common;
import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.hash.HashedQueueMap;
import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.list.List;

/**
 * The primary input system and controller factory.
 * @author Matthew Tropiano
 */
public class InputSystem
{
	/** List of tracked devices. */
	private HashedQueueMap<Controller, InputController> controllerMap;
	/** List of devices hashed by type. */
	private HashedQueueMap<Controller.Type, Controller> controllerTypeMap;
	/** List of rumblers (for resetting). */
	private List<Rumbler> rumblerList;
	/** List of input system listeners. */
	private List<InputSystemListener> listeners;
	
	/**
	 * Creates the new input system.
	 */
	public InputSystem()
	{
		// suppress most logging for ControllerEnvironment.
		Logger.getLogger("net.java.games.input.ControllerEnvironment").setLevel(Level.SEVERE);
		
		controllerMap = new HashedQueueMap<Controller, InputController>();
		controllerTypeMap = new HashedQueueMap<Controller.Type, Controller>();
		rumblerList = new List<Rumbler>();
		listeners = new List<InputSystemListener>(2);
		
		refreshControllers();
	}

	/**
	 * Refreshes the list of controllers that this system knows about.
	 * This will also clear the list of controllers to poll.
	 */
	public void refreshControllers()
	{
		controllerTypeMap.clear();
		stopAllRumblers();
		rumblerList.clear();
		ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
		recurseControllers(env.getControllers());
	}
	
	// Recursively adds controllers.
	private void recurseControllers(Controller ... controllers)
	{
		for (Controller c : controllers)
		{
			controllerTypeMap.enqueue(c.getType(), c);
			for (Rumbler r : c.getRumblers())
				rumblerList.add(r);
		}
	}
	
	/**
	 * Adds an {@link InputSystemListener} to this InputSystem.
	 * @param listener the listener to add.
	 */
	public void addListener(InputSystemListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Removes an {@link InputSystemListener} from this InputSystem.
	 * @param listener the listener to remove.
	 * @return true if removed, false if not.
	 */
	public boolean removeListener(InputSystemListener listener)
	{
		return listeners.remove(listener);
	}
	
	/**
	 * Adds a controller to this system to poll and put the results in
	 * an object that holds polled information.
	 * @param controller the controller to use.
	 * @param monitor the object to dump polled info into.
	 */
	public void addController(Controller controller, Object monitor)
	{
		controllerMap.enqueue(controller, new InputController(controller, monitor));
	}

	/**
	 * Polls all monitored devices, setting all annotated fields.
	 */
	public void pollAll()
	{
		for (InputSystemListener listener : listeners)
			listener.beforePoll();

		boolean change = false;
		for (ObjectPair<Controller, Queue<InputController>> pair : controllerMap)
		{
			Controller controller = pair.getKey();
			controller.poll();
			for (InputController ic : pair.getValue())
				change = change || ic.poll(controller);
		}

		for (InputSystemListener listener : listeners)
			listener.afterPoll(change);
	}

	/**
	 * If any devices are rumbling, turn them off.
	 */
	public void stopAllRumblers()
	{
		for (Rumbler r : rumblerList)
			r.rumble(0f);
	}

	/**
	 * Finds the first keyboard.
	 */
	public Controller findKeyboard()
	{
		return findController(Type.KEYBOARD, null, 100);
	}
	
	/**
	 * Finds the first mouse.
	 */
	public Controller findMouse()
	{
		return findController(Type.MOUSE, null, 0);
	}

	/**
	 * Finds the first gamepad.
	 */
	public Controller findGamepad()
	{
		return findController(Type.GAMEPAD, null, 0);
	}
	
	/**
	 * Finds all gamepads.
	 */
	public Controller[] findGamepads()
	{
		return findControllers(Type.GAMEPAD, null, 0);
	}
	
	/**
	 * Finds the first XBox 360 controller.
	 */
	public Controller findXBoxGamepad()
	{
		return findController(Type.GAMEPAD, "XBox", 16);
	}
	
	/**
	 * Finds all XBox 360 controllers.
	 */
	public Controller[] findXBoxGamepads()
	{
		return findControllers(Type.GAMEPAD, "XBox", 16);
	}
	
	/**
	 * Searches for and finds a controller that may or may not have specific criteria.
	 * @param type the controller type. Leave null for all types.
	 * @param nameContains the name to search for, if any. If null, all names accepted.
	 * @param componentNumber the minimum number of components to search for, or 0 for any number.
	 * @return a controller that fulfills all of the requirements, or null if not found.
	 */
	public Controller findController(Controller.Type type, String nameContains, int componentNumber)
	{
		if (type != null) 
		{
			Queue<Controller> q = controllerTypeMap.get(type);
			if (q != null) for (Controller c : q)
			{
				if (findCheck(c, nameContains, componentNumber))
					return c;
			}
		}
		else for (ObjectPair<Controller.Type, Queue<Controller>> pair : controllerTypeMap)
		{
			for (Controller c : pair.getValue())
			{
				if (findCheck(c, nameContains, componentNumber))
					return c;
			}
		}
		
		return null;
	}

	/**
	 * Searches for and finds controllers that may or may not have specific criteria.
	 * @param type the controller type. Leave null for all types.
	 * @param nameContains the name to search for, if any. If null, all names accepted.
	 * @param componentNumber the minimum number of components to search for, or 0 for any number.
	 * @return a list of controllers that fulfill all of the requirements.
	 */
	public Controller[] findControllers(Controller.Type type, String nameContains, int componentNumber)
	{
		List<Controller> outList = new List<Controller>();
		if (type != null) 
		{
			Queue<Controller> q = controllerTypeMap.get(type);
			if (q != null)
				findQueue(outList, q, nameContains, componentNumber);
		}
		else for (ObjectPair<Controller.Type, Queue<Controller>> pair : controllerTypeMap)
		{
			findQueue(outList, pair.getValue(), nameContains, componentNumber);
		}
		
		Controller[] out = new Controller[outList.size()];
		outList.toArray(out);
		return out;
	}
	
	// Find relevant controllers in a queue.
	private void findQueue(List<Controller> out, Queue<Controller> queue, String nameContains, int componentNumber)
	{
		for (Controller c : queue)
		{
			if (findCheck(c, nameContains, componentNumber))
				out.add(c);
		}
	}
	
	// Check controller by criteria.
	private boolean findCheck(Controller c, String nameContains, int componentNumber)
	{
		return (nameContains == null || c.getName().toLowerCase().contains(nameContains.toLowerCase()))
			&& (componentNumber == 0 || c.getComponents().length >= componentNumber);
	}
	
	/**
	 * Gets an info string for a controller.
	 * @param controller the controller to inspect.
	 */
	public String getControllerInfo(Controller controller)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		recursePrintControllers(pw, 0, controller);
		Common.close(pw);
		return sw.toString();
	}
	
	/**
	 * Checks if a controller is an XBox360 Gamepad.
	 * @return true if so, false if not.
	 */
	public static boolean isXBoxController(Controller controller)
	{
		if (controller.getType() != Type.GAMEPAD)
			return false;
		else if (!controller.getName().toLowerCase().contains("xbox"))
			return false;
		else if (controller.getComponents().length != 16)
			return false;
		
		return true;
	}

	private static void recursePrintControllers(PrintWriter out, int tabs, Controller ... controllers)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tabs; i++)
			sb.append('\t');
		String tabString = sb.toString();
		
		for (Controller c : controllers)
		{
			printController(out, tabString, c);
			if (c.getControllers().length > 0)
				recursePrintControllers(out, tabs + 1, c.getControllers());
		}
	}

	private static void printController(PrintWriter out, String prefix, Controller c)
	{
		out.printf("%s%s \"%s\", ID: %d (Port Type: %s)\n", prefix, 
				c.getType(), c.getName(), c.getPortNumber(), c.getPortType());
		
		if (c.getComponents().length > 0)
		{
			out.printf("%s\tComponents (%d):\n", prefix, c.getComponents().length);
			for (Component comp : c.getComponents())
				out.printf("%s\t\t%s (\"%s\"): %s%sDZ: %f, P: %f\n", prefix, 
						comp.getIdentifier(), comp.getName(),
						comp.isAnalog() ? "ANALOG " : "",
						comp.isRelative() ? "RELATIVE " : "",
						comp.getDeadZone(), comp.getPollData());
		}
		
		if (c.getRumblers().length > 0)
		{
			out.printf("%s\tRumblers (%d):\n", prefix, c.getRumblers().length);
			for (Rumbler r : c.getRumblers())
				out.printf("%s\t\t%s (\"%s\")\n", prefix, 
						r.getAxisIdentifier(), r.getAxisName());
		}
	}

	
}