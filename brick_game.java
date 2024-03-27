import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreaker extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 50;
    private static final int BRICK_HEIGHT = 20;
    private static final int ROWS = 5;
    private static final int COLS = 8;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 3;

    private Point paddle;
    private Point ball;
    private int ballXDir = 1;
    private int ballYDir = -1;
    private boolean gameOver = false;
    private boolean gameWon = false;

    public BrickBreaker() {
        setTitle("Brick Breaker");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        paddle = new Point(WIDTH / 2 - PADDLE_WIDTH / 2, HEIGHT - PADDLE_HEIGHT - 30);
        ball = new Point(WIDTH / 2 - BALL_SIZE / 2, HEIGHT / 2 - BALL_SIZE / 2);

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && !gameWon) {
                    update();
                    repaint();
                }
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    movePaddle(-PADDLE_SPEED);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    movePaddle(PADDLE_SPEED);
                }
            }
        });

        setFocusable(true);
    }

    private void update() {
        // Update ball position
        ball.x += ballXDir * BALL_SPEED;
        ball.y += ballYDir * BALL_SPEED;

        // Check ball collision with walls
        if (ball.x <= 0 || ball.x >= WIDTH - BALL_SIZE) {
            ballXDir *= -1;
        }
        if (ball.y <= 0) {
            ballYDir *= -1;
        }

        // Check ball collision with paddle
        if (ball.y + BALL_SIZE >= paddle.y &&
                ball.x >= paddle.x &&
                ball.x <= paddle.x + PADDLE_WIDTH) {
            ballYDir *= -1;
        }

        // Check game over
        if (ball.y >= HEIGHT) {
            gameOver = true;
        }
    }

    private void movePaddle(int dx) {
        if (paddle.x + dx >= 0 && paddle.x + dx <= WIDTH - PADDLE_WIDTH) {
            paddle.x += dx;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw paddle
        g.setColor(Color.WHITE);
        g.fillRect(paddle.x, paddle.y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, BALL_SIZE, BALL_SIZE);

        // Draw bricks (to be implemented later)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BrickBreaker().setVisible(true);
            }
        });
    }
}
