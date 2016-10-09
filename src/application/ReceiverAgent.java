package application;


/**
   This example shows a minimal agent that just prints "Hello     
   World!" 
   The Agent also returns its name
   and then terminates.
 */
import jade.core.Agent;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import jade.util.leap.*;
import jade.gui.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

import java.util.Iterator;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

public class ReceiverAgent extends Agent {

    protected void setup() {

 /** Registration with the DF */
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ReceiverAgent");
        sd.setName(getName());
        sd.setOwnership("ExampleReceiversOfJADE");
        sd.addOntologies("ReceiverAgent");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
        DFService.register(this,dfd);
        } catch (FIPAException e) {
        System.err.println(getLocalName()+" registration with DF unsucceeded. Reason: "+e.getMessage());
        doDelete();
        }

        System.out.println("Hello World! My name is " + getAID().getLocalName());
        ReceiveMessage rm = new ReceiveMessage();
        addBehaviour(rm);
    }

    public class ReceiveMessage extends CyclicBehaviour {

        // Variable to Hold the content of the received Message
        private String Message_Performative;
        private String Message_Content;
        private String SenderName;
        private String MyPlan;


        public void action() {
            //Receive a Message
            ACLMessage msg = receive();
            if(msg != null) {

                Message_Performative = msg.getPerformative(msg.getPerformative());
                Message_Content = msg.getContent();
                SenderName = msg.getSender().getLocalName();


                System.out.println("***I Received a Message***" +"\n"+
                            "The Sender Name is:"+ SenderName+"\n"+
                            "The Content of the Message is::> " + Message_Content+"\n"+
                            "::: And Performative is:: " + Message_Performative);
                //Reply to the Message
                if (Message_Performative.equals("REQUEST")&& Message_Content.equals("Hello How Are You?")) {

                    ACLMessage out_msg = new ACLMessage(ACLMessage.INFORM);
                    out_msg.addReceiver(new AID(SenderName, AID.ISLOCALNAME));
                    out_msg.setLanguage("English");
                    out_msg.setContent("I am Fine Thank You");
                    send(out_msg);
                    System.out.println("****I Replied to::> " + SenderName+"***");
                    System.out.println("The Content of My Reply is:" + out_msg.getContent());
                    System.out.println("ooooooooooooooooooooooooooooooooooooooo");

                }

            }

        }
    }
}

