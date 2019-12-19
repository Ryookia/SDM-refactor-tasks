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

import java.util.Vector;

/**
 * Singleton anchor point for for the Sudoku data model
 * 
 */
public class SudokuBase {

    // singleton placeholder
    private static SudokuBase singleInstance = null;

    private boolean modelDirty = false;

    private boolean modelLoadedFromFilesystem = false;
    
    private boolean solveMode = false;

    private Vector modelListeners = new Vector();

    private MBlocks blocks = null;

    private MCell[][] mCells = new MCell[9][9];

    private MCellStack stack = new MCellStack();

    /**
         * Private constructor prevents instation from other classes <br>
         * Create the initial model classes
         */
    private SudokuBase() {
	// Create the blocks array
	blocks = new MBlocks();
	for (int x = 0; x < 9; x++) {
	    for (int y = 0; y < 9; y++) {
		MCell cell = new MCell(x, y, getBlocks().findBlock(x, y));
		mCells[x][y] = cell;
		blocks.addCell(cell);
	    }
	}
    }

    /**
         * Getter for the single instance of SudokuBase <br>
         * If no instance exists one is created
         * 
         * @return singleInstance of SudokuBase
         */
    public static SudokuBase getSingleInstance() {
	if (singleInstance == null) {
	    singleInstance = new SudokuBase();
	}
	return singleInstance;
    }

    /**
         * @param x
         *                the row coordinate
         * @param y
         *                the column coordinate
         * 
         * @return the MCell corresponding to the coordinates
         */
    public MCell getCell(int x, int y) {
	return mCells[x][y];
    }

    /**
         * @return MBlocks - the blocks array
         */
    public MBlocks getBlocks() {
	return blocks;
    }

    /**
         * Clear all cells to the start game value
         * 
         * @param newGame
         *                boolean - if true all cells are emptied
         */
    public void clear(boolean newGame) {

	// Clear the stack
	stack.clear();

	for (int row = 0; row < 9; row++) {
	    for (int column = 0; column < 9; column++) {
		MCell cell = mCells[row][column];
		if (newGame || cell.isInitialValue() == false) {
		    cell.setEmpty();
		}
	    }
	}
	// reset model dirty if model is loaded from filesystem
	if (isModelLoadedFromFilesystem()) {
	    setModelDirty(false);
	}
    }

    /**
         * @param listener
         *                IModelListener
         */
    public void addModelListener(IModelListener listener) {
	modelListeners.addElement(listener);
    }

    /**
         * @param listener
         *                IModelListener
         */
    public void removeModelListener(IModelListener listener) {
	modelListeners.removeElement(listener);
    }

    /**
         * Broadcast cellsChanged
         * 
         * @param refreshAll
         *                boolean - refresh all cells
         */
    public void cellsChanged(boolean refreshAll) {
	if (solveMode == false) {
	    // modelDirty not sat in solveMode
	    modelDirty = true;
	}
	// DEBUG -- cellsChanged
//	System.out.println("cellsChanged - model dirty: " + isModelDirty());
	for (int i = 0; i < modelListeners.size(); i++) {
	    IModelListener listener = (IModelListener) modelListeners.get(i);
	    listener.modelChanged(refreshAll);
	}
    }

    /**
         * Clear the gamestack
         */
    public void clearStack() {
	stack.clear();
    }

    /**
         * Push the current game onto the gamestack
         * 
         * @return int elementsInStack
         */
    public int pushStack() {
	return stack.pushGame(mCells);
    }

    /**
         * Restore the game from the gamestack
         * 
         * @return int elementsInStack
         */
    public int popStack() {
	int result = stack.popGame(mCells);
	cellsChanged(true);
	return result;
    }

    /**
         * @return true if the base is loaded
         */
    public boolean isLoaded() {
	boolean result = false;
	for (int row = 0; row < 9; row++) {
	    for (int column = 0; column < 9; column++) {
		MCell cell = mCells[row][column];
		if (cell.isEmpty() == false) {
		    result = true;
		}
	    }
	}
	return result;
    }

    /**
         * @return boolean - true if unsaved sata in model
         */
    public boolean isModelDirty() {
	// DEBUG -- isModelDirty
//	System.out.println("isModelDirty() - model dirty: " + modelDirty);
	return modelDirty;
    }

    /**
         * Set / Reset the model dirty condition
         * 
         * @param modelDirty
         *                boolean - the new condition (true / false)
         */
    public void setModelDirty(boolean modelDirty) {
	// DEBUG -- cellsChanged
//	System.out.println("setModelDirty - model dirty: " + modelDirty);
	this.modelDirty = modelDirty;
    }

    /**
         * @return boolean - modelLoadedFromFilesystem.
         */
    public boolean isModelLoadedFromFilesystem() {
	// DEBUG -- isModelLoadedFromFilesystem()
//	System.out.println("isModelLoadedFromFilesystem(): " + modelLoadedFromFilesystem);
	return modelLoadedFromFilesystem;
    }

    /**
         * Set / Reset The modelLoadedFromFilesystem
         * 
         * @param modelLoadedFromFilesystem
         *                boolean - the new condition (true / false).
         */
    public void setModelLoadedFromFilesystem(boolean modelLoadedFromFilesystem) {
	// DEBUG -- setModelLoadedFromFilesystem()
//	System.out.println("setModelLoadedFromFilesystem(): " + modelLoadedFromFilesystem);
	this.modelLoadedFromFilesystem = modelLoadedFromFilesystem;
    }

    /**
     * @return boolean - true if solveMode
     */
    public boolean isSolveMode() {
        return solveMode;
    }

    /**
     * Set / Reset solveMode
     * 
     * @param solveMode boolean - the new condition (true / false).
     */
    public void setSolveMode(boolean solveMode) {
//	{ //DEBUG -- setSolveMode
//	    System.out.println("setSolveMode " + solveMode);
//	}
        this.solveMode = solveMode;
    }
}
