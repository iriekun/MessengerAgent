package application;

//package examples.hello;

//import ontologyconnection.*;
import jade.core.Agent;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.gui.*;

import javax.swing.*;
import java.awt.event.*;

/**
This example shows a minimal agent that just prints "Hallo World!" 
and then terminates.
@author Giovanni Caire - TILAB
*/
public class AgentWithGUI extends GuiAgent {

  private Gui RA1;

  protected void setup() {
      System.out.println("Hello World! My name is " + getLocalName());
      RA1 = new Gui();
      RA1.setAgent(this);

      // Make this agent terminate
      //doDelete();
  }

  protected void onGuiEvent(GuiEvent ev) {
      RA1.setTitle("My Agent Name is: " + this.getName());
  }
}

class Gui extends JFrame implements ActionListener {

  private AgentWithGUI myAgent;
  public JButton B;
  private JToolBar jToolBar1;

  protected void frameInit() {
      super.frameInit();
      setSize(500, 300);
      setTitle("I am an Agent GUI, Click my Button to Get My Agent's Name");
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      jToolBar1 = new JToolBar();
      this.add(jToolBar1);
      B = new JButton();
      B.setFont(new java.awt.Font("Arial", 0, 14));

//  B.setSize(10, 10);
      B.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
      B.setFocusable(true);
      B.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      B.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      B.setText("Get Agent Name");
      jToolBar1.add(B);
      B.addActionListener(this);
      setVisible(true);
  }

  public void setAgent(AgentWithGUI a) {
      myAgent = a;
  }

  public void actionPerformed(java.awt.event.ActionEvent ae) {
      // TODO add your handling code here:

      GuiEvent ge = new GuiEvent(this, 1);

      if (ae.getSource() == this.B) {
          myAgent.postGuiEvent(ge);
      }
  }
}

