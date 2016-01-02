package jianguo.ds.se.hust.com.sudoku.ds;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jianguo.ds.se.hust.com.sudoku.C;
import jianguo.ds.se.hust.com.sudoku.util.LogUtil;

/**
 * Created by JianGuo on 16/1/1.
 * Wrapper class for SudokuField, for generating a complete sudoku by <em>size</em>
 * Seee {@link SudokuField} for details.
 */
public class SudokuGenerator {
    private SudokuField sudoku;
    private int blockSize;
    private Level level;

    /**
     * return sudoku's length
     * @return sudoku's length
     */
    public int getFieldSize() {
        return sudoku.getFieldSize();
    }

    /**
     * return the basic unit size of a block, for example a 9 * 9 sudoku's blockSize will be 3
     * @return the blockSize
     */
    public int getBlockSize() {
        return blockSize;
    }

    public enum Level {
        HARD, EASY, MID
    }

    /**
     * State for the sudoku
     */
    public enum State {
        ROW_ERROR, COL_ERROR, BLOCK_ERROR, SUCCESS, ROW_NOT_FILLED, COL_NOT_FILLED, BLOCK_NOT_FILLED
    }

    /**
     * set block size of the sudoku, note that a 9 * 9 sudoku its block size should be 3
     * @param size the size of blocks
     */
    public void setBlockSize(int size) {
        sudoku = new SudokuField(size);
        this.blockSize = size;
        sudoku.generate();
    }

    /**
     * Get the sudoku by randomly generating, note that the sudoku right now has no blanks
     * @return the sudoku, See {@link SudokuField}
     */
    public SudokuField getSudoku() {
        return sudoku;
    }


    /**
     * Gererate the problem set by difficulty randomly, which some of the sudoku's cell is hidden.
     */
    public void generateProblems(Level level) {
        this.level = level;
        if (level == Level.HARD) {
            for (int i = 0; i < sudoku.numberOfCells() / 16 * 15; i++) {
                int x = getRandomIndex();
                int y = getRandomIndex();
                if (sudoku.isFilled(x, y)) {
                    sudoku.hide(x, y);
                }
            }
        } else if (level == Level.MID) {
            for (int i = 0; i < sudoku.numberOfCells() / 16 * 7; i++) {
                int x = getRandomIndex();
                int y = getRandomIndex();
                if (sudoku.isFilled(x, y)) {
                    sudoku.hide(x, y);
                }
            }
        } else if (level == Level.EASY) {
            for (int i = 0; i < sudoku.numberOfCells() / 3; i++) {
                int x = getRandomIndex();
                int y = getRandomIndex();
                if (sudoku.isFilled(x, y)) {
                    sudoku.hide(x, y);
                }
            }
        }
    }

    // get random int from(0, getFieldSize())
    private int getRandomIndex() {
        return (int) ((Math.random() * 10) % sudoku.getFieldSize() + 1);
    }

    /**
     * Check row's state
     * @param row the checked row
     * @return STATE See{@link SudokuGenerator.State}
     */
    public State checkRow(int row) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < sudoku.getFieldSize(); i++) {
            if (C.DEBUG) {
                LogUtil.log(sudoku.getField()[row][i].toString() + "row");
            }
            if (!sudoku.getField()[row][i].isFilled()) {
                return State.ROW_NOT_FILLED;
            }
            set.add(sudoku.getField()[row][i].getValue());
        }
        if (set.size() == blockSize * blockSize) {
            return State.SUCCESS;
        } else {
            return State.ROW_ERROR;
        }
    }

    /**
     * Check col's state
     * @param col the checked col
     * @return STATE See{@link SudokuGenerator.State}
     */
    public State checkColumn(int col) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < sudoku.getFieldSize(); i++) {
            if (C.DEBUG) {
                LogUtil.log(sudoku.getField()[i][col].toString() + "col");
            }
            if (!sudoku.getField()[i][col].isFilled()) {
                return State.COL_NOT_FILLED;
            }
            set.add(sudoku.getField()[i][col].getValue());
        }
        if (set.size() == blockSize * blockSize) {
            return State.SUCCESS;
        } else {
            return State.COL_ERROR;
        }
    }

    /**
     * Check block's state
     * @param row the checked row
     * @param column the checked column
     * @return STATE See{@link SudokuGenerator.State}
     */
    public State checkBlock(int row, int column) {
        HashSet<Integer> set = new HashSet<>();
        int left = column / blockSize * 3;
        int bottom = row / blockSize * 3;
        for (int i = bottom; i < bottom + 3; i++) {
            for (int j = left; j < left + 3; j++) {
                if (!sudoku.getField()[i][j].isFilled()) {
                    return State.BLOCK_NOT_FILLED;
                }
                set.add(sudoku.getField()[i][j].getValue());
            }
        }
        if (set.size() == blockSize * blockSize) {
            return State.SUCCESS;
        } else {
            return State.BLOCK_ERROR;
        }
    }

    @Override
    public String toString() {
        return sudoku.toString();
    }


    /**
     * Convert the 2-d sudoku data structure to 1-d list
     * @return the list of sudokucell, see {@link SudokuCell}
     */
    public List<SudokuCell> asList() {
        List<SudokuCell> list = new ArrayList<>();
        for (int i = 1; i <= sudoku.getFieldSize(); i++) {
            for (int j = 1; j <= sudoku.getFieldSize(); j++) {
                list.add(sudoku.get(i, j));
            }
        }
        return list;
    }
}
