import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class TicTacToeAI extends Board {
    static Scanner sk = new Scanner(System.in);
    static Board bf = new Board();
    static String[][] board = bf.board;
    static int[] bestPosition; /****field to hold the bestPosition for the compPlay to play in****/

    public static void compPlay() {
        minimax(bf, 0, true);
        if (legalMove(bestPosition[0], bestPosition[1]))
            board[bestPosition[0]][bestPosition[1]] = "  O  ";
    }

    public static int minimax(Board bd, int depth, boolean max) {
        int[][] possibleMoves = getPossibleMoves(bd);
        if (tie(bd.board)) {
            return 0;
        }
        else if (userWin(bd.board)) {
            return depth - 10;
        }
        else if (compWin(bd.board)) {
            return 10 - depth;
        }

        if (max) {
            double bestScore = Double.NEGATIVE_INFINITY;
            for (int[] move: possibleMoves) {

                Board modified = new Board();

                for (int i = 0; i < bd.board.length; i++)
                    for (int j = 0; j < bd.board[0].length; j++)
                        modified.board[i][j] = bd.board[i][j];

                modified.board[move[0]][move[1]] = "  O  ";

                int score = minimax(modified, depth++, false);

                if (score > bestScore) {
                    bestScore = score;
                    bestPosition = move;
                }
                if (userWin(modified.board)) {
                    bestPosition = move;
                    break;
                }
                else if (compWin(modified.board)) {
                    bestPosition = move;
                    break;
                }
            }
            return (int)bestScore;
        }
        else {
            double bestScore = Double.POSITIVE_INFINITY;
            for (int[] move: possibleMoves) {

                Board modified = new Board();

                for (int i = 0; i < bd.board.length; i++)
                    for (int j = 0; j < bd.board[0].length; j++)
                        modified.board[i][j] = bd.board[i][j];

                modified.board[move[0]][move[1]] = "  X  ";

                int score = minimax(modified, depth++, true);

                if (score < bestScore) {
                    bestScore = score;
                    bestPosition = move;
                }
                if (userWin(modified.board)) {
                    bestPosition = move;
                    break;
                }
                else if (compWin(modified.board)) {
                    bestPosition = move;
                    break;
                }
            }
            return (int)bestScore;
        }
    }

    public static int[][] getPossibleMoves(Board bd) { //gets all possible moves
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = 0; i < bd.board.length; i+=2) {
            for (int j = 0; j < bd.board[0].length; j+=2) {
                if (bd.board[i][j].contains("     ")) {
                    int[] place = {i,j};
                    moves.add(place);
                }
            }
        }
        int[][] possibleMoves = new int[moves.size()][2]; // possiblemoves will contain 1d arrays each of two integers
        for (int i = 0; i < moves.size(); i++) {
            possibleMoves[i] = moves.get(i);
        }
        return possibleMoves;
    }

    public static boolean legalMove(int x, int y) {
        return !board[x][y].equals("  X  ") && !board[x][y].equals("  O  ");
    }

    public static boolean updateBoard(int x, int y) { //updates the board
        if (legalMove(x, y)) {
            board[x][y] = "  X  ";
            return true;
        }
        return false;
    }

    public static void userPlay() { //allows user to enter coordinates into the board
        try {
            System.out.print("\nEnter X, Y coordinates for your move separated by a space: ");
            int x = 2 * sk.nextInt() - 2;
            int y = 2 * sk.nextInt() - 2;
            userAction(x, y);
        }
        catch (InputMismatchException e) {
            System.out.println("\nPlease enter only integer values.");
            sk.nextLine();
            userPlay();
        }
        return;
    }
    public static void userAction(int x, int y) {
        while (!updateBoard(x, y)) {
            System.out.print("\nPlease enter your coordinates again.\n");
            userPlay();
            return;
        }
    }

    public static boolean compWin(String[][] board) { //checks if the computer won
        for (int i = 0; i < 5; i += 2) { // checking the horizontals and verticals
            if (board[i][0].equals(board[i][2]) && board[i][0].equals(board[i][4])) {
                if (board[i][0].contains("O")) {
                    return true;
                }
            }
            if (board[0][i].equals(board[2][i]) && board[0][i].equals(board[4][i])) {
                if (board[0][i].contains("O")) {
                    return true;
                }
            }
        }
        if ((board[0][0].equals(board[2][2]) && board[0][0].equals(board[4][4]))) { // checking first diagonal
            if (board[0][0].contains("O")) {
                return true;
            }
        }
        else if ((board[0][4].equals(board[2][2]) && board[0][4].equals(board[4][0]))) { // checking second diagonal
            if (board[0][4].contains("O")) {
                return true;
            }
        }
        return false;
    }

    public static boolean userWin(String[][] board) { //checks if the player won
        for (int i = 0; i < 5; i += 2) { // checking the horizontals and verticals
            if (board[i][0].equals(board[i][2]) && board[i][0].equals(board[i][4])) {
                if (board[i][0].contains("X")) {
                    return true;
                }
            }
            if (board[0][i].equals(board[2][i]) && board[0][i].equals(board[4][i])) {
                if (board[0][i].contains("X")) {
                    return true;
                }
            }
        }
        if ((board[0][0].equals(board[2][2]) && board[0][0].equals(board[4][4]))) { // checking first diagonal
            if (board[0][0].contains("X")) {
                return true;
            }
        }
        else if ((board[0][4].equals(board[2][2]) && board[0][4].equals(board[4][0]))) { // checking second diagonal
            if (board[0][4].contains("X")) {
                return true;
            }
        }
        return false;
    }

    public static boolean tie(String[][] bd) { // checks tie between computer and user
        for (int i = 0; i < 5; i += 2) {
            for (int j = 0; j < 5; j += 2) {
                if (bd[i][j].equals("     "))
                    return false;
                if (bd[j][i].equals("     "))
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("\nWelcome to the Tic Tac Toe game.\nEnter your move with coordinates. Your input should be two integers separated by a space." +
                "\nYou will be playing against the computer.\n");
        bf.printBoard();
        String decision = "y";
        while (decision.equalsIgnoreCase("y")) {
            for (int i = 0; i < 5; i += 2) {
                for (int j = 0; j < 5; j += 2) {
                    board[i][j] = "     ";
                }
            }
            while (!userWin(bf.board) && !compWin(bf.board)) { // keeps going until either one wins
                if (tie(board)) break;
                userPlay();
                compPlay();
                bf.printBoard();
            }
            if (userWin(bf.board)) { System.out.println("\nGood job user. You won!!\n");}

            else if (compWin(bf.board)) { System.out.println("\nHaha user! You lost!\n");}

            System.out.print("\nDo you want to play again (y/n)?: ");
            decision = sk.next();
        }
        System.out.println("\nOkay. See you later, user.");
    }
}
    





