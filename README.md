# trafikk-lights
A simple trafikk light simulation using sockets.

Description of the project. 
Name : Traffic lights simulator.

OS compatibility : 
1. Windows 64 bit. 
2. Linux 64 bit.
       This software has not been tested for compatibilities. (not tested on macintosh IOS).


Software description.

How to use the software.
	Server:
	After staring the program you will see a gui user-interface.
	The interface is pretty simple. There are five components in the interface.
The client list, which is on the right side. It lets you see the address and port the client are connected from.
On the top you will find the time widget with lets you send time sequence to the traffic lights. You simply drag the widgets (sets time). And send the appropriate command to have the client.
Below the 'times widget' you will find a display that display simple message as to what command the server has sent to which client
Below the display you have the input area. This is where you type in your commands that you want to send to the client.
--List of valid commands:
		1. /time → This command sends a time sequence (the times on the times widgets) to selected client (you have to select a client from the list on the right side by clicking on it.)
		2. /timeall → This command sends a time sequence the times on the times widgets)  to all the connected clients. 
		3. /stop → This command sends a stop command to the client which stop the current time sequence on the client. 
		4. /stopall → Same as stop but to all clients
		5. /disconnect → Try to do a .clean disconnect on the selected client (you have to select a client from the list on the right side by clicking on it).



Below the input area you will find the console output area. This is where you see what really is happening in the software. The detail of this log can be increased and decreased by changing the log level. 

--Changing the log level is really easy. You will find a config.properties file under the server package. Locate the line that says  guiLoggingLevel=info . You can replace “info” to any other level from the following list: severe,warning, info, config, fine, finer, finest. Where severe is the least detailed and finest is the most detailed log respectively. 



	Client:


Description of the inner workings of the software and packages.




descriptions of all the project, and all sub-modules and libraries

3.copyright and licensing information (or "Read LICENSE")

4.Instruction to grab the documentation


Authors and contributors .

Baljit Singh Sarai
Adrian Siim Melsom
Kim Long Vu
Anh Thu Phan Le
