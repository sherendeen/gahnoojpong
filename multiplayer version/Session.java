
//package multiplayerGahnooJfxpong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
	private Socket player1;
	private Socket player2;
	private String sessionStatus = "";

	// Datastream objects
	private DataInputStream fromPlayer1;
	private DataOutputStream toPlayer1;
	private DataInputStream fromPlayer2;
	private DataOutputStream toPlayer2;

	public Session(Socket player1, Socket player2, String sessionStatus) {
		this.player1 = player1;
		this.player2 = player2;
		this.sessionStatus = sessionStatus;
	}

	@Override
	public void run() {
		try {

			DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
			DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
			DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
			DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());

			// Indicate to player 1 that the game has started
			toPlayer1.writeInt(1);

			while (true) {
				// get the x and y location of player 1's paddle
				// double x = fromPlayer1.readDouble();
				double y = fromPlayer1.readDouble();
				// double y2;
				sendMove(toPlayer2, y);
				
				double y2 = fromPlayer2.readDouble();
				

				// receive movement from player 2

				// x = fromPlayer2.readDouble();

				sendMove(toPlayer1, y2);

			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	/**
	 * Sends the paddle movement over the network
	 * 
	 * @param outputStream
	 *            the output stream of the selected player
	 * @param x
	 *            the x location of the paddle
	 * @param y
	 *            the y location of the paddle
	 * @throws IOException
	 *             uh oh
	 */
	private void sendMove(DataOutputStream outputStream, double y) throws IOException {
		// outputStream.writeDouble(x);
		outputStream.writeDouble(y);
		//sending
		this.sessionStatus = "Sending y of "+ y;
//		outputStream.writeDouble(otherY);
	}

}
