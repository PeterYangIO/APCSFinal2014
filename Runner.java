import java.util.Scanner;

public class Runner{
    private String[][] board;
    private final int ROWS = 20, COLS = 20, startCol = 0, WAIT_TIME = 100;
    private String userInput;
    private int type, direction,
                    userIntput /** Intput. I'm so clever */, startRow, goalRow, goalCol;
    private boolean on;
    private double slope, normal, userDoubleput, doubleRow;
    private final double GRAVITY = 2;
    Scanner sc = new Scanner(System.in);
    
    /** Types */
    private String[] types = {"normal", "direction"};
    private final int NORMAL = 0, DIRECTION = 1;
    
    /** States */
    private static final int MENU = 0, PLAY = 1, INSTRUCTIONS = 2, EXIT = 3;
    private static int currState;
    
    public static void main(String[] args){
        Runner game = new Runner();
        currState = MENU;
        while (currState != EXIT){
            if (currState == MENU) game.init();
            else if (currState == INSTRUCTIONS) game.printInstructions();
            else if (currState == PLAY) game.playGame();
            else if (currState == EXIT) System.out.println("Thank you for using this program.\n    -Peter Yang");
            else System.out.println("So you're telling me you expected your code to work?");
        }
    }
    
    /** The operating states */
    private void init(){
        on = true;
        System.out.print("\f");
        System.out.println("Select one of the following options.");
        System.out.println(">Play\n>Instructions\n>Exit");
        userInput = "";
        testMenu(); //Recursive, ending when the user finally enters a valid option
        if (userInput.equals("Play")){
            currState = PLAY;
            board = new String[ROWS][COLS];
        }
        else if (userInput.equals("Instructions")) currState = INSTRUCTIONS;
        else currState = EXIT;
    }
    private void printInstructions(){
        FileReader.readFile("README.TXT");
        System.out.println("Hit enter to return to the menu.");
        sc.nextLine();
        init();
    }
    private void playGame(){
        System.out.print("\f");
        while (on){
            updateBoard();
            printBoard();
            System.out.println("Enter in a " + types[type] + ":");
            doInput();
            animateBoard();
            clearBoard();
        }
        currState = MENU;
    }
    
    /** Used in init() */
    private void testMenu(){
        String temp = sc.nextLine();
        if (temp.equalsIgnoreCase("Play")) userInput = "Play";
        else if (temp.equalsIgnoreCase("Instructions")) userInput = "Instructions";
        else if (temp.equalsIgnoreCase("Exit")) userInput = "Exit";
        else {
            System.out.println("Not a valid option. Please try again.");
            testMenu();
        }
    }
    
    /** Used in playGame() */
    private void updateBoard(){
        //Sets type
        if (Math.random() < 0.5) type = NORMAL;
        else  type = DIRECTION;
        
        ///Assigns value according to type
        if (type == NORMAL) direction = (int)Math.random() * 180 - 90;
        else normal = (int)(Math.random() * 20);    //type == DIRECTION in this case (Probably)
        
        //Sets game objects' coordinates
        startRow = 5;
        doubleRow = startRow;
        boolean OK = false;
        while (!OK){    //continues to generate random points until they are not equal to one another;
            goalCol = (int)(Math.random() * COLS);
            goalRow = (int)(Math.random() * ROWS);
            if ((startCol != goalCol) && (startRow != goalRow)) OK = true;
        }
        
        //Places game objects into array
        board[startRow][startCol] = "O";
        board[goalRow][goalCol] = "X";
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
        
        if (type != NORMAL) System.out.println("Normal: " + normal); 
        if (type != DIRECTION) System.out.println("Direction: " + direction  + " degrees");
    }
    private void doInput(){
        if (type == DIRECTION){
            takeIntput();
            direction = userIntput;
        }
        else if (type == NORMAL){
            takeDoubleput();
            normal = userDoubleput;
        }
        else System.out.println("doInput() doesn't appear to be working.");
    }
    private void animateBoard(){
        boolean isDone = false;
        int r = startRow, c = startCol, prevRow = r, difference = 0;
        double incGrav = GRAVITY;
        String arbitraryValue = "";
        while (!isDone){    //Prints projectile path one by one
            if (c + 1 < COLS) c++;
            else isDone = true;
            prevRow = r;
            r = calculateNewRow(incGrav);
            difference = Math.abs(prevRow - r);
            try{
                arbitraryValue = board[r][c];   //If there's an outofbounds exception it'll go to catch.
            }
            catch (Exception e){
                isDone = true;
            }
            finally{
                if (!isDone){ //If it didn't catch, then it'll keep doing its thing.
                    if (board[r][c] != null && board[r][c].equals("X")) isDone = true; //Cannot use .equals(obj) on null as null is not an object.
                    else{
                        if (difference > 1){ //Interpolates between the two calculated points by drawing a line between them
                            if (prevRow > r){
                                for (int i = prevRow - 1; i > r ; i--){
                                    if (i <= prevRow + (difference/2)) board[i][c - 1] = "O";
                                    else board[i][c] = "O";
                                    printBoard();
                                    wait(WAIT_TIME);
                                }
                            }
                            else{
                                for (int i = prevRow + 1; i < r; i++){
                                    if (i <= prevRow + (difference/2)) board[i][c - 1] = "O";
                                    else board[i][c] = "O";
                                    printBoard();
                                    wait(WAIT_TIME);
                                }
                            }
                        }
                        board[r][c] = "O";
                    }
                }
                else{//Interpolates points until it reaches the end of the board
                    if (prevRow > r){
                        while (prevRow > 1){// && board[prevRow][c] != null && !board[prevRow][c].equals("X")){
                            prevRow--;
                            if (board[prevRow][c] != null && !board[prevRow][c].equals("X")) break;
                            else board[prevRow][c] = "O";
                            printBoard();
                            wait(WAIT_TIME);
                        }
                    }
                    else{
                        while (prevRow < ROWS - 1){// && board[prevRow][c] != null && !board[prevRow][c].equals("X")){
                            prevRow++;
                            if (board[prevRow][c] != null && !board[prevRow][c].equals("X")) break;
                            else board[prevRow][c] = "O";
                            printBoard();
                            wait(WAIT_TIME);
                        }
                    }
                }
            }
            printBoard();
            incGrav *= 2; //Gravity gets stronger over time
            wait(WAIT_TIME);
        }
        printScore();
    }
    private void clearBoard(){
        board = new String[ROWS][COLS];
        System.out.println("Hit enter to play another round, or type anything before hitting enter to return to the main menu.");
        userInput = sc.nextLine();
        if (!userInput.equals("")) on = false;  //else do nothing
    }
    
    /** Used in methods within other methods */
    private void takeIntput(){
        System.out.println("Enter a whole number.");
        boolean worked = false;
        String input = sc.nextLine();
        try{
            userIntput = Integer.parseInt(input);
            worked = true;
        }
        catch (Exception e){
            worked = false;
            System.out.println("Not a valid input. Please try again. Or not. I'm a console, not a cop.");
        }
        if (!worked) takeIntput();
    }
    private void takeDoubleput(){
        System.out.println("Enter a number (May include decimals).");
        boolean worked = false;
        String input = sc.nextLine();
        try{
            userDoubleput = Double.parseDouble(input);
            worked = true;
        }
        catch (Exception e){
            System.out.println("Not a valid input. Please try again. Or not. I'm a console, not a cop.");
            worked = false;
        }
        if (!worked) takeDoubleput();
    }
    private int calculateNewRow(double gravity){
        double slope = -toSlope(direction); //negative because row, col is really y, x
        double newRow;
        double gravFactor;
        /**
         * The row (y) is increased (row decreased) according to the slope. This assumes a gravity factor of zero. This is the current y (row).
         * The gravity factor is multiplied by the reciprocal (or fraction of balance to simulate reality) of the normal velocty.
         * The higher the normal velocity is, the less of a factor the gravity will have.
         * The new gravity factor pulls down the y (increases the row).
         * This is now the y (row) and returns that
         */
        doubleRow += slope;
        newRow = doubleRow;
        gravFactor = gravity * (1/normal);
        newRow += gravFactor;
        return (int)newRow;
    }
    public double toSlope(int deg){
        if (deg == 360) return 0;   //Not sure why the Math.tan doesn't return 0
        return Math.tan(Math.toRadians(deg));
    }
    private double getShortestDistance(){
        double shortest = Double.MAX_VALUE;
        for (int r = 0; r < ROWS; r++){
            for (int c = 0; c < COLS; c++){
                if ((board[r][c] != null) && board[r][c].equals("O")){ //Cannot use .equals(obj) on null as null is not an object.
                    if (distance(r, c, goalRow, goalCol) < shortest) shortest = distance(r, c, goalRow, goalCol);
                }
            }
        }
        return shortest;
    }
    private double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
    private void wait(int ms){
        try{
            Thread.sleep(ms);
        }
        catch (Exception e){
            System.out.println("This computer has insomnia.");
        }
    }
    private void printScore(){
        int currScore = 100;
        int unitsOff = 0;
        double closest = getShortestDistance();
        while (closest > 0){
            closest--;
            unitsOff++;
            currScore -= 10;
        }
        while (closest < 0){
            closest++;
            unitsOff++;
            currScore -= 10;
        }
        System.out.print("You were " + unitsOff);
        if (unitsOff != 1) System.out.print(" units");
        else System.out.print(" unit");
        System.out.println(" off. You have been awarded " + currScore + " points.");
        switch (currScore){
            case 100: System.out.println("Amazing job!"); break;
            case 90 : System.out.println("Great job!"); break;
            case 80 : System.out.println("Good job!"); break;
            case 70 : System.out.println("Fair job."); break;
            case 60 : System.out.println("Mediocre performance."); break;
            case 50 : System.out.println("Better luck next time."); break;
            case 40 : System.out.println("You're not very good at this game."); break;
            case 30 : System.out.println("I'm not even sure how a score this bad is possible."); break;
            case 20 : System.out.println("I feel like you just hit a random number. Don't."); break;
            case 10 : System.out.println("Just like your APCS grade."); break;
            default :
            {
                System.out.println("I don't think you should be allowed near a computer.");
                System.out.println("Deleting system32 in");
                for (int i = 10; i > 0; i--){
                    System.out.println(i);  wait(250);
                    System.out.print(".");  wait(250);
                    System.out.print(".");  wait(250);
                    System.out.print(".");  wait(250);
                }
                while (true){
                    System.out.println();
                    for (int i = 0; i < 50; i++){
                        System.out.print((int)(Math.random() + 0.5));
                    }
                }
            }
        }
    }
}