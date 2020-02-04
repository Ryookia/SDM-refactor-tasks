package com.vitting.rcpsudoku.rules.rule4;

import com.vitting.rcpsudoku.jfc.utils.Logger;
import com.vitting.rcpsudoku.model.MCell;

import java.util.BitSet;

/**
 * SCell (SolverCell) holds solver information for a Sudoku cell
 */

final public class SCell {

    private MCell cell;
    private BitSet originalValue;
    private Logger logger;
    private int index;

    public SCell(Logger logger) {
        this.logger = logger;
    }

    /**
     * Constructor
     *
     * @param cell MCell - the original game cell
     */
    SCell(MCell cell) {
        this.cell = cell;
        originalValue = (BitSet) cell.getContent().getValue().clone();
        index = 0;
    }

    /**
     * Try the next possible solution for the cell
     *
     * @return boolean - true if move possible
     */
    public boolean moveForward() {
        int nextTry = originalValue.nextSetBit(index);
        // DEBUG -- Rule 4 moveForward
        logger.logEvent("(" + cell.getRow() + "." + cell.getColumn()
                + ") " + originalValue + " Next bit: " + nextTry);
        if (nextTry == -1) {
            // No more possibilities
            return false;
        }
        cell.setValue(nextTry);
        index = nextTry + 1;
        return true;
    }

    /**
     * restore the cell content to its original value
     */
    public void restore() {
        cell.setValue((BitSet) originalValue.clone());
        index = 0;
    }

    /**
     * @return MCell - the MCell reference
     */
    public MCell getCell() {
        return cell;
    }
}
