package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class SenderGui extends Application{
	SenderAgent agent;
//	SenderGui(SenderAgent a){
//		agent = a; 


	public void start(Stage primaryStage) {
		SenderAgent agent = new SenderAgent();
		try {
			GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));
	     	
	        Scene scene = new Scene(grid,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Agent"+agent.pass());
		
	        //message performative
	        Label messageLabel = new Label("Message Performative");
	        grid.add(messageLabel, 0, 0); //column0 row1       
	        ObservableList<String> options = 
	        	    FXCollections.observableArrayList(
	        	        "Inform",
	        	        "Query",
	        	        "Request"
	        	    );
			ComboBox comboBox = new ComboBox(options);
	        comboBox.setMinWidth(200);
	        grid.add(comboBox, 0, 1);
	        //reciever
	        Label recieverLabel = new Label("Reciever");
	        grid.add(recieverLabel, 0, 2);
	        TextField recieverTextBox = new TextField();
	        recieverTextBox.setMinWidth(200);
	        grid.add(recieverTextBox, 0, 3);
	        //content
	        Label contentLabel = new Label("Content");
	        grid.add(contentLabel, 0, 4);
	        TextArea contentTextArea = new TextArea();
	        contentTextArea.setMaxHeight(100);
	        grid.add(contentTextArea, 0, 5);
	        //sent messages
	        Label sentMesLabel = new Label("Messages");
	        grid.add(sentMesLabel, 0, 6);
	        TextArea sentMesTextArea = new TextArea();
	        grid.add(sentMesTextArea, 0, 7);
	        
	        Button btn = new Button("Send");
	        HBox hbBtn = new HBox(10);
	        hbBtn.setAlignment(Pos.CENTER);
	        hbBtn.getChildren().add(btn);
	        grid.add(hbBtn, 0, 8);
	        
	        final Text actiontarget = new Text();
	        grid.add(actiontarget, 0, 8);
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	        	
        	//when button "execute" is triggered
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
            }
        });
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
	public static void main(String[] args) {
	//	launch(args);
		
	}


}
