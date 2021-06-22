package com.richiecodes;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS]; // Also holds head of Snake
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten, appleX, appleY;
    char dir = 'R'; // 'U' , 'R' , 'D' , 'L'
    boolean running = false;
    Timer timer;
    Random random;
    private Image head_sprite, background;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        try {
            loadImages();
            System.out.println("Assets successfully loaded");
            startGame();
            System.out.println("New game started");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImages() {
        try {
            ImageIcon i = new ImageIcon("src/com/richiecodes/resources/snake_head.png");
            head_sprite = i.getImage();
            System.out.println("Head sprite loaded successfully");
        } catch(Exception e) {e.printStackTrace();}

        try {
            ImageIcon i = new ImageIcon("src/com/richiecodes/resources/background.png");
            background = i.getImage();
            System.out.println("Background loaded successfully");
        } catch(Exception e) {e.printStackTrace();}
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        draw(g);
    }

    public void draw(Graphics g) {
        // DEBUG GRID
//        for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//        }
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for(int i = 0; i < bodyParts; i++) {
            if(i == 0) {
                g.drawImage(head_sprite, x[i], y[i], null);
            } else {
                g.setColor(new Color(64, 153, 60));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (dir) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if(x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {

        // Checks if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if(x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // Checks if head collides with left border
        if(x[0] < 0) running = false;

        // Checks if head collides with right border
        if(x[0] > SCREEN_WIDTH) running = false;

        // Checks if head collides with top border
        if(y[0] < 0) running = false;

        // Checks if head collides with bottom border
        if(y[0] > SCREEN_HEIGHT) running = false;

        if(!running) timer.stop();
    }

    public void gameOver(Graphics g) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(dir != 'R') dir = 'L';
                    break;

                case KeyEvent.VK_RIGHT:
                    if(dir != 'L') dir = 'R';
                    break;

                case KeyEvent.VK_UP:
                    if(dir != 'D') dir = 'U';
                    break;

                case KeyEvent.VK_DOWN:
                    if(dir != 'U') dir = 'D';
                    break;
            }
        }
    }
}
