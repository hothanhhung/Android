@ECHO on
rem CALL gradlew installDebug
rem GOTO end
CALL gradlew assembleRelease
IF "%ERRORLEVEL%" NEQ "0" GOTO error

SET RELEASE_FOLDER=app/build/outputs/apk/release
SET RELEASE_FOLDER_DEL=app\build\outputs\apk\release
SET BUILD_FOLDER=build-tools\28.0.0
CALL DEL /f %RELEASE_FOLDER_DEL%\app-release-unsigned-aligned.apk
CALL %ANDROID_HOME%\%BUILD_FOLDER%\zipalign -v -p 4 %RELEASE_FOLDER%/app-release-unsigned.apk %RELEASE_FOLDER%/app-release-unsigned-aligned.apk
IF "%ERRORLEVEL%" NEQ "0" GOTO error
CALL DEL /f %RELEASE_FOLDER_DEL%\app-release.apk
CALL %ANDROID_HOME%\%BUILD_FOLDER%\apksigner sign --v1-signing-enabled true --ks-key-alias hung --ks-pass pass:thanhhung --key-pass pass:thanhhung --ks images/KeyStoreFromJun2016.jks --out %RELEASE_FOLDER%/app-release.apk %RELEASE_FOLDER%/app-release-unsigned-aligned.apk
IF "%ERRORLEVEL%" NEQ "0" GOTO  error
CALL %ANDROID_HOME%\%BUILD_FOLDER%\apksigner verify %RELEASE_FOLDER%/app-release.apk
IF "%ERRORLEVEL%" NEQ "0" GOTO  error
ECHO installing to device
CALL %ANDROID_HOME%\platform-tools\adb -d install %RELEASE_FOLDER%/app-release.apk
IF "%ERRORLEVEL%" NEQ "0" GOTO  error
GOTO done

:error
ECHO Unsuccessful
GOTO end

:done
ECHO Done and successful

:end
ECHO --------------End--------------