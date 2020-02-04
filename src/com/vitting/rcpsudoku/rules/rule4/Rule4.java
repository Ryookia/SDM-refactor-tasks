/**
 * Copyright (c) 2006 Henning Vitting and others.
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
//TASKS
package com.vitting.rcpsudoku.rules.rule4;

import com.vitting.rcpsudoku.jfc.utils.Logger;
import com.vitting.rcpsudoku.model.MCell;
import com.vitting.rcpsudoku.model.SudokuBase;
import com.vitting.rcpsudoku.model.SudokuException;

import java.util.Vector;

/**
 * Rule 4, Some Sudoku games, normally catagorized as extremly difficult, cannot
 * be solved using the simple rules. Rule 4 uses an Ariadne's thread like
 * algoritm to traverse the remaining unsolved cells. <br>
 * For each step in the thread, rules 1 - 3 are used again.
 */
final public class Rule4 {

    private Logger logger;

    public Rule4(Logger logger) {
        this.logger = logger;
    }

    static SudokuBase base = SudokuBase.getSingleInstance();

    public int run() throws SudokuException {
        Vector<MCell> unsolvedList = new Vector<>(); // A Vector of unsolved MCells

        // Build the unsolved Vector
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                MCell cell = base.getCell(x, y);
                if (!cell.isValueFound()) {
                    unsolvedList.add(cell);
                }
            }
        }
        //DEBUG -- Running Rule 4
        logger.logEvent("Running Rule 4, unsolved size: " + unsolvedList.size());

        // Create the first Rule4Step and run it
        SCell[] sCells = new SCell[unsolvedList.size()];
        for (int i = 0; i < sCells.length; i++) {
            sCells[i] = new SCell((MCell) unsolvedList.elementAt(i));
        }

        Rule4Step step = new Rule4Step(sCells);
        int result = step.runStep();
        //DEBUG -- Rule4Step returned
        logger.logEvent("Rule4Step returned: " + result);
        return result;
    }
}
