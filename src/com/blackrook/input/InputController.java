/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Rumbler;

import com.blackrook.commons.Reflect;
import com.blackrook.commons.list.List;
import com.blackrook.input.annotation.ComponentBinding;
import com.blackrook.input.annotation.OnChange;
import com.blackrook.input.annotation.OnComponentToggle;
import com.blackrook.input.annotation.OnComponentValue;
import com.blackrook.input.annotation.RumbleFactor;
import com.blackrook.input.exception.InputInitializationException;

/**
 * An encapsulation for a controller object that is the feedback object
 * for controller polling.
 * <p>The fields of a controller are defined by what fields are annotated by {@link ComponentBinding}.
 * The annotated fields must be public. 
 * @author Matthew Tropiano
 */
public class InputController
{
	/** The object to set the fields on. */
	private Object object;
	/** List of field bindings. */
	private List<FieldGroup> componentFields;
	/** Rumbler Field. */
	private Field rumbleField;
	/** On Change Method. */
	private Method onChangeMethod;
	/** On Component toggle Method. */
	private Method onComponentToggleMethod;
	/** On Component Value Method. */
	private Method onComponentValueMethod;
	
	/**
	 * Creates a new controller.
	 * @param object the object to bind to.
	 * @param controller the controller that this polls.
	 * @throw {@link InputInitializationException} if the input object is set up improperly.
	 */
	InputController(Controller controller, Object object)
	{
		this.object = object;
		this.componentFields = new List<FieldGroup>();
		this.onChangeMethod = null;
		
		Class<?> clazz = object.getClass();
		for (Field f : clazz.getFields())
		{
			for (Annotation a : f.getAnnotations())
			{
				if (a instanceof ComponentBinding)
				{
					if (!Reflect.fieldCanContain(f, Float.class, Float.TYPE, Boolean.class, Boolean.TYPE, Integer.class, Integer.TYPE))
						throw new InputInitializationException("The annotation @ComponentBinding must be bound to a float, int, or boolean field.");
					else
						componentFields.add(new FieldGroup(((ComponentBinding)a).value(), f, f.getType()));
				}
				else if (a instanceof RumbleFactor)
				{
					if (!Reflect.fieldCanContain(f, Float.class, Float.TYPE))
						throw new InputInitializationException("The annotation @RumbleFactor must be bound to a float field.");
					else
						rumbleField = f;
				}
			}
		}
		
		for (Method m : clazz.getMethods())
		{
			for (Annotation a : m.getAnnotations())
			{
				if (a instanceof OnChange)
				{
					if (!Reflect.matchParameterTypes(m))
						throw new InputInitializationException("The annotation @OnChange is not bound to a method with no parameters.");
					else
						onChangeMethod = m;
				}
				else if (a instanceof OnComponentToggle)
				{
					if (!Reflect.matchParameterTypes(m, InputComponent.class, Boolean.TYPE))
						throw new InputInitializationException("The annotation @OnComponentToggle is not bound to a method with an InputComponent and Boolean parameter.");
					onComponentToggleMethod = m;
				}
				else if (a instanceof OnComponentValue)
				{
					if (!Reflect.matchParameterTypes(m, InputComponent.class, Float.TYPE))
						throw new InputInitializationException("The annotation @OnComponentValue is not bound to a method with an InputComponent and Float parameter.");
					onComponentValueMethod = m;
				}
			}
		}
		
	}
	
	/**
	 * Polls all components and sets the proper fields.
	 * @return true if any polled controllers changed a value, false otherwise.
	 */
	boolean poll(Controller controller)
	{			
		boolean change = false;
		for (FieldGroup op : componentFields)
		{
			Component component = controller.getComponent(op.component.id);
			if (component != null)
			{
				boolean s = op.set(object, component.getPollData(), component.isRelative());
				if (s && onComponentToggleMethod != null)
					Reflect.invokeBlind(onComponentToggleMethod, object, op.component, component.getPollData() != 0f);
				if (s && onComponentValueMethod != null)
					Reflect.invokeBlind(onComponentValueMethod, object, op.component, component.getPollData());
				change = change || s;
			}
		}
		
		if (rumbleField != null) for (Rumbler r : controller.getRumblers())
		{
			try {
				r.rumble(rumbleField.getFloat(object));
			} catch (Exception e) {}
		}
		
		if (change && onChangeMethod != null)
			Reflect.invokeBlind(onChangeMethod, object);
		
		return change;
	}
	
	/** Field set class. */
	private static class FieldGroup
	{
		InputComponent component;
		Field field;
		Class<?> value;
		
		public FieldGroup(InputComponent component, Field key, Class<?> value)
		{
			this.component = component;
			this.field = key;
			this.value = value;
		}

		public boolean set(Object obj, float polledValue, boolean relative)
		{
			if (value == Boolean.class || value == Boolean.TYPE)
			{
				boolean polledFlag = polledValue != 0f;
				try {
					boolean currentValue = ((Boolean)field.get(obj));
					if (currentValue != polledFlag || (relative && polledFlag))
					{
						field.set(obj, polledFlag);
						return true;
					}
				} catch (Exception e) {}
			}
			else if (value == Integer.class || value == Integer.TYPE)
			{
				try {
					int currentValue = ((Integer)field.get(obj));
					int polled = (int)polledValue;
					if (currentValue != polled || (relative && polled != 0))
					{
						field.set(obj, polled);
						return true;
					}
				} catch (Exception e) {}
			}
			else
			{
				try {
					float currentValue = ((Float)field.get(obj));
					if (currentValue != polledValue || (relative && polledValue != 0f))
					{
						field.set(obj, polledValue);
						return true;
					}
				} catch (Exception e) {}
			}
			
			return false;
		}
		
	}
	
}
