
//package multiplayerGahnooJfxpong;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Seth G. R. Herendeen
 *
 */
public class GameStruct {
	public Line line = this.createCenterLine();
	public Rectangle leftPlayer = this.createPlayerOne();
	public Rectangle rightPlayer = this.createPlayerTwo();
	public Circle ball = this.createBall();

	public Label textLabelNote = new Label("You are");
	public TextField textFieldScore = new TextField("0|0");

	public int speedX = 3;
	public int speedY = 3;
	public int directionVelocity = this.speedX;
	public int dy = this.speedY;
	public final int WIDTH = 1000;
	public final int HEIGHT = 400;

	public Rectangle createPlayerOne() {
		Rectangle playerOne = new Rectangle(10, 80, Color.BLACK);
		playerOne.setStroke(Color.WHITE);
		playerOne.setLayoutX(0);
		playerOne.setLayoutY(HEIGHT / 2 - 40);
		return playerOne;
	}

	public Rectangle createPlayerTwo() {
		Rectangle playerTwo = new Rectangle(10, 80, Color.BLACK);
		playerTwo.setStroke(Color.WHITE);
		playerTwo.setLayoutX(WIDTH - 10);
		playerTwo.setLayoutY(HEIGHT / 2 - 40);
		return playerTwo;
	}

	public Circle createBall() {
		Circle ball = new Circle(10);
		ball.setFill(Color.GREEN);
		ball.setLayoutX(WIDTH / 2);
		ball.setLayoutY(HEIGHT / 2);
		return ball;
	}

	public Line createCenterLine() {
		Line centerLine = new Line(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
		centerLine.setStroke(Color.WHITE);
		return centerLine;
	}
}