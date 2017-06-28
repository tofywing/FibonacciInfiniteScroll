package assignment.parsable.yisfibonacci.database;

/**
 * Created by Yee on 6/20/17.
 */

public class FibonacciSchema {
    public static final class FibonacciTable{
        public static final String NAME = "FibonacciLab";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String PREV = "previousValue";
            public static final String CURR = "currentValue";
        }
    }

}
