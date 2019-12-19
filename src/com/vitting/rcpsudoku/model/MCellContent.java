/**
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
package com.vitting.rcpsudoku.model;

import java.util.BitSet;

/**
 * MCell holds data information for a Sudoku cell 
 */
public class MCellContent {
	
	// possible values, if only 1 the result is found
	private BitSet value;

	/**
	 * Construct a new empty McellContent
	 * 
	 * @param cell MCell - Backward link to the refering MCell
	 */
	public MCellContent(MCell cell) {
		value =  new BitSet(9);
		value.set(0, 9);
	}

	/**
	 * Copy constructor for MCellContent
	 * <br>
	 * Copy wll be a deep clone
	 * 
	 * @param content MCellContent - the copy clone of MCellContent
	 */
	public MCellContent(MCellContent content) {
		value = (BitSet) content.value.clone();
	}

	/**
	 * @return the value BitSet 
	 */
	public BitSet getValue() {
		return value;
	}

	/**
	 * @return int - the number of the first bit active  (0 - 8)
	 */
	public int getIntValue() {
		return value.nextSetBit(0);
	}

	/**
	 * @param value BitSet - the new value to insert in the array
	 */
	public void setValue(BitSet value) {
		this.value = value;
	}

	/**
	 * @param newValue integer - insert the single integer value in the array (0 - 8)
	 */
	public void setValue(int newValue) {
		value.clear();
		value.set(newValue);
	}

	/**
	 * @param newValue integer - ADD the  integer value TO the array (0 - 8)
	 */
	public void AddValue(int newValue) {
		value.set(newValue);
	}
	/**
	 * Clearing matching value in the bit array (0 - 8)
	 * <br>
	 * If the bit to be cleared is the only one, all bits are set,
	 * indicating an empty cell
	 * 
	 * @param toBeCleared int - the value to be cleared
	 */
	public void clearValue(int toBeCleared) {
		if (toBeCleared < 0) {
			// Ignore invalid values
			return;
		}
		value.clear(toBeCleared);
		if (value.isEmpty()) {
			// Indicate empty cell
			value.set(0, 9);
		}
	}

		
	/**
	 * @return true if single value found
	 */
	public boolean isValueFound() {
		return value.cardinality() == 1;
	}

	/**
	 * Set the MCell to empty condition
	 */
	public void setEmpty() {
		value.clear();
		value.set(0, 9);
	}

	/**
	 * @return true if the cell is empty
	 */
	public boolean isEmpty() {
		return value.cardinality() == 9;
	}

	/**
	 * @return true if the cell has a valid content
	 */
	public boolean isPossible() {
		return value.isEmpty() == false;
	}
}
