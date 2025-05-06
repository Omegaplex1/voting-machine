import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
    Expected behavior of the ID Card

    Boolean cardInserted();
        Returns whether a card is inserted

    Note: Not included in original RDD, see what we can do about that
    Void readCard(String cardInput);
        Parses an input string and converts it to the card details.

    char getCardType();
        Returns the type of the card

    int getCardNumber();
        Returns the identifying number of the card

    void eraseCard();
        Erases the magnetic strip on the card.

    void ejectCard();
        Returns the card back to the owner

    boolean failure();
        Detects any and all failures with the device

    Notes:
        - The "card" will be fed into the ID card reader through a scanner
        - Whether the card is erased will be represented as a boolean
 */

public class IDCardReader extends Device implements Serializable {

    private boolean failure;
    private boolean cardPresent;
    boolean erased;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private CardHolder cardHolder;



    private IDCardDetails currentCard;

    public IDCardReader() throws IOException {
        failure = false;
        cardPresent = false;
        erased = false;

        this.socket = new Socket("localhost", 5001);

        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        output.writeObject("IDCardReader");
        output.flush();

    }

    /**
     * Method to set the cardholder
     * @param cardHolder
     */
    public void setCardHolder(CardHolder cardHolder) {
        this.cardHolder = cardHolder;
    }


    public boolean cardInserted(){
        return cardPresent;
    }

    public void readCard(String cardInput){
        //Formatted like so:
        // T12345678
        // Card type is first letter.
        if(cardInput.length() != 9){

            return;
        }

        char type = cardInput.charAt(0);

        if(type != 'V' && type != 'O'){
            failure = true;
            return;
        }

        //Process the card number and update boolean variables
        int cardNumber = Integer.parseInt(cardInput.substring(1));
        cardPresent = true;
        currentCard = new IDCardDetails(cardNumber,type);
    }

    /**
     * Method to check if the card is inserted
     * @return true if a card is inserted false if not
     * @throws IOException ..
     * @throws ClassNotFoundException ..
     */
    public boolean checkForCard() throws IOException, ClassNotFoundException {
        output.writeObject("CARD_INSERTED");
        output.flush();

        boolean cardInserted = (boolean) input.readObject();
//        System.out.println("CardInserted Status: " + cardInserted);

        // update the status
        cardPresent = cardInserted;
        return cardInserted;
    }

    /**
     * Method to get the card type from the terminal window
     * @return 'N' for no card 'A' for admin and 'V' for voter
     * @throws IOException ..
     * @throws ClassNotFoundException ..
     */
    public String getCardType() throws IOException, ClassNotFoundException {
        String cardType = "N";
        if (cardPresent){
            output.writeObject("CARD_TYPE");
            output.flush();

            cardType = (String) input.readObject();
        }

//        System.out.println("CardType Status: " + cardType);
        return cardType;
    }

    /**
     * Method to return the card number
     * @return card number or "" for none
     * @throws IOException ..
     * @throws ClassNotFoundException ..
     */
    public String getCardNumber() throws IOException, ClassNotFoundException {
        String cardNumber = "";
        if (cardPresent){
            output.writeObject("CARD_NUMBER");
            output.flush();

            cardNumber = (String) input.readObject();
        }

//        System.out.println("CardNumber Status: " + cardNumber);
        return cardNumber;
    }

    public void eraseCard() throws IOException {
        if (cardPresent){
            output.writeObject("ERASE_CARD");
            output.flush();
        }
    }

    public void ejectCard1() throws IOException {
        if (cardPresent){
            output.writeObject("EJECT_CARD");
            output.flush();
        }
    }


    public IDCardDetails ejectCard(){
        //Do NOT return the card if it is not erased.
        if(!erased){
            failure = true;
            throw new RuntimeException("ERR - Eject Card: Card is not erased");
        }
        //Can't eject an unpresent card
        else if(!cardPresent){
            failure = true;
            throw new RuntimeException("ERR - Eject Card: No card is present");
        }
        else{
            //No card anymore, so no erasure
            erased = false;
            cardPresent = false;
            return currentCard;
        }
    }

    /**
     * Gets the fail state of the device
     * @author Ira Khan
     * @return Whether the device has failed
     */
    @Override
    public boolean failure(){
        try {
            eraseCard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return failure;
    }

    @Override
    public void simulateFailure() {
        try {
            eraseCard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.failure = true;
        System.err.println("ID Failure.");
    }

    @Override
    public String handleCommand(Command cmd) {
        if (cmd.deviceType() != DeviceType.IDCARD) {
            throw new IllegalArgumentException("Invalid device type command.");
        }

        switch (cmd.command()) {
            case "ejectCard": {
                return "OK " + ejectCard().toString();
            }
            case "eraseCard": {
                try {
                    eraseCard();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "readCard": {
                readCard(cmd.args().getFirst());
                break;
            }
            case "getCardNumber": {
                try {
                    return "OK " + getCardNumber();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case "getCardType": {
                try {
                    return "OK " + getCardType();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case "simulateFailure": {
                simulateFailure();
                break;
            }
            default: {
                return "Invalid command for " + cmd.deviceType() + ": " + cmd.command() + ".";
            }
        }

        return "OK";
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        IDCardReader idCardReader = new IDCardReader();

        while (true){
//            System.out.println("Checking for a card");
            idCardReader.checkForCard();
            idCardReader.getCardType();
            idCardReader.getCardNumber();
//            System.out.println("Waiting for one second ");
            Thread.sleep(1000);

            idCardReader.eraseCard();
        }


    }
}
