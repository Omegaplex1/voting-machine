public class CardHolder {
    private IDCardReader idCardReader;
    private boolean isCardDetected = false;

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
}
