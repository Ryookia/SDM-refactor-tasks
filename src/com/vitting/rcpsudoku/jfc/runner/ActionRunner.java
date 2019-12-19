/*
 * Copyright (c) 2005 Henning Vitting and others.
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
package com.vitting.rcpsudoku.jfc.runner;

import com.vitting.rcpsudoku.jfc.CellElement;
import com.vitting.rcpsudoku.jfc.MainWindow;
import com.vitting.rcpsudoku.jfc.utils.Logger;
import com.vitting.rcpsudoku.model.IRule;
import com.vitting.rcpsudoku.model.SudokuException;
import com.vitting.rcpsudoku.model.VerifyGame;
import com.vitting.rcpsudoku.model.VerifyGameComplete;
import com.vitting.rcpsudoku.rules.RuleRunner;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ActionRunner extends JMenu {
    private static final long serialVersionUID = 1L;
    public static final String MESSAGE_NO_SOLUTION = "No possible solution";
    public static final String MESSAGE_NO_SOLUTION_WITH_HINT = "No solution found, try manual input and run solve again";
    public static final String MESSAGE_OK = "Ok so far";
    public static final String MESSAGE_BLANK = "";
    private static final String TEXT_ACTION = "action";
    private static final String TEXT_SOLVE = "action";
    private static final String TEXT_VERIFY = "action";
    private Logger logger;

    /**
     * Controle execution of rules, it creates and handles the Action menu's
     */

    public ActionRunner(Logger logger) {
        this.logger = logger;
    }

    private MainWindow mainWindow;

    private CellElement[][] cellElements;

    /**
     * Construct the ActionRunner
     *
     * @param mainWindow Window - Owning window
     * @param cellTable  CellElement[9][9] - Cell elements in a 9 by 9 array(
     *                   00 - 88)
     */
    public ActionRunner(MainWindow mainWindow, CellElement[][] cellTable) {
        this.mainWindow = mainWindow;
        this.cellElements = cellTable;
        createActionMenu();
    }

    /**
     * Create the actionMenu
     */
    private void createActionMenu() {
        setText(TEXT_ACTION);

        // Solve
        ActionMenuItem solveAction = new ActionMenuItem();
        add(solveAction);
        solveAction.setText(TEXT_SOLVE);
        solveAction.addActionListener(generateSolveActionListener());

        // run Verify
        ActionMenuItem verifyAction = new ActionMenuItem();
        add(verifyAction);
        verifyAction.setText(TEXT_VERIFY);
        verifyAction.addActionListener(generateVerifyActionListener());
    }

    private ActionListener generateSolveActionListener() {
        return actionEvent -> {
            VerifyGame verify = new VerifyGame();
            try {

                // Clear any messages
                mainWindow.setMessage(MESSAGE_BLANK); // Clear the message field
                // Check game for validity, in verbose mode
                if (verify.verifyAll(false) == IRule.RULE_GAME_COMPLETE) {
                    // Game already complete,do not run
                    mainWindow.gameComplete();
                    return;
                }

                RuleRunner runner = new RuleRunner(logger);
                int result = runner.run();
                // Refresh the cells
                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        cellElements[x][y].refresh();
                    }
                }
                // Do nothing
                if (result == IRule.RULE_NOT_POSSIBLE) {
                    mainWindow.setMessage(MESSAGE_NO_SOLUTION);
                    return;
                }

                switch (verify.verifyAll(true)) {
                    case IRule.RULE_GAME_COMPLETE:
                        mainWindow.gameComplete();
                        break;
                    case IRule.RULE_NOT_POSSIBLE:
                        mainWindow.setMessage(MESSAGE_NO_SOLUTION);
                        break;
                    default:
                        // IRule.RULE_NO_CHANGE
                        mainWindow
                                .setMessage(MESSAGE_NO_SOLUTION_WITH_HINT);
                }
            } catch (SudokuException ex) {
                mainWindow.setMessage(ex);
            }
        };
    }

    private ActionListener generateVerifyActionListener() {
        return actionEvent -> {
            VerifyGameComplete verifyGameComplete = new VerifyGameComplete();
            mainWindow.setMessage(""); // Clear the message field
            // Call VerifyGame()
            try {
                VerifyGame verify = new VerifyGame();
                verify.verifyAll(false);
                boolean result = verifyGameComplete.verifyAll() == IRule.RULE_GAME_COMPLETE;
                if (result) {
                    mainWindow.gameComplete();
                } else {
                    mainWindow.setMessage(MESSAGE_OK);
                }
            } catch (SudokuException e1) {
                mainWindow.setMessage(e1);
            }
        };
    }
}
