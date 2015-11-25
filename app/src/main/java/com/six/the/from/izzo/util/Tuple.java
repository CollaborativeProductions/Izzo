package com.six.the.from.izzo.util;


public class Tuple<X, Y> extends Object {
    private final X x;
    private final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getLeft() {
        return this.x;
    }

    public Y getRight() {
        return this.y;
    }
}
