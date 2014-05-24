import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner{
    private String[][] board;
    private final int ROWS = 20, COLS = 20, startCOL = 0;
    private String userInput;
    private int type, direction, diameter,
                    userIntput /** Intput. I'm so clever */, startROW, goalROW, goalCOL;
    private boolean on;
    private double slope, normal, userDoubleput;
    private final double GRAVITY = 3;
    Scanner sc = new Scanner(System.in);
    
    /** Types */
    private String[] types = {"normal", "direction", "diameter"};
    private final int NORMAL = 0, DIRECTION = 1, DIAMETER = 2;
    
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
        System.out.println("\f");
        ArrayList<String> lines = new ArrayList<String>();
        try{
            Scanner file = new Scanner(new File("README.txt"));
            while (file.hasNext()){
                lines.add(file.nextLine());
            }
        }
        catch (Exception e){
            System.out.println("Bad file.");
        }
        for (String line : lines){
            System.out.println(line);
        }
        init();
    }
    private void playGame(){
        System.out.println("\f");
        while (on){
            updateBoard();
            printBoard();
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
        if (Math.random() < 0.333) type = NORMAL;
        else if (Math.random() < 0.667) type = DIRECTION;
        else type = DIAMETER;
        
        ///Assigns value according to type
        if (type == NORMAL){
            diameter = 1;
            if (goalCOL > startCOL) //If the goal is to the right of it
                direction = (int)(Math.random() * 180);
            else if (goalCOL < startCOL) //If the goal is to the left of it
                direction = (int)(Math.random() * 180 + 180);
            else direction = 180;   //If the goal is right under. Yay free points.
        }
        else if (type == DIRECTION){
            diameter = 1;
            normal = (int)(Math.random() * 20);
        }
        else{
            normal = (int)(Math.random() * 20);
            if (goalCOL > startCOL) //If the goal is to the right of it
                direction = (int)(Math.random() * 180);
            else if (goalCOL < startCOL) //If the goal is to the left of it
                direction = (int)(Math.random() * 180 + 180);
            else direction = 180;   //If the goal is right under. Yay free points.
        }
        
        //Sets game objects' coordinates
        startROW = (int)(Math.random() * ROWS);
        boolean OK = false;
        while (!OK){    //continues to generate random points until they are not equal to one another;
            goalCOL = (int)(Math.random() * COLS);
            goalROW = (int)(Math.random() * ROWS);
            if ((startCOL != goalCOL) && (startROW != goalROW)) OK = true;
        }
        
        //Places game objects into array
        board[startROW][startCOL] = "O";
        board[goalROW][goalCOL] = "X";
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
        System.out.println("Enter in a " + types[type] + ":");
    }
    private void doInput(){
        if (type == DIRECTION || type == DIAMETER){
            takeIntput();
            if (type == DIRECTION) direction = userIntput;
            else diameter = userIntput;
        }
        else if (type == NORMAL){
            takeDoubleput();
            normal = userDoubleput;
        }
        else System.out.println("doInput() doesn't appear to be working.");
    }
    private void animateBoard(){
        boolean isDone = false;
        int r = startROW, c = startCOL;
        double incGrav = GRAVITY;
        
        if (type == DIAMETER){
            
        }
        while (!isDone){    //Prints projectile path one by one
            if (c + 1 < COLS) c += 1;
            else isDone = true;
            if (calculateNewRow(r, incGrav) < ROWS ) r = calculateNewRow(r, incGrav);
            else isDone = true;
            if ((type != DIAMETER) && board[r][c] != null && board[r][c].equals("X")) isDone = true;//Using .equals on a null didn't go too well
            else board[r][c] = "O";
            printBoard();
            wait(100);
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
    private int calculateNewRow(int row, double gravity){
        return 0;
    }
    public double toSlope(int deg){
        return Math.tan(Math.toRadians(deg));
    }
    private double getShortestDistance(){
        double shortest = Double.MAX_VALUE;
        for (int r = 0; r < ROWS; r++){
            for (int c = 0; c < COLS; c++){
                if ((board[r][c] != null) && board[r][c].equals("O")){  //.equals on null doesn't go too well, so check for null first.
                    if (distance(r, goalROW, c, goalCOL) < shortest) shortest = distance(r, goalROW, c, goalCOL);
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
        int smallestValue;
        int currScore = 100;
        int unitsOff = 0;
        if (type == DIAMETER){
            smallestValue = (int)(getShortestDistance() + 0.5);
            while (diameter > smallestValue){
                diameter--;
                unitsOff++;
                currScore -= 10;
            }
            while (diameter < smallestValue){
                diameter++;
                unitsOff++;
                currScore -= 10;
            }
        }
        else{
            smallestValue = 0;
            double closest = getShortestDistance();
            while (closest > smallestValue){
                closest--;
                unitsOff++;
                currScore -= 10;
            }
            while (closest < smallestValue){
                closest++;
                unitsOff++;
                currScore -= 10;
            }
        }
        System.out.println("You were " + unitsOff + " units off. You have been awarded " + currScore + " points.");
        switch (currScore){
            case 100: System.out.println("Amazing job!"); break;
            case 90 : System.out.println("Great job!"); break;
            case 80 : System.out.println("Good job!"); break;
            case 70 : System.out.println("Fair job."); break;
            case 60 : System.out.println("Mediocre performance."); break;
            case 50 : System.out.println("Better luck next time."); break;
            case 40 : System.out.println("You're not very good at this game."); break;
            case 30 : System.out.println("I'm not even sure how this score is possible."); break;
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
    
    /** Either calculating distance or calculating score isn't working correctly. */
    /**
     * vf = vo + at, vf^2 = vo^2 + 2ax, x = xo + vo*t + 0.5at^2
     * vf = final velocity, vo = initial velocity, a = acceleration, t = time in seconds, x = distance, xo = initial distance
     */
}