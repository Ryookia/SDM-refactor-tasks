package com.vitting.rcpsudoku.jfc.utils;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FieldConfig {
    private LayoutManager layoutManager;
    private LineBorder border;

    public FieldConfig(LayoutManager layoutManager, Color borderColor, int thickness, boolean roundedCorners) {
        this.layoutManager = layoutManager;
        this.border = new LineBorder(borderColor, thickness, roundedCorners);
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public Border getBorder() {
        return border;
    }
}
