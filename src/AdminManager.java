import java.io.IOException;

public class AdminManager {

    private Latch latch;
    private Screen screen;


    public AdminManager(Latch latch, Screen screen){
        this.latch = latch;
        this.screen = screen;
    }

    public void handleFailure(String failure) throws IOException {
        System.out.println("(In Admin Manager) This is the failure: " + failure);

        // set the failure screen
        screen.setFailureScreen();

        System.out.println("(In Admin Manager) Locking the Latch");
        latch.lock();
    }
}
