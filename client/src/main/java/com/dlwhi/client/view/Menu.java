package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public interface Menu {
    public void display(PrintStream out);
    public String dispatchInput(Scanner in);
    public void addCommand(String value, String name);
    public void setActiveCommands(Map<String, String> commands);
}
