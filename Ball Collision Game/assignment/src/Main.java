import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
class FileInput {
    /**
     * @param path              Path to the file that is going to be read.
     * @param discardEmptyLines If true, discards empty lines with respect to trim; else, it takes all the lines from the file.
     * @param trim              Trim status; if true, trims (strip in Python) each line; else, it leaves each line as-is.
     * @return Contents of the file as a string array, returns null if there is not such a file or this program does not have sufficient permissions to read that file.
     */
    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
            if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { //Trims each line.
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        }
        catch (IOException e) { //Returns null if there is no such a file.
            e.printStackTrace();
            return null;
        }
    }
}
class FileOutput {
    /**
     * @param path    Path for the file content is going to be written.
     * @param content Content that is going to be written to file.
     * @param append  Append status, true if wanted to append to file if it exists, false if wanted to create file from zero.
     * @param newLine True if wanted to append a new line after content, false if vice versa.
     */
    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
    }
}
public class Main {
    static int rows, cols;
    static int a, b; // these are (row and column) index of white ball
    static int[] starIndex;
    static int game_point = 0;
    static String is_over = "";
    static String movements = "";

    public static char[][] move_white(String[] moves, char[][] board){
        Map<Character, Integer> pointValues = new HashMap<>();
        pointValues.put('R', 10);
        pointValues.put('Y', 5);
        pointValues.put('B', -5);

        for (String move : moves) {
            int dx = 0, dy = 0;
            switch (move) {
                case "L":
                    dy = -1;
                    break;
                case "R":
                    dy = 1;
                    break;
                case "U":
                    dx = -1;
                    break;
                case "D":
                    dx = 1;
                    break;
            }
            movements += move;
            movements += " ";
            int newRow = a + dx, newCol = b + dy;

            if (newRow == -1) {newRow = rows - 1;}
            else if (newRow == rows) {newRow = 0;}
            else if (newCol == -1) {newCol = cols-1;}
            else if (newCol == cols) {newCol = 0;}

            if (board[newRow][newCol] == 'H') {
                board[a][b] = ' ';
                a = newRow;
                b = newCol;
                is_over = "\nGame over!";
                return board;
            }
            else if (board[newRow][newCol] == 'W') {
                if (newRow == 1 && move.equals("D")) {newRow = rows - 1;}
                else if (newRow == rows-2 && move.equals("U")) {newRow = 0;}
                else if (newCol == 1 && move.equals("R")) {newCol = cols-1;}
                else if (newCol == cols-2 && move.equals("L")) {newCol = 0;}
                else {newRow = a - dx; newCol = b - dy;}
                int point = pointValues.getOrDefault(board[newRow][newCol], 0);
                game_point += point;
                switch (board[newRow][newCol]){
                    case 'R':
                    case 'B':
                    case 'Y':
                        board[newRow][newCol] = 'X';
                        break;
                }
                if (board[newRow][newCol] != 'H') {
                    char temp = board[a][b];
                    board[a][b] = board[newRow][newCol];
                    board[newRow][newCol] = temp;
                    a = newRow;
                    b = newCol;
                }
                else { // new row is going to be Hole
                    board[a][b] = ' ';
                    a = newRow;
                    b = newCol;
                    is_over = "\nGame over!";
                    return board;
                }
            }
            else {
                int point = pointValues.getOrDefault(board[newRow][newCol], 0);
                game_point += point;
                switch (board[newRow][newCol]){
                    case 'R':
                    case 'B':
                    case 'Y':
                        board[newRow][newCol] = 'X';
                        break;
                }
                char temp = board[a][b];
                board[a][b] = board[newRow][newCol];
                board[newRow][newCol] = temp;
                a = newRow;
                b = newCol;
            }
        }
        return board;
    }
    public static int[] findStarIndex(char[][] board) {
        int[] index = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '*') {
                    index[0] = i;
                    index[1] = j;
                }
            }
        }
        return index;
    }
    public static void main(String[] args) {
        String board_txt = args[0];
        String move_txt = args[1];
        StringBuilder outputBuilder = new StringBuilder();
        String[] boardLines = FileInput.readFile(board_txt, true, true);
        String[] moveLine = FileInput.readFile(move_txt, true, true);
        assert moveLine != null;
        String[] moves = moveLine[0].split(" ");

        assert boardLines != null;
        rows = boardLines.length;
        cols = boardLines[0].split(" ").length;
        char[][] board = new char[rows][cols];

        // arrange board
        for (int i = 0; i < rows; i++) {
            String[] row = boardLines[i].split(" ");
            for (int j = 0; j < cols; j++) {
                board[i][j] = row[j].charAt(0);
            }
        }
        starIndex = findStarIndex(board);
        a = starIndex[0];
        b = starIndex[1];

        outputBuilder.append("Game board:\n");
        int i,j;
        for(i=0; i<rows; i++) {
            for (j = 0; j < cols; j++)
                outputBuilder.append(board[i][j]).append(" ");
            outputBuilder.append("\n");
        }

        char[][] grid = move_white(moves, board);
        outputBuilder.append("\nYour movement is:\n" + movements);

        outputBuilder.append("\n\nYour output is:\n");


        for(i=0; i< rows; i++) {
            for (j = 0; j < cols; j++)
                outputBuilder.append(grid[i][j]).append(" ");
            outputBuilder.append("\n");
        }
        outputBuilder.append(is_over);
        outputBuilder.append("\nScore: ").append(game_point);

        String output = outputBuilder.toString();
        FileOutput.writeToFile("output.txt", output, false, false); // Write the output to file
    }
}