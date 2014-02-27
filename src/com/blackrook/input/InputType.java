/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input;

import net.java.games.input.Controller.Type;

/**
 * The base for all input devices found through 
 * @author Matthew Tropiano
 */
public enum InputType
{
	UNKNOWN(Type.UNKNOWN),
	MOUSE(Type.MOUSE),
	KEYBOARD(Type.KEYBOARD),
	FINGERSTICK(Type.FINGERSTICK),
	GAMEPAD(Type.GAMEPAD),
	HEADTRACKER(Type.HEADTRACKER),
	RUDDER(Type.RUDDER),
	STICK(Type.STICK),
	TRACKBALL(Type.TRACKBALL),
	TRACKPAD(Type.TRACKPAD),
	WHEEL(Type.WHEEL),
	;
	
	final Type type;
	private InputType(Type type)
	{
		this.type = type;
	}

}
