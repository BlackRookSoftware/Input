package com.blackrook.input.devices;

import com.blackrook.input.InputComponent;
import com.blackrook.input.annotation.ComponentBinding;

/**
 * XBox gamepad mapping.
 * @author Matthew Tropiano
 */
public class XBoxGamepad
{
	/** Control pad value: NONE. */
	public static final float CONTROLPAD_NONE = 0.0f;
	/** Control pad value: UP LEFT. */
	public static final float CONTROLPAD_UP_LEFT = 0.125f;
	/** Control pad value: UP. */
	public static final float CONTROLPAD_UP = 0.25f;
	/** Control pad value: UP RIGHT. */
	public static final float CONTROLPAD_UP_RIGHT = 0.375f;
	/** Control pad value: RIGHT. */
	public static final float CONTROLPAD_RIGHT = 0.5f;
	/** Control pad value: DOWN RIGHT. */
	public static final float CONTROLPAD_DOWN_RIGHT = 0.625f;
	/** Control pad value: DOWN. */
	public static final float CONTROLPAD_DOWN = 0.75f;
	/** Control pad value: DOWN LEFT. */
	public static final float CONTROLPAD_DOWN_LEFT = 0.875f;
	/** Control pad value: LEFT. */
	public static final float CONTROLPAD_LEFT = 1f;

	/** Left stick X. */
	@ComponentBinding(InputComponent.AXIS_X)
	public float leftStickX;
	/** Left stick Y. */
	@ComponentBinding(InputComponent.AXIS_Y)
	public float leftStickY;
	/** Combined trigger. */
	@ComponentBinding(InputComponent.AXIS_Z)
	public float trigger;
	/** Right stick X. */
	@ComponentBinding(InputComponent.AXIS_RX)
	public float rightStickX;
	/** Right stick Y. */
	@ComponentBinding(InputComponent.AXIS_RY)
	public float rightStickY;
	/** Control pad (POV Hat). */
	@ComponentBinding(InputComponent.AXIS_POV)
	public float controlPad;
	
	/** Gamepad button 0. */
	@ComponentBinding(InputComponent.BUTTON_0)
	public boolean a;
	/** Gamepad button 1. */
	@ComponentBinding(InputComponent.BUTTON_1)
	public boolean b;
	/** Gamepad button 2. */
	@ComponentBinding(InputComponent.BUTTON_2)
	public boolean x;
	/** Gamepad button 3. */
	@ComponentBinding(InputComponent.BUTTON_3)
	public boolean y;
	/** Gamepad button 4. */
	@ComponentBinding(InputComponent.BUTTON_4)
	public boolean leftBumper;
	/** Gamepad button 5. */
	@ComponentBinding(InputComponent.BUTTON_5)
	public boolean rightBumper;
	/** Gamepad button 6. */
	@ComponentBinding(InputComponent.BUTTON_6)
	public boolean back;
	/** Gamepad button 7. */
	@ComponentBinding(InputComponent.BUTTON_7)
	public boolean start;
	/** Gamepad button 8. */
	@ComponentBinding(InputComponent.BUTTON_8)
	public boolean leftStick;
	/** Gamepad button 9. */
	@ComponentBinding(InputComponent.BUTTON_9)
	public boolean rightStick;

}
