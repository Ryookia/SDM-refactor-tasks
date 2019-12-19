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

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.BitSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.vitting.rcpsudoku.model.*;

/**
 * 
 * CellElement extends JPanel to hold information for display of a single SuDoKu
 * cell
 * 
 */
public class CellElement extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;

	private static final java.awt.Color activeBackgroundColor = new java.awt.Color(
			255, 255, 255);

	private static final java.awt.Color activeForegroundColor = new java.awt.Color(
			0, 0, 0);

	private static final java.awt.Color initialBackgroundColor = new java.awt.Color(
			202, 202, 202);

	private static final java.awt.Color initialForegroundColor = new java.awt.Color(
			0, 0, 0);

	private static final java.awt.Color inputBackgroundColor = new java.awt.Color(
			255, 255, 255);

	private static final java.awt.Color inputForegroundColor = new java.awt.Color(
			92, 0, 92);

	private static final java.awt.Color informationBackgroundColor = new java.awt.Color(
			255, 255, 255);

	private static final java.awt.Color informationForegroundColor = new java.awt.Color(
			192, 192, 192);

	private static final java.awt.Color errorBackgroundColor = new java.awt.Color(
			255, 0, 0);

	private static final java.awt.Color errorForegroundColor = new java.awt.Color(
			0, 0, 0);

	private static boolean mouseClicksEnabled = true;

	private boolean errorColors = false;

	private java.awt.Color backgroundColor = activeBackgroundColor;

	private java.awt.Color foregroundColor = activeForegroundColor;

	private SudokuBase base = SudokuBase.getSingleInstance();

	MCell modelCell = null;

	/**
	 * Construct a CellElement
	 * 
	 * @param x
	 *            the x cordinate of the Mcell this CellElement is representing
	 * @param y
	 *            the y cordinate of the Mcell this CellElement is representing
	 */
	CellElement(int x, int y, MainWindow w) {
		super();
		mainWindow = w;
		setBackground(activeBackgroundColor);
		setBorder(new LineBorder(new java.awt.Color(128, 128, 128), 1, false));
		addMouseListener(this);

		// Connect the element to the data model
		modelCell = base.getCell(x, y);
	}

	/**
	 * @param value
	 *            int 0 - 8 displays the value in a single cell as 1 - 9
	 */
	void setValue(int value) {
		if (mainWindow.isEditMode()) {
			setInitialValue(value);
		} else {
			setActiveColors();
			setValueInternal(value);
			modelCell.setValue(value);
		}
	}

	private void setActiveColors() {
		if (errorColors) {
			backgroundColor = errorBackgroundColor;
			foregroundColor = errorForegroundColor;
		} else {
			backgroundColor = activeBackgroundColor;
			foregroundColor = activeForegroundColor;
		}
	}

	private void setStringColors() {
		if (errorColors) {
			backgroundColor = errorBackgroundColor;
			foregroundColor = errorForegroundColor;
		} else {
			backgroundColor = informationBackgroundColor;
			if (mainWindow.isEditMode()) {
				foregroundColor = activeForegroundColor;
			} else {
				foregroundColor = informationForegroundColor;
			}
		}
	}

	/**
	 * Set the initial value (brackground color)
	 * 
	 * @param value -
	 *            The initial value (0 - 8)
	 */
	void setInitialValue(int value) {
		setInitialColors();
		modelCell.setInitialValue(true);
		setValueInternal(value);
		modelCell.setValue(value);
	}

	private void setInitialColors() {
		if (errorColors) {
			backgroundColor = errorBackgroundColor;
			foregroundColor = errorForegroundColor;
		} else {
			backgroundColor = initialBackgroundColor;
			foregroundColor = initialForegroundColor;
		}
	}

	/**
	 * @param value
	 *            int 0 - 8 displays the value in a single cell as 1 - 9
	 */
	private void setValueInternal(int value) {
		removeAll();
		setLayout(new GridLayout(1, 1));
		DisplayElement displayElement = new DisplayElement(this);
		displayElement.addMouseListener(this);
		add(displayElement);
		displayElement.setValue(value);
		displayElement.setBackground(backgroundColor);
		displayElement.setForeground(foregroundColor);
		displayElement.setPreferredSize(new java.awt.Dimension(31, 25));
		displayElement.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		displayElement.setFocusable(false);
		displayElement.setFont(new java.awt.Font("Dialog", 1, 28));
	}

	/**
	 * @param BitSet
	 *            the numbers will be displayed ( 1-9) in a 9 x 9 grid
	 */
	private void setInputFields(BitSet value) {
		removeAll();
		setLayout(new GridLayout(3, 3));
		InputElement[] inputElements = new InputElement[9];
		for (int x = 0; x < 9; x++) {
			InputElement iElement = new InputElement(this, x);
			inputElements[x] = iElement;
			iElement.addMouseListener(this);
			add(iElement);
			iElement.setBackground(backgroundColor);
			iElement.setForeground(foregroundColor);
			iElement.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			iElement.setFocusable(false);
			iElement.set(value.get(x));
		}
	}

	/**
	 * set the the cell in input mode
	 */
	private void setInputMode() {
		if (errorColors) {
			backgroundColor = errorBackgroundColor;
			foregroundColor = errorForegroundColor;
		} else {
			backgroundColor = inputBackgroundColor;
			foregroundColor = inputForegroundColor;
		}
		modelCell.setEmpty();
		modelCell.setInitialValue(false);
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (mouseClicksEnabled == false) {
			return;
		}
		if (mainWindow.isEditMode() == false) {
			if (modelCell.isInitialValue()) {
				return;
			}
		}
		mainWindow.setMessage("");
		if (e.getComponent() instanceof InputElement) {
			InputElement inputElement = (InputElement) e.getComponent();
			if (e.getButton() == MouseEvent.BUTTON1) {
				// The user has clicked the left mouse button on the InputField
				// If the field was showing use its value as the cell value
				// If not insert the value again
				if (inputElement.isShowing()) {
					// Use the vale as input
					setValue(inputElement.getValue());
				} else {
					// Insert the value
					modelCell.AddValue(inputElement.getValue());
					refresh();
				}
			} else {
				// The user has clicked the right mouse button on the InputField
				// Ignore the click if in EditMode
				// If the field was showing clear it
				// If not ignore the click
				if (mainWindow.isEditMode() == false
						&& inputElement.isShowing()) {
					modelCell.clearValue(inputElement.getValue());
					refresh();
				}
			}
			base.setModelDirty(true);
		} else {
			if (e.getButton() == MouseEvent.BUTTON1) {
				// The user has clicked the left mouse button on the
				// DisplayField
				// Just ignore it in this version of the program
			} else {
				// The user has clicked the right mouse button on the
				// DisplayField
				// Set the field to input if Edit mode or not initial value

				if (mainWindow.isEditMode()
						|| modelCell.isInitialValue() == false) {
					setInputMode();
					base.setModelDirty(true);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// Not used
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// Not used
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// Not used
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// Not used
	}

	/**
	 * @param newState -
	 *            Set the Cell Elements Mouse Clicks enabled to newState
	 */
	static public void setClicksEnabled(boolean newState) {
		mouseClicksEnabled = newState;
	}

	/**
	 * Refresh the CellElement
	 */
	public void refresh() {
		if (modelCell.isInitialValue()) {
			setInitialColors();
			setValueInternal(modelCell.getIntValue());
		} else if (modelCell.isValueFound()) {
			setActiveColors();
			setValueInternal(modelCell.getIntValue());
		} else {
			setStringColors();
			setInputFields(modelCell.getValue());
		}
		repaint();
	}

	/**
	 * Set the Error Collors mode on/off
	 * 
	 * @param errorColors
	 */
	public void setError(boolean value) {
		this.errorColors = value;
		refresh();
	}

	/**
	 * Clear the cell value
	 */
	public void clear() {
		setInputMode();
	}
}
