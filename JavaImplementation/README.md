javac src/main/Bank.java src/main/model/*.java


java -cp src/main Bank

java -cp src main.Bank

$ javac -cp ".;src;junit-platform-console-standalone.jar" src/test/AccountTest.java

java -cp ".;src;junit-platform-console-standalone.jar" org.junit.platform.console.ConsoleLauncher --scan-class-path --scan-classpath