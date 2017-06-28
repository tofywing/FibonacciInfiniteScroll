package assignment.parsable.yisfibonacci.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Created by Yee on 6/27/17.
 */

public class Fibonacci implements Parcelable {

    private String previous;
    private String current;
    private UUID mId;

    public Fibonacci(String current, String previous) {
        mId = UUID.randomUUID();
        this.previous = previous;
        this.current = current;
    }

    public Fibonacci(String current, String previous, UUID uuid) {
        this.mId = uuid;
        this.previous = previous;
        this.current = current;
    }


    protected Fibonacci(Parcel in) {
        previous = in.readString();
        current = in.readString();
    }

    public static final Creator<Fibonacci> CREATOR = new Creator<Fibonacci>() {
        @Override
        public Fibonacci createFromParcel(Parcel in) {
            return new Fibonacci(in);
        }

        @Override
        public Fibonacci[] newArray(int size) {
            return new Fibonacci[size];
        }
    };

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public UUID getId() {
        return mId;
    }

    public String getPrevious() {
        return previous;
    }

    public String getCurrent() {
        return current;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(previous);
        dest.writeString(current);
    }
}
