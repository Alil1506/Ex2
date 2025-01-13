package assignments;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormtoolsTest {

    @Test
    void isNumber() {
        assertTrue(Formtools.isNumber("1234"));
        assertTrue(Formtools.isNumber("-29.00"));
        assertTrue(Formtools.isNumber("0045"));
        assertTrue(Formtools.isNumber("00000.00"));
        assertTrue(Formtools.isNumber(".45040"));
        assertTrue(Formtools.isNumber("00000.00"));
        assertFalse(Formtools.isNumber("--897"));
        assertFalse(Formtools.isNumber("1234+"));
        assertFalse(Formtools.isNumber("vc1878"));
        assertFalse(Formtools.isNumber("a1a2+a35"));
        assertFalse(Formtools.isNumber("1a + 9.00"));

    }

    @Test
    public void isValidFormulaRecur() {
            assertTrue(Formtools.isValidFormulaRecur("123"));
            assertTrue(Formtools.isValidFormulaRecur("-123"));
            assertTrue(Formtools.isValidFormulaRecur("12.34"));
            assertTrue(Formtools.isValidFormulaRecur("A1"));
            assertTrue(Formtools.isValidFormulaRecur("B2"));
            assertTrue(Formtools.isValidFormulaRecur("C10"));

            assertTrue(Formtools.isValidFormulaRecur("(123)"));
            assertTrue(Formtools.isValidFormulaRecur("(A1)"));
            assertTrue(Formtools.isValidFormulaRecur("((1+2))"));

            assertTrue(Formtools.isValidFormulaRecur("1+2"));
            assertTrue(Formtools.isValidFormulaRecur("A1+B2"));
            assertTrue(Formtools.isValidFormulaRecur("1*2"));
            assertTrue(Formtools.isValidFormulaRecur("(1+2)*3"));

            // Test invalid cases
            assertFalse(Formtools.isValidFormulaRecur(""));  // Empty string
            assertFalse(Formtools.isValidFormulaRecur("abc")); // Invalid text
            assertFalse(Formtools.isValidFormulaRecur("1+")); // Incomplete operation
            assertFalse(Formtools.isValidFormulaRecur("(1")); // Unclosed parenthesis
            assertFalse(Formtools.isValidFormulaRecur("AA1")); // Invalid cell reference
            assertFalse(Formtools.isValidFormulaRecur("1++2")); // Invalid operation
            assertFalse(Formtools.isValidFormulaRecur(null)); // Null input
        }


    @Test
    void isOnlyText() {
        assertTrue(Formtools.isOnlyText("hi"));
        assertTrue(Formtools.isOnlyText("bye"));
        assertTrue(Formtools.isOnlyText("+9++7"));
        assertTrue(Formtools.isOnlyText("X9"));
        assertTrue(Formtools.isOnlyText("Special@#$"));
        assertTrue(Formtools.isOnlyText("hello   7896"));
        assertTrue(Formtools.isOnlyText("=0.9555(1*0)-955"));
        assertTrue(Formtools.isOnlyText(""));
        assertFalse(Formtools.isOnlyText("=1234"));
        assertFalse(Formtools.isOnlyText("=1234+(34*7)"));
        assertFalse(Formtools.isOnlyText("=-30"));
        assertFalse(Formtools.isOnlyText("=A1+S10"));

        }

        @Test
        void computeFormula () {
                Sheet sheet = new Ex2Sheet();  // Create a test sheet
                assertEquals(5.0, Formtools.computeFormula("5", sheet));
                assertEquals(-3.0, Formtools.computeFormula("-3", sheet));
                assertEquals(2.5, Formtools.computeFormula("2.5", sheet));
                assertEquals(7.0, Formtools.computeFormula("=3+4", sheet));
                assertEquals(10.0, Formtools.computeFormula("=2*5", sheet));
                assertEquals(2.0, Formtools.computeFormula("=6-4", sheet));
                assertEquals(14.0, Formtools.computeFormula("=(2+5)*2", sheet));
                assertEquals(13.0, Formtools.computeFormula("=(3*3)+4", sheet));
                assertEquals(15.0, Formtools.computeFormula("=(10+5)*(2-1)", sheet));
                assertEquals(21.0, Formtools.computeFormula("=(3*(5+2))", sheet));
                assertEquals(16.0, Formtools.computeFormula("=((2+2)*(3+1))", sheet));

                sheet.set(0, 0, "10");  // Set A0 to 10
                sheet.set(0, 1, "5");   // Set A1 to 5
                assertEquals(10.0, Formtools.computeFormula("A0", sheet));
                assertEquals(15.0, Formtools.computeFormula("A0+A1", sheet));
                assertEquals(50.0, Formtools.computeFormula("A0*A1", sheet));



    }
    }


