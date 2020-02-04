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
package com.vitting.rcpsudoku.rules.rule3;

import com.vitting.rcpsudoku.model.IRule;
import com.vitting.rcpsudoku.model.MCell;
import com.vitting.rcpsudoku.model.SudokuBase;

import java.util.BitSet;

/**
 * Rule 3, If a number is found in only 1 row or column of a block, remove all
 * possibilities for that number in the same row or column in all other blocks.
 */
public class Rule3 implements IRule {

	/* (non-Javadoc)
	 * @see com.vitting.sudoku.rules.IRule#runRule(com.vitting.sudoku.comon.MCell)
	 */
	public int runRule(MCell cell) {
		int returnResult = RULE_NO_CHANGE;

		// Check holder.getCell()s in the same block
		if (cell.isValueFound()) {
			// Dont run on completed holder.getCell()s
			return returnResult;
		}
		ScanRowDataHolder holder = generateScanRowDataHolder(cell);
		// Scan row
		returnResult = scanRow(holder);
		if (returnResult != RULE_NO_CHANGE)
			return returnResult;

		// Scan column
		returnResult = scanColumn(holder);
		return returnResult;
	}

	private ScanRowDataHolder generateScanRowDataHolder(MCell cell) {
		SudokuBase base = SudokuBase.getSingleInstance();
		int block = cell.getBlock();
		MCell[] blockCells = base.getBlocks().getCells(block);
		int firstRow = blockCells[0].getRow(); // First row in block
		int firstColumn = blockCells[0].getColumn(); // First column in block
		return new ScanRowDataHolder(base, cell, firstRow, firstColumn);
	}

	private int scanRow(ScanRowDataHolder holder) {
		BitSet value = new BitSet(9);
		// Only if holder.getCell() is in first column of block
		if ((holder.getCell().getColumn() == holder.getFirstColumn())) {

			// Gather all holder.getCell() values in block row
			for (int colmn = holder.getFirstColumn(); colmn < holder.getFirstColumn() + 3; colmn++) {
				value.or(holder.getBase().getCell(holder.getCell().getRow(), colmn).getValue());
			}

			// Remove value found in other rows in same block
			for (int row = holder.getFirstRow(); row < holder.getFirstRow() + 3; row++) {
				if (holder.getCell().getRow() != row) {
					// Dont use own row
					for (int column = holder.getFirstColumn(); column < holder.getFirstColumn() + 3; column++) {
						value.andNot(holder.getBase().getCell(row, column).getValue());
					}
				}
			}

			if (value.cardinality() > 0) {
				// knock off matching numbers in same row in other blocks, rule returns true if any found
				for (int colmn = 0; colmn < 9; colmn++) {
					MCell targetCell = holder.getBase().getCell(holder.getCell().getRow(), colmn);
					if (targetCell.getBlock() != holder.getCell().getBlock()) {
						// Dont process same block
						if (targetCell.getValue().intersects(value)) {
							targetCell.getValue().andNot(value);
							if (!targetCell.isPossible()) {
								return RULE_NOT_POSSIBLE;
							}
							return RULE_CELL_CHANGED;
						}
					}
				}
			}

		}
		return RULE_NO_CHANGE;
	}

	private int scanColumn(ScanRowDataHolder holder) {
		BitSet value = new BitSet(9);
		// Only if holder.getCell() is in first row of block
		if ((holder.getCell().getRow() == holder.getFirstRow())) {

			// Gather all values in block column
			for (int row = holder.getFirstRow(); row < holder.getFirstRow() + 3; row++) {
				value.or(holder.getBase().getCell(row, holder.getCell().getColumn()).getValue());
			}

			// Remove value found in other columns in same block
			for (int column = holder.getFirstColumn(); column < holder.getFirstColumn() + 3; column++) {
				if (holder.getCell().getColumn() != column) {
					// Dont use own column
					for (int row = holder.getFirstRow(); row < holder.getFirstRow() + 3; row++) {
						value.andNot(holder.getBase().getCell(row, column).getValue());
					}
				}
			}
			if (value.cardinality() > 0) {
				// knock off matching numbers in same column in other blocks, rule returns true if any found
				for (int row = 0; row < 9; row++) {
					MCell targetCell = holder.getBase().getCell(row, holder.getCell().getColumn());
					if (targetCell.getBlock() != holder.getCell().getBlock()) {
						// Dont process same block
						if (targetCell.getValue().intersects(value)) {
							targetCell.getValue().andNot(value);
							if (!targetCell.isPossible()) {
								return RULE_NOT_POSSIBLE;
							}
							return RULE_CELL_CHANGED;
						}
					}
				}
			}
		}
		return RULE_NO_CHANGE;
	}
}
