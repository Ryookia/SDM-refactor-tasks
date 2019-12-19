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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.vitting.rcpsudoku.jfc.actions.ClearAction;
import com.vitting.rcpsudoku.jfc.actions.IAction;
import com.vitting.rcpsudoku.jfc.actions.LoadAction;
import com.vitting.rcpsudoku.jfc.actions.ModeEditAction;
import com.vitting.rcpsudoku.jfc.actions.NewAction;
import com.vitting.rcpsudoku.jfc.actions.SaveAction;
// import com.vitting.rcpsudoku.jfc.test.Test;
import com.vitting.rcpsudoku.model.*;

/**
 * 
 * Main window for the SuDoCu Application
 */
public class MainWindow extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    public static String windowTitle = "RcpSudoku";

    private MainWindow mainWindow = null;

    public CellElement[][] cellElements = new CellElement[9][9];

    public ActionRunner actionRunner = null;

    private PrintWriter myOut = null;

    private static final int NORMAL = 0;

    private static final int ERROR = 1;

    private static final int COMPLETE = 2;

    private static final java.awt.Color normalBackgroundColor = new java.awt.Color(255, 255, 255);

    private static final java.awt.Color normalForegroundColor = new java.awt.Color(0, 0, 0);

    private static final java.awt.Color completeBackgroundColor = new java.awt.Color(0, 255, 0);

    private static final java.awt.Color completeForegroundColor = new java.awt.Color(0, 0, 0);

    private static final java.awt.Color errorBackgroundColor = new java.awt.Color(255, 0, 0);

    private static final java.awt.Color errorForegroundColor = new java.awt.Color(255, 255, 255);

    private JPanel cellPanel;

    private JTextField messageField;

    private JButton messageButton;

    private JMenu modeMenu;

    private SudokuException error = null;

    private boolean editMode = true;

    public GuiPersistence persistence = null;
    
    private SudokuBase base = null;

    /**
         * Launch the SuDoKu application by creting the main window. <br>
         * Exit the application when the window closes
         */
    public static void main(String[] args) {
	final MainWindow mainWindow = new MainWindow();
	mainWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	mainWindow.addWindowListener(new WindowAdapter() {

	    public void windowClosing(WindowEvent e) {
		mainWindow.closeApplication();
	    }
	});
	mainWindow.setVisible(true);
    }

    /**
         * Construct the main window
         */
    public MainWindow() {
	super();
	mainWindow = this;
	
	// Create data model
	base = SudokuBase.getSingleInstance();

	try {
	    // myOut = new PrintWriter("RcpSudoku.log");
	    // Fix for running under 1.1.4
	    myOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("RcpSudoku.log"))));
	} catch (FileNotFoundException e1) {
	    e1.printStackTrace();
	    System.exit(255);
	}

	// Load the GUI persistence
	persistence = new GuiPersistence();

	// Create the GUI
	initGui();

	// Add a component listener
	this.addComponentListener(new ComponentListener() {

	    public void componentResized(ComponentEvent e) {
		if (mainWindow.getExtendedState() == NORMAL) {
		    persistence.setWindowSize(mainWindow.getSize());
		}
	    }

	    public void componentMoved(ComponentEvent e) {
	    }

	    public void componentShown(ComponentEvent e) {
	    }

	    public void componentHidden(ComponentEvent e) {
	    }
	});
    }

    /**
         * Initialize The gui </br> Create The Menus and Layouts
         */
    private void initGui() {
	try {
	    BorderLayout thisLayout = new BorderLayout();
	    this.getContentPane().setLayout(thisLayout);
	    cellPanel = new JPanel();
	    GridLayout cellLayout = new GridLayout(3, 3);
	    cellLayout.setColumns(3);
	    cellLayout.setRows(3);
	    cellPanel.setLayout(cellLayout);
	    this.getContentPane().add(cellPanel, BorderLayout.CENTER);
	    this.setSize(persistence.getWindowSize());
	    this.setTitle(windowTitle);

	    // Set icon if possible, else forget about it
	    Image icon = null;

	    // Get current classloader
	    ClassLoader cl = this.getClass().getClassLoader();
	    if (cl != null) {
		try {
		    ImageIcon ic = new ImageIcon(cl.getResource("images/WindowIcon.gif"));
		    icon = ic.getImage();
		} catch (Exception ignore) {
		}
	    }

	    if (icon == null) {
		// We are probably not loading from a JAR file, try the toolkit
		icon = Toolkit.getDefaultToolkit().createImage("images/WindowIcon.gif");
	    }

	    if (icon != null) {
		this.setIconImage(icon);
	    }

	    // Get the size of the screen
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	    // Determine the location of the window
	    int ww = this.getSize().width;
	    int hh = this.getSize().height;
	    int xx = (dim.width - ww) / 2;
	    int yy = (dim.height - hh) / 2;

	    // Move the window center to the screen
	    this.setLocation(xx, yy);

	    // Create the Menus
	    JMenuBar jMenuBar = new JMenuBar();
	    setJMenuBar(jMenuBar);
	    JMenu fileMenu = new JMenu();
	    jMenuBar.add(fileMenu);
	    fileMenu.setText("Game");
	    fileMenu.addMenuListener(new MenuListener() {

		public void menuCanceled(MenuEvent e) {
		    // Not used
		}

		public void menuDeselected(MenuEvent e) {
		    // Not used
		}

		public void menuSelected(MenuEvent e) {
		    if (e.getSource() instanceof JMenu) {
			JMenu menu = (JMenu) e.getSource();
			for (int i = 0; i < menu.getItemCount(); i++) {
			    JMenuItem menuItem = menu.getItem(i);
			    if (menuItem instanceof IAction) {
				((IAction) menuItem).controlEnabled();
			    }
			}
		    }
		}
	    });

	    // new
	    NewAction newMenuItem = new NewAction(this);
	    fileMenu.add(newMenuItem);

	    // Separator
	    fileMenu.add(new JSeparator());

	    // clear
	    ClearAction clearMenuItem = new ClearAction(this);
	    fileMenu.add(clearMenuItem);

	    // Separator
	    fileMenu.add(new JSeparator());

	    // Save
	    SaveAction saveMenuItem = new SaveAction(this);
	    fileMenu.add(saveMenuItem);

	    // Load
	    LoadAction loadMenuItem = new LoadAction(this);
	    fileMenu.add(loadMenuItem);

	    // Separator
	    fileMenu.add(new JSeparator());

	    // Exit
	    JMenuItem exitMenuItem = new JMenuItem();
	    fileMenu.add(exitMenuItem);
	    exitMenuItem.setText("Exit");
	    exitMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		    mainWindow.closeApplication();
		}
	    });

	    // Mode
	    modeMenu = new JMenu();
	    jMenuBar.add(modeMenu);
	    modeMenu.setText("Mode");
	    modeMenu.addMenuListener(new MenuListener() {

		public void menuCanceled(MenuEvent e) {
		    // Not used
		}

		public void menuDeselected(MenuEvent e) {
		    // Not used
		}

		public void menuSelected(MenuEvent e) {
		    if (e.getSource() instanceof JMenu) {
			JMenu menu = (JMenu) e.getSource();
			for (int i = 0; i < menu.getItemCount(); i++) {
			    JMenuItem menuItem = menu.getItem(i);
			    if (menuItem instanceof IAction) {
				((IAction) menuItem).controlEnabled();
			    }
			}
		    }
		}
	    });

	    // modeEdit
	    ModeEditAction modeEdit = new ModeEditAction(this);
	    modeMenu.add(modeEdit);

	    // Action
	    actionRunner = new ActionRunner(this, cellElements);
	    jMenuBar.add(actionRunner);
	    actionRunner.addMenuListener(new MenuListener() {

		public void menuCanceled(MenuEvent e) {
		    // Not used
		}

		public void menuDeselected(MenuEvent e) {
		    // Not used
		}

		public void menuSelected(MenuEvent e) {
		    if (e.getSource() instanceof JMenu) {
			JMenu menu = (JMenu) e.getSource();
			for (int i = 0; i < menu.getItemCount(); i++) {
			    JMenuItem menuItem = menu.getItem(i);
			    if (menuItem instanceof IAction) {
				((IAction) menuItem).controlEnabled();
			    }
			}
		    }
		}
	    });

	    // { // DEBUG -- Test Menu
	    // JMenu testMenu = new Test(this);
	    // jMenuBar.add(testMenu);
	    // }

	    // Fill the cell element Array, this must be done here
	    for (int x = 0; x < 9; x++) {
		for (int y = 0; y < 9; y++) {
		    cellElements[x][y] = new CellElement(x, y, this);
		    cellElements[x][y].refresh();
		}
	    }

	    // Construct the 9 fields
	    JPanel field1 = new JPanel();
	    field1.setLayout(new GridLayout(3, 3));
	    field1.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field1.add(cellElements[0][0]);
	    field1.add(cellElements[0][1]);
	    field1.add(cellElements[0][2]);
	    field1.add(cellElements[1][0]);
	    field1.add(cellElements[1][1]);
	    field1.add(cellElements[1][2]);
	    field1.add(cellElements[2][0]);
	    field1.add(cellElements[2][1]);
	    field1.add(cellElements[2][2]);
	    cellPanel.add(field1);
	    JPanel field2 = new JPanel();
	    field2.setLayout(new GridLayout(3, 3));
	    field2.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field2.add(cellElements[0][3]);
	    field2.add(cellElements[0][4]);
	    field2.add(cellElements[0][5]);
	    field2.add(cellElements[1][3]);
	    field2.add(cellElements[1][4]);
	    field2.add(cellElements[1][5]);
	    field2.add(cellElements[2][3]);
	    field2.add(cellElements[2][4]);
	    field2.add(cellElements[2][5]);
	    cellPanel.add(field2);
	    JPanel field3 = new JPanel();
	    field3.setLayout(new GridLayout(3, 3));
	    field3.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field3.add(cellElements[0][6]);
	    field3.add(cellElements[0][7]);
	    field3.add(cellElements[0][8]);
	    field3.add(cellElements[1][6]);
	    field3.add(cellElements[1][7]);
	    field3.add(cellElements[1][8]);
	    field3.add(cellElements[2][6]);
	    field3.add(cellElements[2][7]);
	    field3.add(cellElements[2][8]);
	    cellPanel.add(field3);
	    JPanel field4 = new JPanel();
	    field4.setLayout(new GridLayout(3, 3));
	    field4.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field4.add(cellElements[3][0]);
	    field4.add(cellElements[3][1]);
	    field4.add(cellElements[3][2]);
	    field4.add(cellElements[4][0]);
	    field4.add(cellElements[4][1]);
	    field4.add(cellElements[4][2]);
	    field4.add(cellElements[5][0]);
	    field4.add(cellElements[5][1]);
	    field4.add(cellElements[5][2]);
	    cellPanel.add(field4);
	    JPanel field5 = new JPanel();
	    field5.setLayout(new GridLayout(3, 3));
	    field5.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field5.add(cellElements[3][3]);
	    field5.add(cellElements[3][4]);
	    field5.add(cellElements[3][5]);
	    field5.add(cellElements[4][3]);
	    field5.add(cellElements[4][4]);
	    field5.add(cellElements[4][5]);
	    field5.add(cellElements[5][3]);
	    field5.add(cellElements[5][4]);
	    field5.add(cellElements[5][5]);
	    cellPanel.add(field5);
	    JPanel field6 = new JPanel();
	    field6.setLayout(new GridLayout(3, 3));
	    field6.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field6.add(cellElements[3][6]);
	    field6.add(cellElements[3][7]);
	    field6.add(cellElements[3][8]);
	    field6.add(cellElements[4][6]);
	    field6.add(cellElements[4][7]);
	    field6.add(cellElements[4][8]);
	    field6.add(cellElements[5][6]);
	    field6.add(cellElements[5][7]);
	    field6.add(cellElements[5][8]);
	    cellPanel.add(field6);
	    JPanel field7 = new JPanel();
	    field7.setLayout(new GridLayout(3, 3));
	    field7.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field7.add(cellElements[6][0]);
	    field7.add(cellElements[6][1]);
	    field7.add(cellElements[6][2]);
	    field7.add(cellElements[7][0]);
	    field7.add(cellElements[7][1]);
	    field7.add(cellElements[7][2]);
	    field7.add(cellElements[8][0]);
	    field7.add(cellElements[8][1]);
	    field7.add(cellElements[8][2]);
	    cellPanel.add(field7);
	    JPanel field8 = new JPanel();
	    field8.setLayout(new GridLayout(3, 3));
	    field8.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field8.add(cellElements[6][3]);
	    field8.add(cellElements[6][4]);
	    field8.add(cellElements[6][5]);
	    field8.add(cellElements[7][3]);
	    field8.add(cellElements[7][4]);
	    field8.add(cellElements[7][5]);
	    field8.add(cellElements[8][3]);
	    field8.add(cellElements[8][4]);
	    field8.add(cellElements[8][5]);
	    cellPanel.add(field8);
	    JPanel field9 = new JPanel();
	    field9.setLayout(new GridLayout(3, 3));
	    field9.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
	    field9.add(cellElements[6][6]);
	    field9.add(cellElements[6][7]);
	    field9.add(cellElements[6][8]);
	    field9.add(cellElements[7][6]);
	    field9.add(cellElements[7][7]);
	    field9.add(cellElements[7][8]);
	    field9.add(cellElements[8][6]);
	    field9.add(cellElements[8][7]);
	    field9.add(cellElements[8][8]);
	    cellPanel.add(field9);

	    JPanel messagePanel = new JPanel();
	    BorderLayout messagePanelLayout = new BorderLayout();
	    messagePanel.setLayout(messagePanelLayout);
	    this.getContentPane().add(messagePanel, BorderLayout.SOUTH);
	    messageField = new JTextField();
	    messagePanel.add(messageField, BorderLayout.CENTER);

	    messageField.setPreferredSize(new java.awt.Dimension(4, 30));
	    messageField.setEditable(false);
	    messageField.setEnabled(false);
	    messageField.setFont(new java.awt.Font("Dialog", 1, 12));

	    messageButton = new JButton();
	    messagePanel.add(messageButton, BorderLayout.EAST);
	    messageButton.setText("***");
	    messageButton.setPreferredSize(new java.awt.Dimension(30, 30));
	    messageButton.setFocusable(false);
	    messageButton.setEnabled(false);
	    messageButton.setFont(new java.awt.Font("Dialog", 1, 20));
	    messageButton.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    StatusWindow sw = new StatusWindow(mainWindow, "Extended Message", true);

		    // Compose the status text
		    if (error != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream s = new PrintStream(os);
			s.println(error.getMessage());
			s.println();
			Throwable cause = error.getCause();
			if (cause != null) {
			    s.println(cause.getMessage());
			    s.println();
			}
			sw.setText(os.toString());
			s.close();
			try {
			    os.close();
			} catch (IOException e1) {
			    // Might be ignored
			    e1.printStackTrace();
			}
		    } else {
			sw.setText("No information to display");
		    }
		    sw.setVisible(true);
		}

	    });
	    setMessage("Ready");
	} catch (Exception e) {
	    e.printStackTrace(myOut);
	    myOut.flush();
	}
    }

    /**
         * Close the application <br>
         * If model is dirty prompt the user for save
         */
    private void closeApplication() {
	if (base.isModelDirty()) {
    	    if (new ModelDirtyWarning(this).show()) {
    		boolean result = new SaveAction(this).save();
    		if (result == false) {
    		    return;
    		} 
    	    }	    
	}

	// Close the application
	setVisible(false);
	dispose();
    }

    /**
         * @return Returns the editMode.
         */
    public boolean isEditMode() {
	return editMode;
    }

    /**
         * @param editMode
         *                The editMode to set.
         */
    public void setEditMode(boolean editMode) {
	this.editMode = editMode;
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.awt.Window#dispose()
         */
    public void dispose() {
	try {
	    persistence.save();
	} catch (IOException e) {
	    e.printStackTrace(myOut);
	    myOut.flush();
	}
	super.dispose();
    }

    /**
         * Called by the ActionRunner when a game is complete
         */
    public void gameComplete() {
	// CellElement.setClicksEnabled(false);
	// actionRunner.setEnabled(false);
	editMode = false;
	setMessage("Game Complete", COMPLETE);
    }

    /**
         * Set the message field - the message type will be MESSAGE
         * 
         * @param message
         *                String - The message text
         */
    public void setMessage(String message) {
	setMessage(message, NORMAL);
    }

    /**
         * Set the message field
         * 
         * @param message
         *                String - The message text
         * @param type
         *                int - (MESSAGE, ERROR, COMPLETE)
         */
    public void setMessage(String message, int type) {
	clearError();
	setMessageInternal(message, type, null);
    }

    /**
         * Set the message field
         * 
         * @param message
         *                String - The message text
         * @param type
         *                int - (MESSAGE, ERROR, COMPLETE)
         * @param exception
         *                SudokuException or null
         */
    private void setMessageInternal(String message, int type, SudokuException exception) {
	switch (type) {
	    case ERROR:
		messageField.setBackground(errorBackgroundColor);
		messageField.setDisabledTextColor(errorForegroundColor);
		break;
	    case COMPLETE:
		messageField.setBackground(completeBackgroundColor);
		messageField.setDisabledTextColor(completeForegroundColor);
		break;
	    default:
		messageField.setBackground(normalBackgroundColor);
		messageField.setDisabledTextColor(normalForegroundColor);
	}
	messageField.setText("     " + message);
	error = exception;
	if (error == null) {
	    messageButton.setEnabled(false);
	} else {

	    messageButton.setEnabled(true);
	    error.printStackTrace(myOut);
	    myOut.flush();
	}
    }

    /**
         * Set the message extracted from the SudokuException
         * 
         * @param e1 -
         *                SudokuException
         */
    public void setMessage(SudokuException e1) {
	error = e1;
	setMessageInternal(error.getMessage(), ERROR, e1);
	Vector coordinates = error.getCoordinates();
	for (int i = 0; i < coordinates.size(); i++) {
	    int row = ((Coordinate) coordinates.elementAt(i)).getRow();
	    int column = ((Coordinate) coordinates.elementAt(i)).getColumn();
	    cellElements[row][column].setError(true);
	}
    }

    /**
         * Set the message, and extract information from the SudokuException
         * 
         * @param message -
         *                String
         * @param e1 -
         *                SudokuException
         */
    public void setMessage(String message, SudokuException e1) {
	setMessageInternal(message, ERROR, e1);
	Vector coordinates = e1.getCoordinates();
	for (int i = 0; i < coordinates.size(); i++) {
	    int row = ((Coordinate) coordinates.elementAt(i)).getRow();
	    int column = ((Coordinate) coordinates.elementAt(i)).getColumn();
	    cellElements[row][column].setError(true);
	}
    }

    /**
         * Clear the error status
         */
    public void clearError() {
	if (error == null) {
	    return;
	}
	error = null;
	for (int x = 0; x < 9; x++) {
	    for (int y = 0; y < 9; y++) {
		cellElements[x][y].setError(false);
	    }
	}
	setMessage("");

	// actionRunner.setEnabled(true);
    }

    /**
         * Refresh the cell elements
         */
    public void refresh() {
	for (int x = 0; x < 9; x++) {
	    for (int y = 0; y < 9; y++) {
		cellElements[x][y].refresh();
	    }
	}
    }

}
