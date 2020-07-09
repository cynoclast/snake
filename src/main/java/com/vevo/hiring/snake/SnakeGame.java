package com.vevo.hiring.snake;

public interface SnakeGame {
    enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    /**
     * Creates a String that represents the current game board.  Empty spaces should appear as a '.',
     * spaces containing the snake should appear as an 'X'.
     * @return String representing the snake game board.
     */

    String getGameBoard();

    /**
     * Move the snake one pixel on the board.
     * @param direction Which direction should we move?
     * @param grow      Should the snake become one pixel longer after this move?
     */
    void move(Direction direction, boolean grow);
}
