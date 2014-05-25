import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader{
    public static void readFile(String directory){
        System.out.print("\f");
        ArrayList<String> lines = new ArrayList<String>();
        try{
            Scanner file = new Scanner(new File(directory));
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
    }
}