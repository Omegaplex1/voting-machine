import java.io.*;

public class BallotTestMain {
    public static void main(String[] args) throws FileNotFoundException {

        ClassLoader classLoader = BallotTestMain.class.getClassLoader();
        File file = new File(classLoader.getResource("markUpEx1.txt").getFile());
        Ballot b = new Ballot();

        b.toString();

        System.out.println("Testing Setting Boolean to true based upon index");
        System.out.println("Index of Option: 1");
        System.out.println("Index of Proposition: 0");
        b.selectCertainOption(0, 1);
    }
}
