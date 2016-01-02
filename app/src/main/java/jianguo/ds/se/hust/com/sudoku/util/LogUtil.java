package jianguo.ds.se.hust.com.sudoku.util;

import android.util.Log;

/**
 * Created by JianGuo on 16/1/2.
 * Simple Wrapper of Log
 */
public class LogUtil {
    public static void log(String content) {
        Log.i("sudoku", content);
    }

    public static void log(Object obj) {
        Log.i("sudoku", obj.toString());
    }
}
