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

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.vitting.rcpsudoku.model.SudokuException;

/**
 *
 * GUI Persistence for the SudoKu Application
 */
public class GuiPersistence {

	private static final long serialVersionUID = 1L;
	static final private Dimension defaultWindowSize = new Dimension(796, 764);
    static final private String PROPERTY_FILE_NAME = "RcpSudoku.pro"; 
    static final private String SAVE_FILE = "saveFile";
    static final private String WINDOW_WIDTH = "windowWidth";
    static final private String WINDOW_HEIGHT = "windowHeight";
    private Properties defaultProperties;
    private Properties currentProperties;
    private boolean dirty = true;

		
    /**
     *  GuiPersistence constructor
     */
	public GuiPersistence() {
		super();
        defaultProperties = new Properties();
        currentProperties = new Properties(defaultProperties);
        
        // Set the defaults
        defaultProperties.setProperty(WINDOW_WIDTH, String.valueOf(new Double(defaultWindowSize.getHeight()).intValue()));
        defaultProperties.setProperty(WINDOW_HEIGHT, String.valueOf(new Double(defaultWindowSize.getWidth()).intValue()));
        defaultProperties.setProperty(SAVE_FILE, "");
        restore();
	}
	
    /**
     * Save the Properties to the persistent file system if necessary
     * 
     * @throws SudokuException
     */
    public void save() throws IOException {
        if (!dirty) {
            return;
        }
        dirty = false;
        
        File out = new File(PROPERTY_FILE_NAME);
        FileOutputStream fout = new FileOutputStream(out);
        String header = new String("RcpSudoku GUI Properties");
        currentProperties.store(fout, header);
     }
    
    /**
     * Restore the property from the persistent file system
     */
    public void restore(){ 
        try {
            File in = new File(PROPERTY_FILE_NAME);
            if (in.exists()) {
            	FileInputStream fin = new FileInputStream(in);
            	currentProperties.load(fin);
            	dirty = false;
            } else {
            	reset();
            }
        } catch (IOException ignore) {
            // Ignore the exception
        }
    }

    /**
     * Reset the persistence to defaults
     *
     * @throws IOException
     */
    public void reset() throws IOException {
        currentProperties = new Properties(defaultProperties);
        dirty = true;
        save();
    }

	/**
	 * @return File To use 
	 */
	public File getSaveFile() {
        return new File (currentProperties.getProperty(SAVE_FILE));
	}

	/**
	 * @return Dimension - the window size
	 */
	public Dimension getWindowSize() {
		Dimension dimension = new Dimension(defaultWindowSize);
		try {
			dimension.setSize(Integer.parseInt(currentProperties.getProperty(WINDOW_WIDTH)),
					Integer.parseInt(currentProperties.getProperty(WINDOW_HEIGHT)));
		} catch (NumberFormatException forgetIt) {
			// Use the default if an Exception here
		}
		return dimension;
	}

	/**
	 * @param saveFile File To use 
	 */
	public void setSaveFile(File saveFile) {
        currentProperties.setProperty(SAVE_FILE, saveFile.getAbsolutePath());
        dirty = true;
	}

	/**
	 * @param newSize - Dimension to save
	 */
	public void setWindowSize(Dimension newSize) {
		currentProperties.setProperty(WINDOW_WIDTH, String.valueOf(new Double(newSize.getHeight()).intValue()));
	    currentProperties.setProperty(WINDOW_HEIGHT, String.valueOf(new Double(newSize.getWidth()).intValue()));
        dirty = true;
	}
}
