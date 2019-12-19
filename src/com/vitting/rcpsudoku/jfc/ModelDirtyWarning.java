/**
 * Copyright (c) 2006 Henning Vitting and others.
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
package com.vitting.rcpsudoku.jfc;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Show a Game not saved MessageBox
 */
public class ModelDirtyWarning {
    
    JFrame window;
    
    public ModelDirtyWarning(JFrame window) {
	this.window = window;
    }
 
    /** Show the warning
     * 
     * @return boolean - true if game should be saved
     */
    public boolean show() {
	// Warning dialog
	int result = JOptionPane.showConfirmDialog(window,
			"Do you want to save it?",
			"Game contains unsaved data", 
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);
	if (result == JOptionPane.YES_OPTION) {
		// User wants to save 
		return true;
	}
	return false;
    }

}
