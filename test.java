import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
public class test{
    public static void main(String[] args){
        ArrayList<String> lines = new ArrayList<String>();
        try{
            Scanner file = new Scanner(new File("README.txt"));
            while(file.hasNext()){
                lines.add(file.nextLine());
            }
        }
        catch(Exception e){
            System.out.println("Nope.");
        }
    }
}