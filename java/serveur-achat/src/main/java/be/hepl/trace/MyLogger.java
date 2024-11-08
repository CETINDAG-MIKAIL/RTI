package be.hepl.trace;

import be.hepl.FileHelper;

public class MyLogger implements Logger{
    private static MyLogger instance;
    private static String filename;

    private MyLogger() {}

    public static MyLogger getInstance() {
        if (instance == null) {
            synchronized (MyLogger.class) { //when some threads will get the instance at the same time
                if (instance == null) {
                    instance = new MyLogger();
                    filename = "log.txt";
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized void trace(String msg) {
        if(!FileHelper.writeToFile(filename, msg)) {
            System.out.println("ERROR: Could not write to " + filename);
        }
    }

    @Override
    public synchronized void clearTrace() {
        if(!FileHelper.clearFileContent(filename)) {
            System.out.println("ERROR: Could not clear file " + filename);
        }
    }
}
