package org.example.untitled;


import org.example.untitled.models.Champ;

class Main {
    public static void main(String[] args) {
        final Champ champ = new Champ(32, 5);

        champ.placeMines();

        System.out.println(champ);
    }
}
