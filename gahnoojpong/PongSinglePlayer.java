package gahnoojpong;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
    Javafx look-alike implementation of a classic arcade game that rhymes with "wrong"
    Copyright (C) 2018 Seth G. R. Herendeen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 * @author Seth G. R. Herendeen
 */
class GameStruct {
	public Line line = this.createCenterLine();
	public Rectangle playerOne = this.createPlayerOne();
	public Rectangle playerTwo = this.createPlayerTwo();
	public Circle ball = this.createBall();

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

/**
 * 
 * @author <b>Seth G. R. Herendeen</b> <br />
 *         References/inspirations:
 *         <li>https://youtu.be/n5Kz5CcquXs</li>
 *         <li>http://www.dummies.com/programming/java/javafx-binding-properties/</li>
 *         <li>A pong game that I made in ~2016 in C#</li>
 *
 */
public class PongSinglePlayer extends Application {

	GameStruct struct = new GameStruct();
	AnimationTimer timer;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("JPong: Single-player mode");
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
		primaryStage.getScene().setOnKeyPressed(e -> {
			handleKeyPresses(e);
		});

	}

	private void handleKeyPresses(KeyEvent e) {
		if (e.getCode() == KeyCode.UP)
			this.struct.playerTwo.setLayoutY(this.struct.playerTwo.getLayoutY() - 30);
		if (e.getCode() == KeyCode.DOWN)
			this.struct.playerTwo.setLayoutY(this.struct.playerTwo.getLayoutY() + 30);

	}

	private Parent createContent() {
		Pane pane = new Pane();
		pane.setPrefSize(this.struct.WIDTH, this.struct.HEIGHT);
		pane.setStyle("-fx-background: black");

		pane.getChildren().addAll(this.struct.line, this.struct.playerOne, this.struct.playerTwo, this.struct.ball);
		this.timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				updateAndRedraw();

			}
		};
		this.timer.start();// this line was imperative

		return pane;
	}

	private void updateAndRedraw() {
		double x = this.struct.ball.getLayoutX();
		double y = this.struct.ball.getLayoutY();

		if (x <= 10 && y > this.struct.playerOne.getLayoutY() && y < this.struct.playerOne.getLayoutY() + 80) {
			this.struct.directionVelocity = this.struct.speedX;
		}

		if (x >= this.struct.WIDTH - 12.5 && y > this.struct.playerTwo.getLayoutY()
				&& y < this.struct.playerTwo.getLayoutY() + 80) {
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

		if (x < this.struct.WIDTH && this.struct.playerOne.getLayoutY() > y) {
			this.struct.playerOne.setLayoutY(this.struct.playerOne.getLayoutY() - 5);
		}

		if (x < this.struct.WIDTH && this.struct.playerOne.getLayoutY() + 80 < y) {
			this.struct.playerOne.setLayoutY(this.struct.playerOne.getLayoutY() + 5);
		}

	}

}
