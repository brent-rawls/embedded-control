/*
	code to use a bluetooth gamepad for ECE 306 final project (car control by TCP), Nov 2018 by Brent Rawls
	
	this code makes use of Jamepad (https://github.com/williamahartman/Jamepad), and is used with permission

*/
package com.brent.embedded;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class EmbeddedControl {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		
		//DEBUG MODE
		//must be TRUE when no connection to IOT Module is being made
		//must be FALSE to send messages
		boolean debugMode = true;
		//IF HAVING PROBLEMS, CHECK THIS FIRST
		
		String command = null;
		String lastCommand = null;
	    boolean finalCommand = false;
		boolean sendCommands = true;
		
		//connect to controller
		ControllerManager cm = new ControllerManager();
		cm.initSDLGamepad();
		
		//update this string when IP address has changed
		String ip = "192.168.1.15";
		String prefix = "@1234";
		
		EmbeddedClient client = new EmbeddedClient();
		if(!debugMode) {
			client.startConnection(ip, 51276);
		}
		
		while(true) {
			ControllerState cState = cm.getState(0);			  
			lastCommand = command;
			command = "";
			
			//SPECIAL COMMANDS
			if (cState.a && cState.lb && cState.rb) {								//B + START: small right intercept arc
				command = "051";
				finalCommand = true;
			} else if (cState.x && cState.lb && cState.rb) {								//Y + START: big right intercept arc
				command = "052";
				finalCommand = true;
			} else if (cState.y && cState.lb && cState.rb) {                                          //START: exit linefollowing
				command = "099";
				sendCommands = true;
				finalCommand = true;
			} else if (cState.b && cState.lb && cState.rb) {                                           //SELECT: straightline intercept
				command = "090"; 
				finalCommand = true;
			} else if (cState.lb && cState.rb && cState.back) {
				sendCommands = true;
				finalCommand = false;
			} else if(cState.lb && cState.x) {											//L + Y: speed up left PWM
				command = "010";
			} else if (cState.rb && cState.x) {                                 //R + Y: speed up right PWM
				command = "001";
			} else if (cState.lb && cState.a) {   								//L + B: slow left PWM
				command = "020";
			} else if (cState.rb && cState.a) {   								//R + B: slow right PWM
				command = "002";
			} else if (cState.a && cState.lb && cState.rb) {								//B + START: small right intercept arc
				command = "051";
			} else if (cState.x && cState.lb && cState.rb) {								//Y + START: big right intercept arc
				command = "052";
			} else if (cState.y && cState.lb && cState.rb) {                                          //START: exit linefollowing
				command = "099";
				sendCommands = true;
				finalCommand = true;
			} else if (cState.b && cState.lb && cState.rb) {                                           //SELECT: straightline intercept
				command = "090"; 
				finalCommand = true;
			} else if (cState.lb && cState.rb && cState.back) {
				sendCommands = true;
				finalCommand = false;
			} else if (cState.lb && cState.rb) {
				command = ""; //blank command, don't do anything
			}
			
			else {
				//MOVEMENT COMMANDS
				//SPEED CONTROLS: Y and B
				if(cState.a) {													//B button is pressed-- speed down!
					command += "1";
				} else if(cState.x) {											//Y button is pressed-- speed up!
					command += "4";
				} else {														//none button is pressed-- speed regular!
					command += "2";
				}
				//D-PAD DIRECTIONS
				if(cState.leftStickY > 0.9) {
					if(cState.leftStickX > 0.9) {								//fwd+right
						command +="52";
					} else if(cState.leftStickX < -0.9) {						//fwd+left
						command +="25";
					} else {													//fwd
						command +="55";
					}
				} else if(cState.leftStickX > 0.9) {                            //right spin
					command += "40";
				} else if(cState.leftStickX < -0.9) {							//left spin
					command += "04";
				} else if(cState.leftStickY < -0.9) {							//go backward
					command += "66";
				} else {														//NO DPAD INPUT (stopped)
					command += "00";
				}
			} //end of movement commands
				
			//append CR
			command += '\r';
			//now, do something with the command (debugMode, or realMode(tm)?)			
			
			//under these conditions, don't transmit:
			//command is duplicate of previous AND no DPAD input
			//command is empty
			if(command.equals(lastCommand) && (cState.leftStickX > -0.9 && cState.leftStickX < 0.9 && cState.leftStickY > -0.9 && cState.leftStickY < 0.9)) {
			}
			else if (command.equals("\r")) {
			}
			else if (sendCommands){
				System.out.print(prefix + command);
				if(!debugMode) {
					client.sendCommand(prefix + command);
				}
				if(finalCommand) {
					sendCommands = false;
					finalCommand = false;
				}
			}
			//use a delay to slow signal sending
			Thread.sleep(100);
			
			
		} //end of while loop
		
	}

}
