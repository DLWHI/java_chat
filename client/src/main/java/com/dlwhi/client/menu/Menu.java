package com.dlwhi.client.menu;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

import com.dlwhi.client.app.App.Command;

public interface Menu {
    public void display(PrintStream out);
    public Command dispatchInput(Scanner in);
    public void addCommand(String value, Command name);
    public void setActiveCommands(Map<String, Command> commands);
}
