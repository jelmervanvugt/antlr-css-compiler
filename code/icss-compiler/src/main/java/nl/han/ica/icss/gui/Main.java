package nl.han.ica.icss.gui;

import nl.han.ica.datastructures.HANLinkedList;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        HANLinkedList<String> linkedlist = new HANLinkedList<>();

        linkedlist.addFirst("first");
        linkedlist.addFirst("second");
        linkedlist.addFirst("third");

        linkedlist.delete(2);

        System.out.println(linkedlist.get(0));
        System.out.println(linkedlist.get(1));
        System.out.println(linkedlist.get(2));


        //MainGui.launch(MainGui.class,args);
    }
}
