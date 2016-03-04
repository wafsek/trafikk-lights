package server;

/**
 *
 * @author Adrian Siim Melsom
 */
public enum CommandDescription {

    HELP(" - Lists all command with description."),
    COMMANDS(" - Lists all command with description."),
    TIME(" - Sends the selected times to a selected client."),
    TIMEALL(" - Sends the selected times to all clients."),
    STOP(" - Puts the selected client in idle mode."),
    STOPALL(" - Puts all the clients in idle mode."),
    DISCONNECT(" - Disconnects the socket to the selected client."),
    DISCONNECTALL(" - Disconnects all client sockets.");
    private final String discription;

    /**
     * Returns the description of this enum
     * @param description
     */
    CommandDescription(String description) {
        this.discription = description;
    }

    /**
     * Returns a description of this enum
     * @return a description of this enum
     */
    public String getDiscription() {
        return discription;
    }


}