package com.android.arkyumon.ui.main;

public class MovingAverage extends MainActivity {

    public MovingAverage(int period) {
        assert period > 0 : "Period must be a positive integer";
        p = period;
    }

    public void newNum(double num) {
        sum += num;
        window.add(num);
        if (window.size() > p) {
            sum -= window.remove();
        }
    }

    public double getAvg() {
        if (window.isEmpty()) return 0; // technically the average is undefined
        return sum / window.size();
    }
}
