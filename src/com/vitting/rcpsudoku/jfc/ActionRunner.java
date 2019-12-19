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

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.vitting.rcpsudoku.jfc.actions.IAction;
import com.vitting.rcpsudoku.model.*;
import com.vitting.rcpsudoku.rules.*;

public class ActionRunner extends JMenu {
    private static final long serialVersionUID = 1L;

    /**
         * Controle execution of rules, it creates and handles the Action menu's
         */

    private MainWindow mainWindow;

    private CellElement[][] cellElements;

    /**
         * Construct the ActionRunner
         * 
         * @param w
         *                Window - Owning window
         * @param c
         *                CellElement[9][9] - Cell elements in a 9 by 9 array(
         *                00 - 88)
         */
    public ActionRunner(MainWindow w, CellElement[][] c) {
	mainWindow = w;
	cellElements = c;
	createActionMenu();
    }

    /**
         * Create the actionMenu
         * 
         */
    private void createActionMenu() {
	setText("Action");

	// Solve
	ActionMenuItem solveAction = new ActionMenuItem();
	add(solveAction);
	solveAction.setText("Solve");
	solveAction.addActionListener(new ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {

		VerifyGame verify = new VerifyGame();
		try {

		    // Clear any messages
		    mainWindow.setMessage(""); // Clear the message field
		    // Check game for validity, in verbose mode
		    if (verify.verifyAll(false) == IRule.RULE_GAME_COMPLETE) {
			// Game already complete,do not run
			mainWindow.gameComplete();
			return;
		    }

		    RuleRunner runner = new RuleRunner();
		    int result = runner.run();
		    // Refresh the cells
		    for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
			    cellElements[x][y].refresh();
			}
		    }
		    switch (result) {
			case IRule.RULE_NOT_POSSIBLE:
			    mainWindow.setMessage("No possible solution");
			    return;
			default:
			    // Do nothing
		    }

		    switch (verify.verifyAll(true)) {
			case IRule.RULE_GAME_COMPLETE:
			    mainWindow.gameComplete();
			    break;
			case IRule.RULE_NOT_POSSIBLE:
			    mainWindow.setMessage("No possible solution");
			    break;
			default:
			    // IRule.RULE_NO_CHANGE
			    mainWindow
				.setMessage("No solution found, try manual input and run solve again");
		    }
		} catch (SudokuException ex) {
		    mainWindow.setMessage(ex);
		}
	    }
	});

	// run Verify
	ActionMenuItem verifyAction = new ActionMenuItem();
	add(verifyAction);
	verifyAction.setText("Verify");
	verifyAction.addActionListener(new ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {
		VerifyGameComplete verifyGameComplete = new VerifyGameComplete();
		mainWindow.setMessage(""); // Clear the message field
		// Call VerifyGame()
		try {
		    VerifyGame verify = new VerifyGame();
		    verify.verifyAll(false);
		    boolean result = verifyGameComplete.verifyAll() == IRule.RULE_GAME_COMPLETE;
		    if (result) {
			mainWindow.gameComplete();
		    } else {
			mainWindow.setMessage("Ok so far");
		    }
		} catch (SudokuException e1) {
		    mainWindow.setMessage(e1);
		}
	    }
	});
    }
    
    private class ActionMenuItem extends JMenuItem implements IAction {
		private static final long serialVersionUID = 1L;

		public void controlEnabled() {
			setEnabled(SudokuBase.getSingleInstance().isLoaded());
		}
    	
    }
    
}
