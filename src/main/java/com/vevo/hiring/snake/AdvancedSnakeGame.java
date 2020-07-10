package com.vevo.hiring.snake;

public interface AdvancedSnakeGame extends SnakeGame {

    int getSnakeLength();

    boolean snakeHitSelf();

    Coordinate addRandomFruit();

    Coordinate getHead();
}
