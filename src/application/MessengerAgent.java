package application;

import jade.core.Agent;

public class MessengerAgent extends Agent {
	 protected void setup() {
		  	System.out.println("Hello World! My name is "+getLocalName());
		  	
		  	// Make this agent terminate
		  	doDelete();
		  } 
}
