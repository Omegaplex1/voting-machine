import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;

// THIS CLASS IS GOING TO BE THE ENTRY POINT FOR THE TERMINAL WINDOW FOR USER INPUTS
public class TerminalWindowServer {
    private static boolean cardInserted = false;
    private static String cardType = "";
    private static String cardNumber = "";
    private static Map<String, ObjectOutputStream> mapOfDevices = new HashMap<String, ObjectOutputStream>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(5001);

        // start a thread to handle the user input
        new Thread(() -> {
            try {
                handleUserInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        while (true){
            System.out.println("(In TerminalWindowServer) Waiting for client connection...)");

            Socket socket = serverSocket.accept();

            new Thread(() -> {
                try {
                    handleEveryConnection(socket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    private static void handleEveryConnection(Socket socket) throws IOException, ClassNotFoundException {

            System.out.println("(In TerminalWindowServer) Connected to client");

            //
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // initial message to correlate each device with sockets.
            String initialMessage = (String) in.readObject();
            System.out.println("This is the initial message: " + initialMessage);

            // add the device to the map
            mapOfDevices.put(initialMessage, out);

            String line;

            while ((line = (String) in.readObject()) != null) {
                switch (line) {
                    case "CARD_INSERTED":
                        out.writeObject(cardInserted);
                        break;

                    case "CARD_TYPE":
                        out.writeObject(cardType);
                        break;

                    case "CARD_NUMBER":
                        out.writeObject(cardNumber);
                        break;

                    case "EJECT_CARD":
                        cardInserted = false;
                        cardType = "";
                        cardNumber = "";
                        System.out.println("Ejected Card");
                        break;

                    case "":
                        System.out.println("Invalid input");
                        break;
                }
            }
    }


    private static void handleUserInput() throws IOException {
        Scanner scanner = new Scanner(System.in);
        // we have a scanner that we will use to capture user input

        System.out.println("Type 1 to simulate Printer Failure");
        System.out.println("Type 2 to simulate Screen Failure");
        System.out.println("Type 3 to simulate SDCard1 Failure");
        System.out.println("Type 4 to simulate SDCard2 Failure");
        System.out.println("Type 5 to simulate SDCard3 Failure");
        System.out.println("Type 6 to simulate Latch Failure");
        System.out.println("Type 7 to simulate Battery Failure");
        System.out.println("Type V to simulate a Voter Card Insert");
        System.out.println("Type A to simulate a Admin Card Insert");

        while (scanner.hasNext()){
            String request = scanner.nextLine();


            switch (request){
                case "A":
                case "V":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a Card Insert");
                    System.out.println("Enter a 12-digit card number: ");
                    String cardId = scanner.nextLine();

                    if (cardId.length() == 12){
                        cardInserted = true;
                        cardType = request.equals("A") ? "A" : "V";
                        cardNumber = cardId;
                        System.out.println(cardType + " Inserted");
                    } else {
                        System.out.println("Invalid card number");
                    }

                    break;

                case "1":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a Printer Failure");
                    mapOfDevices.get("Printer").writeObject("Fail");
                    break;
                case "2":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a Screen Failure");
                    mapOfDevices.get("Screen").writeObject("Fail");
                    break;
                case "3":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a SDCard1 Failure");
                    mapOfDevices.get("SDCard1").writeObject("Fail");
                    break;
                case "4":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a SDCard2 Failure");
                    mapOfDevices.get("SDCard2").writeObject("Fail");
                    break;
                case "5":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a SDCard3 Failure");
                    mapOfDevices.get("SDCard3").writeObject("Fail");
                    break;
                case "6":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a Latch Failure");
                    mapOfDevices.get("Latch").writeObject("Fail");
                    break;
                case "7":
                    System.out.println("(In TerminalWindowServer) User wants to simulate a Battery Failure");
                    mapOfDevices.get("Battery").writeObject("Fail");
                    break;
            }
        }
    }
}
