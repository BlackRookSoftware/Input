/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.input.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that is attached to a method to call when a 
 * controller's field changes. Unlike {@link OnChange}, this
 * calls the method for EVERY annotated component that changes.
 * The method that this is attached to is called DURING the poll, 
 * as fields are set.
 * <p>The method must be public and must have three parameters: an InputComponent field, a boolean, and a float field.
 * @author Matthew Tropiano
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnEachChange
{
}
