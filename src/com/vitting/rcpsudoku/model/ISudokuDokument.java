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
*
* This interface contains all XML Tags and fixed values in the Sudoku documents
*/
public interface ISudokuDokument {
	public static String SUDOKU = "RcpSuDoKu"; 
	public static String DOCUMENT_COMMENT = "Document for RcpSuDoKu";
    public static String GAME = "game";
    public static String DOCUMENT_VERSION = "version";
    public static String CURRENT_DOCUMENT_VERSION = "1.0";
    public static String CELL = "Cell";    
    public static String ROW = "row";    
    public static String COLUMN = "column";    
    public static String VALUE = "value";
    public static String STATUS = "status";
    public static String STATUS_INITIAL = "initial";
    public static String STATUS_CALCULATED = "calculated";
}
