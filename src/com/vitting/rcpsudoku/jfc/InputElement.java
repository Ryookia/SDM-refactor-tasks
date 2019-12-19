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
 * InputElement will display the number 1 - 9 or blank, it is used for showing
 * and handling input for a RcpSudoku cell
 */
public class InputElement extends JButton {
	private static final long serialVersionUID = 1L;

	CellElement cellElement;

	boolean showValue = false;

	int value;

	/**
	 * @param element
	 *            CellElement - the owner of this InputField
	 * @param value
	 *            int - (0 - 8)
	 */
	public InputElement(CellElement owner, int value) {
		super();
		cellElement = owner;
		this.value = value;
	}

	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Show value
	 */
	public void set(boolean show) {
		showValue = show;
		setDisplayable();
	}

	/* 
	 * Return true if the InputElement shows its walue
	 * <br>
	 * This overrides java.awt.Component#isShowing()
	 * @see java.awt.Component#isShowing()
	 */
	public boolean isShowing() {
		return showValue;
	}
	
	/**
	 * Set the displayable value
	 */
	private void setDisplayable() {
		if (showValue) {
			setText(String.valueOf(value + 1));
		} else {
			setText("");
		}
	}
}
