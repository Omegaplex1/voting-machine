import java.io.IOException;

public class Monitor {
    private Latch latch;
    private VotingControl vc;
    private SDCardPort sdCard1;
    private SDCardPort sdCard2;
    private SDCardPort sdCard3;
    private Screen screen;
    private Printer printer;
    private Battery battery;

    // just going to have all of these objects monitored every couple of seconds
    public Monitor(Latch latch,
                   SDCardPort sdCard1,  SDCardPort sdCard2,
                   SDCardPort sdCard3, Screen screen,
                   Printer printer, Battery battery) throws IOException {

        
        this.latch = new Latch(); // the latch


        // init all the SD cards
        this.sdCard1 = new SDCardPort(1, SDMode.WRITE_ONLY);
        this.sdCard2 = new SDCardPort(2, SDMode.WRITE_ONLY);
        this.sdCard3 = new SDCardPort(3, SDMode.WRITE_ONLY);

        // init screen driver
        this.screen = new Screen();

        // init the printerDriver
        this.printer = new Printer();

        // init the battery
        this.battery = battery;

        // start a new thread
        new Thread(() -> {
            try {
                startMonitoring();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }


    /**
     * Method to start monitoring the failures
     */
    private void startMonitoring() throws InterruptedException {
        while (true){

            // just some error checking to make sure that we are actually not monitoring before voting controller has started
            while (vc == null){
                System.out.println("Waiting for voting control to start...");
                Thread.sleep(1000);
            }
//            System.out.println(":");

            // wait for 1 second
//            System.out.println("(In Monitor) Waiting for 1 second...");
            Thread.sleep(1000);

//            System.out.println("(In Monitor) Monitoring Devices for Failures...");

//            sdCard1.simulateFailure();

            if (this.sdCard1.failure()){
                System.out.println("SDCard 1 failure");
                vc.sendMessage("Failure");
                break;
            }
            if (this.sdCard2.failure()){
                System.out.println("SDCard 2 failure");
                vc.sendMessage("Failure");
                break;
            }
            if (this.sdCard3.failure()){
                System.out.println("SDCard 3 failure");
                vc.sendMessage("Failure");
                break;
            }
            if (this.screen.failure()){
                System.out.println("Screen failure");
                vc.sendMessage("Failure");
                break;
            }
            if (this.printer.failure()){
                System.out.println("Printer failure");
                vc.sendMessage("Failure");
                break;
            }
            if (this.latch.failure()){
                System.out.println("Latch failure");
                vc.sendMessage("Failure");
                break;
            }
        }
    }

    /**
     * Method to set the voting controller refrence
     * @param vc ..
     */
    public void setRefToVotingControl(VotingControl vc){
        this.vc = vc;
    }
    
}
