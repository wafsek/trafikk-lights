package server;

/**
 * Created by Baljit Singh Sarai on 01.03.16.
 * @author Baljit Singh Sarai
 */


public enum DataControl {
    SUCCESS("Success"),
    NO_CLIENT_SELECTED("No client selected"),
    COMMAND_NOT_FOUND("No such command"),
    EMPTY_CLIENT_LIST("The client list is empty");
    private final String description;


    /**
     * Returns the description of this
     * @param description
     */
    DataControl(String description) {
        this.description = description;
    }

    /**
     * Returns a description of this DataControl?
     *
     * @return a description of this DataControl?
     */
    public String getDescription() {
        return this.description;
    }
}
