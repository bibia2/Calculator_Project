import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    private JTextArea display;
    private String currentInput = "";
    private double result = 0;
    private char operator = ' ';
    private boolean newCalculation = true;

    public Calculator() {
        JFrame frame = new JFrame("Calculator");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        display = new JTextArea(5, 20);
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel clearPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton clearLast = new JButton("Clear Last");
        JButton clearAll = new JButton("Clear All");
        clearLast.addActionListener(e -> clearLastChar());
        clearAll.addActionListener(e -> clearAll());
        clearPanel.add(clearLast);
        clearPanel.add(clearAll);
        frame.add(clearPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout (4, 4, 5, 5));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if ("0123456789.".contains(command)) {
                if (newCalculation) {
                    display.setText("");
                    newCalculation = false;
                }
                if (command.equals(".") && currentInput.contains(".")) {
                    return; // Prevent multiple decimals
                }
                currentInput += command;
                display.append(command);
            } else if ("+-*/".contains(command)) {
                processOperator(command.charAt(0));
            } else if (command.equals("=")) {
                calculate();
            }
        }
    }

    private void processOperator(char newOperator) {
        if (!currentInput.isEmpty()) {
            if (operator != ' ') {
                display.append("=\n");
                calculate();
            }
            result = Double.parseDouble(currentInput);
            operator = newOperator;
            display.append(newOperator + "\n");
            currentInput = "";
        }
    }

    private void calculate() {
        if (!currentInput.isEmpty()) {
            double secondOperand = Double.parseDouble(currentInput);
            switch (operator) {
                case '+': result += secondOperand; break;
                case '-': result -= secondOperand; break;
                case '*': result *= secondOperand; break;
                case '/':
                    if (secondOperand == 0) {
                        display.append("Error: Division by zero\n");
                        return;
                    }
                    result /= secondOperand;
                    break;
            }
            display.append("=\n" + result + "\n");
            currentInput = String.valueOf(result);
            operator = ' ';
            newCalculation = true;
        }
    }

    private void clearLastChar() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(display.getText().substring(0, display.getText().length() - 1));
        }
    }

    private void clearAll() {
        display.setText("");
        currentInput = "";
        result = 0;
        operator = ' ';
        newCalculation = true;
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
