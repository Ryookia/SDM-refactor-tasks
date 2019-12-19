/*
 * Copyright (c) 2005 Henning Vitting and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this 
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Henning Vitting - Initial API and implementation
 *   
  */
package com.vitting.rcpsudoku.jfc;

import javax.swing.JButton;

/**
 * DisplayElement will display the number 1 - 9 or blank, it is used for displaying
 * a RcpSudoku cell
 */
public class DisplayElement extends JButton {
	private static final long serialVersionUID = 1L;
	CellElement cellElement;
	int value;

	/**
	 * @param element CellElement - the owner of this InputField
	 */
	public DisplayElement(CellElement owner) {
		super();
		cellElement = owner;
	}

	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Display parm 
	 * <br>
	 * parm = 0 - 8 will display the vumbers 1 - 9
	 * <br>
	 * parm < 0 will display a blank
	 * 
	 * @param value The value to set. 
	 */
	public void setValue(int value) {
		this.value = value;
		setDisplayable();
	}

	/**
	 * Set the displayable value
	 */
	private void setDisplayable() {
		if (value < 0) {
			setText("");
		} else {
			setText(String.valueOf(value + 1));
		}
	}
}
