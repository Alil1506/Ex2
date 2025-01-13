package assignments;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private String entry;

    public CellEntry(String input) {
        this.entry = input;
    }
    @Override
    public boolean isValid() {
        if (entry == null || entry.length() < 2) {
            return false;
        }
        String column = entry.substring(0, 1).toUpperCase();
        boolean validColumn = false;
        for (String letter : Ex2Utils.ABC) {
            if (letter.equals(column)) {
                validColumn = true;
                break;
            }
        }
        if (!validColumn) {
            return false;
        }

        // Check if remaining characters form a valid row number
        try {
            int row = Integer.parseInt(entry.substring(1));
            return row >= 0 && row < Ex2Utils.HEIGHT;
        } catch (NumberFormatException e) {
            return false;
        }
    }

        @Override
        public int getX () {
            if (!isValid()) {
                return Ex2Utils.ERR;
            }
            String column = entry.substring(0, 1).toUpperCase();
            for (int i = 0; i < Ex2Utils.ABC.length; i++) {
                if (Ex2Utils.ABC[i].equals(column)) {
                    return i;
                }
            }
            return Ex2Utils.ERR;
        }

        @Override
        public int getY () {
            if (!isValid()) {
                return Ex2Utils.ERR;
            }
            try {
                return Integer.parseInt(entry.substring(1));
            } catch (NumberFormatException e) {
                return Ex2Utils.ERR;
            }

        }
    }
