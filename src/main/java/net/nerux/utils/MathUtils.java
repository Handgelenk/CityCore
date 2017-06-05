package net.nerux.utils;

import java.text.DecimalFormat;

public final class MathUtils {
	private static DecimalFormat format = new DecimalFormat("0.00");

	public static String round(double a) {
		return format.format(a);
	}

	public static String getPercent(double a) {
		return format.format(a * 100.0D);
	}
}
