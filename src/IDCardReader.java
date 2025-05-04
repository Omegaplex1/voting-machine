import java.io.Serializable;

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


    private IDCardDetails currentCard;

    public IDCardReader(){
        failure = false;
        cardPresent = false;
        erased = false;
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
     * Gets card type. May return null if there is no card inserted.
     *
     * @author  Ira Khan
     * @return  card type if card is present
     *          null otherwise
     */
    public char getCardType(){
        if (cardPresent){
            return currentCard.cardType();
        }
        failure = true;
        throw new RuntimeException("ERR - Get Card Type: No card present");
    }

    /**
     * Gets card number. May return null if there is no card inserted.
     *
     * @author  Ira Khan
     * @return  card type if card is present
     *          null otherwise
     */
    public int getCardNumber(){
        if (cardPresent) {
            return currentCard.cardNumber();
        }
        failure = true;
        throw new RuntimeException("ERR - Get Card Number: No card present");
    }

    public void eraseCard(){
        currentCard = new IDCardDetails(-1,'N');
        erased = true;
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
        eraseCard();
        return failure;
    }

    @Override
    public void simulateFailure() {
        eraseCard();
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
                eraseCard();
                break;
            }
            case "readCard": {
                readCard(cmd.args().getFirst());
                break;
            }
            case "getCardNumber": {
                return "OK " + getCardNumber();
            }
            case "getCardType": {
                return "OK " + getCardType();
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
}
