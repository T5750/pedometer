[![Build Status](https://travis-ci.org/T5750/pedometer.svg?branch=master)](https://travis-ci.org/T5750/pedometer)
[![Releases](https://img.shields.io/github/release/T5750/pedometer.svg)](https://github.com/T5750/pedometer/releases/latest)
[![License](https://img.shields.io/badge/license-Apache-blue.svg)](https://github.com/T5750/pedometer/blob/master/LICENSE.txt)

![homepage](http://www.wailian.work/images/2018/08/22/homepage.gif)

# Pedometer
[<img src="http://www.wailian.work/images/2018/08/23/direct-apk-download.png" alt="Direct apk download" height="80">](https://github.com/T5750/pedometer/releases/latest)

## Runtime Environment
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Android Studio](http://www.androiddevtools.cn/#android-studio)
- [HelloCharts for Android](https://github.com/lecho/hellocharts-android)
- [LeakCanary](https://github.com/square/leakcanary)
- [Genymotion](https://www.genymotion.com/download/)
- [Android Debug Database](https://github.com/amitshekhariitbhu/Android-Debug-Database)
- [Logger](https://github.com/orhanobut/logger)

## What's included
- 计步模块（类似微信运动，支付宝计步，今日步数）
- 步数仿支付宝余额数字累加效果
- 计算BMI指数，仿支付宝芝麻信用分仪表盘
- HelloChart绘制今日步数柱状图
- ~~设置透明状态栏，Android Version >= 4.4~~
- 备份 / 还原数据库

### HelloCharts for Android
- ```implementation 'com.github.lecho:hellocharts-library:1.5.8@aar'```
- ```lecho.lib.hellocharts.view.ColumnChartView```

### LeakCanary
```
  debugImplementation 'com.squareup.leakcanary:leakcanary-android:1.6.1'
  releaseImplementation 'com.squareup.leakcanary:leakcanary-android-no-op:1.6.1'
```

```
public class ExampleApplication extends Application {
  @Override 
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code...
  }
}
```
启动Activity的shell命令
```
adb shell am start -n [包名]/[Activity名]
adb shell am start -n com.evangel.pedometer/com.evangel.pedometer.activity.MainActivity
```

### Android Debug Database
1. ```debugImplementation 'com.amitshekhar.android:debug-db:1.0.4'```
1. Genymotion -> Configuration -> Network mode -> Bridge
1. D/DebugDB: Open http://xxx.xxx.xxx.xxx:8080 in your browser

### Logger
1. ```implementation 'com.orhanobut:logger:2.2.0'```
1. ```Logger.addLogAdapter(new AndroidLogAdapter());```
1. ```Logger.d("hello");```

> Changed Android Studio from 3.1.4 to 3.3 Canary 5.

## Tips
- 一些不能后台的手机，需告诉用户每天早上打开一次app才可以正常计步
- 数据库中，运动数据保存366天，`TodayStepService.DB_LIMIT`
- 保存数据库的频率，`TodayStepService.DB_SAVE_COUNTER`
- 卡路里，目前默认按体重60kg来进行计算
- 设置中的提醒，仅供参考

## References
- [TodayStepCounter](https://github.com/jiahongfei/TodayStepCounter) Commits on Feb 24, 2018
- [DylanStepCount](https://github.com/linglongxin24/DylanStepCount) Commits on Nov 23, 2017
- [FastHub](https://github.com/k0shk0sh/FastHub)

## License
Pedometer is Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).