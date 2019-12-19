/**
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
 * Interface for Sudoku Rules
 */
public interface IRule {
	public static final int RULE_NO_CHANGE = 0;
	public static final int RULE_CELL_CHANGED = 1;
	public static final int RULE_GAME_COMPLETE = 2;
	public static final int RULE_NOT_POSSIBLE = 3;
	/**
	 * Run the rule on cell
	 * 
	 * @param cell
	 * @return int one of {RULE_NO_CHANGE, RULE_CELL_CHANGED, RULE_GAME_COMPLETE, RULE_NOT_POSSIBLE}   
	 */
	public abstract int runRule(MCell cell) throws SudokuException;
	
}
