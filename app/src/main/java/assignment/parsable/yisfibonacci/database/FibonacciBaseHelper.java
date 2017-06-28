package assignment.parsable.yisfibonacci.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import assignment.parsable.yisfibonacci.database.FibonacciSchema.FibonacciTable;
import assignment.parsable.yisfibonacci.model.Fibonacci;


/**
 * Created by Yee on 6/20/17.
 */

public class FibonacciBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fibonacciBase.db";
    public static final int DATABASE_VERSION = 1;

    public FibonacciBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FibonacciTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FibonacciTable.Cols.UUID + ", " +
                FibonacciTable.Cols.PREV + ", " +
                FibonacciTable.Cols.CURR +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
