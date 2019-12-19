/**
 * Copyright (c) 2005, 2006 Henning Vitting and others.
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
package com.vitting.rcpsudoku.model;

import java.util.Vector;

public class SudokuException extends Exception {

	private static final long serialVersionUID = 1L;
	
	static final public int SEVERITY_ERROR = 0;
    static final public int SEVERITY_QUESTION = 1;
    static final public int SEVERITY_WARNING = 2;
    static final public int SEVERITY_INFORMATION = 4;
    
    static final public int DISPOSITION_TERMINATE = 0;
    static final public int DISPOSITION_RETRY = 1;
    static final public int DISPOSITION_CONTINUE = 2;
    
    private int disposition = DISPOSITION_TERMINATE;
    private int severity = SEVERITY_ERROR;
	
	private Vector coordinates = null;
	private boolean exceptonHandled = false;

	/**
	 * Constructor for SudokuException 
	 *  
	 * @param message
	 */
	public SudokuException(String message) {
		super(message);
		severity = SEVERITY_INFORMATION;
		disposition = DISPOSITION_CONTINUE;
	}

	/**
	 * Constructor for SudokuException 
	 *  
	 * @param message
     * @param severity
     * @param disposition
	 */
	public SudokuException(String message, int severity, int disposition) {
		super(message);
		this.severity = severity;
		this.disposition = disposition;
	}

	/**
	 * Constructor for SudokuException
	 *  
	 * @param message
	 * @param cause
	 */
	public SudokuException(String message, Throwable cause) {
		super(message, cause);
		severity = SEVERITY_ERROR;
		disposition = DISPOSITION_TERMINATE;
	}

	/**
	 * Constructor for SudokuException
	 *  
	 * @param cause
	 */
	public SudokuException(Throwable cause) {
		super(cause);
		severity = SEVERITY_ERROR;
		disposition = DISPOSITION_TERMINATE;
	}

    /**
     * Constructor for SudokuException
     *  
     * @param message
     * @param cause
     * @param severity
     * @param disposition
     */
    public SudokuException(String message, Throwable cause, int severity, int disposition) {
        super(message, cause);
        this.severity = severity;
        this.disposition = disposition;
    }

    /**
     * Constructor for SudokuException
     * 
     * @param message
     * @param coordinates
     * @param cause
     * @param severity
     * @param disposition
     */
    public SudokuException(String message, Vector coordinates, Throwable cause, int severity, int disposition) {
        super(message, cause);
        this.coordinates = coordinates;
        this.severity = severity;
        this.disposition = disposition;
    }
    
	/**
	 * @return a Vector of coordinates in error
	 */
	public Vector getCoordinates() {
		if (coordinates == null) {
			return new Vector();
		}
		return coordinates;
	}

	/**
	 * @return int - Disposition
	 */
	public int getDisposition() {
		return disposition;
	}

	/**
	 * @return int - Severity
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * @param coordinates
	 */
	public void setCoordinates(Vector coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @param disposition
	 */
	public void setDisposition(int disposition) {
		this.disposition = disposition;
	}

	/**
	 * @param severity
	 */
	public void setSeverity(int severity) {
		this.severity = severity;
	}

	/**
	 * @return
	 */
	public boolean isExceptonHandled() {
		return exceptonHandled;
	}

	/**
	 * @param exceptonHandled
	 */
	public void setExceptonHandled(boolean exceptonHandled) {
		this.exceptonHandled = exceptonHandled;
	}
}
