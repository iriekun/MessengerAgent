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
import javafx.application.Application;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;


public class SenderAgent extends Agent {
	public String agentName = "";
    public void setup() {

        /** Registration with the DF */
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("SenderAgent");
        sd.setName(getName());
        sd.setOwnership("ExampleReceiversOfJADE");
        sd.addOntologies("SenderAgent");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
        DFService.register(this,dfd);
        } catch (FIPAException e) {
        System.err.println(getLocalName()+" registration with DF unsucceeded. Reason: "+e.getMessage());
        doDelete();
        }
        agentName = getAID().getLocalName();

        System.out.println("Hello World! My name is " + agentName);
        SendMessage sm = new SendMessage();
        ReceiveMessage rm = new ReceiveMessage();
        addBehaviour(sm);
        addBehaviour(rm);
        Application.launch(SenderGui.class);
     
    }
    public String pass(){
    	return agentName;
    }
 
    public class SendMessage extends OneShotBehaviour {

        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID("R", AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setContent("Hello How Are You?");
            send(msg);
            System.out.println("****I Sent Message to::> R *****"+"\n"+
                                "The Content of My Message is::>"+ msg.getContent());
        }
    }

    public class ReceiveMessage extends CyclicBehaviour {

        // Variable to Hold the content of the received Message
        private String Message_Performative;
        private String Message_Content;
        private String SenderName;
        private String MyPlan;


        public void action() {
            ACLMessage msg = receive();
            if(msg != null) {

                Message_Performative = msg.getPerformative(msg.getPerformative());
                Message_Content = msg.getContent();
                SenderName = msg.getSender().getLocalName();
                System.out.println(" ****I Received a Message***" +"\n"+
                        "The Sender Name is::>"+ SenderName+"\n"+
                        "The Content of the Message is::> " + Message_Content + "\n"+
                        "::: And Performative is::> " + Message_Performative + "\n");
                System.out.println("ooooooooooooooooooooooooooooooooooooooo");

            }

        }
    }
    public class sender{
    	SenderGui gui = new SenderGui();
    	
    }
}


