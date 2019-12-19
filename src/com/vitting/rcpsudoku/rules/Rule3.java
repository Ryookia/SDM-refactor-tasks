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

/**
 * Rule 3, If a number is found in only 1 row or column of a block, remove all
 * possibilities for that number in the same row or column in all other blocks.
 */
public class Rule3 implements IRule {

	/* (non-Javadoc)
	 * @see com.vitting.sudoku.rules.IRule#runRule(com.vitting.sudoku.comon.MCell)
	 */
	public int runRule(MCell cell) {
		int retValue = RULE_NO_CHANGE;

		// Check cells in the same block
		if (cell.isValueFound()) {
			// Dont run on completed cells
			return RULE_NO_CHANGE;
		}
		SudokuBase base = SudokuBase.getSingleInstance();
		int block = cell.getBlock();
		MCell[] blockCells = base.getBlocks().getCells(block);
		int firstRow = blockCells[0].getRow(); // First row in block
		int firstColumn = blockCells[0].getColumn(); // First column in block

		// Scan row
		BitSet value = new BitSet(9);
		// Only if cell is in first column of block
		if ((cell.getColumn() == firstColumn)) {
			
			// Gather all cell values in block row  
			for (int colmn = firstColumn; colmn < firstColumn + 3; colmn++) {
				value.or(base.getCell(cell.getRow(), colmn).getValue());
			}
			
			// Remove value found in other rows in same block
			for (int row = firstRow; row < firstRow + 3; row++) {
				if(cell.getRow() != row) {
					// Dont use own row
					for (int column = firstColumn; column < firstColumn + 3; column++){
						value.andNot(base.getCell(row, column).getValue());
					}
				}
			}
			
			if (value.cardinality() > 0) {
				// knock off matching numbers in same row in other blocks, rule returns true if any found 
				for (int colmn = 0; colmn < 9; colmn++) {
					MCell targetCell = base.getCell(cell.getRow(), colmn);
					if (targetCell.getBlock() != cell.getBlock()) {
						// Dont process same block 
						if (targetCell.getValue().intersects(value)) {
							retValue = RULE_CELL_CHANGED;
							targetCell.getValue().andNot(value);
							if (targetCell.isPossible() == false) {
								return RULE_NOT_POSSIBLE;
							}
						}
					}
				}			
			}
			
		}

		// Scan column
		value.clear();
		// Only if cell is in first row of block
		if ((cell.getRow() == firstRow)) {
			
			// Gather all values in block column 
			for (int row = firstRow; row < firstRow + 3; row++) {
				value.or(base.getCell(row, cell.getColumn()).getValue());
			}
			
			// Remove value found in other columns in same block
			for (int column = firstColumn; column < firstColumn + 3; column++) {
				if(cell.getColumn() != column) {
					// Dont use own column
					for (int row = firstRow; row < firstRow + 3; row++){
						value.andNot(base.getCell(row, column).getValue());
					}
				}
			}
			if (value.cardinality() > 0) {
				// knock off matching numbers in same column in other blocks, rule returns true if any found 
				for (int row = 0; row < 9; row++) {
					MCell targetCell = base.getCell(row, cell.getColumn());
					if (targetCell.getBlock() != cell.getBlock()) {
						// Dont process same block 
						if (targetCell.getValue().intersects(value)) {
							retValue = RULE_CELL_CHANGED;
							targetCell.getValue().andNot(value);
							if (targetCell.isPossible() == false) {
								return RULE_NOT_POSSIBLE;
							}
						}
					}
				}			
			}			
		}
		return retValue;
	}
}
