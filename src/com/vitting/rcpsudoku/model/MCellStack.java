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
package com.vitting.rcpsudoku.model;

import java.util.Vector;

/**
 * 
 * Push/Pop stack for MCell content
 * 
 */
public class MCellStack {

    private Vector cellStack = new Vector();

    /**
         * Clear the stack
         */
    public void clear() {
//	{ // DEBUG -- Clearing stack
//	    System.out.println("Clearing stack");
//	}
	cellStack.clear();
    }

    /**
         * Push the game data onto the stack
         * 
         * @param cells
         *                MCell - MCells cotaining tata
         * @return int - the number of saved games on stack
         */
    public int pushGame(MCell[][] cells) {
//	{ // DEBUG -- pushing game onto stack
//	    System.out.println("pushing game onto stack");
//	}
	MCellContent[][] stackElement = new MCellContent[9][9];
	cellStack.add(stackElement);

	for (int x = 0; x < 9; x++) {
	    for (int y = 0; y < 9; y++) {
		stackElement[x][y] = new MCellContent(cells[x][y].getContent());
	    }
	}
//	{ // DEBUG -- Stack size
//	    System.out.println("Stack size: " + cellStack.size());
//	}
	return cellStack.size();
    }

    /**
         * Pop the game data from the stack <br>
         * Stack empty == NOOP
         * 
         * @param cells
         *                MCell - MCells cotaining tata
         * @return int - the number of remaining games on stack
         */
    public int popGame(MCell[][] cells) {
//	{ // DEBUG -- Restoring game from Stack
//	    System.out.println("Restoring game from Stack");
//	}
	if (cellStack.size() > 0) {
	    MCellContent[][] stackElement = (MCellContent[][]) cellStack
		    .lastElement();
	    cellStack.removeElement(cellStack.lastElement());
	    for (int x = 0; x < 9; x++) {
		for (int y = 0; y < 9; y++) {
		    cells[x][y].setValue(stackElement[x][y].getValue());
		}
	    }
	}
//	{ // DEBUG -- Stack size
//	    System.out.println("Stack size: " + cellStack.size());
//	}
	return cellStack.size();
    }
}
