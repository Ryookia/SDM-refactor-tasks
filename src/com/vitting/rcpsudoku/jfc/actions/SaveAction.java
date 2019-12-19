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
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.vitting.rcpsudoku.jfc.MainWindow;
import com.vitting.rcpsudoku.model.SudokuBase;
import com.vitting.rcpsudoku.model.SudokuDocument;
import com.vitting.rcpsudoku.model.SudokuException;



/**
 *
 * Handle Save Action
 */
public class SaveAction extends JMenuItem implements ActionListener, IAction{

	private static final long serialVersionUID = 1L;
	private SudokuBase base = SudokuBase.getSingleInstance();
	MainWindow window;
	
	/**
	 * Constructor
	 * 
	 * @param window MainWindow 
	 */
	public SaveAction(MainWindow window) {
		this.window = window;
		setText("Save");
		addActionListener(this);
	}

	/**
	 * Save the game
	 * 
	 * @return boolean - true if the game was saved
	 */
	public boolean save() {
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
		
		int retcode = fileChooser.showSaveDialog(window);
		if (retcode != JFileChooser.APPROVE_OPTION) {
			return false;
		}

		// User has selected to save, find the correct filename and extension
		File savefile = fileChooser.getSelectedFile();
		String savefileName = savefile.getName();
		int i = savefileName.lastIndexOf('.');
		savefileName = savefile.getAbsolutePath();
		if (i == -1) {
			// no ext
			savefileName = savefileName.concat(".sud");
		} else {
			i = savefileName.lastIndexOf('.');
			savefileName = savefileName.substring(0, i) + ".sud";
		}
		File saveFile = new File(savefileName);

		// Check for existing file
		if (saveFile.exists()) {
			// Warning dialog
			int result = JOptionPane.showConfirmDialog(window,
					"Save it anyway?",
					"File already exists", 
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (result != JOptionPane.YES_OPTION) {
				// User rejected
				return false;
			}
		}
		
		SudokuDocument persistence = new SudokuDocument(new File(
				savefileName));
		try {
			persistence.save(base);
		} catch (SudokuException e1) {
			window.setMessage(e1);
			return false;
		}
		window.persistence.setSaveFile(saveFile);
		// Set the window tittle
		String shortname = savefile.getName().substring(0, savefile.getName().length() - 4);					
		window.setTitle(MainWindow.windowTitle + " - " + shortname);
		try {
			window.persistence.save();
		} catch (IOException e2) {
			window.setMessage(new SudokuException(
					"Failed to save game", e2,
				SudokuException.SEVERITY_ERROR,
				SudokuException.DISPOSITION_RETRY));
			return false;
		}
		window.setMessage("Game " + shortname + " saved");
		base.setModelDirty(false);
		base.setModelLoadedFromFilesystem(true);
		window.refresh();
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	    save();
	}
	
	/* (non-Javadoc)
	 * @see com.vitting.rcpsudoku.jfc.actions.IAction#controlEnabled()
	 */
	public void controlEnabled() {
		// The menu is enabled when the model is loaded
		setEnabled(base.isLoaded());
	}
}
