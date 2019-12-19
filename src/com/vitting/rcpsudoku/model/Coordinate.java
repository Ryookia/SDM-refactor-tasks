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
 * Coordinate holds row / column coordinates for locating a MCell
 * 
 */
public class Coordinate {

	int row;
	int column;
	
	/**
	 * Construct the Coordinate
	 * <br>
	 * The constructor is not public, a Coordinate cannot be constructed by classes from outside this package
	 * 
	 * @param x the row coordinate
	 * @param y column coordinate
	 */
	Coordinate(int x, int y) {
		row = x;
		column = y;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	public String toString() {
		return "Coordinate= " + row + ":" + column;
	}
}
