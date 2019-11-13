package com.tsc;

import java.io.*;

public class Writer {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String p) {
        path = p;
    }

    public Writer(String p) {
        path = p;
    }

    public void writeToFile(String text) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(text + "\n");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ix) {
            System.out.println(ix.toString());
        }
    }
}
