package server;

/**
 * Created by Baljit Singh Sarai on 03.03.16.
 * @author Baljit Singh Sarai
 */
public class CommandHandler {

    private final String[] COMMANDS = {"time","timeall","disconnect","stop","stopall"};
    private TrafficServer trafficServer;

    public CommandHandler(TrafficServer trafficServer){
        this.trafficServer = trafficServer;
    }

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
        System.out.println("ddddd");
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


    public String command(String command,Client client,Double[] times){
        byte[] msg = new byte[trafficServer.MESSAGE_SIZE];
        msg[0] = 2;
        String result = "";
        switch (command){
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
            }case "stop":{
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
