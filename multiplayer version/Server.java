
//package multiplayerGahnooJfxpong;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server extends Application {

	TextArea textLog = new TextArea();
	Button stopServer = new Button("Stop server");
	private final int PORT = 8420;
	int sessionNumber = 1;

	String statusFromSession = "";
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Gahnoo JfxPong Game Server");
		primaryStage.setScene(createScene());
		primaryStage.show();

		//// horrible
		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(PORT);
				Platform.runLater(() -> {
					textLog.appendText(
							new Date() + ": Server bound to  " + serverSocket.getInetAddress() + " using port " + PORT);
				});

				stopServer.setOnMouseClicked(e -> {
					try {
						textLog.appendText("Attempting to close server");
						System.out.println("Attempting to close server...");

						serverSocket.close();
						Platform.exit();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				});

				while (true) {
					Platform.runLater(() -> textLog.appendText(
							new Date() + ": Wait for players to join session " + this.sessionNumber + "\n"));

					Socket player1 = serverSocket.accept();
					textLog.appendText("Player 1:" + player1.getInetAddress().toString());
					Platform.runLater(() -> {
						System.out.println("Supposed to give player 1 IP address here");
						
						textLog.appendText(new Date() + ": Player 1 joined session " + this.sessionNumber + "\n");
						textLog.appendText(
								"Player 1's IP address is " + player1.getInetAddress().getHostAddress() + "\n");
					});

					// INFORM WHO IS PLAYER 1 (LEFT) and who is PLAYER 2 (RIGHT)
					new DataOutputStream(player1.getOutputStream()).writeInt(GameConstantsStruct.PLAYER1);
					
					// Connect to the second player
					Socket player2 = serverSocket.accept();
					
					Platform.runLater(() -> {
						System.out.println("Supposed to run this shit later");
						textLog.appendText(new Date() + ": Player 2 joined session " + this.sessionNumber + "\n");
						textLog.appendText(
								"Player 2's IP address is " + player2.getInetAddress().getHostAddress() + "\n");
					});

					// Inform player of being second
					new DataOutputStream(player2.getOutputStream()).writeInt(GameConstantsStruct.PLAYER2);

					// Display this darn session and increment the session number
					Platform.runLater(() -> {
						textLog.appendText(new Date() + ": Start a thread for session " + this.sessionNumber++ + "\n");
					});

					Platform.runLater(() ->{
						textLog.appendText(new Date( )+ this.statusFromSession + "");
					});
					
					// L A U N C H
					// A NEW THREAD FOR THIS SESSION OF TWO BEAUTIFUL PLAYERS
					new Thread(new Session(player1, player2,this.statusFromSession)).start();
					
					

				}

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}).start();
		///////// end of horrible

	}

	private Scene createScene() {
		this.textLog = new TextArea();

		ScrollPane scrollPane = new ScrollPane(this.textLog);

		VBox pane = new VBox();
		pane.getChildren().addAll(scrollPane, stopServer);

		// Create the form and make it visible, my comrade
		Scene scene = new Scene(pane, 450, 200);
		return scene;
	}
}
