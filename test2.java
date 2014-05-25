public class test2{
    public final int ROWS = 20, COLS = 20;
    public String[][] board = new String[ROWS][COLS];
    public int goalROW = 20;
    public int goalCOL = 20;
    public int diameter = 5;
    public static void main (String [] args){
        test2 t = new test2();
        t.addCircle(10, 10);
        t.printBoard();
    }
    private void addCircle(int centRow, int centCol){
        //(x - h)^2 + (y - k)^2 = r^2
        //h, k = center
        //(x - h )^2 = r^2 -(y-k)^2)
        //x = Math.sqrt(Math.pow(r, 2) - Math.pow(y-k, 2)) + h
        //x = Maty.sqrt(Math.pow(radius, 2) - Math.pow(y - r, 2)) + c;
        int startCol = 0;   //calculate this
        int endCol = 10;    //calculate this
        int newCol, newRow; 
        for (int i = startCol; i < endCol; i++){
            newCol = i;
            newRow = circleEquation(newCol, goalROW, goalCOL, diameter/2);
            if (!(newRow < ROWS || newRow < 0)){    //else is out of bounds and doesn't attempt to draw out of index
                board[newCol][newRow] = "X";
                //how2otherside of circle
            }
        }
    }
    private int circleEquation(int currCol, int centRow, int centCol, int radius){
        //returns a row value from an inputted col
        int difference = currCol - centRow;
        if (difference < 0) difference = -difference;
        return (int)(Math.sqrt(Math.pow(radius, 2) - Math.pow(difference, 2)) + centCol);
    }
    
    private void printBoard(){
        System.out.print("\f");
        for (int i = 0; i <= COLS; i++) System.out.print(" * ");
        System.out.println();
        
        for (int r = 0; r < ROWS; r++){
            System.out.print("* ");
            for (int c = 0; c < COLS; c++){
                if (board[r][c] == null) System.out.print("   ");
                else if (board[r][c].equals("O")) System.out.print(" O ");
                else System.out.print(" X ");
            }
            System.out.println("*");
        }
        
        for (int i = 0; i <= COLS; i++) System.out.print(" * ");
        System.out.println();
    }
}