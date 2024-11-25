 @echo off
REM Change to the source directory
cd /d c:\robot\binance\Binance\src

REM Compile the Java file
REM javac -cp "C:/robot/binance/Binance/lib/json-20210307.jar" -d ../build C:/robot/binance/Binance/src/com/example/App.java
REM javac -d out src/com/example/Appp.java
javac -cp "C:/robot/binance/Binance/lib/json-20210307.jar" -d C:/robot/binance/Binance/build/out C:/robot/binance/Binance/build/ServerOriginal.java
REM cd /d c:\robot\binance\Binance
REM javac -d out src/com/example/*.java

REM Check if the compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed.
    exit /b %errorlevel%
)

REM Change to the build directory
cd /d c:\robot\binance\Binance\build\out

REM Run the compiled class
java -cp ".;../lib/json-20210307.jar" ServerOriginal
REM java -cp "C:/robot/binance/Binance/lib/json-20210307.jar" com.example.Server
REM java -cp ".;../lib/json-20210307.jar" App

REM Check if the execution was successful
if %errorlevel% neq 0 (
    echo Execution failed.
    exit /b %errorlevel%
)
REM echo creating the jar executable
REM jar cfm MyApp.jar c:\robot\binance\Binance\build\out\MANIFEST.MF -C c:\robot\binance\Binance\build\out .

REM c:\robot\binance\Binance\build\out

echo build the jar executable
REM java -jar MyApp.jar

echo Execution completed successfully.
REM cd /d c:\robot\binance\Binance\build