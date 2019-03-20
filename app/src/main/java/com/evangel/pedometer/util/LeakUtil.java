package com.evangel.pedometer.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Leaks
 */
public class LeakUtil {
	private static final Field TEXT_LINE_CACHED;
	static {
		Field textLineCached = null;
		try {
			textLineCached = Class.forName("android.text.TextLine")
					.getDeclaredField("sCached");
			textLineCached.setAccessible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		TEXT_LINE_CACHED = textLineCached;
	}

	/**
	 * TextLine.sCached<br/>
	 * https://stackoverflow.com/questions/30397356/android-memory-leak-on-textview-leakcanary-leak-can-be-ignored
	 */
	public static void clearTextLineCache() {
		// If the field was not found for whatever reason just return.
		if (TEXT_LINE_CACHED == null)
			return;
		Object cached = null;
		try {
			// Get reference to the TextLine sCached array.
			cached = TEXT_LINE_CACHED.get(null);
		} catch (Exception ex) {
			//
		}
		if (cached != null) {
			// Clear the array.
			for (int i = 0, size = Array.getLength(cached); i < size; i++) {
				Array.set(cached, i, null);
			}
		}
	}
}
