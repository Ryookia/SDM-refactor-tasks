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

import com.vitting.rcpsudoku.jfc.actions.*;
import com.vitting.rcpsudoku.jfc.config.BuildConfig;
import com.vitting.rcpsudoku.jfc.runner.ActionRunner;
import com.vitting.rcpsudoku.jfc.test.Test;
import com.vitting.rcpsudoku.jfc.utils.CellElementLayout;
import com.vitting.rcpsudoku.jfc.utils.DebugLogger;
import com.vitting.rcpsudoku.jfc.utils.FieldConfig;
import com.vitting.rcpsudoku.jfc.utils.Logger;
import com.vitting.rcpsudoku.model.Coordinate;
import com.vitting.rcpsudoku.model.SudokuBase;
import com.vitting.rcpsudoku.model.SudokuException;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Vector;

/**
 * Main window for the SuDoCu Application
 */
public class MainWindow extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    public static String windowTitle = "RcpSudoku";

    private MainWindow mainWindow = null;

    public CellElement[][] cellElements = new CellElement[9][9];

    public ActionRunner actionRunner = null;

    private PrintWriter myOut = null;

    public static final String MESSAGE_GAME = "Game";
    public static final String MESSAGE_EXIT = "Exit";
    public static final String MESSAGE_MODE = "Mode";
    public static final String MESSAGE_UNKNOWN = "***";
    public static final String MESSAGE_NO_INFO = "No information to display";
    private static final String MESSAGE_INTERNAL = "     ";

    private static final int NORMAL = 0;

    private static final int ERROR = 1;

    private static final int COMPLETE = 2;

    private static final java.awt.Color normalBackgroundColor = new java.awt.Color(255, 255, 255);

    private static final java.awt.Color normalForegroundColor = new java.awt.Color(0, 0, 0);

    private static final java.awt.Color completeBackgroundColor = new java.awt.Color(0, 255, 0);

    private static final java.awt.Color completeForegroundColor = new java.awt.Color(0, 0, 0);

    private static final java.awt.Color errorBackgroundColor = new java.awt.Color(255, 0, 0);

    private static final java.awt.Color errorForegroundColor = new java.awt.Color(255, 255, 255);

    private JTextField messageField;

    private JButton messageButton;

    private SudokuException error = null;

    private boolean editMode = true;

    public GuiPersistence persistence = null;

    private SudokuBase base = null;
    private BuildConfig config = new BuildConfig();

    private Logger logger;

    /**
     * Construct the main window
     */
    public MainWindow() {
        super();
        config.setIsDebug(true);
        logger = DebugLogger.getInstance(config);
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
     * Initialize The gui </br> Create The Menus and Layouts
     */
    private void initGui() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            this.getContentPane().setLayout(thisLayout);
            JPanel cellPanel = new JPanel();
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
                URL resource = cl.getResource("images/WindowIcon.gif");
                if (resource != null) {
                    icon = new ImageIcon(resource).getImage();
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
            int windowWidth = this.getSize().width;
            int windowHeight = this.getSize().height;
            int xLocation = (dim.width - windowWidth) / 2;
            int yLocation = (dim.height - windowHeight) / 2;

            // Move the window center to the screen
            this.setLocation(xLocation, yLocation);

            // Create the Menus
            JMenuBar jMenuBar = new JMenuBar();
            setJMenuBar(jMenuBar);
            JMenu fileMenu = new JMenu();
            jMenuBar.add(fileMenu);
            fileMenu.setText(MESSAGE_GAME);
            fileMenu.addMenuListener(generateFileMenuListener());

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
            exitMenuItem.setText(MESSAGE_EXIT);
            exitMenuItem.addActionListener(generateExitMenuListener());

            // Mode
            JMenu modeMenu = new JMenu();
            jMenuBar.add(modeMenu);
            modeMenu.setText(MESSAGE_MODE);
            modeMenu.addMenuListener(generateModeMenuListener());

            // modeEdit
            ModeEditAction modeEdit = new ModeEditAction(this);
            modeMenu.add(modeEdit);

            // Action
            actionRunner = new ActionRunner(this, cellElements);
            jMenuBar.add(actionRunner);
            actionRunner.addMenuListener(generateActionMenuListener());

            handleTestMenu(jMenuBar);

            // Fill the cell element Array, this must be done here
            fillCellElement();

            // Construct the 9 fields
            FieldConfig fieldConfig = new FieldConfig(new GridLayout(3, 3), new java.awt.Color(0, 0, 0), 1, false);
            java.util.List<java.util.List<CellElement>> blocksToAdd = CellElementLayout.getFieldBlockLayoutList(cellElements);
            for (java.util.List<CellElement> elements : blocksToAdd) {
                cellPanel.add(generateField(fieldConfig, elements));
            }

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
            messageButton.setText(MESSAGE_UNKNOWN);
            messageButton.setPreferredSize(new java.awt.Dimension(30, 30));
            messageButton.setFocusable(false);
            messageButton.setEnabled(false);
            messageButton.setFont(new java.awt.Font("Dialog", 1, 20));
            messageButton.addActionListener(generateMessageButtonListener());
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
                if (!result) {
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
     * @param editMode The editMode to set.
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
     * @param message String - The message text
     */
    public void setMessage(String message) {
        setMessage(message, NORMAL);
    }

    /**
     * Set the message field
     *
     * @param message String - The message text
     * @param type    int - (MESSAGE, ERROR, COMPLETE)
     */
    public void setMessage(String message, int type) {
        clearError();
        setMessageInternal(message, type, null);
    }


    /**
     * Set the message extracted from the SudokuException
     *
     * @param e1 -
     *           SudokuException
     */
    public void setMessage(SudokuException e1) {
        error = e1;
        setMessageInternal(error.getMessage(), ERROR, e1);
        Vector<Coordinate> coordinates = error.getCoordinates();
        for (int i = 0; i < coordinates.size(); i++) {
            int row = (coordinates.elementAt(i)).getRow();
            int column = (coordinates.elementAt(i)).getColumn();
            cellElements[row][column].setError(true);
        }
    }

    /**
     * Set the message, and extract information from the SudokuException
     *
     * @param message -
     *                String
     * @param e1      -
     *                SudokuException
     */
    public void setMessage(String message, SudokuException e1) {
        setMessageInternal(message, ERROR, e1);
        Vector<Coordinate> coordinates = e1.getCoordinates();
        for (int i = 0; i < coordinates.size(); i++) {
            int row = (coordinates.elementAt(i)).getRow();
            int column = (coordinates.elementAt(i)).getColumn();
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

    public JPanel generateField(FieldConfig config, java.util.List<CellElement> elements) {
        JPanel field = new JPanel();
        field.setLayout(config.getLayoutManager());
        field.setBorder(config.getBorder());
        for (CellElement element : elements) {
            field.add(element);
        }
        return field;
    }

    private MenuListener generateFileMenuListener() {
        return new MenuListener() {

            public void menuCanceled(MenuEvent e) {
                //NOP
            }

            public void menuDeselected(MenuEvent e) {
                //NOP
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
        };
    }

    private MenuListener generateModeMenuListener() {
        return new MenuListener() {

            public void menuCanceled(MenuEvent e) {
                //NOP
            }

            public void menuDeselected(MenuEvent e) {
                //NOP
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
        };
    }

    private MenuListener generateActionMenuListener() {
        return new MenuListener() {

            public void menuCanceled(MenuEvent e) {
                //NOP
            }

            public void menuDeselected(MenuEvent e) {
                //NOP
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
        };
    }

    //DEBUG -- Test Menu
    private void handleTestMenu(JMenuBar jMenuBar) {
        if (!config.getIsDebug()) return;
        JMenu testMenu = new Test(this, config);
        jMenuBar.add(testMenu);
    }

    private void fillCellElement() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                cellElements[x][y] = new CellElement(x, y, this);
                cellElements[x][y].refresh();
            }
        }
    }

    private ActionListener generateMessageButtonListener() {
        return actionEvent -> {
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
                sw.setText(MESSAGE_NO_INFO);
            }
            sw.setVisible(true);
        };
    }

    private ActionListener generateExitMenuListener() {
        return actionEvent -> mainWindow.closeApplication();
    }

    /**
     * Set the message field
     *
     * @param message   String - The message text
     * @param type      int - (MESSAGE, ERROR, COMPLETE)
     * @param exception SudokuException or null
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
        messageField.setText(MESSAGE_INTERNAL + message);
        error = exception;
        if (error == null) {
            messageButton.setEnabled(false);
        } else {

            messageButton.setEnabled(true);
            error.printStackTrace(myOut);
            myOut.flush();
        }
    }
}
