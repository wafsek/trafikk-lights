package server;

/**
 * This class Validates and Executes the server commands.
 * Created by Baljit Singh Sarai on 03.03.16.
 * @author Baljit Singh Sarai
 */
public class CommandHandler {

    private final String[] COM_DESC = {CommandDescription.HELP.getDiscription(),
            CommandDescription.COMMANDS.getDiscription(), CommandDescription.TIME.getDiscription(),
            CommandDescription.TIMEALL.getDiscription(), CommandDescription.DISCONNECT.getDiscription(),
            CommandDescription.DISCONNECTALL.getDiscription(), CommandDescription.STOP.getDiscription(),
            CommandDescription.STOPALL.getDiscription()};
    private final String[] COMMANDS = {"help","commands","time","timeall","disconnect","disconnectall","stop","stopall"};
    private TrafficServer trafficServer;
    public CommandHandler(TrafficServer trafficServer){
        this.trafficServer = trafficServer;
    }


    /**
     * Validates the commands given in the parameters.
     * @param command- The command.
     * @param client- The Client to whome this command is going to be send (if it is going to be send).
     * @return <code>true</code> if the commands is valid in everyway, <code>false</code> otherwise.
     */
    public  DataControl validateCommand(String command,Client client){
        boolean found = false;
        for(String co: this.COMMANDS){
            if(co != null && co.equals(command)){
                found = true;
            }
        }

        if (!found){
            return DataControl.COMMAND_NOT_FOUND;
        }
        switch (command){
            case "time":{
                if(client == null){
                    return DataControl.NO_CLIENT_SELECTED;
                }break;
            }case "timeall":{
                if(trafficServer.clientArrayList.isEmpty()){
                    return DataControl.EMPTY_CLIENT_LIST;
                }
                break;
            }case "disconnect":{
                if(client == null){
                    return DataControl.NO_CLIENT_SELECTED;
                }break;
            }case "disconnectall": {
                if (trafficServer.clientArrayList.isEmpty()) {
                    return DataControl.EMPTY_CLIENT_LIST;
                }
                break;
            }case "stop":{
                if(client == null){
                    return DataControl.NO_CLIENT_SELECTED;
                }break;
            }case "stopall":{
                if(trafficServer.clientArrayList.isEmpty()){
                    return DataControl.EMPTY_CLIENT_LIST;
                }break;
            }
        }
        return DataControl.SUCCESS;
    }


    /**
     * Excecute the command.
     * @param command- The command to be executed.
     * @param client-The Client {@link server.Client} the command is intended for.
     * @param times- The times on the server widgets.
     * @return A User-friendly message as feedback for the user.
     */
    public String command(String command,Client client,Double[] times){
        byte[] msg = new byte[trafficServer.MESSAGE_SIZE];
        msg[0] = 2;
        String result = "";
        switch (command){
            case "help": {
                result += "\n";
                for(int i = 0; i < COMMANDS.length; i++){
                    result += '/'+COMMANDS[i] + COM_DESC[i]+'\n';
                }
                break;
            }case "commands": {
                result += "\n";
                for(int i = 0; i < COMMANDS.length; i++){
                    result += '/'+COMMANDS[i] + COM_DESC[i]+'\n';
                }
                break;
            }
            case "time":{
                msg[1] = 5;
                msg[2] = 'T';
                msg[3] = 'M';
                msg[4] =  times[0].byteValue();
                msg[5] = times[1].byteValue();
                msg[6] = times[2].byteValue();
                trafficServer.send(client,msg);
                result = "Server -> "+client.getName()+": "+command+" "+msg[4]+msg[5]+msg[6]+"\n";
                break;
            }
            case "timeall":{
                msg[1] = 5;
                msg[2] = 'T';
                msg[3] = 'M';
                msg[4] =  times[0].byteValue();
                msg[5] = times[1].byteValue();
                msg[6] = times[2].byteValue();
                trafficServer.broardcast(msg);
                result = "Server -> all clients : "+command+" "+msg[4]+msg[5]+msg[6]+"\n";
                break;
            }case "disconnect":{
                trafficServer.disconnectClient(client);
            }case "disconnectall":{
                trafficServer.disconnectAall();
            }
            case "stop":{
                    msg[1] = 2;
                    msg[2] = 'S';
                    msg[3] = 'T';
                    trafficServer.send(client,msg);
                    result = "Server -> "+client.getName()+": "+command+"\n";
                    break;
            }case "stopall":{
                msg[1] = 5;
                msg[2] = 'S';
                msg[3] = 'T';
                trafficServer.broardcast(msg);
                result = "Server -> all clients : "+command+"\n";
                break;
            }
        }
        return result;
    }
}
