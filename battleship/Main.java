package battleship;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int turn = 0;


        System.out.println("Player 1, place your ships on the game field\n");
        Cell[][] firstBoard = new Cell[10][10];
        arrangeShips(firstBoard, in);


        turn = swapPlayer(in, turn);


        System.out.println("Player 2, place your ships on the game field\n");
        Cell[][] secondBoard = new Cell[10][10];
        arrangeShips(secondBoard, in);


        turn = swapPlayer(in, turn);
        System.out.print("\nThe game starts!\n\n");


        Cell[][] firstSmokeBoard = new Cell[10][10];
        fillBoard(firstSmokeBoard);
        int firstCntShips = 5;

        Cell[][] secondSmokeBoard = new Cell[10][10];
        fillBoard(secondSmokeBoard);
        int secondCntShips = 5;


        while (true) {
            if (turn == 0) {
                secondCntShips = doShot(firstBoard, firstSmokeBoard, secondBoard, secondSmokeBoard, in, turn, secondCntShips);
            } else {
                firstCntShips = doShot(secondBoard, secondSmokeBoard, firstBoard, firstSmokeBoard, in, turn, firstCntShips);
            }
            if (secondCntShips == 0 || firstCntShips == 0) {
                break;
            }
            turn = swapPlayer(in, turn);
        }

    }

    private static int doShot(Cell[][] myBoard, Cell[][] mySmokeBoard, Cell[][] enemyBoard, Cell[][] enemySmokeBoard, Scanner in, int playerNumber, int enemyCntShips) {
        printBoard(enemySmokeBoard);
        System.out.println("--------------------");
        printChangedBoard(myBoard, mySmokeBoard);
        System.out.println("\nPlayer " + (playerNumber + 1) + ", it's your turn:\n");
        String hitPosition = in.next();
        int hitRow = hitPosition.charAt(0) - 'A';
        int hitCol = getNumberFromPosition(hitPosition);
        while (checkErrorForHit(hitRow, hitCol)) {
            System.out.print("\nError! You entered the wrong coordinates! Try again:\n\n");
            hitPosition = in.next();
            hitRow = hitPosition.charAt(0) - 'A';
            hitCol = getNumberFromPosition(hitPosition);
        }
        if (enemyBoard[hitRow][hitCol] == Cell.Ship) {
            Cell was = enemySmokeBoard[hitRow][hitCol];
            enemySmokeBoard[hitRow][hitCol] = Cell.Hit;
            if (checkSankShip(hitRow, hitCol, enemySmokeBoard, enemyBoard)) {
                enemyCntShips -= was == Cell.Smoke ? 1 : 0;
                System.out.println(enemyCntShips);
                if (enemyCntShips == 0) {
                    System.out.print("\nYou sank the last ship. You won. Congratulations!\n\n");
                } else {
                    System.out.print("\nYou sank a ship! Specify a new target:\n\n");
                }
            } else {
                System.out.print("\nYou hit a ship! Try again:\n\n");
            }
        } else {
            enemySmokeBoard[hitRow][hitCol] = Cell.Miss;
            System.out.print("\nYou missed! Try again:\n\n");
        }
        return enemyCntShips;
    }

    private static void arrangeShips(Cell[][] board, Scanner in) {
        fillBoard(board);
        printBoard(board);
        String[] ships = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] sizeShips = {5, 4, 3, 3, 2};
        int index = 0;
        while (index < 5) {
            System.out.printf("\nEnter the coordinates of the %s (%d cells):\n\n", ships[index], sizeShips[index]);
            //startInput = in.next();
            String frontShip = in.next();
            String backShip = in.next();
            //System.out.println(frontShip + " " + backShip);
            //System.out.println();
            int frontShipRow = frontShip.charAt(0) - 'A';
            int frontShipCol = getNumberFromPosition(frontShip);
            int backShipRow = backShip.charAt(0) - 'A';
            int backShipCol = getNumberFromPosition(backShip);
            String error = checkErrorForTakePosition(frontShipRow, frontShipCol, backShipRow, backShipCol, sizeShips[index], board);
            if (error.equals("OK")) {
                takePosition(frontShipRow, frontShipCol, backShipRow, backShipCol, board);
                printBoard(board);
                index++;
            } else {
                System.out.printf("Error! %s Try again:\n", error);
            }
        }
    }

    private static int swapPlayer(Scanner in, int turn) {
        System.out.println("\nPress Enter and pass the move to another player");
        String kek = in.nextLine();
        String kek1 = in.nextLine();
        return (turn + 1) % 2;
    }

    private static boolean checkSankShip(int x, int y, Cell[][] smokeBoard, Cell[][] myBoard) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i == 0 || j == 0) && (i != 0 || j != 0)) {
                    int x1 = x + i, y1 = y + j;
                    while (!checkNotBelongOfSegment(x1) && !checkNotBelongOfSegment(y1) && myBoard[x1][y1] == Cell.Ship) {
                        if (smokeBoard[x1][y1] != Cell.Hit) {
                            return false;
                        }
                        x1 += i;
                        y1 += j;
                        //System.out.println(x1 + " " + y1);
                    }
                }
            }
        }
        return true;
    }

    private static int getNumberFromPosition(String str) {
        int num = 0;
        for (int i = 1; i < str.length(); i++) {
            num *= 10;
            num += str.charAt(i) - '0';
        }
        return num - 1;
    }

    private static boolean checkErrorForHit(int x, int y) {
        return checkNotBelongOfSegment(x) || checkNotBelongOfSegment(y);
    }

    private static void takePosition(int x1, int y1, int x2, int y2, Cell[][] board) {
        int x = x1;
        int y = y1;
        //System.out.printf("x1 = %d y1 = %d x2 = %d y2 = %d\n", x1, y1, x2, y2);
        while (x != x2 || y != y2) {
            //System.out.println(x + " " + y);
            board[x][y] = Cell.Ship;
            x += x1 != x2 ? (x1 < x2 ? 1 : -1) : 0;
            y += y1 != y2 ? (y1 < y2 ? 1 : -1) : 0;
        }
        board[x][y] = Cell.Ship;
    }

    private static String checkErrorForTakePosition(int x1, int y1, int x2, int y2, int len, Cell[][] board) {
        if ((x1 != x2 && y1 != y2) || checkNotBelongOfSegment(x1) || checkNotBelongOfSegment(y1) ||
                checkNotBelongOfSegment(x2) || checkNotBelongOfSegment(y2)) {
            return "Wrong ship location!";
        } else if (Math.abs((x1 - x2) + (y1 - y2)) + 1 != len) {
            return "Wrong length of the ship!";
        } else if (checkCloseToOtherShips(x1, y1, x2, y2, board)) {
            return "You placed it too close to another one.";
        } else {
            return "OK";
        }
    }

    private static boolean checkCloseToOtherShips(int x1, int y1, int x2, int y2, Cell[][] board) {
        int x = x1;
        int y = y1;
        while (x != x2 || y != y2) {
            if (checkClosePosition(x, y, board)) {
                return true;
            }
            x += x2 - x1;
            y += y2 - y1;
        }
        return checkClosePosition(x, y, board);
    }

    private static boolean checkClosePosition(int x, int y, Cell[][] board) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0 || j != 0) && !checkNotBelongOfSegment(x + i) && !checkNotBelongOfSegment(y + j)) {
                    if (board[x + i][y + j] != Cell.Smoke) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkNotBelongOfSegment(int x) {
        return x < 0 || x > 9;
    }

    private static void fillBoard(Cell[][] board) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = Cell.Smoke;
            }
        }
    }

    private static void printBoard(Cell[][] board) {
        System.out.print(" ");
        for (int i = 1; i <= 10; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        char rowNumber = 'A';
        for (int i = 0; i < 10; i++) {
            System.out.print(rowNumber);
            for (Cell cell : board[i]) {
                System.out.print(" " + cell.getForBoard());
            }
            rowNumber++;
            System.out.println();
        }
    }

    private static void printChangedBoard(Cell[][] board, Cell[][] smokeBoard) {
        System.out.print(" ");
        for (int i = 1; i <= 10; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        char rowNumber = 'A';
        for (int i = 0; i < 10; i++) {
            System.out.print(rowNumber);
            for (int j = 0; j < 10; j++) {
                if (smokeBoard[i][j] != Cell.Smoke && board[i][j] != smokeBoard[i][j]) {
                    System.out.print(" " + smokeBoard[i][j].getForBoard());
                } else {
                    System.out.print(" " + board[i][j].getForBoard());
                }
            }
            rowNumber++;
            System.out.println();
        }
    }

}
