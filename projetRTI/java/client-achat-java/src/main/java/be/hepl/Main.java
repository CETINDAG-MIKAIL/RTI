package be.hepl;

import static be.hepl.FileHelper.getSettingInt;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        int portServeur = getSettingInt("./config.txt", "PORT_ENCODING");   // juste pour te montrer que tu peux utiliser la classe FileHelper

        System.out.println("portServeur = " + portServeur);
    }
}