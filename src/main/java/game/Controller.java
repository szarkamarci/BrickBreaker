package game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import json.JsonAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static game.GameSettings.*;


public class Controller{

    final static private Logger logger = LogManager.getLogger(Controller.class.getName());

    /**
     * Score counter.
     */
    int score = 0;

    @FXML
    private AnchorPane scene;

    @FXML
    private Circle ball;

    @FXML
    private Rectangle slider;

    @FXML
    private Rectangle bottomZone;

    @FXML
    private Button startButton;

    /**
     * Built in JavaFX object, helps tracking mouse pos.
     */
    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    /**
     * Ball direction.
     */
    double directionX = BALL_DIRECTION_X;
    double directionY = BALL_DIRECTION_Y;

    /**
     * Timeline helps make the game interactive, this object contains handle method.
     */
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        /**
         * This method contains all the necessary checks.
         * @param actionEvent "this param is all the things that happening on the screen"
         */
        @Override
        public void handle(ActionEvent actionEvent) {
            movePaddle();

            //Checks at every frame if it collided with the paddle
            checkCollisionSlider(slider);
            ball.setLayoutX(ball.getLayoutX() + directionX);
            ball.setLayoutY(ball.getLayoutY() + directionY);

            if(!bricks.isEmpty()){
                bricks.removeIf(brick -> checkCollisionBrick(brick));
            } else {
                timeline.stop();
            }

            checkCollisionScene(scene);
            checkCollisionBottomZone();
        }
    }));


    /**
     * Initializing the paddles.
     */
    public void initialize() {
        slider.setWidth(SLIDER_START_SIZE);
        //How many times will timeline run? = forever
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * The button that starts the game.
     * @param event mouse click event
     */
    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);
        startGame();
    }

    /**
     * The method that starts the game.
     */
    public void startGame(){
        logger.trace("game starting");
        createBricks();
        timeline.play();
    }

    /**
     * Checks the collisions with the scene.
     * @param node returns the scene information
     */
    public void checkCollisionScene(Node node){
        Bounds bounds = node.getBoundsInLocal();
        boolean rightBorder = ball.getLayoutX() >= (bounds.getMaxX() - ball.getRadius());
        boolean leftBorder = ball.getLayoutX() <= (bounds.getMinX() + ball.getRadius());
        boolean bottomBorder = ball.getLayoutY() >= (bounds.getMaxY() - ball.getRadius());
        boolean topBorder = ball.getLayoutY() <= (bounds.getMinY() + ball.getRadius());

        if (rightBorder || leftBorder) {
            directionX *= CHANGE_DIRECTION;
        }
        if (bottomBorder || topBorder) {
            directionY *= CHANGE_DIRECTION;
        }
    }


    /**
     * Checking if the ball is collided with any of the bricks.
     * @param brick needs a brick param
     * @return true or false depends if it hits a brick
     */
    public boolean checkCollisionBrick(Rectangle brick){

        //Check if the ball contacted with a brick
        if(ball.getBoundsInParent().intersects(brick.getBoundsInParent())){
            boolean rightBorder = ball.getLayoutX() >= ((brick.getX() + brick.getWidth()) - ball.getRadius());
            boolean leftBorder = ball.getLayoutX() <= (brick.getX() + ball.getRadius());
            boolean bottomBorder = ball.getLayoutY() >= ((brick.getY() + brick.getHeight()) - ball.getRadius());
            boolean topBorder = ball.getLayoutY() <= (brick.getY() + ball.getRadius());

            if (rightBorder || leftBorder) {
                directionX *= CHANGE_DIRECTION;
            }
            if (bottomBorder || topBorder) {
                directionY *= CHANGE_DIRECTION;
            }

            //Make the game harder
            slider.setWidth(slider.getWidth() - (0.10 * slider.getWidth()));
            //Removes the brick that collided with the ball
            scene.getChildren().remove(brick);
            score++;
            logger.debug("new score: {}", score);
            return true;
        }
        return false;
    }

    /**
     * Method for creating bricks.
     */
    public void createBricks(){
        double width = BRICK_WIDTH;
        double height = BRICK_HEIGHT;

        int spaceCheck = 1;

        for (double i = height; i > 0 ; i = i - 50) {
            for (double j = width; j > 0 ; j = j - 25) {
                if(spaceCheck % 2 == 0){
                    Rectangle rectangle = new Rectangle(j,i,30,30);
                    rectangle.setFill(Color.RED);
                    scene.getChildren().add(rectangle);
                    bricks.add(rectangle);
                }
                spaceCheck++;
            }
        }
    }

    /**
     *Moves paddle
     */
    public void movePaddle(){
        Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
        double sceneXPos = bounds.getMinX();

        double xPos = robot.getMouseX();
        double paddleWidth = slider.getWidth();

        if(xPos >= sceneXPos + (paddleWidth/2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            slider.setLayoutX(xPos - sceneXPos - (paddleWidth/2));
        } else if (xPos < sceneXPos + (paddleWidth/2)){
            slider.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth/2)){
            slider.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    /**
     * Checks if the ball collided with the slider.
     * @param slider slider param
     */
    public void checkCollisionSlider(Rectangle slider){

        if(ball.getBoundsInParent().intersects(slider.getBoundsInParent())){

            boolean rightBorder = ball.getLayoutX() >= ((slider.getLayoutX() + slider.getWidth()) - ball.getRadius());
            boolean leftBorder = ball.getLayoutX() <= (slider.getLayoutX() + ball.getRadius());
            boolean bottomBorder = ball.getLayoutY() >= ((slider.getLayoutY() + slider.getHeight()) - ball.getRadius());
            boolean topBorder = ball.getLayoutY() <= (slider.getLayoutY() + ball.getRadius());

            if (rightBorder || leftBorder) {
                directionX *= CHANGE_DIRECTION;
            }
            if (bottomBorder || topBorder) {
                directionY *= CHANGE_DIRECTION;
            }
        }
    }

    /**
     * Checks if the ball collided with the bottom zone
     */
    public void checkCollisionBottomZone(){
        if(ball.getBoundsInParent().intersects(bottomZone.getBoundsInParent())){
            timeline.stop();
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            startButton.setVisible(true);

            slider.setWidth(SLIDER_START_SIZE);

            directionX = BALL_DIRECTION_X;
            directionY = BALL_DIRECTION_Y;

            ball.setLayoutX(300);
            ball.setLayoutY(300);

            JsonAppender.append(score);
            WinScreen.gameOver(score).show();
            score = 0;
            logger.debug("final score: {}", score);
            logger.trace("Game over");
            logger.trace("Score set to zero");
        }
    }
}