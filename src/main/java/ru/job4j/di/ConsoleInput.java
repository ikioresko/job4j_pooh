package ru.job4j.di;

import java.util.Scanner;

public class ConsoleInput {
    private Scanner scanner = new Scanner(System.in);

    public String askStr() {
        return scanner.nextLine();
    }
}
