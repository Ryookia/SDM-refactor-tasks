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
package com.vitting.rcpsudoku.jfc.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

import com.vitting.rcpsudoku.jfc.MainWindow;
import com.vitting.rcpsudoku.model.SudokuException;
import com.vitting.rcpsudoku.model.VerifyGame;



/**
 *
 * Handle Clear Action
 */
public class ModeEditAction extends JCheckBoxMenuItem implements ActionListener, IAction{

	private static final long serialVersionUID = 1L;
	MainWindow window;
	
	/**
	 * Constructor
	 * 
	 * @param window MainWindow 
	 */
	public ModeEditAction(MainWindow window) {
		this.window = window;
		setText("Edit");
		setState(true);
		addActionListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		try {
			//Verify the game first
			new VerifyGame().verifyAll(false);
		} catch (SudokuException e1) {
			window.setMessage(e1);
			return;
		}
		if (getState()) {
			// Edit mode
			window.setMessage("Game in edit mode");
			window.setEditMode(true);
		} else {
			// Non edit mode
			window.setMessage("Game in play mode");
			window.setEditMode(false);
		}
		window.refresh();
	}
	
	/* (non-Javadoc)
	 * @see com.vitting.rcpsudoku.jfc.actions.IAction#controlEnabled()
	 */
	public void controlEnabled() {
		// The menu is enabled when the model is loaded
		setSelected(window.isEditMode());
		setEnabled(true);
	}

}
