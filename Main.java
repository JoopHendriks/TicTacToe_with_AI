package tictactoe;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TTTGame tictactoe = new TTTGame();
        tictactoe.gameLoop();
    }
}

class TTTGame{
    PlayingField playingField;
    Scanner scanner;
    Player player1;
    Player player2;
    boolean gameOn;

    public TTTGame() {
        this.playingField = new PlayingField();
        this.scanner = new Scanner(System.in);
    }
    public void gameLoop() {
        menu();
        boolean ended;

        while (this.gameOn) {
            this.playingField.resetField();
            this.playingField.printField();
            while (true) {
                // First, turn for playerX
                int[] coordinates = new int[2];
                boolean valid = false;
                this.player1.announceTurn();
                while (!valid) {
                    coordinates = this.player1.makeMove();
                    valid = checkValidity(coordinates, this.player1.getPlayerType());
                }

                this.playingField.setPlayingfield(coordinates[0], coordinates[1], 'X');
                this.playingField.printField();
                ended = isEnded();
                // if X won or field is full, stop game and return to menu
                if (ended) {
                    break;
                }

                // Now, it is the turn of player O
                valid = false;
                this.player2.announceTurn();
                while (!valid) {
                    coordinates = this.player2.makeMove();
                    valid = checkValidity(coordinates, this.player2.getPlayerType());
                }
                this.playingField.setPlayingfield(coordinates[0], coordinates[1], 'O');
                this.playingField.printField();
                ended = isEnded();
                // if O won or field is full, stop game and return to menu
                if (ended) {
                    break;
                }
            }
            menu();
        }
    }

    private void menu() {
        boolean valid = false;

        while (!valid) {
            System.out.println("Input Command: ");
            String command = scanner.nextLine();
            String[] splitted = command.split(" ");
            if (splitted[0].equals("start")) {
                if (splitted.length == 3) {
                    this.gameOn = true;
                    this.player1 = new Player('X', splitted[1], this.playingField);
                    this.player2 = new Player('O', splitted[2], this.playingField);
                    valid = true;
                } else {
                    System.out.println("Bad parameters!");
                }
            } else {
                this.gameOn = false;
                valid = true;
            }
        }
    }

    boolean checkValidity(int[] coordinates, String playerType) {
        int xCoordinate = coordinates[0];
        int yCoordinate = coordinates[1];


        if (playerType.equals("user")) {
            if (xCoordinate < 1 || xCoordinate > 3 ||
                    yCoordinate < 1 || yCoordinate > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            } else if (!this.playingField.isAvailable(xCoordinate, yCoordinate)) {
                System.out.println("This cell is occupied! Choose another one!");
                return false;
            } else {
                return true;
            }
        } else {
            return this.playingField.isAvailable(xCoordinate, yCoordinate);
        }
    }

    private boolean isEnded() {
        char[][] field = playingField.getFields();

        char outcome = ' ';
        boolean won = false;
        char[] player = {'O', 'X'};

        for (char character : player) {
            won = (field[0][0] == field[1][1] && field[1][1] == field[2][2] && field[0][0] == character) ||
                    (field[0][0] == field[0][1] && field[0][1] == field[0][2] && field[0][0] == character) ||
                    (field[1][0] == field[1][1] && field[1][1] == field[1][2] && field[1][0] == character) ||
                    (field[2][0] == field[2][1] && field[2][1] == field[2][2] && field[2][0] == character) ||
                    (field[0][0] == field[1][0] && field[1][0] == field[2][0] && field[0][0] == character) ||
                    (field[0][1] == field[1][1] && field[1][1] == field[2][1] && field[0][1] == character) ||
                    (field[0][2] == field[1][2] && field[1][2] == field[2][2] && field[0][2] == character) ||
                    (field[2][0] == field[1][1] && field[1][1] == field[0][2] && field[2][0] == character);
            if (won) {
                outcome = character;
            }
        }

        boolean spotAvailable = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == ' ') {
                    spotAvailable = true;
                    break;
                }
            }
        }
        if (!spotAvailable && !won) {
            outcome = 'd';
        }
        boolean ended;

        switch (outcome) {
            case 'O':
                System.out.println("O wins");
                ended = true;
                break;
            case 'X':
                System.out.println("X wins");
                ended = true;
                break;
            case 'd':
                System.out.println("Draw");
                ended = true;
                break;
            default:
                ended = false;
                break;
        }
        return ended;
    }
}

class Player {
    final char playsWith;
    final char opponentPlaysWith;
    String playerType;
    PlayingField playingField;

    public Player(char playsWith, String playerType, PlayingField playingField) {
        this.playsWith = playsWith;
        this.playerType = playerType;
        this.playingField = playingField;
        if (playsWith == 'X') {
            this.opponentPlaysWith = 'O';
        } else{
            this.opponentPlaysWith = 'X';
        }
    }

    int[] makeMove() {
        int[] coordinates = new int[2];
        switch (this.playerType) {
            case "user":
                coordinates = userMove();
                break;
            case "easy":
                coordinates = easyMove();
                break;
            case "medium":
                coordinates = mediumMove();
                break;
            case "hard":
                coordinates = hardMove();
                break;
        }
        return coordinates;
    }


    int[] userMove() {
        int[] coordinates = new int[2];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates: > ");
        try {
            coordinates[0] = scanner.nextInt();
            coordinates[1] = scanner.nextInt();

        } catch (Exception e) {
            System.out.println("You should enter numbers!");
            // Remove whatever is still in the buffer
            scanner.nextLine();
        }
        return coordinates;
    }

    int[] easyMove() {
        Random random = new Random();
        int[] coordinates = new int[2];

        coordinates[0] = random.nextInt(3) + 1;
        coordinates[1] = random.nextInt(3) + 1;
        return coordinates;
    }

    int[] mediumMove() {
        // Determine what to do
        int[] coordinates;
        int [] noResult = new int[] {0, 0};
        coordinates = checkWinningCoordinates(this.playsWith);

        if (Arrays.equals(coordinates, noResult)) {
            char compareTo;
            if (this.playsWith == 'X') {
                compareTo = 'O';
            } else {
                compareTo = 'X';
            }
            coordinates = checkWinningCoordinates(compareTo);

            if (Arrays.equals(coordinates, noResult)) {
                coordinates = easyMove();
            }
        }
        return coordinates;
    }

    int [] hardMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] coordinates = new int[2];
        int score;

        char[][] field = this.playingField.getFields();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                if (this.playingField.isAvailable(i + 1, j + 1)) {
                    field[i][j] = this.playsWith;
                    score = minimax(field, false);
                    field[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        coordinates[0] = i + 1;
                        coordinates[1] = j + 1;
                    }
                }
            }
        }
        return coordinates;
    }

    private int minimax(char[][] field, boolean isMaximizing) {
        char outcome = checkWinner(field);

        if (outcome != ' ') {
            if (outcome == this.playsWith) {
                return 1;
            } else if (outcome == 'd') {
                return 0;
            } else {
                return -1;
            }
        }

        int score;
        int bestScore;

        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == ' ') {
                        field[i][j] = this.playsWith;
                        score = minimax(field, false);
                        field[i][j] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == ' ') {
                        field[i][j] = this.opponentPlaysWith;
                        score = minimax(field, true);
                        field[i][j] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }
        return bestScore;
    }

    private char checkWinner(char[][] field) {
        char outcome = ' ';
        boolean won = false;
        char[] player = {'O', 'X'};

        for (char character : player) {
            won = (field[0][0] == field[1][1] && field[1][1] == field[2][2] && field[0][0] == character) ||
                    (field[0][0] == field[0][1] && field[0][1] == field[0][2] && field[0][0] == character) ||
                    (field[1][0] == field[1][1] && field[1][1] == field[1][2] && field[1][0] == character) ||
                    (field[2][0] == field[2][1] && field[2][1] == field[2][2] && field[2][0] == character) ||
                    (field[0][0] == field[1][0] && field[1][0] == field[2][0] && field[0][0] == character) ||
                    (field[0][1] == field[1][1] && field[1][1] == field[2][1] && field[0][1] == character) ||
                    (field[0][2] == field[1][2] && field[1][2] == field[2][2] && field[0][2] == character) ||
                    (field[2][0] == field[1][1] && field[1][1] == field[0][2] && field[2][0] == character);
            if (won) {
                outcome = character;
            }
        }

        boolean spotAvailable = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == ' ') {
                    spotAvailable = true;
                    break;
                }
            }
        }
        if (!spotAvailable && !won) {
            outcome = 'd';
        }
        return outcome;
    }



    int[] checkWinningCoordinates(char compareTo) {
        int[] coordinates = new int[] {0, 0};
        char[][] field = this.playingField.getFields();
        boolean condition;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; i < 3; i++) {
                condition = field[i][j] == compareTo &&
                        field[i][(j + 1) % 3] == compareTo &&
                        this.playingField.isAvailable(i + 1, (j + 2) % 3 + 1);
                if (condition) {
                    coordinates[0] = i + 1;
                    coordinates[1] = (j + 2) % 3 + 1;
                    return coordinates;
                }
                condition = field[j][i] == compareTo &&
                        field[j][(i + 1) % 3] == compareTo &&
                        this.playingField.isAvailable((j + 2) % 3 + 1, i + 1);
                if (condition) {
                    coordinates[0] = (j + 2) % 3 + 1;
                    coordinates[1] = i + 1;
                    return coordinates;
                }
            }
        }
        // Check diagonals
        for (int j = 0; j < 3; j++) {
            condition = field[j][j] == compareTo &&
                    field[(j + 1) % 3][(j + 1) % 3] == compareTo &&
                    this.playingField.isAvailable(((j + 2) % 3 + 1), ((j + 2) % 3 + 1));
            if (condition) {
                coordinates[0] = (j + 2) % 3 + 1;
                coordinates[1] = (j + 2) % 3 + 1;
                return coordinates;
            }
        }
        for (int j = 2; j > 0; j--) {
            for (int i = 0; i < 3; i++) {
                condition = field[i][j] == compareTo &&
                        field[(i + 1) % 3][(j + 2) % 3] == compareTo &&
                        this.playingField.isAvailable((i + 2) % 3 + 1, (j + 1) % 3 + 1);
                if (condition) {
                    coordinates[0] = (i + 2) % 3 + 1;
                    coordinates[1] = (j + 1) % 3 + 1;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }

    void announceTurn() {
        switch (this.playerType) {
            case "easy":
                System.out.println("Making move level \"easy\"");
                break;
            case "medium":
                System.out.println("Making move level \"medium\"");
                break;
            case "hard":
                System.out.println("Making move level \"hard\"");
                break;
        }
    }

    String getPlayerType() {
        return this.playerType;
    }
}


class PlayingField {
    private char[][] fields;

    PlayingField() {
        fields = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.fields[i][j] = ' ';
            }
        }
    }

    void resetField() {
        fields = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.fields[i][j] = ' ';
            }
        }
    }

    protected void setPlayingfield(int xCoordinate, int yCoordinate, char entry) {
        this.fields[xCoordinate - 1][yCoordinate - 1] = entry;
    }

    protected char[][] getFields() {
        return this.fields;
    }
    protected boolean isAvailable(int xCoordinate, int yCoordinate) {
        char field = this.fields[xCoordinate - 1][yCoordinate - 1];
        return field != 'O' && field != 'X';
    }

    protected void printField() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(this.fields[i][j] + " ");
            }
            System.out.println("| ");
        }
        System.out.println("---------");
    }
}