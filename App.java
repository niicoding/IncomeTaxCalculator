//-----------------------------------------------------------
//  Author: Zachary Perales
//  Course: CSC240-80
//  Due   : 11/12/2019
//  Description: This program calculates the taxes for a user
//               and validates text. It uses many neat features
//               such as document listeners, and document formatters.
//-----------------------------------------------------------

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {
    private JButton calculateButton;
    private JButton clearFieldsButton;
    private JButton exitButton;
    private JPanel panelMain;
    private JRadioButton refundRadioButton;
    private JRadioButton oweRadioButton;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private boolean toggle = false;
    private boolean comboToggle1 = false;
    private boolean comboToggle2 = false;

    public boolean isComboToggle2() {
        return comboToggle2;
    }

    public void setComboToggle2(boolean comboToggle2) {
        this.comboToggle2 = comboToggle2;
    }

    public boolean isComboToggle1() {
        return comboToggle1;
    }

    public void setComboToggle1(boolean comboToggle1) {
        this.comboToggle1 = comboToggle1;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    PlainDocument document;

    // User input text fields
    private ArrayList<JTextField> textFields = new ArrayList<>();

    // User input combo boxes
    private ArrayList<JComboBox> comboBoxes = new ArrayList<>();

    public ArrayList<JTextField> getTextFields() {
        return textFields;
    }

    public void setTextFields() {
        getTextFields().add(textField2);
        getTextFields().add(textField3);
        getTextFields().add(textField4);
        getTextFields().add(textField5);
        getTextFields().add(textField6);
        getTextFields().add(textField7);
        getTextFields().add(textField8);
    }

    public ArrayList<JComboBox> getComboBoxes() {
        return comboBoxes;
    }

    public void setComboBoxes() {
        getComboBoxes().add(comboBox1);
        getComboBoxes().add(comboBox2);
        comboBox1.setSelectedIndex(-1);
        comboBox2.setSelectedIndex(-1);
    }

    public App() {
        $$$setupUI$$$();

        setTextFields();
        setComboBoxes();

        Taxes taxes = new Taxes();

        DocumentListener listener = new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                boolean enabled = true;

                for (JTextField textField : getTextFields()) {
                    if (textField.getText().isEmpty()) {
                        enabled = false;
                    }
                }


                int i = 0;
                for (JTextField textField : textFields) {
                    if (i < 4) {
                        if ((textField.getText().length() == 1) && textField.getText().endsWith("$")) {
                            enabled = false;
                        }

                        if ((textField.getText().contains(",")) && !(textField.getText().contains(".")) && ((textField.getText().length() - textField.getText().lastIndexOf(",")) < 4)) {
                            enabled = false;
                        }

                        if ((textField.getText().contains(".")) && ((textField.getText().length() - textField.getText().lastIndexOf(".")) < 3)) {
                            enabled = false;
                        }
                    }

                    if (i == 4) {
                        if (!(textField.getText().length() == 9) && !textField.getText().contains("-")) {
                            enabled = false;
                        }

                        if (!(textField.getText().length() == 11) && textField.getText().contains("-")) {
                            enabled = false;
                        }
                    }

                    if (i > 4 && i < 7) {

                    }

                    i++;
                }

                setToggle(enabled);

                if (isComboToggle1() && isComboToggle2() && isToggle()) {
                    calculateButton.setEnabled(true);
                } else {
                    calculateButton.setEnabled(false);
                }
            }
        };

        DocumentFilter nameFilter = new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
                    throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + str;

                if (off < fb.getDocument().getText(0, fb.getDocument().getLength()).length()) {
                    return;
                }

                if ((fb.getDocument().getLength() == 0) && Character.isLetter(str.charAt(0))) {
                    fb.insertString(off, str, attr);
                } else if (!(fb.getDocument().getLength() == 0) && (Character.isLetter(str.charAt(0)) || (str.equals("'") && !fb.getDocument().getText(0, fb.getDocument().getLength()).endsWith("'")))) {
                    fb.insertString(off, str, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
                    throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + str;

                if (off < fb.getDocument().getText(0, fb.getDocument().getLength()).length()) {
                    len = Math.max(0, len + off - fb.getDocument().getText(0, fb.getDocument().getLength()).length());
                    off = fb.getDocument().getText(0, fb.getDocument().getLength()).length();
                }

                if ((fb.getDocument().getLength() == 0) && Character.isLetter(str.charAt(0))) {
                    fb.replace(off, len, str, attr);
                } else if (!(fb.getDocument().getLength() == 0) && (Character.isLetter(str.charAt(0)) || (str.equals("'") && !fb.getDocument().getText(0, fb.getDocument().getLength()).endsWith("'")))) {
                    fb.replace(off, len, str, attr);
                }

            }
        };

        DocumentFilter ssnFilter = new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
                    throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + str;

                if (off < fb.getDocument().getText(0, fb.getDocument().getLength()).length()) {
                    return;
                }

                if (Character.isDigit(str.charAt(0)) || (str.charAt(0) == '-' && (string.length() == 4 || (string.length() == 7) && string.contains("-")))) {
                    if ((fb.getDocument().getLength() == 0)) {
                        fb.insertString(off, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 9 && !string.contains("-")) {
                        fb.insertString(off, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 4 && string.contains("-")) {
                        fb.insertString(off, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 6 && string.contains("-")) {
                        fb.insertString(off, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 11 && string.length() > 4 && (string.charAt(6) == '-')) {
                        fb.insertString(off, str, attr);
                    }
                }
            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
                    throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + str;

                if (off < fb.getDocument().getText(0, fb.getDocument().getLength()).length()) {
                    len = Math.max(0, len + off - fb.getDocument().getText(0, fb.getDocument().getLength()).length());
                    off = fb.getDocument().getText(0, fb.getDocument().getLength()).length();
                }

                if (Character.isDigit(str.charAt(0)) || (str.charAt(0) == '-' && (string.length() == 4 || (string.length() == 7) && string.contains("-")))) {
                    if ((fb.getDocument().getLength() == 0)) {
                        fb.replace(off, len, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 9 && !string.contains("-")) {
                        fb.replace(off, len, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 4 && string.contains("-")) {
                        fb.replace(off, len, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 6 && string.contains("-")) {
                        fb.replace(off, len, str, attr);
                    } else if (!(fb.getDocument().getLength() == 0) && fb.getDocument().getLength() < 11 && string.length() > 4 && (string.charAt(6) == '-')) {
                        fb.replace(off, len, str, attr);
                    }
                }
            }
        };

        DocumentFilter currencyFilter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
                    throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + str;
                if (!string.startsWith("$")) {
                    string = "$" + string;
                }

                if (off < fb.getDocument().getText(0, fb.getDocument().getLength()).length()) {
                    return;
                }

                if (!string.substring(0, string.length() - 1).contains(".")) {
                    if (fb.getDocument().getLength() == 0 && ((str.equals("$") || Character.isDigit(str.charAt(0))))) {
                        fb.insertString(off, str, attr);
                    } else if (fb.getDocument().getLength() == 0 && Character.isDigit(str.charAt(0))) {
                        fb.insertString(off, str, attr);
                        return;
                    }

                    if (!str.equals("$")) {
                        if (!(fb.getDocument().getLength() == 0) && (((Character.isDigit(str.charAt(0)) && !(string.charAt(1) == '0') && ((string.length() == 4) || string.length() == 3) && Character.isDigit(string.charAt(1)))
                                || (Character.isDigit(str.charAt(0)) && !string.contains(",") && (string.length() < 3))
                                || (Character.isDigit(str.charAt(0)) && !string.contains(",") && (string.length() > 3 && !(string.charAt(1) == '0')))
                                || (Character.isDigit(str.charAt(0)) && !(string.charAt(1) == '0') && (string.length() == 4) && Character.isDigit(string.charAt(1)))
                                || (Character.isDigit(str.charAt(0)) && string.contains(",") && (string.length() > 4)
                                && !(string.charAt(string.length() - 5) == ',')) || (!string.substring(0, string.length() - 2).contains(",")
                                && str.equals(",") && !(string.charAt(1) == '0') && ((Character.isDigit(string.charAt(string.length() - 2))
                                && (string.length() < 4)) || (Character.isDigit(string.charAt(string.length() - 2))
                                && Character.isDigit(string.charAt(string.length() - 3)) && (string.length() < 5))
                                || (Character.isDigit(string.charAt(string.length() - 2)) && Character.isDigit(string.charAt(string.length() - 3))
                                && Character.isDigit(string.charAt(string.length() - 4)) && (string.length() < 6)
                                && !Character.isDigit(string.charAt(string.length() - 5))))) || (string.substring(0, string.length() - 2).contains(",")
                                && str.equals(",") && (Character.isDigit(string.charAt(string.length() - 2))
                                && Character.isDigit(string.charAt(string.length() - 3)) && Character.isDigit(string.charAt(string.length() - 4))
                                && (string.charAt(string.length() - 5) == ',')))))) {
                            fb.insertString(off, str, attr);
                        } else if (!(fb.getDocument().getLength() == 0) && (str.equals(".")) && string.contains(",") && (string.length() > 4)
                                && (string.charAt(string.length() - 5) == ',')) {
                            fb.insertString(off, str, attr);
                        } else if (!(fb.getDocument().getLength() == 0) && (str.equals(".")) && !(string.contains(","))
                                && Character.isDigit(string.charAt(string.length() - 2))) {
                            fb.insertString(off, str, attr);
                        }

                    }
                } else if (string.contains(".")) {
                    if (!(fb.getDocument().getLength() == 0) && Character.isDigit(str.charAt(0)) && ((string.charAt(string.length() - 2) == '.')
                            || (string.charAt(string.length() - 3) == '.'))) {
                        fb.insertString(off, str, attr);
                    }
                }

            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
                    throws BadLocationException {
                String string = fb.getDocument().getText(0, fb.getDocument().getLength()) + str;
                if (!string.startsWith("$")) {
                    string = "$" + string;
                }


                if (off < fb.getDocument().getText(0, fb.getDocument().getLength()).length()) {
                    len = Math.max(0, len + off - fb.getDocument().getText(0, fb.getDocument().getLength()).length());
                    off = fb.getDocument().getText(0, fb.getDocument().getLength()).length();
                }

                if (!string.substring(0, string.length() - 1).contains(".")) {
                    if (fb.getDocument().getLength() == 0 && ((str.equals("$")))) {
                        fb.replace(off, len, str, attr);
                    } else if (fb.getDocument().getLength() == 0 && Character.isDigit(str.charAt(0))) {
                        fb.replace(off, len, str, attr);
                        return;
                    }

                    if (!str.equals("$")) {
                        if (!(fb.getDocument().getLength() == 0) && (((Character.isDigit(str.charAt(0)) && !(string.charAt(1) == '0') && ((string.length() == 4) || string.length() == 3) && Character.isDigit(string.charAt(1)))
                                || (Character.isDigit(str.charAt(0)) && !string.contains(",") && (string.length() < 3))
                                || (Character.isDigit(str.charAt(0)) && !string.contains(",") && (string.length() > 3 && !(string.charAt(1) == '0')))
                                || (Character.isDigit(str.charAt(0)) && string.contains(",") && (string.length() > 4)
                                && !(string.charAt(string.length() - 5) == ',')) || (!string.substring(0, string.length() - 2).contains(",")
                                && str.equals(",") && !(string.charAt(1) == '0') && ((Character.isDigit(string.charAt(string.length() - 2))
                                && (string.length() < 4)) || (Character.isDigit(string.charAt(string.length() - 2))
                                && Character.isDigit(string.charAt(string.length() - 3)) && (string.length() < 5))
                                || (Character.isDigit(string.charAt(string.length() - 2)) && Character.isDigit(string.charAt(string.length() - 3))
                                && Character.isDigit(string.charAt(string.length() - 4)) && (string.length() < 6)
                                && !Character.isDigit(string.charAt(string.length() - 5))))) || (string.substring(0, string.length() - 2).contains(",")
                                && str.equals(",") && (Character.isDigit(string.charAt(string.length() - 2))
                                && Character.isDigit(string.charAt(string.length() - 3)) && Character.isDigit(string.charAt(string.length() - 4))
                                && (string.charAt(string.length() - 5) == ',')))))) {
                            fb.replace(off, len, str, attr);
                        } else if (!(fb.getDocument().getLength() == 0) && (str.equals(".")) && string.contains(",") && (string.length() > 4)
                                && (string.charAt(string.length() - 5) == ',')) {
                            fb.replace(off, len, str, attr);
                        } else if (!(fb.getDocument().getLength() == 0) && (str.equals(".")) && !(string.contains(","))
                                && Character.isDigit(string.charAt(string.length() - 2))) {
                            fb.replace(off, len, str, attr);
                        }
                    }
                } else if (string.contains(".")) {
                    if (!(fb.getDocument().getLength() == 0) && Character.isDigit(str.charAt(0)) && ((string.charAt(string.length() - 2) == '.')
                            || (string.charAt(string.length() - 3) == '.'))) {
                        fb.replace(off, len, str, attr);
                    }
                }
            }
        };

        // Instantiate plain documents and assign the currencyFilter
        // Add filtered documents to each jTextField
        // Add document listeners to each jTextField
        int i = 0;
        for (JTextField textField : getTextFields()) {
            if (i < 4) {
                document = new PlainDocument();
                document.setDocumentFilter(currencyFilter);
                textField.setDocument(document);
            }

            if (i == 4) {
                document = new PlainDocument();
                document.setDocumentFilter(ssnFilter);
                textField.setDocument(document);
            }

            if (i > 4 && i < 7) {
                document = new PlainDocument();
                document.setDocumentFilter(nameFilter);
                textField.setDocument(document);
            }

            textField.getDocument().addDocumentListener(listener);
            i++;
        }


        // Add document listener to jComboBox items
        for (JComboBox comboBox : getComboBoxes()) {
            ((JTextField) comboBox.getEditor().getEditorComponent()).getDocument().addDocumentListener(listener);
        }

        // Exit button function
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(panelMain, "Are you sure you want to close?", "Confirm Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Clear Fields button function
        clearFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    textField1.getDocument().remove(0, textField1.getText().length());
                    textField2.getDocument().remove(0, textField2.getText().length());
                    textField3.getDocument().remove(0, textField3.getText().length());
                    textField4.getDocument().remove(0, textField4.getText().length());
                    textField5.getDocument().remove(0, textField5.getText().length());
                    textField6.getDocument().remove(0, textField6.getText().length());
                    textField7.getDocument().remove(0, textField7.getText().length());
                    textField8.getDocument().remove(0, textField8.getText().length());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }

                comboBox1.setSelectedItem(null);
                comboBox2.setSelectedItem(null);
                refundRadioButton.setSelected(false);
                refundRadioButton.setEnabled(false);
                oweRadioButton.setSelected(false);
                oweRadioButton.setEnabled(false);
                calculateButton.setEnabled(false);

                taxes.reset();
            }
        });

        // Calculate button function
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                class Money {
                    public Double getMoney(JTextField field) {
                        String string = "";

                        string = field.getText().replace("$", "");
                        string = string.replace(",", "");

                        if (string.endsWith(".")) {
                            string = string.replace(".", "");
                        }

                        return Double.parseDouble(string);
                    }
                }
                ;

                Money money = new Money();

                taxes.setCharitableContributions(money.getMoney(textField3));
                taxes.setTotalDeductions(money.getMoney(textField4));
                taxes.setGrossIncome(money.getMoney(textField5));
                taxes.setTaxesPaid(money.getMoney(textField2));
                taxes.setFilingStatus(comboBox1.getSelectedItem().toString());
                taxes.setNumberOfDependants(Integer.parseInt(comboBox2.getSelectedItem().toString()));

                taxes.calculateAgi();
                taxes.calculateTaxRate();
                taxes.calculateTaxesTotal();
                taxes.calculateTaxesOwed();

                Double val;
                DecimalFormat df = new DecimalFormat("#.##");
                val = Double.valueOf(df.format(taxes.getTaxesOwed()));

                textField1.setText(val.toString()); //@todo format mamybe

                if (taxes.getTaxesOwed() > 0) {
                    oweRadioButton.setSelected(true);
                } else if (taxes.getTaxesOwed() < 0) {
                    refundRadioButton.setSelected(true);
                } else if (taxes.getTaxesOwed() == 0) {
                    oweRadioButton.setSelected(false);
                    refundRadioButton.setSelected(false);
                }
            }
        });

        // ComboBox function for calculate button
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox1.getSelectedIndex() == -1) {
                    setComboToggle1(false);
                } else if (comboBox1.getSelectedIndex() != -1) {
                    setComboToggle1(true);
                }

                if (isComboToggle1() && isComboToggle2() && isToggle()) {
                    calculateButton.setEnabled(true);
                }
            }
        });

        // ComboBox function for calculate button
        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox2.getSelectedIndex() == -1) {
                    setComboToggle2(false);
                } else if (comboBox2.getSelectedIndex() != -1) {
                    setComboToggle2(true);
                }

                if (isComboToggle1() && isComboToggle2() && isToggle()) {
                    calculateButton.setEnabled(true);
                }
            }
        });


    }

    public static void main(String[] args) {
        // Everything that is not related to the GUI itself
        Taxes taxes = new Taxes();

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.toString());
        }


        JFrame frame = new JFrame("The Tax Application");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550, 441));
        frame.setMinimumSize(new Dimension(550, 441));
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        calculateButton = new JButton("Calculate");
        clearFieldsButton = new JButton("Clear Fields");
        exitButton = new JButton("Exit");
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panelMain = new JPanel();
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 3, new Insets(3, 3, 3, 3), -1, -1));
        panelMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        calculateButton.setEnabled(false);
        Font calculateButtonFont = this.$$$getFont$$$("Tahoma", Font.PLAIN, 12, calculateButton.getFont());
        if (calculateButtonFont != null) calculateButton.setFont(calculateButtonFont);
        calculateButton.setText("Calculate");
        panelMain.add(calculateButton, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Font clearFieldsButtonFont = this.$$$getFont$$$("Tahoma", Font.PLAIN, 12, clearFieldsButton.getFont());
        if (clearFieldsButtonFont != null) clearFieldsButton.setFont(clearFieldsButtonFont);
        clearFieldsButton.setText("Clear Fields");
        panelMain.add(clearFieldsButton, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Font exitButtonFont = this.$$$getFont$$$("Tahoma", Font.PLAIN, 12, exitButton.getFont());
        if (exitButtonFont != null) exitButton.setFont(exitButtonFont);
        exitButton.setText("Exit");
        panelMain.add(exitButton, new com.intellij.uiDesigner.core.GridConstraints(9, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        refundRadioButton = new JRadioButton();
        refundRadioButton.setEnabled(false);
        Font refundRadioButtonFont = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, refundRadioButton.getFont());
        if (refundRadioButtonFont != null) refundRadioButton.setFont(refundRadioButtonFont);
        refundRadioButton.setText("Refund");
        panelMain.add(refundRadioButton, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        oweRadioButton = new JRadioButton();
        oweRadioButton.setEnabled(false);
        Font oweRadioButtonFont = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, oweRadioButton.getFont());
        if (oweRadioButtonFont != null) oweRadioButton.setFont(oweRadioButtonFont);
        oweRadioButton.setText("Owe");
        panelMain.add(oweRadioButton, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        textField1.setEditable(false);
        textField1.setEnabled(false);
        panelMain.add(textField1, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        textField2 = new JTextField();
        panelMain.add(textField2, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Taxes Paid");
        panelMain.add(label1, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Tax Amount Owed");
        panelMain.add(label2, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Married Filing Jointly");
        defaultComboBoxModel1.addElement("Married Filing Separately");
        defaultComboBoxModel1.addElement("Single");
        defaultComboBoxModel1.addElement("Head of Household");
        comboBox1.setModel(defaultComboBoxModel1);
        comboBox1.setOpaque(true);
        panelMain.add(comboBox1, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 24), null, null, 0, false));
        comboBox2 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("0");
        defaultComboBoxModel2.addElement("1");
        defaultComboBoxModel2.addElement("2");
        defaultComboBoxModel2.addElement("3");
        defaultComboBoxModel2.addElement("4");
        defaultComboBoxModel2.addElement("5");
        defaultComboBoxModel2.addElement("6");
        defaultComboBoxModel2.addElement("7");
        defaultComboBoxModel2.addElement("8");
        defaultComboBoxModel2.addElement("9");
        defaultComboBoxModel2.addElement("10");
        defaultComboBoxModel2.addElement("11");
        defaultComboBoxModel2.addElement("12");
        defaultComboBoxModel2.addElement("13");
        defaultComboBoxModel2.addElement("14");
        defaultComboBoxModel2.addElement("15");
        defaultComboBoxModel2.addElement("16");
        defaultComboBoxModel2.addElement("17");
        defaultComboBoxModel2.addElement("18");
        defaultComboBoxModel2.addElement("19");
        defaultComboBoxModel2.addElement("20");
        defaultComboBoxModel2.addElement("21");
        defaultComboBoxModel2.addElement("22");
        defaultComboBoxModel2.addElement("23");
        defaultComboBoxModel2.addElement("24");
        defaultComboBoxModel2.addElement("25");
        defaultComboBoxModel2.addElement("26");
        defaultComboBoxModel2.addElement("27");
        defaultComboBoxModel2.addElement("28");
        defaultComboBoxModel2.addElement("29");
        defaultComboBoxModel2.addElement("30");
        defaultComboBoxModel2.addElement("31");
        defaultComboBoxModel2.addElement("32");
        defaultComboBoxModel2.addElement("33");
        defaultComboBoxModel2.addElement("34");
        defaultComboBoxModel2.addElement("35");
        defaultComboBoxModel2.addElement("36");
        defaultComboBoxModel2.addElement("37");
        defaultComboBoxModel2.addElement("38");
        defaultComboBoxModel2.addElement("39");
        defaultComboBoxModel2.addElement("40");
        defaultComboBoxModel2.addElement("41");
        defaultComboBoxModel2.addElement("42");
        defaultComboBoxModel2.addElement("43");
        defaultComboBoxModel2.addElement("44");
        defaultComboBoxModel2.addElement("45");
        defaultComboBoxModel2.addElement("46");
        defaultComboBoxModel2.addElement("47");
        defaultComboBoxModel2.addElement("48");
        defaultComboBoxModel2.addElement("49");
        defaultComboBoxModel2.addElement("50");
        defaultComboBoxModel2.addElement("51");
        defaultComboBoxModel2.addElement("52");
        defaultComboBoxModel2.addElement("53");
        defaultComboBoxModel2.addElement("54");
        defaultComboBoxModel2.addElement("55");
        defaultComboBoxModel2.addElement("56");
        defaultComboBoxModel2.addElement("57");
        defaultComboBoxModel2.addElement("58");
        defaultComboBoxModel2.addElement("59");
        defaultComboBoxModel2.addElement("60");
        defaultComboBoxModel2.addElement("61");
        defaultComboBoxModel2.addElement("62");
        defaultComboBoxModel2.addElement("63");
        defaultComboBoxModel2.addElement("64");
        defaultComboBoxModel2.addElement("65");
        defaultComboBoxModel2.addElement("66");
        defaultComboBoxModel2.addElement("67");
        defaultComboBoxModel2.addElement("68");
        defaultComboBoxModel2.addElement("69");
        defaultComboBoxModel2.addElement("70");
        defaultComboBoxModel2.addElement("71");
        defaultComboBoxModel2.addElement("72");
        defaultComboBoxModel2.addElement("73");
        defaultComboBoxModel2.addElement("74");
        defaultComboBoxModel2.addElement("75");
        defaultComboBoxModel2.addElement("76");
        defaultComboBoxModel2.addElement("77");
        defaultComboBoxModel2.addElement("78");
        defaultComboBoxModel2.addElement("79");
        defaultComboBoxModel2.addElement("80");
        defaultComboBoxModel2.addElement("81");
        defaultComboBoxModel2.addElement("82");
        defaultComboBoxModel2.addElement("83");
        defaultComboBoxModel2.addElement("84");
        defaultComboBoxModel2.addElement("85");
        defaultComboBoxModel2.addElement("86");
        defaultComboBoxModel2.addElement("87");
        defaultComboBoxModel2.addElement("88");
        defaultComboBoxModel2.addElement("89");
        defaultComboBoxModel2.addElement("90");
        defaultComboBoxModel2.addElement("91");
        defaultComboBoxModel2.addElement("92");
        defaultComboBoxModel2.addElement("93");
        defaultComboBoxModel2.addElement("94");
        defaultComboBoxModel2.addElement("95");
        defaultComboBoxModel2.addElement("96");
        defaultComboBoxModel2.addElement("97");
        defaultComboBoxModel2.addElement("98");
        defaultComboBoxModel2.addElement("99");
        comboBox2.setModel(defaultComboBoxModel2);
        panelMain.add(comboBox2, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 24), null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Filing Status");
        panelMain.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setFocusable(false);
        Font label4Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("# of Dependants");
        panelMain.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField3 = new JTextField();
        panelMain.add(textField3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        textField4 = new JTextField();
        panelMain.add(textField4, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        textField5 = new JTextField();
        panelMain.add(textField5, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Charitable Contributions");
        panelMain.add(label5, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Total # of Deductions");
        panelMain.add(label6, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Gross Income");
        panelMain.add(label7, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField6 = new JTextField();
        panelMain.add(textField6, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        textField7 = new JTextField();
        panelMain.add(textField7, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        textField8 = new JTextField();
        panelMain.add(textField8, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 23), new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("SSN");
        panelMain.add(label8, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("First Name");
        panelMain.add(label9, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$("Tahoma", Font.BOLD, 12, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Last Name");
        panelMain.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
