package com.vitting.rcpsudoku.jfc.utils;

import com.vitting.rcpsudoku.jfc.CellElement;

import java.util.ArrayList;
import java.util.List;

public class CellElementLayout {
    public static List<List<CellElement>> getFieldBlockLayoutList(CellElement[][] cellElements) {
        ArrayList<List<CellElement>> result = new ArrayList<>();
        for (int i = 0; i <= 9; i = i + 3) {
            for (int j = 0; j <= 9; j = j + 3) {
                result.add(generateBlock(i, j, cellElements));
            }
        }
        return result;
    }

    private static List<CellElement> generateBlock(int outsideLoop, int innerLoop, CellElement[][] cellElements) {
        ArrayList<CellElement> result = new ArrayList<>();
        for (int i = outsideLoop; i <= outsideLoop + 3; i++) {
            for (int j = innerLoop; j <= innerLoop; j++) {
                result.add(cellElements[i][j]);
            }
        }
        return result;
    }
}
