
//package multiplayerGahnooJfxpong;

import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author Seth G. R. Herendeen
 *
 */
public class Client extends Application {
	GameStruct struct = new GameStruct();
	AnimationTimer timer;

	private DataInputStream fromServer;
	private DataOutputStream toServer;

	private final String HOST = "10.130.16.20";
	private final int PORT = 8420;
	private boolean isLeftPlayer = false;
	
	
	
	private String status = "";
	private String debugInfo = "";
	
	
	private boolean isGameStarted = false;

	private void connectToGameServer(Stage primaryStage) {
		try {
			// Attempt to connect to the server
			Socket socket = new Socket(HOST, PORT);
			System.out.println("Attempting to open socket");//

			// create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());
			System.out.println("Attempting to create an input stream");

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
			System.out.println("Creating output stream socket");

		} catch (Exception exc) {
			exc.printStackTrace();
		}

		// Control the game on a separate DARN thread
		new Thread(() -> {
			System.out.println("Starting thread");
			try {
				System.out.println("Receiving acknowledgement from server");
				int player = fromServer.readInt();
				System.out.println("Received integer value from server of: "+player);
				
				if (player == GameConstantsStruct.PLAYER1) {
					isLeftPlayer = true;
					Platform.runLater(() -> {
						status = "Waiting for player 2 to join";
						System.out.println(status);

					});

					// Receive startup notification from the darn server
					int startUpNotification = fromServer.readInt();
					System.out.println("Received startup notification: " + startUpNotification);

					// The other player has joined
					Platform.runLater(() -> {
						status = "Player 2 has joined. I am LEFT player";

						primaryStage.setTitle("Multiplayer Gahnoo JfxPong : " + status);

						isGameStarted = true;
					});

				} else if (player == GameConstantsStruct.PLAYER2) {

					Platform.runLater(() -> {
						status = "I am Player 2. I am RIGHT side of the screen";
						System.out.println(status);

						primaryStage.setTitle("Multiplayer Gahnoo JfxPong : " + status);

						isGameStarted = true;
					});
				}


			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}).start();

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Pane pane = new Pane();
		// Button connectToGameServerButton = new Button("Connect to game server");
		// connectToGameServerButton.setOnMouseClicked(e -> {
		connectToGameServer(primaryStage);
		// });
		// pane.getChildren().addAll(new Label("Waiting..."),
		// connectToGameServerButton);

		// primaryStage.setTitle("Gahnoo JfxPong: Waiting for players....");
		// primaryStage.setScene(new Scene(pane));
		// primaryStage.show();

		// while(!isGameStarted) {
		// System.out.println("Waiting for game to start...");
		// }

		primaryStage.setTitle("Gahnoo JfxPong: Multi-player mode -- you are ");
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
		primaryStage.getScene().setOnKeyPressed(e -> {
			try {
				handleKeyPresses(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

	}

	private void handleKeyPresses(KeyEvent e) throws IOException {
		if (!isLeftPlayer) {

			if (e.getCode() == KeyCode.UP) {
				this.struct.rightPlayer.setLayoutY(this.struct.rightPlayer.getLayoutY() - 30);
				sendMove();
			}

			if (e.getCode() == KeyCode.DOWN) {
				this.struct.rightPlayer.setLayoutY(this.struct.rightPlayer.getLayoutY() + 30);
				sendMove();
			}

		} else {

			if (e.getCode() == KeyCode.UP) {
				this.struct.leftPlayer.setLayoutY(this.struct.leftPlayer.getLayoutY() - 30);
				sendMove();
			}

			if (e.getCode() == KeyCode.DOWN) {
				this.struct.leftPlayer.setLayoutY(this.struct.leftPlayer.getLayoutY() + 30);
				sendMove();
			}
		}
		
		if (isLeftPlayer) {
			// then do the right player
			// this.struct.rightPlayer.setLayoutX(fromServer.readDouble());
			System.out.println("Attempting to receive the right Y from the server");
		//	double y = fromServer.readDouble();
			double y2 = fromServer.readDouble();
			
		//	this.debugInfo = y + " " + y2;
			
		//	this.struct.rightPlayer.setLayoutY(y);
			this.struct.leftPlayer.setLayoutY(y2);//
		} else {
			// do the left player
			// this.struct.leftPlayer.setLayoutX(fromServer.readDouble());
			System.out.println("Attempting to receive the LEFT Y from the server");
			double y = fromServer.readDouble();
		//	double y2 = fromServer.readDouble();
			
		//	this.debugInfo = y + " " + y2;
			
		//	this.struct.leftPlayer.setLayoutY(y2);
			this.struct.rightPlayer.setLayoutY(y);//
		}

	}

	private void sendMove() throws IOException {
		System.out.println("sendMove()");
		if (isLeftPlayer) {
			// Send X location of paddle
			// toServer.writeDouble(this.struct.leftPlayer.getLayoutX());
			// Send Y location of paddle
			System.out.println("Attempting to send the y position of the left player paddle");
			toServer.writeDouble(this.struct.leftPlayer.getLayoutY());
//			System.out.println("resync");
//			toServer.writeDouble(this.struct.rightPlayer.getLayoutY());

		} else {
			// Send X location of paddle
			// toServer.writeDouble(this.struct.rightPlayer.getLayoutX());
			// Send Y location of paddle
			System.out.println("Attempting to send the y position of the right player paddle");
			toServer.writeDouble(this.struct.rightPlayer.getLayoutY());
//			System.out.println("Resync");
//			toServer.writeDouble(this.struct.leftPlayer.getLayoutY());
		}
	}

	private Parent createContent() {

		Pane pane = new Pane();
		pane.setPrefSize(this.struct.WIDTH, this.struct.HEIGHT);
		pane.setStyle("-fx-background-color: black;");

		pane.getChildren().addAll(this.struct.line, this.struct.leftPlayer, this.struct.rightPlayer, this.struct.ball);
		// vbox.getChildren().addAll(pane,this.struct.textLabelNote,this.struct.textFieldScore);

		this.timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				try {
					updateAndRedraw();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		this.timer.start();// this line was imperative

		return pane;
	}

	private void updateAndRedraw() throws IOException {
		if (isGameStarted) {

			double x = this.struct.ball.getLayoutX();
			double y = this.struct.ball.getLayoutY();

			if (x <= 10 && y > this.struct.leftPlayer.getLayoutY() && y < this.struct.leftPlayer.getLayoutY() + 80) {
				this.struct.directionVelocity = this.struct.speedX;
			}

			if (x >= this.struct.WIDTH - 12.5 && y > this.struct.rightPlayer.getLayoutY()
					&& y < this.struct.rightPlayer.getLayoutY() + 80) {
				this.struct.speedX++;
				this.struct.directionVelocity = -this.struct.speedX;
			}

			if (y <= 0) {
				this.struct.dy = this.struct.speedY;
			}

			if (y >= this.struct.HEIGHT - 5) {
				this.struct.dy = -this.struct.speedY;
			}

			this.struct.ball.setLayoutX(this.struct.ball.getLayoutX() + this.struct.directionVelocity);
			this.struct.ball.setLayoutY(this.struct.ball.getLayoutY() + this.struct.dy);

			// handle player input from the otherside!
			// if (isLeftPlayer) {
			// // then do the right player
			// // this.struct.rightPlayer.setLayoutX(fromServer.readDouble());
			// System.out.println("Attempting to receive the right Y from the server");
			// this.struct.rightPlayer.setLayoutY(fromServer.readDouble());
			// } else {
			// // do the left player
			// // this.struct.leftPlayer.setLayoutX(fromServer.readDouble());
			// System.out.println("Attempting to receive the LEFT Y from the server");
			// this.struct.leftPlayer.setLayoutY(fromServer.readDouble());
			// }

		}

		// ARTIFICIAL INTELLIGENT RESPONSE TO WHERE BALL IS LOCATED
		// if (x < this.struct.WIDTH && this.struct.playerOne.getLayoutY() > y) {
		// this.struct.playerOne.setLayoutY(this.struct.playerOne.getLayoutY() - 5);
		// }

		// if (x < this.struct.WIDTH && this.struct.playerOne.getLayoutY() + 80 < y) {
		// this.struct.playerOne.setLayoutY(this.struct.playerOne.getLayoutY() + 5);
		// }

	}
}

class GameConstantsStruct {
	public static int PLAYER1 = 1; // Indicate player 1
	public static int PLAYER2 = 2; // Indicate player 2
	public static int DRAW = 3; // Indicate a draw
	public static int CONTINUE = 4; // Indicate to continue
}
