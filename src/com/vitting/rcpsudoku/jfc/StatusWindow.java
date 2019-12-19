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
package com.vitting.rcpsudoku.jfc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Show Status Text Window
 */
public class StatusWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextArea jTextArea;
	
	private MainWindow mainWindow;

	/**
	 * Window constructor
	 * 
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public StatusWindow(MainWindow owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		mainWindow = owner; 
		initGUI();
	}

	/**
	 * Initialize The gui
	 */
	private void initGUI() {

		try {
			// Get the size of the screen
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

			// Determine the location of the window
			setSize(mainWindow.getSize());
			int ww = this.getSize().width;
			int hh = this.getSize().height;
			int xx = (dim.width - ww) / 2;
			int yy = (dim.height - hh) / 2;

			// Move the window center to the screen
			this.setLocation(xx, yy);

			JScrollPane jScrollPane = new JScrollPane();
			this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
			jScrollPane.setAutoscrolls(true);
			{
				jTextArea = new JTextArea();
				jScrollPane.setViewportView(jTextArea);
				jTextArea
						.setText("*******************************************");
				jTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10,
						10, 10));
				jTextArea.setEditable(false);
				jTextArea.setFocusable(false);
				jTextArea.setBackground(new java.awt.Color(255,255,128));				
			}
		} catch (Exception e) {
			// LATER Implement error logging
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the message text
	 * 
	 * @param text String - The message
	 */
	public void setText(String text) {
		jTextArea.setText(text);
	}
}
