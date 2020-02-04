package com.vitting.rcpsudoku.rules.rule3;

import com.vitting.rcpsudoku.model.MCell;
import com.vitting.rcpsudoku.model.SudokuBase;

class ScanRowDataHolder {

    private SudokuBase base;
    private MCell cell;
    private int firstRow;
    private int firstColumn;

    public ScanRowDataHolder(SudokuBase base, MCell cell, int firstRow, int firstColumn) {
        this.base = base;
        this.cell = cell;
        this.firstRow = firstRow;
        this.firstColumn = firstColumn;
    }

    public SudokuBase getBase() {
        return base;
    }

    public MCell getCell() {
        return cell;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public int getFirstColumn() {
        return firstColumn;
    }
}
