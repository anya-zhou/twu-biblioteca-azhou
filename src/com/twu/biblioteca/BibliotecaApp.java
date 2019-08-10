package com.twu.biblioteca;

import java.io.PrintStream;

public class BibliotecaApp {
    PrintStream printer;
    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";

    public BibliotecaApp() {
        this.printer = System.out;
    }

    public BibliotecaApp(PrintStream printer) {
        this.printer = printer;
    }

    public void start() {
        this.printer.println(WELCOME_MSG);
    }

    public static void main(String[] args) {
        BibliotecaApp app = new BibliotecaApp();
        app.start();
    }
}
