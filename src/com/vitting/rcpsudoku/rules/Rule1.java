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
 * Rule 1, set all possiblities when all numbers in the same block, row or
 * column are excluded
 */
public final class Rule1 implements IRule {

	/* (non-Javadoc)
	 * @see com.vitting.sudoku.rules.IRule#runRule(com.vitting.sudoku.comon.MCell)
	 */
	public int runRule(MCell cell) throws SudokuException {
		
		// Rule 1 must not run if value is found
		if (cell.isValueFound()) {
			return RULE_NO_CHANGE;
		}
		SudokuBase base = SudokuBase.getSingleInstance();
		BitSet result = (BitSet)cell.getValue().clone();
		int originalBitcount = result.cardinality();

		// Remove numbers from same block
		MCell[] blockCells = base.getBlocks()
				.getCells(cell.getBlock());
		for (int i = 0; i < 9; i++) {
			if (blockCells[i].isValueFound()) {
				int x = blockCells[i].getIntValue();
				result.clear(x);
			}
		}

		//Remove numbers from same row
		int row = cell.getRow();
		for (int i = 0; i < 9; i++) {
			if (base.getCell(row, i).isValueFound()) {
				int x = base.getCell(row, i).getIntValue();
				result.clear(x);
			}
		}
		
		//Remove numbers from same column
		int column = cell.getColumn();
		for (int i = 0; i < 9; i++) {
			if (base.getCell(i, column).isValueFound()) {
				int x = base.getCell(i, column).getIntValue();
				result.clear(x);
			}
		}
		if (result.isEmpty()) {
			return RULE_NOT_POSSIBLE;
		}
		cell.setValue(result);
		if (originalBitcount == result.cardinality()) {
			return RULE_NO_CHANGE;
		} else {
			return RULE_CELL_CHANGED;
		}
	}
}
