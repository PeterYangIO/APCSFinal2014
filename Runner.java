public class Runner{
    public static void main(String[] args){
        GameCode game = new GameCode();
        game.init();
        while (true){
            if (game.currState == game.MENU) game.mainMenu();
            else if (game.currState == game.SETTINGS) game.doSettings();
            else if (game.currState == game.INSTRUCTIONS) game.printInstructions();
            else if (game.currState == game.PLAY) game.playGame();
            else if (game.currState == game.EXIT){
                System.out.println("Thank you for using this program.\n    -Peter Yang");
                break;
            }
            else System.out.println("So you're telling me you expected your code to work?");
        }
    }
}