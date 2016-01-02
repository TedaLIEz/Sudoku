package jianguo.ds.se.hust.com.sudoku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import jianguo.ds.se.hust.com.sudoku.C;
import jianguo.ds.se.hust.com.sudoku.R;
import jianguo.ds.se.hust.com.sudoku.ds.SudokuCell;
import jianguo.ds.se.hust.com.sudoku.ds.SudokuGenerator;
import jianguo.ds.se.hust.com.sudoku.ui.OnSudokuChangedListener;
import jianguo.ds.se.hust.com.sudoku.util.LogUtil;

/**
 * Created by JianGuo on 16/1/2.
 */
public class SudokuAdapter extends BaseAdapter {
    List<SudokuCell> datas;
    LayoutInflater inflater;
    OnSudokuChangedListener listener;
    SudokuGenerator generator;


    public SudokuAdapter(Context ctx, List<SudokuCell> list, SudokuGenerator generator) {
        inflater = LayoutInflater.from(ctx);
        datas = list;
        this.generator = generator;
    }



    public void setOnChangedListener(OnSudokuChangedListener listener) {
        this.listener = listener;
    }



    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    public void clear(int position) {
        datas.get(position).clear();
    }

    private int getCol(int position) {
        return position % generator.getSudoku().getFieldSize() + 1;
    }

    private int getRow(int position) {
        return position / generator.getSudoku().getFieldSize() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.mTv = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SudokuCell cell = datas.get(position);
        if (cell.isShown()) {
            holder.mTv.setText(cell.getValue()+"");
        }
        return convertView;
    }



    static class ViewHolder {
        TextView mTv;
    }


    public void notifyDataSetChanged(int pos) {
        super.notifyDataSetChanged();
        if (pos < 0) return;
        SudokuGenerator.State row = generator.checkRow(getRow(pos) - 1);
        SudokuGenerator.State column = generator.checkColumn(getCol(pos) - 1);
        SudokuGenerator.State block = generator.checkBlock(getRow(pos) - 1, getCol(pos) - 1);
        boolean allFill = generator.getSudoku().allCellsFilled();
        if (listener != null) {
            if (block == SudokuGenerator.State.BLOCK_ERROR) {
                if (C.DEBUG) {
                    LogUtil.log("block error");
                }
                listener.onBlockError(getRow(pos) - 1, getCol(pos) - 1);
            } else if (column == SudokuGenerator.State.COL_ERROR) {
                if (C.DEBUG) {
                    LogUtil.log("col error");
                }
                listener.onColError(getCol(pos) - 1);
            } else if (row == SudokuGenerator.State.ROW_ERROR) {
                if (C.DEBUG) {
                    LogUtil.log("row error");
                }
                listener.onRowError(getRow(pos) - 1);
            } else if (allFill) {
                listener.onSuccess();
            }
        }


    }
}
