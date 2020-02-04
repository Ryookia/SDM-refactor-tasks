package com.vitting.rcpsudoku.jfc.runner;

import com.vitting.rcpsudoku.jfc.actions.IAction;
import com.vitting.rcpsudoku.model.SudokuBase;

import javax.swing.*;

class ActionMenuItem extends JMenuItem implements IAction {
    private static final long serialVersionUID = 1L;

    public void controlEnabled() {
        setEnabled(SudokuBase.getSingleInstance().isLoaded());
    }

}