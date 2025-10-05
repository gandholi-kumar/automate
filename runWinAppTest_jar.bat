@echo off
REM Edit these paths as needed:
set WINAPPDRIVER_PATH=C:\Program Files (x86)\Windows Application Driver\WinAppDriver.exe
set WINDOWS_APPFORM_PATH=C:\Users\User\Desktop\publish\WindowsAppForm.exe

REM Run the tests
java -cp target/automate-0.0.1-SNAPSHOT.jar;target/dependency/* ^
-Dwinappdriver.path=%WINAPPDRIVER_PATH% ^
-Dwindowsappform.path=%WINDOWS_APPFORM_PATH% ^
org.testng.TestNG testng.xml
pause

