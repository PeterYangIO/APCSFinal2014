import java.util.Scanner;
import java.text.DecimalFormat;

public class Runner{
    private String[][] board;
    private String userInput;
    private int startCol = 0, type, userIntput /** Intput. I'm so clever */,
        startRow, goalRow, goalCol, totalScore = 0, roundsPlayed = 0;
    private boolean on;
    private double slope, speed, direction, userDoubleput, doubleRow, averageScore = 0;
    private final double GRAVITY = 2;
    private final double INC_GRAV_FACTOR = 1.3;
    Scanner sc = new Scanner(System.in);
    DecimalFormat df = new DecimalFormat("###.00");
    
    /** Types */
    private String[] types = {"speed", "direction"};
    private final int SPEED = 0, DIRECTION = 1;
    
    /** States */
    private static final int MENU = 0, PLAY = 1, SETTINGS = 2, INSTRUCTIONS = 3, EXIT = 4;
    private static int currState;
    
    /** Settings */
    private int[] defaults = {20, 20, 100};
    private int[] settings = new int[3];
    private final int ROWS = 0, COLS = 1, WAIT_TIME = 2;
    public static void main(String[] args){
        Runner game = new Runner();
        game.init();
        while (currState != EXIT){
            if (currState == MENU) game.mainMenu();
            else if (currState == SETTINGS) game.doSettings();
            else if (currState == INSTRUCTIONS) game.printInstructions();
            else if (currState == PLAY) game.playGame();
            else if (currState == EXIT) System.out.println("Thank you for using this program.\n    -Peter Yang");
            else System.out.println("So you're telling me you expected your code to work?");
        }
    }
    /** The operating states */
    private void init(){
        on = true;
        currState = MENU;
        for (int i = 0; i < defaults.length; i++){
            settings[i] = defaults[i];
        }
    }
    private void mainMenu(){
        System.out.print("\f");
        System.out.println("Select one of the following options.");
        System.out.println(">Play\n>Settings\n>Instructions\n>Exit");
        userInput = "";
        testMenu(); //Recursive, ending when the user finally enters a valid option
        if (userInput.equals("Play")){
            currState = PLAY;
            board = new String[settings[ROWS]][settings[COLS]];
        }
        else if (userInput.equals("Settings")) currState = SETTINGS;
        else if (userInput.equals("Instructions")) currState = INSTRUCTIONS;
        else currState = EXIT;
    }
    private void doSettings(){
        System.out.print("\f");
        System.out.println("To set a value, type in the name and new value on separate lines.");
        System.out.println("To reset the options back to defaults, type \"Defaults\".");
        System.out.println("When done, type \"Exit\" to return to the main menu.");
        System.out.println("Rows: " + settings[ROWS]);
        System.out.println("Columns: " + settings[COLS]);
        System.out.println("Wait Time: " + settings[WAIT_TIME] + "ms");
        
        testSettings();
        if (userInput.equals("Rows")){
            takeIntput();
            settings[ROWS] = userIntput;
        }
        else if (userInput.equals("Cols")){
            takeIntput();
            settings[COLS] = userIntput;
        }
        else if (userInput.equals("Wait Time")){
            takeIntput();
            settings[WAIT_TIME] = userIntput;
        }
        else if (userInput.equals("Defaults")){
            for (int i = 0; i < defaults.length; i++){
                settings[i] = defaults[i];
            }
        }
        else if (userInput.equals("Exit")){
            currState = MENU;
        }
        else{
            System.out.println("doSettings() isn't working properly.");
        }
    }
    private void printInstructions(){
        FileReader.readFile("README.TXT");
        System.out.println("Hit enter to return to the menu.");
        sc.nextLine();
        currState = MENU;
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
        if (temp.equalsIgnoreCase("Play") || temp.substring(0, 1).equalsIgnoreCase("p")) userInput = "Play";
        else if (temp.equalsIgnoreCase("Settings") || temp.substring(0, 1).equalsIgnoreCase("s")) userInput = "Settings";
        else if (temp.equalsIgnoreCase("Instructions") || temp.substring(0, 1).equalsIgnoreCase("i")) userInput = "Instructions";
        else if (temp.equalsIgnoreCase("Exit") || temp.substring(0, 1).equalsIgnoreCase("e")) userInput = "Exit";
        else{
            System.out.println("Not a valid option. Please try again.");
            testMenu();
        }
    }
    
    /** Used in doSettings() */
    private void testSettings(){
        String temp = sc.nextLine();
        if (temp.equalsIgnoreCase("Rows") || temp.substring(0, 1).equalsIgnoreCase("r")) userInput = "Rows";
        else if (temp.equalsIgnoreCase("Columns") || temp.substring(0, 1).equalsIgnoreCase("c")) userInput = "Cols";
        else if (temp.equalsIgnoreCase("Wait Time") || temp.substring(0, 1).equalsIgnoreCase("w")) userInput = "Wait Time";
        else if (temp.equalsIgnoreCase("Defaults") || temp.substring(0, 1).equalsIgnoreCase("d")) userInput = "Defaults";
        else if (temp.equalsIgnoreCase("Exit") || temp.substring(0, 1).equalsIgnoreCase("e")) userInput = "Exit";
        else{
            System.out.println("Not a valid option. Please try again.");
            testSettings();
        }
    }
    /** Used in playGame() */
    private void updateBoard(){
        //Sets type
        if (Math.random() < 0.5) type = SPEED;
        else  type = DIRECTION;
        
        ///Assigns value according to type
        if (type == SPEED) direction = (int)(Math.random() * 180 - 90);
        else speed = (int)(Math.random() * settings[COLS] + 10);    //type == DIRECTION in this case (Probably)
        
        //Sets game objects' coordinates
        startRow = (int)(Math.random() * (settings[ROWS] * 0.5)); //Spawns the start within the middle 50% of the grid.
        doubleRow = startRow;   //The double value is used in calculation to retain accuracy
        boolean OK = false;
        while (!OK){ //Continues to generate random points until they are not equal to one another;
            goalCol = (int)(Math.random() * settings[COLS]);
            goalRow = (int)(Math.random() * settings[ROWS]);
            if ((startCol != goalCol) && (startRow != goalRow)) OK = true;
        }
        
        //Places game objects into array
        board[startRow][startCol] = "O";
        board[goalRow][goalCol] = "X";
    }
    private void printBoard(){
        System.out.print("\f");
        for (int i = 0; i <= settings[COLS]; i++) System.out.print(" * ");
        System.out.println();
        
        for (int r = 0; r < settings[ROWS]; r++){
            System.out.print("* ");
            for (int c = 0; c < settings[COLS]; c++){
                if (board[r][c] == null) System.out.print("   ");
                else if (board[r][c].equals("O")) System.out.print(" O ");
                else System.out.print(" X ");
            }
            System.out.println("*");
        }
        
        for (int i = 0; i <= settings[COLS]; i++) System.out.print(" * ");
        System.out.println();
        
        System.out.println("Total Score: " + totalScore + "\t\t\t" + "Average per Round: " + averageScore);
        if (type != SPEED) System.out.println("Speed: " + speed); 
        if (type != DIRECTION) System.out.println("Direction: " + direction  + " degrees");
    }
    private void doInput(){
        takeDoubleput();
        if (type == DIRECTION){
            direction = userDoubleput;
        }
        else if (type == SPEED){
            speed = userDoubleput;
        }
        else System.out.println("doInput() doesn't appear to be working.");
    }
    private void animateBoard(){
        boolean isDone = false, hitEdge = false;
        int r = startRow, c = startCol, prevRow = r, difference = 0;
        double incGrav = GRAVITY;
        String arbitraryValue = "";
        while (!isDone){    //Prints projectile path one by one
            if (c + 1 < settings[COLS]) c++;
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
                    if (c == settings[COLS] - 1) hitEdge = true;
                    if (difference > 1){ //Interpolates between the two calculated points by drawing a line between them
                        if (prevRow > r){
                            for (int i = prevRow - 1; i > r ; i--){
                                if (i <= prevRow + (difference/2)) board[i][c - 1] = "O";
                                else board[i][c] = "O";
                                printBoard();
                                wait(settings[WAIT_TIME]);
                            }
                        }
                        else{
                            for (int i = prevRow + 1; i < r; i++){
                                if (i <= prevRow + (difference/2)) board[i][c - 1] = "O";
                                else board[i][c] = "O";
                                printBoard();
                                wait(settings[WAIT_TIME]);
                            }
                        }
                    }
                    board[r][c] = "O";
                }
                else if (!hitEdge){ //Interpolates points until it reaches the end of the board
                    if (prevRow > r){
                        while (prevRow > 1){// && board[prevRow][c] != null && !board[prevRow][c].equals("X")){
                            prevRow--;
                            //if (board[prevRow][c] != null && !board[prevRow][c].equals("X")) break;
                            board[prevRow][c] = "O";
                            printBoard();
                            wait(settings[WAIT_TIME]);
                        }
                    }
                    else{
                        while (prevRow < settings[ROWS] - 1){// && board[prevRow][c] != null && !board[prevRow][c].equals("X")){
                            prevRow++;
                            //if (board[prevRow][c] != null && !board[prevRow][c].equals("X")) break;
                            board[prevRow][c] = "O";
                            printBoard();
                            wait(settings[WAIT_TIME]);
                        }
                    }
                }
            }
            printBoard();
            incGrav *= INC_GRAV_FACTOR; //Gravity gets stronger over time
            wait(settings[WAIT_TIME]);
        }
        printScore();
    }
    private void clearBoard(){
        board = new String[settings[ROWS]][settings[COLS]];
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
         * The gravity factor is multiplied by the reciprocal (or fraction of balance to simulate reality) of the speed.
         * The higher the speed is, the less of a factor the gravity will have.
         * The new gravity factor pulls down the y (increases the row).
         * This is now the y (row) and returns that
         */
        doubleRow += slope;
        newRow = doubleRow;
        gravFactor = gravity * (1/speed);
        newRow += gravFactor;
        return (int)newRow;
    }
    private double toSlope(double deg){
        if (deg == 360) return 0; //Not sure why the Math.tan doesn't return 0
        return Math.tan(Math.toRadians(deg));
    }
    private double getShortestDistance(){
        double shortest = Double.MAX_VALUE;
        for (int r = 0; r < settings[ROWS]; r++){
            for (int c = 0; c < settings[COLS]; c++){
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
        if (unitsOff != 0){
            System.out.print("You were " + unitsOff);
            if (unitsOff != 1) System.out.print(" units");
            else System.out.print(" unit");
            System.out.print(" off.");
        }
        else System.out.print("Right on target! ");
        System.out.println(" You have been awarded " + currScore + " points.");
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
            default : System.out.println("I don't think you should be allowed near a computer.");
        }
        if (currScore < 0) System.out.println("Yes, this game is programmed to give you negative points. Deal with it.");
        totalScore += currScore;
        roundsPlayed++;
        averageScore = (double)(totalScore)/roundsPlayed;
        averageScore = new Double(df.format(averageScore)).doubleValue();
    }
}