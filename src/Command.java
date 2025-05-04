/**
 * Command.java
 * <p>
 * Record of commands to be sent over the socket and executed on the server side
 */
import java.io.Serializable;
import java.util.List;

public record Command(DeviceType deviceType, String command, List<String> args) implements Serializable {
}
