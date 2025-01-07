package assignments.ex2;

public class Formtools {


    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isCell(String s) {
        for (String c: Ex2Utils.ABC )

            return true;
    }


    public static boolean isValidFormulaRecur(String s) {
        try {
            computeFormula(s, sheet);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static boolean isValidFormula(String s) {
        if (s == null || !s.startsWith("="))
            return false;

        if ((s.startsWith("+") || s.startsWith("-")) && isNumber(s.substring(1))) // =+2 , =-7.5 , etc.
            return true;

        String f = s.substring(1); // no =
        return isValidFormulaRecur(f);
    }

    public static boolean isOnlyText(String s) {
        if (isNumber(s))
            return false;
        if (isValidFormulaRecur(s))
            return false;
        return true;
    }

    public static double computeFormula(String s, Sheet sheet) {
        if (s.isBlank())
            return 0;

        if (isNumber(s))  // a
            return Double.parseDouble(s);
        if (isCell(s)) // d
            return ((SCell) sheet.get(s)).getValue();

        // b
        if (s.startsWith("(") && s.endsWith(")"))
            return computeFormula(s.substring(1, s.length() - 1), sheet);

        // c
        // find op *4
        // split [:op],[op+1:]
        // check is formula recursive
        for (String op : Ex2Utils.M_OPS) {
            int op_index = s.indexOf(op);
            if (op_index > 0 && op_index < s.length() - 1)
                if (op.equals("*"))
                    return computeFormula() * computeFormula();
                else if (op.equals("+"))
                    return computeFormula() + computeFormula();
                else if (op.equals("-"))
                    return computeFormula() - computeFormula();

        }

        // not a formula
        throw new RuntimeException();
    }
}
