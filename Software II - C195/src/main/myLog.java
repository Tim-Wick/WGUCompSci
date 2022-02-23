package main;

import java.io.*;
import java.time.Instant;

interface Loggable {
    public void logIt(myLog log);
}

/**
 * Class for logging to file
 */
public class myLog {
    private String username;
    private boolean success;
    public myLog(String username, boolean success) {
        this.username = username;
        this.success = success;
    }
    public String getUsername() { return this.username; }
    public boolean getSuccess() { return this.success; }
    public String toString() {return this.username;}

    /**
     * Write log out to file
     */
    public void writeLog() {
        System.out.println("Trying to log");
        try (FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            String loginStatus;
            if (this.success) {
                loginStatus = " successfully logged in";
            } else {
                loginStatus = " failed to log in";
            }
            printWriter.println(this.username + loginStatus + " at " + Instant.now().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Lambda to print to CLI implementing the Loggable interface
        Loggable logMessage = log -> System.out.println(log.getUsername() + "successfully logged in? " + log.getSuccess());
    }
}
