package edu.school21.client.menu;

import java.io.PrintStream;
import java.util.Scanner;

public interface Menu {
    public void display(PrintStream out);
    public String dispatchInput(Scanner in);
}
