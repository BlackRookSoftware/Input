/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input.exception;

/**
 * An exception that can be thrown during the creation of an input controller. 
 * @author Matthew Tropiano
 */
public class InputInitializationException extends RuntimeException
{
	private static final long serialVersionUID = 2710052030971709440L;

	public InputInitializationException()
	{
		super("An exception was thrown.");
	}
	
	public InputInitializationException(String message)
	{
		super(message);
	}
	
	public InputInitializationException(Throwable throwable)
	{
		super(throwable);
	}
	
	public InputInitializationException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
	
}
