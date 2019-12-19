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

/**
 * ImodelListener Interface
 * 
 */
public interface IModelListener {
	/**
	 * Indicate model has changed

	 * @param refresh boolean - refresh requested if true 
	 */
	public void modelChanged(boolean refresh);
	

}
