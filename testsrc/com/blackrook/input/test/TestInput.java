/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input.test;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.java.games.input.Controller;

import com.blackrook.commons.Common;
import com.blackrook.input.InputComponent;
import com.blackrook.input.InputSystem;
import com.blackrook.input.annotation.ComponentBinding;

public class TestInput
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		final MouseJunk j = new MouseJunk();
		
		InputSystem inputSystem = new InputSystem();
		Controller controller = inputSystem.findMouse();
		//Controller controller = inputSystem.findXBoxGamepad();
		
		if (controller != null)
		{
			inputSystem.addController(controller, j);
		}
		
		JFrame frame = new JFrame();
		frame.setTitle("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel label = new JLabel("Close this window.");
		label.setPreferredSize(new Dimension(192, 48));
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
		
		while (frame.isVisible())
		{
			long t = System.nanoTime();
			inputSystem.pollAll();
			long x = System.nanoTime() - t;
			label.setText(String.format("%.3fms %.3f %.3f %.3f", x / 1000000d, 
					j.x, j.y, j.z
					));
			Common.sleep(33);
		}
}

	public static class MouseJunk
	{
		@ComponentBinding(InputComponent.AXIS_X)
		public float x;
		@ComponentBinding(InputComponent.AXIS_Y)
		public float y;
		@ComponentBinding(InputComponent.AXIS_Z)
		public float z;
	}
	
	public static class XBoxJunk
	{
		@ComponentBinding(InputComponent.AXIS_X)
		public float x;
		@ComponentBinding(InputComponent.AXIS_Y)
		public float y;
		@ComponentBinding(InputComponent.AXIS_Z)
		public float z;
		@ComponentBinding(InputComponent.AXIS_RX)
		public float rx;
		@ComponentBinding(InputComponent.AXIS_RY)
		public float ry;

		@ComponentBinding(InputComponent.BUTTON_0)
		public boolean ba;
		@ComponentBinding(InputComponent.BUTTON_1)
		public boolean bb;
		@ComponentBinding(InputComponent.BUTTON_2)
		public boolean bx;
		@ComponentBinding(InputComponent.BUTTON_3)
		public boolean by;

	}
	
}
