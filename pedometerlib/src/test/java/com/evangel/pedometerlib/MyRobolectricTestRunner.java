package com.evangel.pedometerlib;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

/**
 */
public class MyRobolectricTestRunner extends RobolectricTestRunner {
	/**
	 * Creates a runner to run {@code testClass}. Looks in your working
	 * directory for your AndroidManifest.xml file and res directory by default.
	 * Use the {@link Config} annotation to configure.
	 *
	 * @param testClass
	 *            the test class to be run
	 * @throws InitializationError
	 *             if junit says so
	 */
	public MyRobolectricTestRunner(Class<?> testClass)
			throws InitializationError {
		super(testClass);
	}

	@Override
	protected AndroidManifest getAppManifest(Config config) {
		String projectName = "pedometer";
		int nameLength = projectName.length();
		String rootPath = System.getProperty("user.dir", "./");
		int index = rootPath.indexOf(projectName);
		if (index == -1) {
			throw new RuntimeException("project name not found in user.dir");
		}
		// 获取项目的根目录
		rootPath = rootPath.substring(0, index + nameLength);
		String manifestProperty = rootPath
				+ "/pedometerlib/src/main/AndroidManifest.xml";
		String resProperty = rootPath + "/pedometerlib/src/main/res";
		String assetsProperty = rootPath + "/pedometerlib/src/main/assets";
		return new AndroidManifest(Fs.fileFromPath(manifestProperty),
				Fs.fileFromPath(resProperty), Fs.fileFromPath(assetsProperty)) {
			@Override
			public int getTargetSdkVersion() {
				return 21;
			}
		};
	}
}
