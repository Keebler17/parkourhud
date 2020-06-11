package io.github.keebler17.parkourhud;

public enum Buttons {
	PKHUD(300),
	DONE(301),
	DECIMALS(302),
	XYZ_DECIMALS(303),
	FACING_DECIMALS(304),
	VELOCITY_DECIMALS(305),
	SHADOW(306),
	
	;
	private final int id;
	
	Buttons(int id) {
		this.id = id;
	}
	
	public int getValue() {
		return id;
	}
}
