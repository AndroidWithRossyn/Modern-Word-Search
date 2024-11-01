package generator;

public enum Direction {

	RIGHT(0),
	UP_RIGHT(315),
	UP(270),
	UP_LEFT(225),
	LEFT(180),
	DOWN_LEFT(135),
	DOWN(90),
	DOWN_RIGHT(45);

	private static final float RADIAN_SNAP = 0.785398f;

	private float mAngleDegree;
	private int mPositionShift;

	/**
	 * @param angleDegree
	 * @param positionShift
	 */
	private Direction(float angleDegree) {
		mAngleDegree = angleDegree;
	}

	public static Direction getDirection(float radians) {
		if (radians <= (.5 * RADIAN_SNAP) && radians >= -(.5 * RADIAN_SNAP)) {
			return RIGHT;
		} else if (radians > (.5 * RADIAN_SNAP) && radians < (1.5 * RADIAN_SNAP)) {
			return UP_RIGHT;
		} else if (radians >= (1.5 * RADIAN_SNAP) && radians <= (2.5 * RADIAN_SNAP)) {
			return UP;
		} else if (radians > (2.5 * RADIAN_SNAP) && radians < (3.5 * RADIAN_SNAP)) {
			return UP_LEFT;
		} else if (radians >= (3.5 * RADIAN_SNAP) || radians <= -(3.5 * RADIAN_SNAP)) {
			return LEFT;
		} else if (radians < -(2.5 * RADIAN_SNAP) && radians > -(3.5 * RADIAN_SNAP)) {
			return DOWN_LEFT;
		} else if (radians <= -(1.5 * RADIAN_SNAP) && radians >= -(2.5 * RADIAN_SNAP)) {
			return DOWN;
		} else {
			return DOWN_RIGHT;
		}
	}

	public boolean isAngle() {
		return (this == UP_RIGHT || this == DOWN_RIGHT || this == UP_LEFT || this == DOWN_LEFT);
	}

	public boolean isLeft() {
		return (this == LEFT || this == UP_LEFT || this == DOWN_LEFT);
	}

	public boolean isUp() {
		return (this == UP || this == UP_LEFT || this == UP_RIGHT);
	}

	public boolean isDown() {
		return (this == DOWN || this == DOWN_LEFT || this == DOWN_RIGHT);
	}

	public boolean isRight() {
		return (this == RIGHT || this == UP_RIGHT || this == DOWN_RIGHT);
	}

	public float getAngleDegree() {
		return mAngleDegree;
	}

	public int getPositionShift() {
		return mPositionShift;
	}
}
