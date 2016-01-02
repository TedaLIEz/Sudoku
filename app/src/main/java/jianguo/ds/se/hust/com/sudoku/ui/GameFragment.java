package jianguo.ds.se.hust.com.sudoku.ui;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Stack;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealLinearLayout;
import jianguo.ds.se.hust.com.sudoku.C;
import jianguo.ds.se.hust.com.sudoku.R;
import jianguo.ds.se.hust.com.sudoku.adapter.SudokuAdapter;
import jianguo.ds.se.hust.com.sudoku.ds.SudokuCell;
import jianguo.ds.se.hust.com.sudoku.ds.SudokuGenerator;
import jianguo.ds.se.hust.com.sudoku.util.LogUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnSudokuChangedListener {
    private static final String ARG_LEVEL = "level";

    private String mLv;

    private GridView sudoku;
    private GridView number;
    FloatingActionButton fab;
    private LinearLayout frame;

    private View mCurrSelectedView;
    private SudokuCell mCurrCell;
    SudokuAdapter sudokuAdapter;
    private Stack<Integer> mCurrPos;
    private int pos;

    private SudokuGenerator generater;

    public GameFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lv Game level
     * @return A new instance of fragment GameFragment.
     */
    public static GameFragment newInstance(String lv) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LEVEL, lv);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLv = getArguments().getString(ARG_LEVEL);
            mCurrPos = new Stack<>();
            generater = new SudokuGenerator();
            generater.setBlockSize(3);
            switch (mLv) {
                case "easy":
                    generater.generateProblems(SudokuGenerator.Level.EASY);
                    break;
                case "mid":
                    generater.generateProblems(SudokuGenerator.Level.MID);
                    break;
                case "hard":
                    generater.generateProblems(SudokuGenerator.Level.HARD);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid level");
            }

        } else {
            throw new IllegalArgumentException("You must have a level args first!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (C.DEBUG) {
            LogUtil.log(mLv);
        }

        View view;
        if (Build.VERSION_CODES.LOLLIPOP > Build.VERSION.SDK_INT) {
            view = inflater.inflate(R.layout.fragment_game, container, false);
            frame = (RevealLinearLayout) view.findViewById(R.id.frame);
        } else {
            view = inflater.inflate(R.layout.fragment_game, container, false);
            frame = (LinearLayout) view.findViewById(R.id.frame);
        }

        sudoku = (GridView) view.findViewById(R.id.gv_sudoku);
        number = (GridView) view.findViewById(R.id.gv_number);

        sudokuAdapter = new SudokuAdapter(getActivity(), generater.asList(), generater);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_undo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrCell != null) {
                    while (!mCurrPos.isEmpty()) {
                        int pos = mCurrPos.pop();
                        sudokuAdapter.clear(pos);
                        if (sudoku.getChildAt(pos) instanceof TextView) {
                            ((TextView) sudoku.getChildAt(pos)).setText("");
                        }
                        showRevealAnimation();
                    }
                }

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        adapter.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9");
        initSudoku();
        number.setAdapter(adapter);
        number.setOnItemClickListener(this);
        return view;
    }

    private void showRevealAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = (fab.getPaddingStart() + fab.getPaddingEnd() + fab.getLeft() + fab.getRight()) / 2;
            int cy = (fab.getPaddingTop() + fab.getPaddingBottom() + fab.getBottom() + fab.getTop()) / 2;
            int finalRadius = (int) Math.hypot(frame.getMeasuredWidth(), frame.getMeasuredHeight());
            Animator animator = android.view.ViewAnimationUtils.createCircularReveal(frame, cx, cy, 0, finalRadius);
            frame.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    sudoku.setVisibility(View.INVISIBLE);
                    number.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    frame.setBackgroundColor(getResources().getColor(R.color.colorText));
                    sudoku.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(400);
            animator.start();
        } else {
            int cx = (fab.getPaddingLeft() + fab.getPaddingRight() + fab.getLeft() + fab.getRight()) / 2;
            int cy = (fab.getPaddingTop() + fab.getPaddingBottom() + fab.getBottom() + fab.getTop()) / 2;
            int finalRadius = (int) Math.hypot(frame.getMeasuredWidth(), frame.getMeasuredHeight());
            Animator animator = ViewAnimationUtils.createCircularReveal(frame, cx, cy, 0, finalRadius);
            frame.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    sudoku.setVisibility(View.INVISIBLE);
                    number.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    frame.setBackgroundColor(getResources().getColor(R.color.colorText));
                    sudoku.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(400);
            animator.start();
        }
    }

    private void initSudoku() {
        sudoku.setOnItemClickListener(this);
        sudokuAdapter.setOnChangedListener(this);
        sudoku.setAdapter(sudokuAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.gv_sudoku) {
            pos = position;
            view.setBackground(getActivity().getResources().getDrawable(R.drawable.item_border_selected));
            if (view != mCurrSelectedView && mCurrSelectedView != null) {
                mCurrSelectedView.setBackground(getActivity().getResources().getDrawable(R.drawable.item_border));
            }
            mCurrSelectedView = view;
            mCurrCell = (SudokuCell) parent.getAdapter().getItem(position);
        } else if (parent.getId() == R.id.gv_number) {
            mCurrPos.push(pos);
            if (mCurrSelectedView != null) {
                if (mCurrSelectedView instanceof TextView) {
                    if (((TextView) mCurrSelectedView).getCurrentTextColor()
                            != getResources().getColor(R.color.colorAccent)
                            && mCurrCell.isShown()) {
                        return;
                    }
                    TextView tv = (TextView) mCurrSelectedView;
                    tv.setText(parent.getAdapter().getItem(position) + "");
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    mCurrCell.setValue(Integer.parseInt(parent.getAdapter().getItem(position) + ""));
                    sudokuAdapter.notifyDataSetChanged(mCurrPos.peek());
                    if (C.DEBUG) {
                        LogUtil.log("input after \n" + generater.toString());
                    }

                }
            }
        }
    }


    @Override
    public void onSuccess() {
        Snackbar.make(frame, getResources().getString(R.string.success), Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.another_game), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SudokuGenerator generator = new SudokuGenerator();
                        generator.setBlockSize(3);
                        switch (mLv) {
                            case "easy":
                                generator.generateProblems(SudokuGenerator.Level.EASY);
                                break;
                            case "mid":
                                generator.generateProblems(SudokuGenerator.Level.MID);
                                break;
                            case "hard":
                                generator.generateProblems(SudokuGenerator.Level.HARD);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid level");
                        }
                        sudokuAdapter = new SudokuAdapter(getActivity(), generator.asList(), generator);
                        sudoku.setAdapter(sudokuAdapter);
                        sudoku.invalidateViews();
                    }
                }).show();
    }


    @Override
    public void onRowError(int row) {
        @ColorInt int red = getResources().getColor(R.color.android_red_dark);
        for (int i = 0; i < generater.getFieldSize(); i++) {
            View view = sudoku.getChildAt(row * generater.getFieldSize() + i);
            if (view instanceof TextView) {
                animateFlashText((TextView) view, ((TextView) view).getCurrentTextColor(), red, false);
            }
        }
    }


    @Override
    public void onColError(int col) {
        @ColorInt int red = getResources().getColor(R.color.android_red_dark);
        for (int i = 0; i <= col; i++) {
            View view = sudoku.getChildAt(col + i * generater.getFieldSize());
            if (view instanceof TextView) {
                animateFlashText((TextView) view, ((TextView) view).getCurrentTextColor(), red, false);
            }
        }
    }


    @Override
    public void onBlockError(int row, int col) {
        @ColorInt int red = getResources().getColor(R.color.android_red_dark);
        int left = col / generater.getBlockSize() * 3;
        int bottom = row / generater.getBlockSize() * 3;
        for (int i = bottom; i < bottom + 3; i++) {
            for (int j = left; j < left + 3; j++) {
                View view = sudoku.getChildAt(j + i * generater.getFieldSize());
                if (view instanceof TextView) {
                    animateFlashText((TextView) view, ((TextView) view).getCurrentTextColor(), red, false);
                }
            }
        }

    }

    private static void animateFlashText(
            final TextView textView, @ColorInt int color1, @ColorInt int color2, boolean staySecondColor) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setTextColor((Integer) animation.getAnimatedValue());
            }
        });
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(staySecondColor ? 2 : 3);
        anim.setDuration(250);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }
}
