# 1. VMApplication.

 - Ở application kế thừa application từ thư viện .

Example:
```java
public class  XXXApp extent VMApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initInfoDevice(AppConstant.CODE_APP, BuildConfig.VERSION_NAME);

        .....
        //to do code init app
    }
}
```

# 2. Extend : BaseVMActivity.java  -> theme old && BaseVMAppCompatActivity.java -> theme new


     Kế thừa tất cả Activity trong app từ base BaseVMActivity trong service <br>
     mục đích để biết minh đang ở trong app chính . Nếu app mình đã có 1 base <br>
     Activty khác mình chỉ cần cho base đó kế thừa lại BaseVMActivity .<br>

# 3. Add lib Realm build.gradle project.

 ```java
   dependencies {
          classpath 'io.realm:realm-gradle-plugin:5.10.0'
          // NOTE: Do not place your application dependencies here; they belong
          // in the individual module build.gradle files
      }
  }
```

# 3. Set target 26.

 ```java
   defaultConfig {
          ...
          targetSdkVersion 26
          ....
          testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
          flavorDimensions "versionCode"
          multiDexEnabled true
          ndk.abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
      }
```