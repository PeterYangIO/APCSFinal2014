public class test3{
    public static void main (String [] args){
        System.out.println(distance(0, 8, 8, 0));
    }
    private static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
}