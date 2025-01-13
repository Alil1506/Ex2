package assignments;
// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;

    public SCell(String s) {
        setData(s);
        determineType(s);
    }
    private Sheet sheet;

    // Add a method to set the sheet reference
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
    public double getValue() {
        if (this.getType() == Ex2Utils.NUMBER) {
            return Double.parseDouble(this.getData());
        }

        if (this.getType() == Ex2Utils.FORM) {
            String formula = this.getData().substring(1); // Remove the '=' sign
            return Formtools.computeFormula(formula, sheet);
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
            return;
        }

        if (Formtools.isValidFormula(s)) {
            if (hasCycle(s)) {
                setType(Ex2Utils.ERR_CYCLE_FORM);
                return;
            }
            setType(Ex2Utils.FORM);
            return;
        }

        setType(Ex2Utils.ERR_FORM_FORMAT);
    }

    private boolean hasCycle(String formula) {
        // Implementation for cycle detection
        // This should check if the formula references itself directly or indirectly
        // For now, returning false as placeholder - you'll need to implement this
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
        // For cells referenced in the formula, get their order and add 1
        if (Formtools.isCell(formula)) {
            SCell referencedCell = (SCell) sheet.get(formula);
            return referencedCell.getOrder() + 1;
        }

        // For operations, calculate max order of operands and add 1
        for (String op : Ex2Utils.M_OPS) {
            int opIndex = formula.indexOf(op);
            if (opIndex > 0 && opIndex < formula.length() - 1) {
                String left = formula.substring(0, opIndex).trim();
                String right = formula.substring(opIndex + 1).trim();

                int leftOrder = calculateFormulaOrder(left);
                int rightOrder = calculateFormulaOrder(right);

                return Math.max(leftOrder, rightOrder) + 1;
            }
        }

        return 0;
    }

    //@Override
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
