import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class DinosaurJumpGame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 300;
    private static final int GROUND_Y = HEIGHT - 40;
    private static final int DINOSAUR_WIDTH = 60;
    private static final int DINOSAUR_HEIGHT = 60;
    private static final int TREE_WIDTH = 40;
    private static final int TREE_HEIGHT = 60;
    private static final int CLOUD_SIZE = 50;
    private static final int JUMP_SPEED = 20;
    private static final int GRAVITY = 1;
    private static final Color GROUND_COLOR = new Color(180, 180, 180);
    private static final Color SKY_COLOR = new Color(135, 206, 235);
    private static final Color CLOUD_COLOR = new Color(240, 255, 255);

    private int dinosaurX = 50;
    private int dinosaurY = GROUND_Y - DINOSAUR_HEIGHT;
    private int dinosaurYSpeed = 0;
    private int cactusSpeed = 5;
    private int treeSpawnTimer = 0;
    private ArrayList<Tree> trees;
    private ArrayList<Cloud> clouds;

    public DinosaurJumpGame() {
        setTitle("Dinosaur Jump Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        trees = new ArrayList<>();
        clouds = new ArrayList<>();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw sky
                g.setColor(SKY_COLOR);
                g.fillRect(0, 0, WIDTH, GROUND_Y);
                // Draw ground
                g.setColor(GROUND_COLOR);
                g.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);
                // Draw clouds
                g.setColor(CLOUD_COLOR);
                for (Cloud cloud : clouds) {
                    g.fillOval(cloud.x, cloud.y, CLOUD_SIZE, CLOUD_SIZE);
                }
                // Draw trees
                for (Tree tree : trees) {
                    g.setColor(tree.color);
                    g.fillRect(tree.x, tree.y, TREE_WIDTH, TREE_HEIGHT);
                }
                // Draw dinosaur
                g.setColor(Color.GREEN);
                g.fillRect(dinosaurX, dinosaurY, DINOSAUR_WIDTH, DINOSAUR_HEIGHT);
            }
        };
        add(panel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && dinosaurY == GROUND_Y - DINOSAUR_HEIGHT) {
                    dinosaurYSpeed = -JUMP_SPEED;
                }
            }
        });

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {
        // Update dinosaur position
        dinosaurY += dinosaurYSpeed;
        dinosaurYSpeed += GRAVITY;
        if (dinosaurY >= GROUND_Y - DINOSAUR_HEIGHT) {
            dinosaurY = GROUND_Y - DINOSAUR_HEIGHT;
            dinosaurYSpeed = 0;
        }
        // Spawn trees
        spawnTrees();
        // Move trees and clouds
        moveTreesAndClouds();
        // Increase dinosaur speed after 1 minute
        increaseDinosaurSpeed();
        // Check for collisions
        checkCollisions();
    }

    private void spawnTrees() {
        treeSpawnTimer++;
        if (treeSpawnTimer >= 80) { // Adjust the spawn rate as needed
            Random random = new Random();
            int treeHeight = random.nextInt(3) * TREE_HEIGHT / 2 + TREE_HEIGHT / 2;
            Tree tree = new Tree(WIDTH, GROUND_Y - treeHeight, TREE_WIDTH, treeHeight, Color.GREEN.darker());
            trees.add(tree);
            treeSpawnTimer = 0;
        }
    }

    private void moveTreesAndClouds() {
        // Move trees
        for (int i = trees.size() - 1; i >= 0; i--) {
            Tree tree = trees.get(i);
            tree.x -= cactusSpeed;
            if (tree.x + TREE_WIDTH <= 0) {
                trees.remove(i);
            }
        }
        // Move clouds
        for (int i = clouds.size() - 1; i >= 0; i--) {
            Cloud cloud = clouds.get(i);
            cloud.x -= cactusSpeed / 2; // Clouds move slower than trees
            if (cloud.x + CLOUD_SIZE <= 0) {
                clouds.remove(i);
            }
        }
        // Spawn clouds
        if (Math.random() < 0.005) {
            Random random = new Random();
            int cloudY = random.nextInt(GROUND_Y / 2);
            clouds.add(new Cloud(WIDTH, cloudY));
        }
    }

    private void increaseDinosaurSpeed() {
        if (treeSpawnTimer % 600 == 0) { // Increase speed after 1 minute (600 * 20ms)
            cactusSpeed++;
        }
    }

    private void checkCollisions() {
        Rectangle dinosaurBounds = new Rectangle(dinosaurX, dinosaurY, DINOSAUR_WIDTH, DINOSAUR_HEIGHT);
        for (Tree tree : trees) {
            Rectangle treeBounds = new Rectangle(tree.x, tree.y, TREE_WIDTH, TREE_HEIGHT);
            if (dinosaurBounds.intersects(treeBounds)) {
                JOptionPane.showMessageDialog(null, "Game Over!", "Game Over", JOptionPane.ERROR_MESSAGE);
                reset();
                return;
            }
        }
    }

    private void reset() {
        dinosaurY = GROUND_Y - DINOSAUR_HEIGHT;
        trees.clear();
        clouds.clear();
        cactusSpeed = 5;
        treeSpawnTimer = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DinosaurJumpGame().setVisible(true);
            }
        });
    }

    private class Tree {
        int x, y, width, height;
        Color color;

        public Tree(int x, int y, int width, int height, Color color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
        }
    }

    private class Cloud {
        int x, y;

        public Cloud(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
