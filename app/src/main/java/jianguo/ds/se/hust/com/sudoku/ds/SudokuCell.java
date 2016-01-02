package jianguo.ds.se.hust.com.sudoku.ds;

import java.util.HashSet;
import java.util.Observable;

/**
 * Created by JianGuo on 16/1/2.
 * The basic unit of a Sudoku's cell
 */
public class SudokuCell {
    private int value;
    private boolean filled;
    private boolean shown;
    private HashSet<Integer> tried;


    public SudokuCell() {
        filled = false;
        shown = true;
        tried = new HashSet<>();
    }

    /**
     * return true if is shown
     * @return <tt>true</tt> if is shown
     */
    public boolean isShown() {
        return shown;
    }

    /**
     * return true if is filled
     * @return <tt>true</tt> if is filled
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * return the value
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * set value to the cell
     * @param number the value
     */
    public void setValue(int number) {
        filled = true;
        shown = true;
        value = number;
        tried.add(number);
    }

    /**
     * Clear the current value with tried remained.
     */
    public void clear() {
        value = 0;
        hide();
    }


    /**
     * Reset all the data in the cell.
     */
    public void reset() {
        clear();
        tried.clear();
    }

    /**
     * hide the cell
     */
    public void hide() {
        filled = false;
        shown = false;
    }

    /**
     * show the cell
     */
    public void show() {
        filled = true;
        shown = true;
    }

    /**
     * Has this number been tried in the cell?
     * @param number the number
     * @return <tt>true</tt> if has
     */
    public boolean isTried(int number) {
        return tried.contains(number);
    }

    /**
     * Try this number
     * @param number the number
     */
    public void tryNumber(int number) {
        tried.add(number);
    }

    /**
     * Size of the number tried in the cell
     * @return the size
     */
    public int numberOfTried() {
        return tried.size();
    }

    @Override
    public String toString() {
        return "SudokuCell{" +
                "value=" + value +
                ", filled=" + filled +
                ", tried=" + tried +
                '}';
    }
}
