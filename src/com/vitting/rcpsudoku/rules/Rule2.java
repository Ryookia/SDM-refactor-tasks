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
package com.vitting.rcpsudoku.rules;

import java.util.BitSet;

import com.vitting.rcpsudoku.model.IRule;
import com.vitting.rcpsudoku.model.MCell;
import com.vitting.rcpsudoku.model.SudokuBase;
import com.vitting.rcpsudoku.model.SudokuException;

/**
 * Rule 2, Check for only 1 possible solution for a number in a block
 */
public final class Rule2 implements IRule {

	/* (non-Javadoc)
	 * @see com.vitting.sudoku.rules.IRule#runRule(com.vitting.sudoku.comon.MCell)
	 */
	public int runRule(MCell cell) throws SudokuException {
		if (cell.isValueFound()) {
			return RULE_NO_CHANGE;
		}
		
		// Check cells in the same block
		SudokuBase base = SudokuBase.getSingleInstance();
		MCell[] blockCells = base.getBlocks().getCells(cell.getBlock());
		BitSet result = (BitSet)cell.getValue().clone();
		for (int i = 0; i < 9; i++) {
			if (blockCells[i] != cell){
				// Dont knockut bits for the same block
				result.andNot(blockCells[i].getValue());
			}
		}

		// Only one possible solution
		if (result.cardinality() == 1) {
			cell.setValue(result);
			return RULE_CELL_CHANGED;
		} else {
			return RULE_NO_CHANGE;
		}
	}
}
