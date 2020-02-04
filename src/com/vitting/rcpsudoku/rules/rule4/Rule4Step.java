package com.vitting.rcpsudoku.rules.rule4;

import com.vitting.rcpsudoku.jfc.utils.Logger;
import com.vitting.rcpsudoku.model.IRule;
import com.vitting.rcpsudoku.model.SudokuException;
import com.vitting.rcpsudoku.rules.RuleRunner;

final public class Rule4Step {

    private Logger logger;
    // The unsolved cells remaining for this step
    private SCell[] cells;
    // The first cell
    private SCell cell;

    public Rule4Step(Logger logger) {
        this.logger = logger;
    }

    public Rule4Step(SCell[] cells) {
        this.cells = cells;
        // This step handles the first SCell in the array
        cell = cells[0];
    }

    public int runStep() throws SudokuException {
        // SCell move forward inserts the value in the MCell
        while (cell.moveForward()) {

            // Test with rules 1 - 3
            int result = RuleRunner.internalrun();
            switch (result) {
                case IRule.RULE_GAME_COMPLETE:
                    //DEBUG -- Rule4 GAME_COMPLETE
                    logger.logEvent("Rule4Test result: RULE_GAME_COMPLETE");
                    return result;
                case IRule.RULE_NOT_POSSIBLE:
                    //DEBUG -- Rule4 NOT_POSSIBLE
                    logger.logEvent("     Rule4Test result: RULE_NOT_POSSIBLE");
                    // The previous run had no possible result, restore the cells
                    // to cleanup the partial results from the run
                    restoreCells();
                    break;
                case IRule.RULE_NO_CHANGE:
                    //DEBUG -- Rule4 NO_CHANGE
                    logger.logEvent("Rule4Test result: RULE_NO_CHANGE");
                    int stepResult = handleNoChange();
                    switch (stepResult) {
                        case IRule.RULE_GAME_COMPLETE:
                            return stepResult;
                        case IRule.RULE_NOT_POSSIBLE:
                            break;
                        case IRule.RULE_NO_CHANGE:
                    }
                    // Nothing so far continue
            }
        }

        // No result found, restore the cell
        cell.restore();
        return IRule.RULE_NOT_POSSIBLE;
    }

    private void restoreCells() {
        for (int j = 1; j < cells.length; j++) {
            cells[j].restore();
        }
    }

    private int handleNoChange() throws SudokuException {
        if (cells.length > 1) {
            // Call next rule4step for the next cell in the
            // unsolved array
            int stepResult = pickUpResultsAndRun();
            // debug
            logger.logEvent("Nested step returned: " + stepResult);
            return stepResult;
        }
        return IRule.RULE_NO_CHANGE;
    }

    private int pickUpResultsAndRun() throws SudokuException {
        SCell[] sCells = new SCell[cells.length - 1];
        for (int j = 1; j < cells.length; j++) {
            // Pick up the intermediate results
            sCells[j - 1] = new SCell(cells[j].getCell());
        }
        return new Rule4Step(sCells).runStep();
    }
}