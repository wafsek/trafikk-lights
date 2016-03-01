package server;

/**
 * Created by Baljit Singh Sarai on 01.03.16.
 * @author Baljit Singh Sarai
 */


public enum DataControl {
    SUCCESS("Success"),
    NO_CLIENT_SELECTED("No client stected");
    private final String description;


    /**
     * Returns the description of this
     * @param description
     */
    DataControl(String description) {
        this.description = description;
    }
}
