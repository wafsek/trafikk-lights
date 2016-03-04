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
	After staring the program you will see a gui.
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
        6. /commands → Show all the available commands that can be used at the command line.


Below the input area you will find the console output area. This is where you see what really is happening in the software. The detail of this log can be increased and decreased by changing the log level. 

--Changing the log level is really easy. You will find a config.properties file under the server package. Locate the line that says  guiLoggingLevel=info . You can replace “info” to any other level from the following list: severe,warning, info, config, fine, finer, finest. Where severe is the least detailed and finest is the most detailed log respectively. 



	Client:

	After starting the program you will see a gui.
	There are 4 components on the interface.
	The 3 input ares (TextField) represents 3 different inputs needed in order to establish a lasting connection with the server.
	The first field must contain the host ID (e.g "localhost").
	The second field must contain the port number to be used. (Default : 12345 this can be modified)
	The third is a mandatory handshake (This HAS to be "handshake", which is defined by the host).
	The fourth component is a button, which sends a connect request when pressed, using the data from the fields above.

	If all the data was verified, and a connection was established, there will pop up a window with 2 components.
	An input field (TextField) and a button.
	The field is a second mandatory handshake (this HAS to be "protocol", which is defined by the host).
	The button sends another handshake containing the data from the input field.

	Disclaimers:
	- There is no way to disconnect the socket via the client but to terminate the entire program instance.
	This should be done from the server.
	- Only the server can decide what state the traffic light is in (Idle or Running).
	- Only the server can change the time intervals in the sequence of which the lights change.



3. copyright and licensing information.


    Traffic lights simulator . A simple traffic lights simulation using sockets.
    Copyright (C) 2016 Baljit Singh Sarai ,Adrian Siim Melsom, Kim Long Vu

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.


4.Instruction to grab the documentation
    javadocs/index.html 

5.Authors and contributors .
    
    Baljit Singh Sarai
    Adrian Siim Melsom
    Kim Long Vu
    Anh Thu Phan Le
