/*
 * Copyright (c) 2005, 2006 Henning Vitting and others.
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
package com.vitting.rcpsudoku.model;

/**
 * Verify that all the MCells has value found
 */
public class VerifyGameComplete {

    /**
         * Check for game complete
         * 
         * @return int one of {RULE_NO_CHANGE, RULE_GAME_COMPLETE,
         *         RULE_NOT_POSSIBLE}
         */
    public int verifyAll() {
	boolean complete = true;
	SudokuBase base = SudokuBase.getSingleInstance();
	for (int row = 0; row < 9; row++) {
	    for (int column = 0; column < 9; column++) {
		MCell cell = base.getCell(row, column);
		if (cell.isValueFound() == false) {
		    complete = false;
		}
		if (cell.isPossible() == false) {
		    return IRule.RULE_NOT_POSSIBLE;
		}
	    }
	}
	// ALL MCells completed
	if (complete) {
	    return IRule.RULE_GAME_COMPLETE;
	}
	return IRule.RULE_NO_CHANGE;
    }
}
