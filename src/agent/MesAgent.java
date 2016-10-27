package agent;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
/*These imports are needed to Query AMS for all active agents*/
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;



public class MesAgent extends GuiAgent {
	//	private MesAgentGui myGui;
	private AgentGui myGui;
	private String receiver = "";
	private String content = "";
	private String messagePerformative="";
	private String allConversation = ""; // all the conversations will be appended here
	public  static ArrayList<String> agentList;


	protected void setup() {
		// Printout a welcome message
		System.out.println("Messenger agent "+getAID().getName()+" is ready.");

		AMSAgentDescription [] agents = null;
		agentList	=	new ArrayList();

		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults ( new Long(-1) );
			agents = AMSService.search( this, new AMSAgentDescription (), c );
		}
		catch (Exception e) {  }
		
		//search all agents and put into agentList
		//this feature is used when sending messages to agent within the same platform
		for (int i=0; i<agents.length;i++){
			AID agentID = agents[i].getName();
			if(agentID.getLocalName().equals("ams") || agentID.getLocalName().equals("rma") || agentID.getLocalName().equals("df"))
				continue;

			agentList.add(agentID.getName());
		}

		// Create and show the GUI
		myGui = new AgentGui(this);
		myGui.setTitle(this.getAID().getLocalName());

		/*This part will query the AMS to get list of all active agents in all containers*/

		/** This piece of code, to register services with the DF, is explained
		 * in the book in section 4.4.2.1, page 73 
		 **/
		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("messenger-agent");
		sd.setName(getLocalName()+"-Messenger agent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new ReceiveMessage());
	}

	/**
	 * Agent clean-up
	 **/
	protected void takeDown() {
		// Dispose the GUI if it is there
		if (myGui != null) {
			myGui.dispose();
		}

		// Printout a dismissal message
		System.out.println("Seller-agent "+getAID().getName()+"terminating.");

		/** This piece of code, to deregister with the DF, is explained
		 * in the book in section 4.4.2.1, page 73 
		 **/
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	public class SendMessage extends OneShotBehaviour {
		public void action() {
			ACLMessage msg;
			if(messagePerformative.equals("Propose")){
				msg = new ACLMessage(ACLMessage.PROPOSE);
			}else{
				msg = new ACLMessage(ACLMessage.REQUEST);
			}
			
			//receiver : host_ip<space>agent_name@platform_id
			String destDetails[] = receiver.split(" ");
			//for distributed system (agent accross platform) 
			AID r = new AID(destDetails[1], AID.ISGUID);
			r.addAddresses("http://"+destDetails[0]+":7778/acc");			    

			msg.addReceiver(r);
			//	msg.addReceiver(new AID(receiver, AID.ISGUID));  
			msg.setLanguage("English");
			msg.setContent(content);
			send(msg);
			allConversation += "\n"+getAID().getName()+": "+msg.getContent();
			myGui.setMessageTextArea(allConversation);

			System.out.println(getAID().getName()+"sent Message to "+receiver+"\n"+
					"The Content of My Message is::>"+ msg.getContent());
		}
	}
	public class ReceiveMessage extends CyclicBehaviour {

		// Variable to Hold the content of the received Message
		private String Message_Performative;
		private String Message_Content;
		private String SenderName;
		
		public void action() {
			ACLMessage msg = receive();
			if(msg != null) {

				Message_Performative = msg.getPerformative(msg.getPerformative());
				Message_Content = msg.getContent();
				//	SenderName = msg.getSender().getLocalName();
				SenderName = msg.getSender().getName();
				System.out.println(" ****I Received a Message***" +"\n"+
						"The Sender Name is::>"+ SenderName+"\n"+
						"The Content of the Message is::> " + Message_Content + "\n"+
						"::: And Performative is::> " + Message_Performative + "\n");
				System.out.println("ooooooooooooooooooooooooooooooooooooooo");
				allConversation += "\n"+SenderName+": "+Message_Content;
				myGui.setMessageTextArea(allConversation);

			}

		}
	}
	//get all entered input from gui agent
	public void getFromGui(final String messageType, final String dest, final String messages) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				messagePerformative = messageType;
				receiver = dest; //the correct input from the gui is <ipaddress> <agent@platform-id>
				content = messages;
			}
		} );
	}
	//predefined function of GuiAgent - see postGuiEvent() in MesAgentGui.java
	@Override
	protected void onGuiEvent(GuiEvent ge) {
		addBehaviour(new SendMessage());
		//check out function getType() of GuiEvent for post and get more than one guievent
		//postGuiEvent(class, int) ->int is return to onGuiEvent() to specify which event is return 

	}

}