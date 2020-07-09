package com.vevo.hiring.snake;

import java.util.LinkedList;

/**
 * @author Trampas Kirk
 */
public class MySnakeGame implements SnakeGame {

    private static final char BLANK_SQUARE = '.';
    private static final char SNAKE_SEGMENT = 'X';

    private int widthInSquares;
    private int heightInSquares;

    private char[][] board;

    /**
     * LinkedList is doubly linked, making a loop...
     */
    private LinkedList<Coordinate> ouroboros = new LinkedList<>();

    private Direction currentDirection = Direction.EAST;

    public MySnakeGame(int widthInSquares, int heightInSquares) {
        this.widthInSquares = widthInSquares;
        this.heightInSquares = heightInSquares;
        board = new char[widthInSquares][heightInSquares];
        createBlankBoard();
        createSnake();
    }

    /**
     * Creates the snake and start it in the approximate center.
     */
    private void createSnake() {
        int horizontalCenter = widthInSquares / 2;
        int verticalCenter = heightInSquares / 2;

        Coordinate snakeStart = new Coordinate(horizontalCenter, verticalCenter);
        ouroboros.addFirst(snakeStart);
        board[snakeStart.getX()][snakeStart.getY()] = SNAKE_SEGMENT;
        // (There is now a snake on the "plane".)
    }

    /**
     * board is in cartesian quadrant IV, so 0,0 is at the top left.
     */
    private void createBlankBoard() {
        for (int y = 0; y < heightInSquares; y++) {
            for (int x = 0; x < widthInSquares; x++) {
                board[x][y] = BLANK_SQUARE;
            }
        }
    }

    @Override
    public String getGameBoard() {

        StringBuilder temp = new StringBuilder();

        for (int y = 0; y < heightInSquares; y++) {
            for (int x = 0; x < widthInSquares; x++) {
                temp.append(board[x][y]);
            }
            if (y != heightInSquares - 1) {
                temp.append('\n');
            }
        }
        return temp.toString();
    }

    @Override
    public void move(Direction direction, boolean grow) {

        Coordinate currentHead = ouroboros.getFirst();
        Coordinate newHead;

        switch (direction) {
            case NORTH:
                newHead = new Coordinate(currentHead.getX(), currentHead.getY() - 1);
                ouroboros.addFirst(newHead);
                board[newHead.getX()][newHead.getY()] = SNAKE_SEGMENT;
                break;

            case SOUTH:
                newHead = new Coordinate(currentHead.getX(), currentHead.getY() + 1);
                ouroboros.addFirst(newHead);
                board[newHead.getX()][newHead.getY()] = SNAKE_SEGMENT;

                break;

            case EAST:
                newHead = new Coordinate(currentHead.getX() + 1, currentHead.getY());
                ouroboros.addFirst(newHead);
                board[newHead.getX()][newHead.getY()] = SNAKE_SEGMENT;
                break;

            case WEST:
                newHead = new Coordinate(currentHead.getX() - 1, currentHead.getY());
                ouroboros.addFirst(newHead);
                board[newHead.getX()][newHead.getY()] = SNAKE_SEGMENT;
                break;
        }

        if (!grow) {
            Coordinate tail = ouroboros.removeLast();
            board[tail.getX()][tail.getY()] = BLANK_SQUARE;
        }
    }

    public static void main(String[] args) {
        SnakeGame game = new MySnakeGame(10, 10);
        System.out.println(game.getGameBoard());

        System.out.println("----> MOVE EAST (true)");
        game.move(SnakeGame.Direction.EAST, true);
        System.out.println(game.getGameBoard());

        System.out.println("----> MOVE EAST (false)");
        game.move(SnakeGame.Direction.EAST, false);
        System.out.println(game.getGameBoard());

        System.out.println("----> MOVE EAST (false)");
        game.move(SnakeGame.Direction.EAST, false);
        System.out.println(game.getGameBoard());

        System.out.println("\\/\\/  MOVE SOUTH (true)");
        game.move(SnakeGame.Direction.SOUTH, true);
        System.out.println(game.getGameBoard());
    }
}
