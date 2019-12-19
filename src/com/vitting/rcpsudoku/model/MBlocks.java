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
 * MBlocks keeps track of the MBlocks
 * 
 */
public class MBlocks {

	MCell[][] mblocks = new MCell[9][9];

	/**
	 * Construct the MBlocks <br>
	 * The constructor is not public, MBlocks cannot be constructed by classes
	 * from outside this package
	 * 
	 */
	MBlocks() {
		// initialise the mblock to all 0
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				mblocks[x][y] = null;
			}
		}

	}

	/**
	 * Add a cell to the blocks array
	 * 
	 * @param cell
	 */
	void addCell(MCell cell) {
		int block = cell.getBlock();
		for (int x = 0; x < 9; x++) {
			if (mblocks[block][x] == null) {
				mblocks[block][x] = cell;
				return;
			}
		}
	}
	
	/**
	 * @param block
	 * @return MCell[] - an array of MCells belonging to the block 
	 */
	public MCell[] getCells(int block) {
		MCell[] cells = new MCell[9];
		for (int x = 0; x < 9; x++) {
			cells[x] = mblocks[block][x];
		}
		return cells;
	}

	
	/**
	 * find the block number for a given coordinate
	 * 
	 * @param x row
	 * @param y column
	 * @return int the block number 0 - 8
	 */
	int findBlock(int x, int y) {
		
		int modifier = 0;
		switch (x) {
		case 0:
		case 1:
		case 2:
			// modifier already 0
			break;
		case 3:
		case 4:
		case 5:
			modifier = 3;
			break;
		case 6:
		case 7:
		case 8:
			modifier = 6;
			break;
		}
		switch (y) {
		case 0:
		case 1:
		case 2:
			return modifier + 0;
		case 3:
		case 4:
		case 5:
			return modifier + 1;
		case 6:
		case 7:
		case 8:
			return modifier + 2;
		}
		return 0; // Should never occour
	}
}
