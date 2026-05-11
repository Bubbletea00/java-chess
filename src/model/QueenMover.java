package model;

public final class QueenMover extends SlidingMover {
	public QueenMover() {
		super(new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
	}
}

