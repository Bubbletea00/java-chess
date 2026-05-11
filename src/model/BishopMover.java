package model;

public final class BishopMover extends SlidingMover {
    public BishopMover() {
        super(new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
    }
}

