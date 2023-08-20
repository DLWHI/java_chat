package edu.school21.client.menu;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import edu.school21.client.exceptions.InvalidCommandException;

@Component("startMenu")
@PropertySource("classpath:config/edu/school21/client/bindings.cfg")
public class StartMenu implements Menu {
    private static final String text =
        "1. Sign In%n" +
        "2. Sign Up%n" +
        "3. Exit%n";
    
    @Value("#{${menu}}")
    private Map<String, String> commands;

    @Override
    public void display(PrintStream out) {
        out.printf(text);
    }

    @Override
    public String dispatchInput(Scanner in) {
        String input = in.nextLine();
        String output = commands.get(input);
        if (output == null) {
            throw new InvalidCommandException("Unknown command " + input);
        }
        return output;
    }
}
