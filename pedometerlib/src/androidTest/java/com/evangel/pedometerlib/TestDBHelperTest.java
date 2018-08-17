package com.evangel.pedometerlib;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

/**
 * test
 */
@RunWith(AndroidJUnit4.class)
public class TestDBHelperTest {
	private TestDBHelper testDBHelper;

	@Before
	public void before() {
		Context appContext = InstrumentationRegistry.getTargetContext();
		System.out.println(appContext);
		testDBHelper = TestDBHelper.factory(appContext);
	}

	/**
	 * 根据id修改步数
	 */
	@Test
	public void updateStepById() {
		String _id = "1";
		long stepNum = 100;
		testDBHelper.updateStepById(_id, stepNum);
	}
}
