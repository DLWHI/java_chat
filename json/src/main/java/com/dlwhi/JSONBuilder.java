package com.dlwhi;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;

public class JSONBuilder {
    private enum state {
        KEY,
        VALUE
    }

    private int depth = 1;
    private JSONPackage root = new JSONPackage();

    // TODO add array handling
    // TODO move this to json builder
    public JSONPackage parseString(String source) {
        reset();
        try (Scanner parser = getJSONReader(source)) {
            JSONPackage current = root;
            while(depth != 0) {
                Object element = parser.next();
                
            }
        } catch (NoSuchElementException e) {
            throw new InvalidJSONException(e.getMessage());
        }
        return root;
    }

    public Object next() {
        
    }

    public void reset() {
        depth = 1;
        root.clear();
        queue.clear();;
    }

    private static Scanner getJSONReader(String source) {
        return new Scanner(source).skip("\\{");
    }

    private JSONPackage dive(String childName, JSONPackage current) {
        JSONPackage child = new JSONPackage(current);
        current.add(childName, child);
        depth++;
        return child;
    }

    private JSONPackage rise(JSONPackage current) {
        depth--;
        return current.getParent();
    }

    private void queue(String element, JSONPackage current) {
        // if (queue.size() == 1) {
        //     current.add(queue.poll(), element);
        // } else {
        //     queue.add(element);
        // }
    }
}
