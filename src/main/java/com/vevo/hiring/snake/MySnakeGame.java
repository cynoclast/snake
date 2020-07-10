package com.vevo.hiring.snake;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import static com.vevo.hiring.snake.SnakeGame.Direction.EAST;
import static com.vevo.hiring.snake.SnakeGame.Direction.NORTH;
import static com.vevo.hiring.snake.SnakeGame.Direction.SOUTH;
import static com.vevo.hiring.snake.SnakeGame.Direction.WEST;

public class MySnakeGame implements SnakeGame, AdvancedSnakeGame {

    private static final char BLANK_SQUARE = '.';
    private static final char SNAKE_SEGMENT = 'X';
    private static final char FRUIT = '@';

    private int widthInSquares;
    private int heightInSquares;

    /**
     * board is in cartesian quadrant IV, so 0,0 is at the top left.
     */
    private char[][] board;

    /**
     * LinkedList is doubly linked, making a loop, so this is our snake.
     */
    private LinkedList<Coordinate> ouroboros = new LinkedList<>();

    /**
     * For adding fruit.
     */
    Random random = new Random();

    /**
     * Creates a snake game.
     *
     * @param widthInSquares  width of the play area
     * @param heightInSquares height of the play area
     */
    public MySnakeGame(int widthInSquares, int heightInSquares) {
        this.widthInSquares = widthInSquares;
        this.heightInSquares = heightInSquares;
        board = new char[widthInSquares][heightInSquares];
        createBlankBoard();
        createSnake();
    }

    /**
     * Draws the blank game board.
     */
    private void createBlankBoard() {
        for (int y = 0; y < heightInSquares; y++) {
            for (int x = 0; x < widthInSquares; x++) {
                board[x][y] = BLANK_SQUARE;
            }
        }
    }

    /**
     * Creates the snake and starts it in the approximate center.
     */
    private void createSnake() {
        int horizontalCenter = widthInSquares / 2;
        int verticalCenter = heightInSquares / 2;

        Coordinate snakeStart = new Coordinate(horizontalCenter, verticalCenter);
        ouroboros.addFirst(snakeStart);
        board[snakeStart.getX()][snakeStart.getY()] = SNAKE_SEGMENT;
        // (There is now a snake on the "plane".)
    }

    @Override
    public String getGameBoard() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int y = 0; y < heightInSquares; y++) {
            for (int x = 0; x < widthInSquares; x++) {
                stringBuilder.append(board[x][y]);
            }
            if (y < heightInSquares - 1) {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        terminal.enterPrivateMode();

        boolean gameOver;
        boolean sessionOver = false;
        int highScore = 1;
        while (!sessionOver) {
            Direction currentDirection = EAST;
            terminal.clearScreen();
            terminal.flush();

            // just under standard terminal size, odd numbered so the snake starts in the center, not one off
            AdvancedSnakeGame game = new MySnakeGame(79, 23);
            Coordinate fruit = game.addRandomFruit();

            writeStringToTerminal(terminal, game.getGameBoard());
            writeStringToTerminal(terminal, "\nPress any key to start (Heading East). Press Escape to quit.");

            final KeyStroke keyStroke1 = terminal.readInput();
            if (keyStroke1.getKeyType() == KeyType.Escape) {
                terminal.exitPrivateMode();
                System.exit(0);
            }

            gameOver = false;
            boolean ateFruit = false;
            while (!gameOver) {
                terminal.clearScreen();
                terminal.flush();
                writeStringToTerminal(terminal, game.getGameBoard() + "\n");
                terminal.flush();
                Thread.sleep(100);

                if (game.snakeHitSelf()) {
                    gameOver = true;
                }
                if (fruit.equals(game.getHead())) {
                    ateFruit = true;
                }

                try {
                    if (ateFruit) {
                        game.move(currentDirection, true);
                        ateFruit = false;
                        fruit = game.addRandomFruit();
                    } else {
                        game.move(currentDirection, false);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // lazy wall detection
                    break;
                }

                KeyStroke keyStroke = terminal.pollInput();
                if (keyStroke != null) {
                    KeyType keyType = keyStroke.getKeyType();

                    switch (keyType) {
                        case ArrowUp:
                            if (currentDirection != SOUTH) {
                                currentDirection = NORTH;
                            }
                            break;
                        case ArrowDown:
                            if (currentDirection != NORTH) {
                                currentDirection = SOUTH;
                            }
                            break;
                        case ArrowRight:
                            if (currentDirection != WEST) {
                                currentDirection = EAST;
                            }
                            break;
                        case ArrowLeft:
                            if (currentDirection != EAST) {
                                currentDirection = WEST;
                            }
                            break;
                        case Escape:
                            System.exit(0);
                            break;
                        default:
                            break;
                    }
                }
            }

            if (game.getSnakeLength() > highScore) {
                highScore = game.getSnakeLength();
            }

            String endText = "\nGame over. High score: " + highScore + "\nPress any key to continue, Escape to quit.";
            writeStringToTerminal(terminal, endText);
            final KeyStroke keyStroke = terminal.readInput();
            if (keyStroke.getKeyType() == KeyType.Escape) {
                sessionOver = true;
            }
        }
        terminal.exitPrivateMode();
    }

    private static void writeStringToTerminal(Terminal terminal, String string) throws IOException {
        for (char character : string.toCharArray()) {
            terminal.putCharacter(character);
        }
        terminal.flush();
    }

    @Override
    public int getSnakeLength() {
        return ouroboros.size() - 1;
    }

    /**
     * Returns true if the snake his hit itself.
     * @return true if the snake his hit itself
     */
    @Override
    public boolean snakeHitSelf() {
        if (ouroboros.size() < 4) {
            // a snake of 3 or less can't hit itself and this avoids any tedious array bounds checking
            return false;
        }
        // If the snake contains its head, other than at its head, it has hit itself.
        ArrayList<Coordinate> tempSnake = new ArrayList<>(ouroboros.subList(1, ouroboros.size() - 1));
        return tempSnake.contains(ouroboros.getFirst());
    }

    /**
     * Adds a random fruit object. Randomly locates a non-snake square.
     * May take a while if the board is mostly snake.
     * @return a Coordinate object representing the fruit
     */
    @Override
    public Coordinate addRandomFruit() {
        int randX;
        int randY;
        Coordinate coordinate;
        do {
            randX = random.nextInt(widthInSquares);
            randY = random.nextInt(heightInSquares);
            coordinate = new Coordinate(randX, randY);
        } while (ouroboros.contains(coordinate));
        board[randX][randY] = FRUIT;
        return coordinate;
    }

    @Override
    public Coordinate getHead() {
        return ouroboros.getFirst();
    }
}
