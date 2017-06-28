package assignment.parsable.yisfibonacci;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigInteger;
import java.util.ArrayList;

import assignment.parsable.yisfibonacci.adapter.LoaderAdapter;
import assignment.parsable.yisfibonacci.database.FibonacciBaseHelper;
import assignment.parsable.yisfibonacci.database.FibonacciSchema;
import assignment.parsable.yisfibonacci.model.Fibonacci;
import assignment.parsable.yisfibonacci.model.FibonacciCursorWrapper;
import assignment.parsable.yisfibonacci.model.FibonacciLab;


/**
 * Created by Yee on 6/19/17.
 */

public class FibonacciFragment extends Fragment {
    public static final String TAG = "FibonacciFragment";
    public static final String PREFS = "SharedPreferenceFibonacciFragment";
    public static final String FIBONACCI_DATA = "current fibonacci data";
    public static final String FIBONACCIS_DATA = "current fibonaccis data";

    public static final int NO_POSITION = 0;

    LoaderAdapter mAdapter;
    RecyclerView mLoader;
    BroadcastReceiver mReceiver;
    ProgressBar mProgressBar;
    SwipeRefreshLayout mRefreshLayout;
    SharedPreferences mPreferences;
    ArrayList<Fibonacci> mFibonaccis;
    FibonacciLab mLab;

    public static FibonacciFragment newInstance() {
        Bundle args = new Bundle();
        FibonacciFragment fragment = new FibonacciFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fibonacci, container, false);
        mLab = new FibonacciLab(getContext());
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mFibonaccis = mLab.getFibonaccis();
                Log.d(TAG, "Fibo is fetched from database");
                setAdapter();
                mProgressBar.setVisibility(View.INVISIBLE);
                mRefreshLayout.setRefreshing(false);
            }
        };
        mLoader = (RecyclerView) view.findViewById(R.id.loader_container);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mLoader.setLayoutManager(manager);
        mLoader.setHasFixedSize(true);
        mLoader.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isBottomViewVisible()) {
                    loadAction();
                }
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ResourcesCompat.getColor(getResources(), R.color
                        .colorPrimary, null),
                android.graphics
                        .PorterDuff
                        .Mode
                        .MULTIPLY);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_container);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAction();
            }
        });
        mPreferences = getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        if (savedInstanceState != null) {
            mFibonaccis = savedInstanceState.getParcelableArrayList(FIBONACCIS_DATA);
        } else {
            String json = mPreferences.getString(FIBONACCI_DATA, "");
            Gson gson = new Gson();
            mFibonaccis = gson.fromJson(json, new TypeToken<ArrayList<Fibonacci>>() {
            }.getType());
            if (mFibonaccis == null || mFibonaccis.isEmpty()) {
                loadAction();
            } else {
                Log.d(TAG, "Fibo is fetched from SharedPreference");
                setAdapter();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LoaderAdapter(mFibonaccis, getContext());
            mLoader.setAdapter(mAdapter);
        } else {
            mAdapter.setDataSet(mFibonaccis);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(FibonacciService.FIBONACCI_PREPARED);
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mFibonaccis);
        editor.putString(FIBONACCI_DATA, json);
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FIBONACCIS_DATA, mFibonaccis);
    }

    private void loadAction() {
        Intent intent = FibonacciService.newIntent(getContext());
        Log.d(TAG, mFibonaccis == null ? "Fibo is null" : mFibonaccis.isEmpty() ? "Fibo is empty" : "Fibo size > 0");
        Fibonacci fibonacci = mFibonaccis != null && !mFibonaccis.isEmpty() ? mFibonaccis.get(mFibonaccis.size() - 1)
                : null;
        intent.putExtra(FibonacciService.EXTRA_FIBONACCI, fibonacci);
        getActivity().startService(intent);
    }

    private int getLastVisibleItemPosition() {
        RecyclerView.LayoutManager manager = mLoader.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        }
        return NO_POSITION;
    }

    private boolean isBottomViewVisible() {
        int lastItemIndex = getLastVisibleItemPosition();
        return lastItemIndex != NO_POSITION && lastItemIndex == mAdapter.getItemCount() - 1;
    }
}
