package jianguo.ds.se.hust.com.sudoku.ds;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by JianGuo on 16/1/1.
 *
 * This is the class implementing the basic data structure for the sudoku
 * Containing the cell of Sudoku, See {@link SudokuCell} for details.
 */
public class SudokuField implements Serializable {
    private final int blockSize;
    private final int fieldSize;
    private SudokuCell[][] field; // the sudoku

    /**
     * Construct the sudoku
     * @param blocks the min blocks of sudoku, for 9 * 9 sudoku the blocks should be 3
     */
    public SudokuField(int blocks) {
        if (blocks <= 0) throw new IllegalArgumentException("Invalid block size!");
        blockSize = blocks;
        fieldSize = blockSize * blockSize;
        field = new SudokuCell[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j] = new SudokuCell();
            }
        }
        generateFullField(1, 1);
    }

    /**
     * get the generated sudoku
     * @return the generated sudoku
     */
    public SudokuCell[][] getField() {
        return field;
    }


    /**
     * get block size
     * @return block size
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * get the sudoku length
     * @return the sudoku length
     */
    public int getFieldSize() {
        return fieldSize;
    }

    /**
     * the number of cells
     * @return the number of cells
     */
    public int numberOfCells() {
        return fieldSize * fieldSize;
    }

    // reset the cell at (row - 1, column - 1)
    private void reset(int row, int column) {
        field[row - 1][column - 1].reset();
    }

    // clear the cell at (row - 1, column - 1)
    private void clear(int row, int column) {
        field[row - 1][column - 1].clear();
    }

    /**
     * Clear all the cells in the sudoku
     * See {@link SudokuCell}
     */
    public void clearAllCells() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j].clear();
            }
        }
    }

    /**
     * Reset all the cells in the sudoku
     * See {@link SudokuCell}
     */
    public void resetAllCells() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j].reset();
            }
        }
    }


    /**
     * returns true if the cell in (row, column) is filled.
     * @param row the row
     * @param column the column
     * @return true if the cell in (row, column) is filled.
     */
    public boolean isFilled(int row, int column) {
        return field[row - 1][column - 1].isFilled();
    }


    /**
     * Whether all the cells is filled
     * @return true if all filled
     */
    public boolean allCellsFilled() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (!field[i][j].isFilled()) return false;
            }
        }
        return true;
    }
    // number of filled cells
    private int numberOfFilledCells() {
        int filled = 0;
        for (int i = 1; i <= fieldSize; i++) {
            for (int j = 1; j <= fieldSize; j++) {
                if (isFilled(i, j)) filled++;
            }
        }
        return filled;
    }

    /**
     * number of hidden cells
     * @return number of hidden cells
     */
    public int numberOfHiddenCells() {
        return numberOfCells() - numberOfFilledCells();
    }

    /**
     * get Cell from (row, column)
     * @param row row
     * @param column column
     * @throws IllegalArgumentException if row or column is invalid
     * @return the sudoku cell
     */
    public SudokuCell get(int row, int column) {
        if (row > fieldSize || row < 0) throw new IllegalArgumentException("no such row in the sudoku!" + row);
        if (column > fieldSize || column < 0) throw new IllegalArgumentException("no such column in the sudoku!" + column);
        return field[row - 1][column - 1];
    }

    /**
     * set value at (row, column)
     * @param number the value
     * @param row the row
     * @param column the column
     * @throws IllegalArgumentException if row or column is invalid
     */
    public void set(int number, int row, int column) {
        if (row > fieldSize || row < 0) throw new IllegalArgumentException("no such row in the sudoku!");
        if (column > fieldSize || column < 0) throw new IllegalArgumentException("no such column in the sudoku!");
        field[row - 1][column - 1].setValue(number);
    }

    /**
     * hide cell
     * @param row the row
     * @param column the column
     * @throws IllegalArgumentException if row or column is invalid
     */
    public void hide(int row, int column) {
        if (row > fieldSize || row < 0) throw new IllegalArgumentException("no such row in the sudoku!");
        if (column > fieldSize || column < 0) throw new IllegalArgumentException("no such column in the sudoku!");
        field[row - 1][column - 1].hide();
    }

    /**
     * show cell
     * @param row the row
     * @param column the column
     * @throws IllegalArgumentException if row or column is invalid
     */
    public void show(int row, int column) {
        if (row > fieldSize || row < 0) throw new IllegalArgumentException("no such row in the sudoku!");
        if (column > fieldSize || column < 0) throw new IllegalArgumentException("no such column in the sudoku!");
        field[row - 1][column - 1].show();
    }

    /**
     * Return true if cell at (row, col) is shown
     * @param row the row
     * @param column the column
     * @return <tt>true</tt> if cell is shown
     * @throws IllegalArgumentException if row or column is invalid
     */
    public boolean isShown(int row, int column) {
        if (row > fieldSize || row < 0) throw new IllegalArgumentException("no such row in the sudoku!");
        if (column > fieldSize || column < 0) throw new IllegalArgumentException("no such column in the sudoku!");
        return field[row - 1][column - 1].isShown();
    }


    /**
     * Generate the sudoku randomly.
     */
    public void generate() {
        generateFullField(1, 1);
    }

    // return the fieldSize
    private int variantsPerCell() {
        return fieldSize;
    }
    /**
     * insert an number trial into the cell's hashset see {@link SudokuCell}
     * @param number the number
     * @param row the target row
     * @param column the cell's row
     */
    private void tryNumber(int number, int row, int column) {
        field[row - 1][column - 1].tryNumber(number);
    }

    // the number has been tried in (row, column)?
    private boolean numberHasBeenTried(int number, int row, int column) {
        return field[row - 1][column - 1].isTried(number);
    }

    // size of the number tried in (row, column)
    private int numberOfTriedNumbers(int row, int column) {
        return field[row - 1][column - 1].numberOfTried();
    }

    // Check a sub-grid inside the sudoku
    private boolean checkNumberBox(int number, int row, int column) {
        int r = row, c = column;
        if (r % blockSize == 0) {
            r -= blockSize - 1;
        } else {
            r = (r / blockSize) * blockSize + 1;
        }
        if (c % blockSize == 0) {
            c -= blockSize - 1;
        } else {
            c = (c / blockSize) * blockSize + 1;
        }
        for (int i = r; i < r + blockSize; i++) {
            for (int j = c; j < c + blockSize; j++) {
                if (field[i - 1][j - 1].isFilled() && field[i - 1][j - 1].getValue() == number) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check whether there is number in row
    private boolean checkNumberRow(int number, int row) {
        for (int i = 0; i < fieldSize; i++) {
            if (field[row - 1][i].isFilled() && field[row - 1][i].getValue() == number) {
                return false;
            }
        }
        return true;
    }


    // Check whether there is number in column
    private boolean checkNumberColumn(int number, int column) {
        for (int i = 0; i < fieldSize; i++) {
            if (field[i][column - 1].isFilled() && field[i][column - 1].getValue() == number) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNumberField(int number, int row, int column) {
        return (checkNumberBox(number, row, column)
                && checkNumberRow(number, row)
                && checkNumberColumn(number, column));
    }

    private int numberOfPossibleVariants(int row, int number) {
        int rst = 0;
        for (int i = 1; i <= fieldSize; i++) {
            if (checkNumberField(i, row, number)) {
                rst++;
            }
        }
        return rst;
    }

    @Deprecated
    private boolean isCorrect() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (field[i][j].isFilled()) {
                    int value = field[i][j].getValue();
                    field[i][j].hide();
                    boolean correct = checkNumberField(value, i + 1, j + 1);
                    field[i][j].show();
                    if (!correct) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    // class containing the entry(x,y) for the cell
    class Index {
        int row, column;
        public Index(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    // find the next cell
    private Index nextCell(int row, int column) {
        int r = row, c = column;
        if (c < fieldSize) {
            ++c;
        } else {
            c = 1;
            ++r;
        }
        return new Index(r, c);
    }

    private Index cellWithMinVariants() {
        int r = 1, c = 1, min = 9;
        for (int i = 1; i <= fieldSize; i++) {
            for (int j = 1; j <= fieldSize; j++) {
                if (!field[i - 1][j - 1].isFilled()) {
                    if (numberOfPossibleVariants(i, j) < min) {
                        min = numberOfPossibleVariants(i, j);
                        r = i;
                        c = j;
                    }
                }
            }
        }
        return new Index(r, c);
    }

    // generate the random number between (0, fieldsize)
    private int getRandomIndex() {
        return (int) ((Math.random() * 10) % fieldSize + 1);
    }



    // generate the sudoku
    private void generateFullField(int row, int column) {
        if (!isFilled(getFieldSize(), getFieldSize())) {
            while (numberOfTriedNumbers(row, column) < variantsPerCell()) {
                int candidate = 0;
                do {
                    candidate = getRandomIndex();
                } while (numberHasBeenTried(candidate, row, column));
                if (checkNumberField(candidate, row, column)) {
                    set(candidate, row, column);
                    Index nextCell = nextCell(row, column);
                    if (nextCell.row <= getFieldSize() && nextCell.column <= getFieldSize()) {
                        generateFullField(nextCell.row, nextCell.column);
                    }
                } else {
                    tryNumber(candidate, row, column);
                }
            }
            if (!isFilled(getFieldSize(), getFieldSize())) {
                reset(row, column);
            }
        }
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= fieldSize; i++) {
            for (int j = 1; j <= fieldSize; j++) {
                if (isShown(i, j)) {
                    sb.append(" ").append(get(i, j).getValue()).append(" ");
                } else {
                    sb.append("   ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }



}
