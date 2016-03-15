package com.blackrook.input.devices;

import java.awt.MouseInfo;
import java.awt.Point;

import com.blackrook.input.InputComponent;
import com.blackrook.input.annotation.ComponentBinding;
import com.blackrook.input.annotation.OnChange;

/**
 * Mouse mapping.
 * @author Matthew Tropiano
 */
public class Mouse
{
	/** Mouse position X. */
	public int positionX;
	/** Mouse position Y. */
	public int positionY;
	
	/** Controller field for mouse X. */
	@ComponentBinding(InputComponent.AXIS_X)
	public int x;
	/** Controller field for mouse Y. */
	@ComponentBinding(InputComponent.AXIS_Y)
	public int y;
	/** Controller field for mouse wheel. */
	@ComponentBinding(InputComponent.AXIS_Z)
	public int wheel;
	
	@ComponentBinding(InputComponent.BUTTON_LEFT)
	public boolean left;
	@ComponentBinding(InputComponent.BUTTON_MIDDLE)
	public boolean middle;
	@ComponentBinding(InputComponent.BUTTON_RIGHT)
	public boolean right;
	@ComponentBinding(InputComponent.BUTTON_3)
	public boolean back;
	@ComponentBinding(InputComponent.BUTTON_4)
	public boolean forward;
	
	@OnChange
	public void onChange()
	{
		if (x != 0 || y != 0)
		{
			Point p = MouseInfo.getPointerInfo().getLocation();
			positionX = p.x;
			positionY = p.y;
		}
	}
	
}
