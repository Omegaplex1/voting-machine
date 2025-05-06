import java.io.IOException;

public class CardHolder {
    private IDCardReader idCardReader;
    private boolean isCardDetected = false;
    private String cardType = "";
    private long cardNumber = 0;

    public CardHolder(IDCardReader idCardReader) {
        this.idCardReader = idCardReader;
    }

    /**
     * Method to return if a card is detected
     * @return if the card is detected
     */
    public boolean cardDetected() {
        return isCardDetected;
    }

    public long getCardNumber() {
        return cardNumber;
    }
    public String getCardType() {
        return cardType;
    }

    /**
     * Method to check if a card was inserted or not
     * @return true if it was inserted false if not
     * @throws IOException ..
     * @throws ClassNotFoundException ..
     */
    public boolean checkCard() throws IOException, ClassNotFoundException {
        if (idCardReader.checkForCard()){
            this.isCardDetected = true;
            this.cardType = idCardReader.getCardType();
            this.cardNumber = Long.parseLong(idCardReader.getCardNumber());
            return true;
        }
        return false;
    }

    /**
     * Method to eject the card
     * @throws IOException ..
     * @throws ClassNotFoundException ..
     */
    public void ejectCard() throws IOException, ClassNotFoundException {
        idCardReader.ejectCard1();
        isCardDetected = false;
        cardType = "";
        cardNumber = 0;
    }


}
