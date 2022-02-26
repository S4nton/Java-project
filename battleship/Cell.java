package battleship;

public enum Cell {
    Ship('O'),
    Smoke('~'),
    Miss('M'),
    Hit('X');
    
    char forBoard;
    
    Cell(char forBoard) {
        this.forBoard = forBoard;
    }
    
    public char getForBoard() {
        return this.forBoard;
    }
}
