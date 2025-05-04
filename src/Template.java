/**
 * Template.java
 * <p>
 * This will act as the main template for every screen of the voting machine.
 * It contains just enough data to construct a full voting screen.
 */

import java.io.Serializable;

public class Template implements Serializable {
    public String majorHeading;
    public String descHeading;
    public int selectionCount;
    public Selection[] selections;




















    public String title;
    public String description;
    public String[] selections;
    public boolean[] buttons;
    // The user's selected vote
    public String vote;

    /**
     * Template that can be sent over the socket
     * @param title Election race title
     * @param description race description
     * @param selections candidate selection strings
     * @param buttons bool array of length 3. buttons = [hasBackButton, hasSubmitButton, hasNextButton]
     */
    public Template(String title, String description, String[] selections, boolean[] buttons) {
        try {
            this.title = title;
            this.description = description;
            this.selections = selections;
            this.buttons = buttons;
            if (title.isEmpty()) {
                throw new IllegalArgumentException("title length must be at least 1");
            }
            if (description.isEmpty()) {
                throw new IllegalArgumentException("description length must be at least 1");
            }
            if (selections.length < 1 || selections.length > 5) {
                throw new IllegalArgumentException("selections length must be at least 1 and less than 6");
            }
            if (buttons.length != 3) {
                throw new IllegalArgumentException("buttons array must be of length 3!");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            this.title = null;
            this.description = null;
            this.selections = null;
            this.buttons = null;
        }
    }

    @Override
    public String toString() {
        return "Template for: " + title;
    }

}