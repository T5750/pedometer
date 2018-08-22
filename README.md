# Pedometer

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
- 设置透明状态栏，Android Version >= 4.4

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
- 保存数据库频率，TodayStepService.DB_SAVE_COUNTER
- 卡路里，目前默认按体重60kg来进行计算

## References
- [TodayStepCounter](https://github.com/jiahongfei/TodayStepCounter) Commits on Feb 24, 2018
- [DylanStepCount](https://github.com/linglongxin24/DylanStepCount) Commits on Nov 23, 2017

## License
Pedometer is Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).