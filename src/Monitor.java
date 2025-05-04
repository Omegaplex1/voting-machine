public class Monitor {
    private Latch latch;
    private VotingControl vc;
    private SDCardPort sdCard1;
    private SDCardPort sdCard2;
    private SDCardPort sdCard3;
    private Screen screen;
    private Printer printer;

    // todo : Find a mechanism to constantly moitor these devices to make sure that no errors occur
    // just going to have all of these objects monitored every couple of seconds
    public Monitor(Latch latch,
                   SDCardPort sdCard1,  SDCardPort sdCard2,
                   SDCardPort sdCard3, Screen screen,
                   Printer printer){

        
        this.latch = new Latch(); // the latch


        // init all the SD cards
        this.sdCard1 = new SDCardPort(1, SDMode.WRITE_ONLY);
        this.sdCard2 = new SDCardPort(2, SDMode.WRITE_ONLY);
        this.sdCard3 = new SDCardPort(3, SDMode.WRITE_ONLY);

        // init screen driver
        this.screen = new Screen();

        // init the printerDriver
        this.printer = new Printer();

    }

    /**
     * MEthod to set the voting controller refrence
     * @param vc ..
     */
    public void setRefToVotingControl(VotingControl vc){
        this.vc = vc;
    }
    
}
