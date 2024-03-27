import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int CELL_SIZE = 20;
    private static final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private Direction direction = null;
    private boolean gameOver = false;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        snake = new ArrayList<>();
        snake.add(new Point(3, 0));
        snake.add(new Point(2, 0));
        snake.add(new Point(1, 0));

        generateFood();

        Timer timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    move();
                    checkCollisions();
                    repaint();
                }
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != Direction.DOWN) direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.UP) direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.RIGHT) direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.LEFT) direction = Direction.RIGHT;
                        break;
                }
            }
        });

        setFocusable(true);
    }

    private void move() {
        if (direction == null) return; // Don't move if direction is null

        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        // Check if the snake hits the walls
        if (head.x < 0 || head.x >= WIDTH / CELL_SIZE || head.y < 0 || head.y >= HEIGHT / CELL_SIZE) {
            gameOver = true;
            return;
        }

        // Check if the snake hits itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                return;
            }
        }
    }

    private void generateFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / CELL_SIZE);
        int y = random.nextInt(HEIGHT / CELL_SIZE);
        food = new Point(x, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw game over message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g.getFontMetrics();
            String gameOverMsg = "Game Over! Press any key to restart.";
            int x = (WIDTH - fm.stringWidth(gameOverMsg)) / 2;
            int y = (HEIGHT - fm.getHeight()) / 2;
            g.drawString(gameOverMsg, x, y + fm.getAscent());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
