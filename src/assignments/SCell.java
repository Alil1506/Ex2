package assignments;
// Add your documentation below:

import java.util.HashSet;
import java.util.Set;

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;
    private Sheet sheet;

    public SCell(String s) {
        setData(s);
        determineType(s);
    }
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public double getValue() {
        try {
            if (this.getType() == Ex2Utils.NUMBER) {
                return Double.parseDouble(this.getData());
            }

            if (this.getType() == Ex2Utils.FORM) {
                String formula = this.getData().substring(1); // Remove the '=' sign
                return Formtools.computeFormula(formula, sheet);
            }
        } catch (Exception e) {
            setType(Ex2Utils.ERR_FORM_FORMAT);
        }
        throw new RuntimeException("Cell does not contain a numeric value");

    }

    private void determineType(String s) {
        if (s == null || s.isBlank()) {
            setType(Ex2Utils.TEXT);
            return;
        }
        if (Formtools.isNumber(s)) {
            setType(Ex2Utils.NUMBER);
            try {
                line = String.format("%.1f", Double.parseDouble(s));
            } catch (NumberFormatException e) {
                line = s;
            }
            return;
        }
        if (s.startsWith("=")) {  // Check if it's a formula attempt
            System.out.println("Checking formula: " + s);  // Debug print
            if (Formtools.isValidFormula(s)) {
                System.out.println("Formula is valid");  // Debug print
                try {
                    String formulaContent = s.substring(1);
                    System.out.println("Computing formula: " + formulaContent);  // Debug print
                    Formtools.computeFormula(formulaContent, sheet);
                    setType(Ex2Utils.FORM);
                    System.out.println("Formula type set to FORM");  // Debug print
                } catch (Exception e) {
                    System.out.println("Error computing formula: " + e.getMessage());  // Debug print
                    setType(Ex2Utils.ERR_FORM_FORMAT);
                }
                return;
            }
            System.out.println("Formula is not valid");  // Debug print
            setType(Ex2Utils.ERR_FORM_FORMAT);
            return;
        }

        setType(Ex2Utils.TEXT);
    }

    private boolean hasCycle(String formula) {
        if (!formula.startsWith("=")) {
            return false;
        }
        Set<String> visited = new HashSet<>();
        return detectCycle(formula.substring(1), visited);
    }
    private boolean detectCycle(String formula, Set<String> visited) {
        if (Formtools.isCell(formula)) {
            String cellId = formula;
            if (visited.contains(cellId)) {
                return true;  // Cycle detected
            }
            visited.add(cellId);

            Cell referencedCell = sheet.get(cellId);
            if (referencedCell != null && referencedCell.getType() == Ex2Utils.FORM) {
                String referencedFormula = referencedCell.getData().substring(1);
                return detectCycle(referencedFormula, visited);
            }

            visited.remove(cellId);
            return false;
        }
        for (String op : Ex2Utils.M_OPS) {
            int opIndex = formula.indexOf(op);
            if (opIndex > 0 && opIndex < formula.length() - 1) {
                String left = formula.substring(0, opIndex).trim();
                String right = formula.substring(opIndex + 1).trim();

                if (detectCycle(left, visited) || detectCycle(right, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getOrder() {
        // Add your code here
        if (type == Ex2Utils.TEXT || type == Ex2Utils.NUMBER) {
            return 0;
        }

        if (type == Ex2Utils.FORM) {
            try {
                String formula = line.substring(1); // Remove the '=' sign
                return calculateFormulaOrder(formula);
            } catch (RuntimeException e) {
                return 0;
            }
        }

        return 0;
    }

    private int calculateFormulaOrder(String formula) {
        if (formula == null || formula.isBlank()) {
            return 0;
        }
        if (Formtools.isCell(formula)) {
            Cell referencedCell = sheet.get(formula);
            if (referencedCell == null) {
                return 0;
            }
            return referencedCell.getOrder() + 1;
        }

        // For parenthesized expressions
        if (formula.startsWith("(") && formula.endsWith(")")) {
            return calculateFormulaOrder(formula.substring(1, formula.length() - 1));
        }
        int maxOrder = 0;
        int parenthesesCount = 0;
        int lastOpIndex = -1;
        for (String op : Ex2Utils.M_OPS) {
            int opIndex = formula.lastIndexOf(op);
            if (opIndex > 0) {
                String left = formula.substring(0, opIndex).trim();
                String right = formula.substring(opIndex + 1).trim();

                int leftOrder = calculateFormulaOrder(left);
                int rightOrder = calculateFormulaOrder(right);

                maxOrder = Math.max(maxOrder, Math.max(leftOrder, rightOrder) + 1);
            }
        }

        return maxOrder;
    }
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        line = s;
        determineType(s);
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        order = t;

    }
}
