package com.dlwhi.client.view;

import java.io.PrintStream;
import java.util.Scanner;

public class Option {
    private final String text;
    private Call onChooseEvent;

    public Option(String text, Call onChoose) {
        this.text = text;
        this.onChooseEvent = onChoose;
    }

    public Option(String text) {
        this.text = text;
    }

    public void display(PrintStream out) {
        out.println(text);
    }

    public void onChoose(Call action) {
        onChooseEvent = action;
    }

    public void fire(Scanner source) {
        if (onChooseEvent == null) {
            return;
        }
        Object[] args = onChooseEvent.getParameterArray();
        for (int i = 0; i < args.length; ++i) {
            args[i] = source.nextLine();
        }
        onChooseEvent.invoke(args);
    }
}
