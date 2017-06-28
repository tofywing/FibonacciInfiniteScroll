package assignment.parsable.yisfibonacci.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.UUID;

import assignment.parsable.yisfibonacci.database.FibonacciBaseHelper;
import assignment.parsable.yisfibonacci.database.FibonacciSchema.FibonacciTable;

/**
 * Created by Yee on 6/20/17.
 */

public class FibonacciLab {

    private static FibonacciLab instance;
    private static Context mContext;
    private SQLiteDatabase mDataBase;

    private static class FibonacciHolder {
        private static final FibonacciLab INSTANCE = new FibonacciLab(mContext);
    }

    public FibonacciLab(Context context) {
        mContext = context;
        mDataBase = new FibonacciBaseHelper(mContext).getWritableDatabase();

    }

    public static FibonacciLab getInstance(Context context) {
        mContext = context;
        return FibonacciHolder.INSTANCE;
    }

    public void addFibonacci(Fibonacci fibonacci) {
        ContentValues values = getContentValues(fibonacci);
        mDataBase.insert(FibonacciTable.NAME, null, values);
    }

    public Fibonacci getFibonacci(UUID id) {
        try (FibonacciCursorWrapper cursor = queryFibonaccis(
                FibonacciTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getFibonacci();
        }
    }

    public ArrayList<Fibonacci> getFibonaccis() {
        ArrayList<Fibonacci> fibonaccis = new ArrayList<>();
        try (FibonacciCursorWrapper cursor = queryFibonaccis(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                fibonaccis.add(cursor.getFibonacci());
                cursor.moveToNext();
            }
        }
        return fibonaccis;
    }

    public void updateFibonacci(Fibonacci fibonacci) {
        String uuidString = fibonacci.getId().toString();
        ContentValues values = getContentValues(fibonacci);
        mDataBase.update(FibonacciTable.NAME, values,
                FibonacciTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );
    }

    private static ContentValues getContentValues(Fibonacci fibonacci) {
        ContentValues values = new ContentValues();
        values.put(FibonacciTable.Cols.UUID, fibonacci.getId().toString());
        values.put(FibonacciTable.Cols.PREV, fibonacci.getPrevious());
        values.put(FibonacciTable.Cols.CURR, fibonacci.getCurrent());
        return values;
    }


    private FibonacciCursorWrapper queryFibonaccis(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                FibonacciTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new FibonacciCursorWrapper(cursor);
    }
}
