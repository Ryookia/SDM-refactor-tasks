/*
 * Copyright (c) 2006 Henning Vitting and others.
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
package com.vitting.rcpsudoku.jfc.test;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.vitting.rcpsudoku.jfc.MainWindow;
import com.vitting.rcpsudoku.model.IRule;
import com.vitting.rcpsudoku.model.SudokuBase;
import com.vitting.rcpsudoku.model.SudokuException;
import com.vitting.rcpsudoku.model.VerifyGame;
import com.vitting.rcpsudoku.rules.Rule4;
import com.vitting.rcpsudoku.rules.RuleRunner;

/**
 * 
 * Handle Test Actions
 */
public class Test extends JMenu {

    private static final long serialVersionUID = 1L;

    SudokuBase base = SudokuBase.getSingleInstance();

    MainWindow window;

    RuleRunner ruleRunner = new RuleRunner();

    /**
         * Constructor
         * 
         * @param window
         *                MainWindow
         */
    public Test(MainWindow window) {
	this.window = window;
	setText("Test");
	JMenuItem testMenuItem = new JMenuItem();
	add(testMenuItem);
	testMenuItem.setText("Test 1");
	testMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent e) {
		test1();
	    }
	});
    }

    protected void test1() {

	// Run Rules 1 - 3
	try {
	    int result = new RuleRunner().run();
//	    { // DEBUG -- Result rules 1 - 3 was
//		System.out.println("Result rules 1 - 3 was: " + result);
//	    }
	    switch (result) {
		case IRule.RULE_NO_CHANGE:
		    // Check for game complete
		    window.setMessage(""); // Clear the message field
		    // Call VerifyGame()
		    try {
			VerifyGame verify = new VerifyGame();
			verify.verifyAll(false);
		    } catch (SudokuException e1) {
			window.setMessage(e1);
		    }

		    Rule4 rule4 = new Rule4();
		    if (rule4.run() == IRule.RULE_GAME_COMPLETE) {
			window.gameComplete();
			return;
		    }
		    break;
		case IRule.RULE_CELL_CHANGED:
		    // do nothing
		    break;
		case IRule.RULE_NOT_POSSIBLE:
		    // do nothing
		    break;
		case IRule.RULE_GAME_COMPLETE:
		    window.gameComplete();
		    return;
	    }
	} catch (SudokuException e) {
	    e.printStackTrace();
	    return;
	} finally {
	    window.refresh();
	}
    }
}
