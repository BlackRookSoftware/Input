/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input;

/**
 * A listener that contains methods that are invoked before 
 * controller polling takes place and after all controllers are polled. 
 * @author Matthew Tropiano
 */
public interface InputSystemListener
{
	/**
	 * Called when {@link InputSystem#pollAll()} is called,
	 * but before each added controller is actually polled.
	 */
	public void beforePoll();
	
	/**
	 * Called when {@link InputSystem#pollAll()} is called,
	 * and after each added controller is actually polled and pollAll().
	 * @param change if true, a component change occurred during polling, false if not. 
	 */
	public void afterPoll(boolean change);
	
}
