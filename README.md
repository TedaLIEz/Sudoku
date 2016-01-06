#Sudoku
#A simple Sudoku game in Android

##Development

###Build with gradle
***

1. Have Android SDK "tools", "platform-tools", and "build-tools" directories in your PATH (http://developer.android.com/sdk/index.html)
2. Open the Android SDK Manager (shell command: `android`).</br>
   Expand the Tools directory and select "Android SDK Build-tools (Version 21.1.2)".</br>
   Expand the Extras directory and install "Android Support Repository"</br>
   Select everything for the newest SDK Platform, API 22, and also API 21</br>
   Export `ANDROID_HOME` pointing to your Android SDK
3. Execute ./gradlew
5. You can install the app directly with `adb install -r Sudoku/build/outputs/apk/app-debug.apk` instead

###Build with AndroidStudio
***

We are using the newest `Android Studio` for development. Development with Eclipse is currently not possible because we are using the new project structure.

From Android Studio: File -> Import Project -> Select the top folder