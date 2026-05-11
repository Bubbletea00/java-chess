package model;

public final class RookMover extends SlidingMover {
    public RookMover() {
        super(new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
    }
}

