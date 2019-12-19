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
package com.vitting.rcpsudoku.jfc.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import com.vitting.rcpsudoku.jfc.MainWindow;
import com.vitting.rcpsudoku.jfc.ModelDirtyWarning;
import com.vitting.rcpsudoku.model.SudokuBase;
import com.vitting.rcpsudoku.model.SudokuDocument;
import com.vitting.rcpsudoku.model.SudokuException;



/**
 *
 * Handle Load Action
 */
public class LoadAction extends JMenuItem implements ActionListener, IAction{

	private static final long serialVersionUID = 1L;
	private SudokuBase base = SudokuBase.getSingleInstance();
	MainWindow window;
	
	/**
	 * Constructor
	 * 
	 * @param window MainWindow 
	 */
	public LoadAction(MainWindow window) {
		this.window = window;
		setText("Load");
		addActionListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		window.clearError();
	    	// Check for unsaved data
	    	if (base.isModelDirty()) {
	    	    if (new ModelDirtyWarning(window).show()) {
	    		boolean result = new SaveAction(window).save();
	    		if (result == false) {
	    		    return;
	    		}
	    	    }
	    	}

	    	File oldFile = window.persistence.getSaveFile();
		String directory = oldFile.getParent();
		if (directory == null) {
			directory = "";
		}
		JFileChooser fileChooser = new JFileChooser(directory);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (oldFile.exists()) {
			fileChooser.setSelectedFile(window.persistence
					.getSaveFile());
		}
		fileChooser.setFileFilter(new FileFilter() {

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String s = f.getName();
				int i = s.lastIndexOf('.');

				if (i > 0 && i < s.length() - 1) {
					if (s.substring(i + 1).equalsIgnoreCase("sud")) {
						return true;
					}
				}
				return false;
			}

			public String getDescription() {
				return "Sudoku saved game";
			}

		});
		int retcode = fileChooser.showOpenDialog(window);
		if (retcode != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File savefile = fileChooser.getSelectedFile();
		window.persistence.setSaveFile(savefile);

		// Set the window tittle
		String shortname = savefile.getName().substring(0, savefile.getName().length() - 4);					
		window.setTitle(MainWindow.windowTitle + " - " + shortname);
		try {
			window.persistence.save();
		} catch (IOException e2) {
			window.setMessage(new SudokuException(
					"Failed to load game", e2,
				SudokuException.SEVERITY_ERROR,
				SudokuException.DISPOSITION_RETRY));

			return;
		}
		SudokuDocument persistence = new SudokuDocument(savefile);
		window.setEditMode(false);
		try {
			persistence.load(base);
			base.setModelDirty(false);
			base.setModelLoadedFromFilesystem(true);
		} catch (SudokuException e1) {
			window.setMessage("Failed to load game", e1);
			return;
		}
		window.setMessage("Game " + shortname + " loaded");
		window.refresh();
	}
	
	/* (non-Javadoc)
	 * @see com.vitting.rcpsudoku.jfc.actions.IAction#controlEnabled()
	 */
	public void controlEnabled() {
		// The menu is always enabled
		setEnabled(true);
	}

}
