package jianguo.ds.se.hust.com.sudoku.ui;


/**
 * Created by JianGuo on 16/1/2.
 * Interface for dealing with the sudoku when changes.
 */
public interface OnSudokuChangedListener {
    /**
     * When sudoku completed and is correct.
     */
    void onSuccess();
    /**
     * When there is an error in row
     * @param row the row
     */
    void onRowError(int row);
    /**
     * When there is an error in col
     * @param col the col
     */
    void onColError(int col);
    /**
     * When there is an error in the current block
     * @param row the row
     * @param col the col
     */
    void onBlockError(int row, int col);
}
