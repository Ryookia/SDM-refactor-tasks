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
 * Extension to RcpSudoku rules must implement this interface
 */
public interface IRuleExtension {
	/**
	 * Called when the action must run
	 *  
	 * @return int one of {RULE_NO_CHANGE, RULE_CELL_CHANGED, RULE_GAME_COMPLETE, RULE_NOT_POSSIBLE}   
	 * @throws SudokuException
	 */
	public int run() throws SudokuException;
}
