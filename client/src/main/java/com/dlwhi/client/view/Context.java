package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.Scanner;

public interface Context {
    void show(PrintStream out);
    String dispatchInput(Scanner in);

    void subscribe(String event, Call handler);
    void notifyRecieve(String message);
}
