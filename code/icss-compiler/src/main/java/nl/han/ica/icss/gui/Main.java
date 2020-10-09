package nl.han.ica.icss.gui;

import nl.han.ica.datastructures.HANLinkedList;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        HANLinkedList<String> linkedlist = new HANLinkedList<>();

        MainGui.launch(MainGui.class,args);
    }
}
