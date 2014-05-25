public class test4{
    public static void main (String [] args){
        Runner w = new Runner();
        for (int i = 0; i <= 360; i++){
            System.out.print(i + "degrees: ");
            System.out.println(w.toSlope(i));
            wait(100);
        }
    }
    private static void wait(int ms){
        try{
            Thread.sleep(ms);
        }
        catch (Exception e){
            System.out.println("This computer has insomnia.");
        }
    }
}