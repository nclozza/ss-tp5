package ar.edu.itba.ss;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Writer {

    private PrintWriter writer;

    public Writer(final String path) {
        try {
            this.writer = new PrintWriter(path, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
