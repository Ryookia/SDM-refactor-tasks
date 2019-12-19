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
package com.vitting.rcpsudoku.rules;

import com.vitting.rcpsudoku.jfc.utils.Logger;
import com.vitting.rcpsudoku.model.*;
import com.vitting.rcpsudoku.rules.rule4.Rule4;

/**
 * Run the rules in this plugin
 */
public class RuleRunner implements IRuleExtension {

	static private IRule[] rules = new IRule[]{new Rule1(), new Rule2(),
			new Rule3()};

	static SudokuBase base = SudokuBase.getSingleInstance();

	private Logger logger;

	/**
	 * Constructor
	 */
	public RuleRunner(Logger logger) {
		this.logger = logger;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vitting.rcpsudoku.model.IRuleExtension#run()
	 */
	public static int internalrun() throws SudokuException {
		int result = IRule.RULE_NO_CHANGE;
		runrun:
		while (true) {
			for (int i = 0; i < rules.length; i++) {
				result = runRule(i);
//				{ // DEBUG -- Rule 1 - 3 result
//				  System.out.println("Rule " + i + " " + result);
//				}
				switch (result) {
					case IRule.RULE_CELL_CHANGED:
						continue runrun;
					case IRule.RULE_NOT_POSSIBLE:
						break runrun;
					default:
						// No more changes, check for a solution
						if (new VerifyGameComplete().verifyAll() == IRule.RULE_GAME_COMPLETE) {
							result = IRule.RULE_GAME_COMPLETE;
							break runrun;
						}
				}
			}
			break;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vitting.rcpsudoku.model.IRuleExtension#run()
	 */
	public int run() throws SudokuException {
		int result = IRule.RULE_NO_CHANGE;

		try {
			// Clear the stack
			base.clearStack();

			// Set solveMode
			base.setSolveMode(true);

			// Save game onto stack
			base.pushStack();

			// Run rules 1 - 3
			result = internalrun();

			switch (result) {
				case IRule.RULE_NO_CHANGE:
					// Call VerifyGame()
					new VerifyGame().verifyAll(false);

					// Run rule 4
					result = new Rule4(logger).run();
					break;
				case IRule.RULE_CELL_CHANGED:
					throw new SudokuException(
							"RULE_CELL_CHANGED returned from RuleRunner#internalRun");
				case IRule.RULE_NOT_POSSIBLE:
					base.popStack();
					break;
				default:
					// IRule.RULE_GAME_COMPLETE:
			}
			return result;
		} finally {
			base.clearStack();
			// reset solveMode
			base.setSolveMode(false);
//			{ // DEBUG -- RuleRunner#Run returns
//				System.out.println("RuleRunner#Run returns: " + result);
//			}
		}
	}

	/**
	 * Run a rule on all the cells
	 * 
	 * @param i -
	 *            rule number to run
	 * @return int one of {RULE_NO_CHANGE, RULE_CELL_CHANGED,
	 *         RULE_GAME_COMPLETE, RULE_NOT_POSSIBLE}
	 * @throws SudokuException
	 */
	static private int runRule(int i) throws SudokuException {
		int retValue = IRule.RULE_NO_CHANGE;
		IRule rule = rules[i];

		// Call all the cell elements with rule
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				int result = rule.runRule(base.getCell(x, y));
				switch (result) {
				case IRule.RULE_CELL_CHANGED:
					retValue = IRule.RULE_CELL_CHANGED;
					break;
				case IRule.RULE_NOT_POSSIBLE:
					return IRule.RULE_NOT_POSSIBLE;
				default:
					// Do nothing
				}
			}
		}
		return retValue;
	}

}
