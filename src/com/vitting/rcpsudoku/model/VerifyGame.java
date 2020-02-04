/**
 * Copyright (c) 2005, 2006 Henning Vitting and others.
 * All rights reserved.
 * <p>
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Henning Vitting - Initial API and implementation
 */
package com.vitting.rcpsudoku.model;

import java.util.Vector;

/**
 * Verify the MCells with valueFound for validity
 */
public class VerifyGame {

    private boolean solutionPossible = false;

    private boolean gameComplete = false;

    private MCell fail;

    /**
     * Verify the validity of the cell
     *
     * @return boolean - true if the cell is valid
     */
    public boolean verifyCell(MCell cell) {
        SudokuBase base = SudokuBase.getSingleInstance();
        fail = null;

        // Check for empty cells
        if (cell.getValue().cardinality() == 0) {
            solutionPossible = false;
        }

        // Only verify cells with a value, if no value game cannot be complete
        if (!cell.isValueFound()) {
            gameComplete = false;
            return true;
        }
        int cellValue = cell.getIntValue();

        return (isBlockPossible(base, cell, cellValue)
                && isRowPossible(base, cell, cellValue)
                && isColumnPossible(base, cell, cellValue));
    }

    /**
     * @return MCell the cell causing the verify error
     */
    public MCell getFail() {
        return fail;
    }

    /**
     * Verify all the cells <br>
     * Verify is reset to its initial state before verifyAll
     *
     * @param silent boolean - don't rapport an exception if true
     * @return int one of {RULE_NO_CHANGE, RULE_GAME_COMPLETE,
     * RULE_NOT_POSSIBLE}
     * @throws SudokuException -
     *                         only if silent = false, exception will contaim
     *                         failing coordinates
     */
    public int verifyAll(boolean silent) throws SudokuException {
        SudokuBase base = SudokuBase.getSingleInstance();
        solutionPossible = true;
        gameComplete = true;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                MCell cell = base.getCell(x, y);
                if (!verifyCell(cell)) {
                    if (silent) {
                        solutionPossible = false;
                        return IRule.RULE_NOT_POSSIBLE;
                    }
                    // Stop on first error
                    throwError(cell);
                }
            }
        }

        if (gameComplete) {
            return IRule.RULE_GAME_COMPLETE;
        } else {
            return IRule.RULE_NO_CHANGE;
        }
    }

    private void throwError(MCell cell) throws SudokuException {
        Vector<Coordinate> coordinates = new Vector<>();
        coordinates.addElement(cell.getCoordinate());
        coordinates.addElement(getFail().getCoordinate());
        throw new SudokuException("Verify failed", coordinates,
                null, SudokuException.SEVERITY_INFORMATION,
                SudokuException.DISPOSITION_RETRY);
    }

    // Check the cells in the same row
    private boolean isRowPossible(SudokuBase base, MCell cell, int cellValue) {
        int row = cell.getRow();
        for (int i = 0; i < 9; i++) {
            if (base.getCell(row, i) == cell) {
                // dont check self
                continue;
            }
            if (base.getCell(row, i).isValueFound()) {
                if (base.getCell(row, i).getIntValue() == cellValue) {
                    // This is an error
                    fail = base.getCell(row, i);
                    return false;
                }
            }
        }
        return true;
    }

    // Check the cells in same column
    private boolean isColumnPossible(SudokuBase base, MCell cell, int cellValue) {
        int column = cell.getColumn();
        for (int i = 0; i < 9; i++) {
            if (base.getCell(i, column) == cell) {
                // dont check self
                continue;
            }
            if (base.getCell(i, column).isValueFound()) {
                if (base.getCell(i, column).getIntValue() == cellValue) {
                    // This is an error
                    fail = base.getCell(i, column);
                    return false;
                }
            }
        }
        return true;
    }

    // Check the cells in the same block
    private boolean isBlockPossible(SudokuBase base, MCell cell, int cellValue) {
        MCell[] blockCells = base.getBlocks().getCells(cell.getBlock());
        for (int i = 0; i < 9; i++) {
            if (blockCells[i] == cell) {
                // dont check self
                continue;
            }
            if (blockCells[i].isValueFound()) {
                if (blockCells[i].getIntValue() == cellValue) {
                    // This is an error
                    fail = blockCells[i];
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return true is a solution is possible
     */
    public boolean isSolutionPossible() {
        return solutionPossible;
    }
}
