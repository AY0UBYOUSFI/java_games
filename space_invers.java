import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvaders extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int PLAYER_WIDTH = 20;
    private static final int PLAYER_HEIGHT = 20;
    private static final int ALIEN_WIDTH = 20;
    private static final int ALIEN_HEIGHT = 20;
    private static final int PROJECTILE_SIZE = 5;
    private static final int PLAYER_SPEED = 5;
    private static final int ALIEN_SPEED = 2;
    private static final int PROJECTILE_SPEED = 5;
    private static final int PROJECTILE_INTERVAL = 30000; // Projectile interval in milliseconds

    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean isShooting = false;

    private Point player;
    private ArrayList<Point> aliens;
    private ArrayList<Point> playerProjectiles;
    private ArrayList<Point> alienProjectiles;
    private Timer alienProjectileTimer;
    private int score = 0;

    public SpaceInvaders() {
        setTitle("Space Invaders");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        player = new Point(WIDTH / 2 - PLAYER_WIDTH / 2, HEIGHT - PLAYER_HEIGHT - 10);
        aliens = new ArrayList<>();
        playerProjectiles = new ArrayList<>();
        alienProjectiles = new ArrayList<>();

        // Initialize aliens
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 10; x++) {
                aliens.add(new Point(x * 30 + 30, y * 30 + 30));
            }
        }

        // Timer for alien projectiles
        alienProjectileTimer = new Timer(PROJECTILE_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && !gameWon) {
                    generateAlienProjectile();
                }
            }
        });
        alienProjectileTimer.start();

        Timer timer = new Timer(20, new ActionListener() {
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
                    movePlayer(-PLAYER_SPEED);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    movePlayer(PLAYER_SPEED);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !isShooting) {
                    isShooting = true;
                    playerProjectiles.add(new Point(player.x + PLAYER_WIDTH / 2 - PROJECTILE_SIZE / 2, player.y - PROJECTILE_SIZE));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isShooting = false;
                }
            }
        });

        setFocusable(true);
    }

    private void update() {
        // Move aliens
        for (Point alien : aliens) {
            alien.y += ALIEN_SPEED;
        }

        // Move player projectiles
        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
            Point projectile = playerProjectiles.get(i);
            projectile.y -= PROJECTILE_SPEED;
            if (projectile.y < 0) {
                playerProjectiles.remove(i);
            }
        }

        // Move alien projectiles
        for (int i = alienProjectiles.size() - 1; i >= 0; i--) {
            Point projectile = alienProjectiles.get(i);
            projectile.y += PROJECTILE_SPEED;
            if (projectile.y > HEIGHT) {
                alienProjectiles.remove(i);
            }
        }

        // Check collisions
        checkCollisions();
    }

    private void movePlayer(int dx) {
        if (player.x + dx >= 0 && player.x + dx <= WIDTH - PLAYER_WIDTH) {
            player.x += dx;
        }
    }

    private void generateAlienProjectile() {
        Random random = new Random();
        int index = random.nextInt(aliens.size());
        Point alien = aliens.get(index);
        alienProjectiles.add(new Point(alien.x + ALIEN_WIDTH / 2 - PROJECTILE_SIZE / 2, alien.y + ALIEN_HEIGHT));
    }

    private void checkCollisions() {
        // Check player projectile collisions with aliens
        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
                        Point projectile = playerProjectiles.get(i);
            for (int j = aliens.size() - 1; j >= 0; j--) {
                Point alien = aliens.get(j);
                if (new Rectangle(projectile.x, projectile.y, PROJECTILE_SIZE, PROJECTILE_SIZE)
                        .intersects(new Rectangle(alien.x, alien.y, ALIEN_WIDTH, ALIEN_HEIGHT))) {
                    playerProjectiles.remove(i);
                    aliens.remove(j);
                    score += 10; // Increase score
                    if (aliens.isEmpty()) {
                        gameWon = true;
                    }
                    break;
                }
            }
        }

        // Check alien projectile collisions with player
        for (int i = alienProjectiles.size() - 1; i >= 0; i--) {
            Point projectile = alienProjectiles.get(i);
            if (new Rectangle(projectile.x, projectile.y, PROJECTILE_SIZE, PROJECTILE_SIZE)
                    .intersects(new Rectangle(player.x, player.y, PLAYER_WIDTH, PLAYER_HEIGHT))) {
                gameOver = true;
                break;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw player
        g.setColor(Color.WHITE);
        g.fillRect(player.x, player.y, PLAYER_WIDTH, PLAYER_HEIGHT);

        // Draw aliens
        g.setColor(Color.GREEN);
        for (Point alien : aliens) {
            g.fillRect(alien.x, alien.y, ALIEN_WIDTH, ALIEN_HEIGHT);
        }

        // Draw player projectiles
        g.setColor(Color.YELLOW);
        for (Point projectile : playerProjectiles) {
            g.fillRect(projectile.x, projectile.y, PROJECTILE_SIZE, PROJECTILE_SIZE);
        }

        // Draw alien projectiles
        g.setColor(Color.RED);
        for (Point projectile : alienProjectiles) {
            g.fillRect(projectile.x, projectile.y, PROJECTILE_SIZE, PROJECTILE_SIZE);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);

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

        // Draw game won message
        if (gameWon) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g.getFontMetrics();
            String gameWonMsg = "You Won! Press any key to play again.";
            int x = (WIDTH - fm.stringWidth(gameWonMsg)) / 2;
            int y = (HEIGHT - fm.getHeight()) / 2;
            g.drawString(gameWonMsg, x, y + fm.getAscent());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SpaceInvaders().setVisible(true);
            }
        });
    }
}
