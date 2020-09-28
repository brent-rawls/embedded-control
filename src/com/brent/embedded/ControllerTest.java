package com.brent.embedded;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class ControllerTest {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub

		
		ControllerManager cm = new ControllerManager();
		cm.initSDLGamepad();
		
		EmbeddedClient client = new EmbeddedClient();
		Scanner scan = new Scanner(System.in);
		
		//update this string when IP address has changed
		/*String ip = "10.123.20.1";
		client.startConnection(ip, 51276);
		
		String command = null;
		*/
		
		//Print a message when the "A" button is pressed. Exit if the "B" button is pressed 
		//or the controller disconnects.
		while(true) {
		  ControllerState currState = cm.getState(0);
		  
		  if(!currState.isConnected) {
			System.out.println("No connection.");
		    break;
		  }

		  //System.out.println("X: " + currState.leftStickX + "\tY: " + currState.leftStickY );			  

		  if(currState.leftStickX > 0.9) {
			  System.out.print("RIGHT ");
		  }
		  else if(currState.leftStickX < -0.9) {
			  System.out.print("LEFT ");
		  }
		  else {
			  System.out.print("      ");
		  }

		  if(currState.leftStickY > 0.9) {
			  System.out.print("UP  ");
		  }
		  else if(currState.leftStickY < -0.9) {
			  System.out.print("DOWN");
		  }
		  else {
			  System.out.print("    ");
		  }


		  if(currState.a) {
			  System.out.print("B ");
		  }
		  else {
			  System.out.print("  ");
		  }
		  if(currState.b) {
			  System.out.print("A ");
		  }
		  else {
			  System.out.print("  ");
		  }
		  if(currState.x) {
			  System.out.print("Y ");
		  }
		  else {
			  System.out.print("  ");
		  }
		  if(currState.y) {
			  System.out.print("X ");
		  }
		  else {
			  System.out.print("  ");
		  }
		  if(currState.lb) {
			  System.out.print("L ");
		  }
		  else {
			  System.out.print("  ");
		  }
		  if(currState.rb) {
			  System.out.print("R ");
		  }
		  else {
			  System.out.print("  ");
		  }
		  if(currState.start) {
			  System.out.print("START ");
		  }
		  else {
			  System.out.print("      ");
		  }
		  if(currState.back) {
			  System.out.print("SELECT ");
		  }
		  else {
			  System.out.print("       ");
		  }

		  System.out.println("");
		}
		
		/*
		while(true) {
			command = scan.next();
			if (command.equalsIgnoreCase("quit")) break;
			client.sendCommand(command);
			System.out.println(command);
		}
		*/
		
		scan.close();
		
	}

}
