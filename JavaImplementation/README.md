javac src/main/Bank.java src/main/data/*.java src/main/model/*.java

javac src/main/data/BankDataStorage.java


java -cp src/main Bank

java -cp src main.Bank

javac -cp ".;src;junit-platform-console-standalone.jar" src/test/AccountTest.java src/test/BankDataStorageTest.java

java -cp ".;src;junit-platform-console-standalone.jar" org.junit.platform.console.ConsoleLauncher --scan-class-path --scan-classpath