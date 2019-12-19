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

import java.util.BitSet;

import com.vitting.rcpsudoku.model.Coordinate;

/**
 *	MCell (ModelCell) holds information for a Sudoku cell
 *
 */
public class MCell {
	
	// coordinates for this sell
	private Coordinate coordinate;
	private int block = 0;
	private MCellContent content = new MCellContent(this); 
	private boolean initialValue;
	
	
	/**
	 * Construct a MCell
	 * <br>
	 * The constructor is not public, MCells cannot be constructed by classes from outside this package
	 * 
	 * @param row
	 * @param column
	 * @param block
	 */
	MCell (int row, int column, int block) {
		coordinate = new Coordinate(row , column);
		this.block = block;
		content = new MCellContent(this);
		initialValue = false;
	}


	/**
	 * @return the coordinate for the cell
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	/**
	 * @return the MCellContent for the cell
	 */
	public MCellContent getContent() {
		return content;
	}
	
	/**
	 * @return the row number of the cell ( 0 - 8)
	 */
	public int getRow() {
		return coordinate.row;
	}
	
	/**
	 * @return the column number of the cell ( 0 - 8)
	 */
	public int getColumn() {
		return coordinate.column;
	}

	/**
	 * @return the block number of the cell ( 0 - 8)
	 */
	public int getBlock() {
		return block;
	}

	/**
	 * @return true if initialValue
	 */
	public boolean isInitialValue() {
		return initialValue;
	}

	/**
	 * @param initialValue boolean - the new value of initialValue - true/false
	 */
	public void setInitialValue(boolean initialValue) {
		this.initialValue = initialValue;
	}

	/**
	 * @return the value BitSet 
	 */
	public BitSet getValue() {
		return content.getValue();
	}

	/**
	 * @return int - the number of the first bit active  (0 - 8)
	 */
	public int getIntValue() {
		return content.getIntValue();
	}

	/**
	 * @param value BitSet - the new value to insert in the content array
	 */
	public void setValue(BitSet value) {
		content.setValue(value);
	}

	/**
	 * @param newValue integer - insert the single integer value in the array (0 - 8)
	 */
	public void setValue(int newValue) {
		content.setValue(newValue);
	}

	/**
	 * @param newValue integer - ADD the  integer value TO the array (0 - 8)
	 */
	public void AddValue(int newValue) {
		content.AddValue(newValue);
	}

	/**
	 * Clearing matching value in the content bit array (0 - 8)
	 * <br>
	 * If the bit to be cleared is the only one, all bits are set,
	 * indicating an empty cell
	 * 
	 * @param toBeCleared int - the value to be cleared
	 */
	public void clearValue(int toBeCleared) {
		content.clearValue(toBeCleared);
	}
		
	/**
	 * @return true if single value found
	 */
	public boolean isValueFound() {
		return content.isValueFound();
	}

	/**
	 * Set the MCell to empty condition
	 */
	public void setEmpty() {
		content.setEmpty();
		initialValue = false;
	}

	/**
	 * @return true if the cell is empty
	 */
	public boolean isEmpty() {
		return content.isEmpty();
	}

	/**
	 * @return true if the cell has a valid content
	 */
	public boolean isPossible() {
		return content.isPossible();
	}
}
