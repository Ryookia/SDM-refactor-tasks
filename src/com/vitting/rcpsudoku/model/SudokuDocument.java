/**
 * Copyright (c) 2005, 2006 Henning Vitting and others.
 * All rights reserved.
 * <p>
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Henning Vitting - Initial API and implementation
 */

package com.vitting.rcpsudoku.model;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.Date;

/**
 * Persistent infoemation for saving a Sudoku game
 */
public class SudokuDocument implements ISudokuDokument {

    Document document;

    private File saveFile;

    /**
     * @param file file to use for save
     */
    public SudokuDocument(File file) {
        saveFile = file;
    }

    /**
     * Load the base
     *
     * @param base a base class for sudoku game
     * @throws SudokuException exception in case of invalid state
     */
    public void load(SudokuBase base) throws SudokuException {
        //Null exception chance
        checkIfCanReadFile();

        // Clear the base
        base.clear(true);

        parseFile();

        validateDocument(base);
        // Notify all cells
        base.cellsChanged(true);
    }

    /**
     * Save the base
     *
     * @param base base class for sudoku game
     * @throws SudokuException throws in case of invalid state
     */
    public void save(SudokuBase base) throws SudokuException {
        // Create the descriptor file
        Element game = generateGameNode();

        // Write the game data
        writeGameData(base, game);

        // Use a Transformer for output
        transformOutput();
    }

    private void checkIfCanReadFile() throws SudokuException {
        if (saveFile.canRead()) {
            throw new SudokuException("Document could not be found: "
                    + saveFile.getAbsolutePath(),
                    SudokuException.DISPOSITION_CONTINUE,
                    SudokuException.SEVERITY_WARNING);
        }
    }

    private void parseFile() throws SudokuException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(generateErrorHandler());
            document = builder.parse(saveFile);
        } catch (SAXException sxe) {
            // Error generated during parsing
            throw new SudokuException(
                    "SAXException during passing of BackupSet Descriptor Document", sxe,
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            throw new SudokuException(
                    "Error Loading BackupSet Descriptor Document", pce,
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        } catch (IOException ioe) {
            // IO Error
            throw new SudokuException(
                    "IOException during passing BackupSet Descriptor Document", ioe,
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        }
    }

    private ErrorHandler generateErrorHandler() {
        return new org.xml.sax.ErrorHandler() {

            // Treat warnings as fatal
            public void warning(SAXParseException exception)
                    throws SAXException {
                throw exception;
            }

            // Treat warnings as fatal
            public void error(SAXParseException exception)
                    throws SAXException {
                throw exception;
            }

            public void fatalError(SAXParseException exception) {
            }
        };
    }

    private void validateFileFormat() throws SudokuException {
        Node documentElement = document.getDocumentElement();
        if (!documentElement.getNodeName().equals(SUDOKU)) {
            throw new SudokuException(" Incorrect file format",
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        }
    }

    private void validateFileDescriptor() throws SudokuException {
        Node documentElement = document.getDocumentElement();
        NamedNodeMap bsdAttributes = documentElement.getAttributes();
        Node version = bsdAttributes.getNamedItem(DOCUMENT_VERSION);
        if (version == null) {
            throw new SudokuException("File descriptor missing",
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        }
    }

    private void validateFileDescriptorVersion() throws SudokuException {
        Node documentElement = document.getDocumentElement();
        NamedNodeMap bsdAttributes = documentElement.getAttributes();
        Node version = bsdAttributes.getNamedItem(DOCUMENT_VERSION);
        if (version.getNodeValue().compareTo(CURRENT_DOCUMENT_VERSION) != 0) {
            throw new SudokuException(
                    "Unsupported File Descriptor file version"
                            + version.getNodeValue(),
                    SudokuException.SEVERITY_INFORMATION,
                    SudokuException.DISPOSITION_CONTINUE);
        }
    }

    private void validateFileFormatLength(Element documentElement) throws SudokuException {
        NodeList rootNodes = documentElement.getChildNodes();
        if (rootNodes.getLength() != 1) {
            throw new SudokuException("Invalid File format",
                    SudokuException.SEVERITY_WARNING,
                    SudokuException.DISPOSITION_CONTINUE);
        }
    }

    private void validateCell(RowColumnHolder holder, Integer position, SudokuBase gameBase, NodeList cells) {
        if ((cells.item(position).getNodeType() == Node.ELEMENT_NODE)
                && (cells.item(position).getNodeName().compareTo(CELL) == 0)) {
            NamedNodeMap cellAttributes = cells.item(position).getAttributes();
            Node node = cellAttributes.getNamedItem(ROW);
            if (node != null) {
                holder.setRow(Integer.parseInt(node.getNodeValue()));
            }
            node = cellAttributes.getNamedItem(COLUMN);
            if (node != null) {
                holder.setColumn(Integer.parseInt(node.getNodeValue()));
            }
            MCell cell = gameBase.getCell(holder.getRow(), holder.getColumn());
            node = cellAttributes.getNamedItem(VALUE);
            if (node != null) {

                // Convert the string to a BitSet
                String stringValue = node.getNodeValue();
                BitSet value = new BitSet();
                if (stringValue.length() < 9) {
                    for (int j = 0; j < stringValue.length(); j++) {
                        int x = Character.getNumericValue(stringValue
                                .charAt(j));
                        if ((x > 0) && (x < 10)) {
                            value.set(x - 1);
                        }
                    }
                } else {
                    // The cell is empty
                    value.set(0, 9);
                }
                cell.setValue(value);
            }
            node = cellAttributes.getNamedItem(STATUS);
            if (node != null) {
                cell.setInitialValue(node.getNodeValue().equals(
                        STATUS_INITIAL));
            }
        }
    }

    private void validateCells(SudokuBase gameBase, Element documentElement) {
        NodeList rootNodes = documentElement.getChildNodes();
        RowColumnHolder holder = new RowColumnHolder();
        Node game = rootNodes.item(0);
        NodeList cells = game.getChildNodes();
        for (int i = 0; i < cells.getLength(); i++) {
            validateCell(holder, i, gameBase, rootNodes);
        }
    }

    private void validateDocument(SudokuBase gameBase) throws SudokuException {
        validateFileFormat();
        //[BS]Weird variable name
        validateFileDescriptor();
        // Only version 1.0 supported
        validateFileDescriptorVersion();

        // Parse the root Node and the only child
        validateFileFormatLength(document.getDocumentElement());

        validateCells(gameBase, document.getDocumentElement());
    }

    private Element generateGameNode() throws SudokuException {
        Element game;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            document.appendChild(document.createComment(DOCUMENT_COMMENT + " "
                    + new Date()));
            Element root = document.createElement(SUDOKU);
            document.appendChild(root);
            root.setAttribute(DOCUMENT_VERSION, CURRENT_DOCUMENT_VERSION);
            game = document.createElement(GAME);
            root.appendChild(game);
        } catch (ParserConfigurationException pce) {
            throw new SudokuException("Error Creating Initial Document", pce,
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        }
        return game;
    }

    private void writeGameData(SudokuBase base, Element gameNode) {
        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                MCell cell = base.getCell(x, y);
                // Dont save empty cells
                if (!cell.isEmpty()) {
                    Element cellElement = document.createElement(CELL);
                    cellElement.setAttribute(ROW, Integer.toString(x));
                    cellElement.setAttribute(COLUMN, Integer.toString(y));

                    // Convert cell value to a string
                    StringBuilder stringBuilder = new StringBuilder();
                    BitSet value = cell.getValue();
                    for (int i = value.nextSetBit(0); i >= 0; i = value
                            .nextSetBit(i + 1)) {
                        stringBuilder.append(i + 1);
                    }
                    cellElement.setAttribute(VALUE, stringBuilder.toString());
                    cellElement.setAttribute(STATUS,
                            cell.isInitialValue() ? STATUS_INITIAL
                                    : STATUS_CALCULATED);
                    gameNode.appendChild(cellElement);
                }
            }
    }

    private void transformOutput() throws SudokuException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(saveFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new SudokuException(
                    "Failed to write BackupSet descriptor document", e,
                    SudokuException.SEVERITY_ERROR,
                    SudokuException.DISPOSITION_CONTINUE);
        }
    }

    private static class RowColumnHolder {
        private Integer row;
        private Integer column;

        public Integer getRow() {
            return row;
        }

        public void setRow(Integer row) {
            this.row = row;
        }

        public Integer getColumn() {
            return column;
        }

        public void setColumn(Integer column) {
            this.column = column;
        }
    }
}
