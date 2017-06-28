package assignment.parsable.yisfibonacci.model;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.icu.text.LocaleDisplayNames;
import android.nfc.Tag;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.UUID;

import assignment.parsable.yisfibonacci.database.FibonacciSchema;
import assignment.parsable.yisfibonacci.database.FibonacciSchema.FibonacciTable;

/**
 * Created by Yee on 6/27/17.
 */

public class FibonacciCursorWrapper extends CursorWrapper {
    public static final String TAG = "fibonacciCursorWrapper";

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public FibonacciCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Fibonacci getFibonacci() {
        String uuidString = getString(getColumnIndex(FibonacciTable.Cols.UUID));
        String prev = getString(getColumnIndex(FibonacciTable.Cols.PREV));
        String curr = getString(getColumnIndex(FibonacciTable.Cols.CURR));
        UUID uuid = UUID.fromString(uuidString);
        return new Fibonacci(curr, prev, uuid);
    }
}
