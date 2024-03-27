import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 3;

    private int paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballXSpeed = BALL_SPEED;
    private int ballYSpeed = BALL_SPEED;

    public PongGame() {
        setTitle("Pong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        PongPanel panel = new PongPanel();
        add(panel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        movePaddle1(-PADDLE_SPEED);
                        break;
                    case KeyEvent.VK_S:
                        movePaddle1(PADDLE_SPEED);
                        break;
                    case KeyEvent.VK_UP:
                        movePaddle2(-PADDLE_SPEED);
                        break;
                    case KeyEvent.VK_DOWN:
                        movePaddle2(PADDLE_SPEED);
                        break;
                }
            }
        });

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void movePaddle1(int dy) {
        paddle1Y += dy;
        if (paddle1Y < 0) {
            paddle1Y = 0;
        } else if (paddle1Y > HEIGHT - PADDLE_HEIGHT) {
            paddle1Y = HEIGHT - PADDLE_HEIGHT;
        }
    }

    private void movePaddle2(int dy) {
        paddle2Y += dy;
        if (paddle2Y < 0) {
            paddle2Y = 0;
        } else if (paddle2Y > HEIGHT - PADDLE_HEIGHT) {
            paddle2Y = HEIGHT - PADDLE_HEIGHT;
        }
    }

    private void update() {
        // Move the ball
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Check collision with walls
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballYSpeed = -ballYSpeed;
        }

        // Check collision with paddles
        if (ballX <= PADDLE_WIDTH && ballY + BALL_SIZE >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = BALL_SPEED;
        } else if (ballX >= WIDTH - PADDLE_WIDTH - BALL_SIZE && ballY + BALL_SIZE >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -BALL_SPEED;
        }

        // Check if ball goes out of bounds
        if (ballX < 0) {
            // Player 2 scores
            resetBall();
        } else if (ballX > WIDTH - BALL_SIZE) {
            // Player 1 scores
            resetBall();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = BALL_SPEED;
        ballYSpeed = BALL_SPEED;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PongGame().setVisible(true);
            }
        });
    }

    private class PongPanel extends JPanel {
        public PongPanel() {
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw center line
            g.setColor(Color.WHITE);
            for (int i = 0; i < HEIGHT; i += 50) {
                g.fillRect(WIDTH / 2 - 1, i, 2, 25);
            }
            // Draw paddles
            g.fillRect(50, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillRect(WIDTH - 50 - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
            // Draw ball
            g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
        }
    }
}
