package assignment.parsable.yisfibonacci;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.math.BigInteger;

import assignment.parsable.yisfibonacci.database.FibonacciBaseHelper;
import assignment.parsable.yisfibonacci.model.Fibonacci;
import assignment.parsable.yisfibonacci.model.FibonacciLab;

/**
 * Created by Yee on 6/19/17.
 */

public class FibonacciService extends IntentService {
    public static final String TAG = "FibonacciService";
    public static final String EXTRA_FIBONACCI = "current referred fibonacci value";
    public static final String FIBONACCI_PREPARED = "fibonacci data is prepared";
    public static final Fibonacci FIRST_FIBONACCI = new Fibonacci("0", "0");
    public static final Fibonacci SECOND_FIBONACCI = new Fibonacci("1", "0");

    static Context mContext;
    SQLiteDatabase mDatabase;
    FibonacciLab mLab;

    public FibonacciService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        mContext = context;
        return new Intent(context, FibonacciService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mDatabase = new FibonacciBaseHelper(mContext).getWritableDatabase();
        mLab = new FibonacciLab(mContext);
        assert intent != null;
        Fibonacci fibonacci = intent.getParcelableExtra(EXTRA_FIBONACCI);
        BigInteger curr;
        BigInteger prev;
        Log.d(TAG, (fibonacci != null) + "fibo is null");
        if (fibonacci != null) {
            curr = new BigInteger(fibonacci.getCurrent());
            prev = new BigInteger(fibonacci.getPrevious());

        } else {
            Log.d(TAG, "initialize database in service");
            initializeDatabase();
            curr = new BigInteger(SECOND_FIBONACCI.getCurrent());
            prev = new BigInteger(SECOND_FIBONACCI.getPrevious());
        }
        calculateIncreasedFibonacci(curr, prev);
        Intent intent1 = new Intent(FibonacciService.FIBONACCI_PREPARED);
        sendBroadcast(intent1);
    }

    void calculateIncreasedFibonacci(BigInteger curr, BigInteger prev) {
        for (int i = 0; i < 20; i++) {
            BigInteger newCurr = curr.add(prev);
            prev = curr;
            curr = newCurr;
            //database insert
            Fibonacci fibonacci = new Fibonacci(curr.toString(), prev.toString());
            mLab.addFibonacci(fibonacci);
        }
    }

    private void initializeDatabase() {
        mLab.addFibonacci(FIRST_FIBONACCI);
        mLab.addFibonacci(SECOND_FIBONACCI);
    }
}
