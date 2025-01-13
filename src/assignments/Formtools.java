package assignments;

public class Formtools {

    private static Sheet sheet;  // Add this as a static field

    public static void setSheet(Sheet s) {  // Add this setter method
        sheet = s;
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isCell(String s) {
        if (s == null || s.length() < 2) {  // Cell reference must be at least 2 chars
            return false;
        }

        // Get the letter part (first character)
        String letter = s.substring(0, 1).toUpperCase();

        // Check if letter is valid
        boolean validLetter = false;
        for (String c : Ex2Utils.ABC) {
            if (letter.equals(c)) {
                validLetter = true;
                break;
            }
        }

        if (!validLetter) {
            return false;
        }

        // Get the number part (rest of string)
        String number = s.substring(1);

        // Check if number part is valid
        try {
            int row = Integer.parseInt(number);
            return row >= 0 && row < Ex2Utils.HEIGHT;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean isValidFormulaRecur(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }

        if (isNumber(s)) {
            return true;
        }

        if (isCell(s)) {
            return true;
        }

        if (s.startsWith("(") && s.endsWith(")")) {
            return isValidFormulaRecur(s.substring(1, s.length() - 1));
        }
        for (String op : Ex2Utils.M_OPS) {
            if (s.contains(op + op)) {  // Check for consecutive operators like "++"
                return false;
            }
        }


        int parenthesesCount = 0;
        int lastOpIndex = -1;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') parenthesesCount++;
            if (c == ')') parenthesesCount--;

            if (parenthesesCount == 0) {
                for (String op : Ex2Utils.M_OPS) {
                    if (i + op.length() <= s.length() &&
                            s.substring(i, i + op.length()).equals(op)) {
                        lastOpIndex = i;
                    }
                }
            }
        }

        // If we found an operator, validate both sides
        if (lastOpIndex > 0) {
            String left = s.substring(0, lastOpIndex).trim();
            String right = s.substring(lastOpIndex + 1).trim();
            return isValidFormulaRecur(left) && isValidFormulaRecur(right);
        }

        return false;
    }


    public static boolean isValidFormula(String s) {
        if (s == null || !s.startsWith("=")) // check for only one initial =
            return false;

        if ((s.startsWith("+") || s.startsWith("-")) && isNumber(s.substring(1))) // =+2 , =-7.5 , etc.
            return true;

        String f = s.substring(1); // no =
        return isValidFormulaRecur(f);
    }

    public static boolean isOnlyText(String s) {
        if (s == null) return false;
        if (s.isBlank()) return true;

        if (isNumber(s))
            return false;
        if (isValidFormula(s))
            return false;
        return true;
    }

    public static double computeFormula(String s, Sheet sheet) {
        if (s.startsWith("=")) {
            s = s.substring(1); // Remove the "=" prefix
        }

        s = s.trim();

        if (s.isBlank()) return 0;

        // Base case: if it's a number, return it
        if (isNumber(s)) {
            return Double.parseDouble(s);
        }

        // Base case: if it's a cell reference, get the value
        if (isCell(s)) {
            return ((SCell) sheet.get(s)).getValue();
        }

        // Handle parentheses by finding the innermost pair and evaluating it
        while (s.contains("(")) {
            int openIndex = -1;
            int closeIndex = -1;
            int openCount = 0;

            // Find the innermost pair of parentheses
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    if (openCount == 0) openIndex = i; // First '(' in the current level
                    openCount++;
                } else if (s.charAt(i) == ')') {
                    openCount--;
                    if (openCount == 0) {
                        closeIndex = i;
                        break; // Found the matching ')'
                    }
                }
            }

            if (openIndex == -1 || closeIndex == -1) {
                throw new RuntimeException("Unbalanced parentheses");
            }

            // Extract and evaluate the innermost expression
            String inside = s.substring(openIndex + 1, closeIndex).trim();
            double value = computeFormula(inside, sheet);

            // Replace the evaluated expression with its result in the original string
            s = s.substring(0, openIndex) + value + s.substring(closeIndex + 1);
            return computeFormula(s,sheet);
        }

        // Handle operators outside parentheses
        for (String op : Ex2Utils.M_OPS) {
            int opIndex = findTopLevelOperator(s, op);
            if (opIndex > 0) {
                String left = s.substring(0, opIndex).trim();
                String right = s.substring(opIndex + 1).trim();

                return switch (op) {
                    case "+" -> computeFormula(left, sheet) + computeFormula(right, sheet);
                    case "-" -> computeFormula(left, sheet) - computeFormula(right, sheet);
                    case "*" -> computeFormula(left, sheet) * computeFormula(right, sheet);
                    case "/" -> computeFormula(left, sheet) / computeFormula(right, sheet);
                    default -> throw new RuntimeException("Invalid operator: " + op);
                };
            }
        }

        throw new RuntimeException("Invalid formula");
    }

    // Helper function to find the top-level operator while respecting parentheses
    private static int findTopLevelOperator(String s, String op) {
        int openCount = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') openCount++;
            if (c == ')') openCount--;
            if (openCount == 0 && s.startsWith(op, i)) {
                return i;
            }
        }
        return -1; // No top-level operator found
    }
        }