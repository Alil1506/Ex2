package assignments;
import java.io.IOException;
import java.util.*;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    private static Sheet currentSheet;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0; i<x; i=i+1) {
            for(int j=0; j<y; j=j+1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        Formtools.setSheet(this);
        eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;

        Cell c = get(x,y);
        if(c!=null) {if(c.getType() == Ex2Utils.NUMBER || c.getType() == Ex2Utils.TEXT) {
            ans = c.getData();
        } else if(c.getType() == Ex2Utils.FORM) {
            ans = eval(x, y);
        } else if(c.getType() == Ex2Utils.ERR_CYCLE_FORM) {
            ans = Ex2Utils.ERR_CYCLE;
        } else if(c.getType() == Ex2Utils.ERR_FORM_FORMAT) {
            ans = Ex2Utils.ERR_FORM;
        }
        }


        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        int row = cords.charAt(0);
        int col = cords.charAt(1);
        ans = get(row,col);
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;
        c.setData(s);

    }
    @Override
    public void eval() {
        int[][] dd = depth();
        List<Cell[]> levelOrder = new ArrayList<>();
        int maxDepth = 0;

        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                maxDepth = Math.max(maxDepth, dd[i][j]);
            }
        }

        // Evaluate cells in order of increasing depth
        for(int d = 0; d <= maxDepth; d++) {
            for(int i = 0; i < width(); i++) {
                for(int j = 0; j < height(); j++) {
                    if(dd[i][j] == d) {
                        eval(i, j);
                    }
                }
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && yy >= 0 && xx < table.length && yy < table[0].length;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];

        for(int i = 0; i < width(); i++) {
            for(int j = 0; j < height(); j++) {
                ans[i][j] = calculateCellDepth(i, j, new HashSet<>());
            }
        }

        return ans;
    }

    private int calculateCellDepth(int x, int y, Set<String> visited) {
        Cell cell = get(x, y);
        if(cell == null || cell.getType() != Ex2Utils.FORM) {
            return 0;
        }

        String formula = cell.getData().substring(1); // Remove '='
        String cellId = Ex2Utils.ABC[x] + y;

        if(visited.contains(cellId)) {
            return Ex2Utils.ERR; // Cycle detected
        }

        visited.add(cellId);
        int maxDepth = 0;

        // Find all referenced cells and get their depths
        for(String ref : findCellReferences(formula)) {
            Cell refCell = get(ref);
            if(refCell != null) {
                int refX = Arrays.asList(Ex2Utils.ABC).indexOf(ref.substring(0,1));
                int refY = Integer.parseInt(ref.substring(1));
                int depth = calculateCellDepth(refX, refY, visited);
                if(depth == Ex2Utils.ERR) {
                    return Ex2Utils.ERR;
                }
                maxDepth = Math.max(maxDepth, depth);
            }
        }

        visited.remove(cellId);
        return maxDepth + 1;
    }

    private Set<String> findCellReferences(String formula) {
        Set<String> refs = new HashSet<>();
        for(String abc : Ex2Utils.ABC) {
            int index = formula.indexOf(abc);
            while(index >= 0) {
                int endIndex = index + 1;
                while(endIndex < formula.length() && Character.isDigit(formula.charAt(endIndex))) {
                    endIndex++;
                }
                if(endIndex > index + 1) {
                    refs.add(formula.substring(index, endIndex));
                }
                index = formula.indexOf(abc, index + 1);
            }
        }
        return refs;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if(get(x,y)!=null) {
            Cell cell = get(x,y);
            if(cell.getType() == Ex2Utils.NUMBER) {
                return cell.getData();
            } else if(cell.getType() == Ex2Utils.TEXT) {
                return cell.getData();
            } else if(cell.getType() == Ex2Utils.FORM) {
                try {
                    String formula = cell.getData().substring(1); // Remove '='
                    double result = Formtools.computeFormula(formula, this);
                    return String.valueOf(result);
                } catch(RuntimeException e) {
                    return Ex2Utils.ERR_FORM;
                }
            } else if(cell.getType() == Ex2Utils.ERR_CYCLE_FORM) {
                return Ex2Utils.ERR_CYCLE;
            } else {
                return Ex2Utils.ERR_FORM;
            }
        }
        return ans;
    }

}
