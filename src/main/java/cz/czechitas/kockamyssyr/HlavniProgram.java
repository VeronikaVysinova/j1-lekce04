package cz.czechitas.kockamyssyr;

import dev.czechitas.java1.kockamyssyr.api.*;

import java.awt.*;
import java.util.Random;

/**
 * Hlaví třída pro hru Kočka–myš–sýr.
 */
public class HlavniProgram {
    private final Random random = new Random();

    private final int VELIKOST_PRVKU = 50;
    private final int SIRKA_OKNA = 1000 - VELIKOST_PRVKU;
    private final int VYSKA_OKNA = 600 - VELIKOST_PRVKU;

    private Cat tom;
    private Mouse jerry;

    /**
     * Spouštěcí metoda celé aplikace.
     *
     * @param args
     */
    public static void main(String[] args) {
        new HlavniProgram().run();
    }

    /**
     * Hlavní metoda obsahující výkonný kód.
     */
    public void run() {
        tom = vytvorKocku();
        // tom.setBrain(new KeyboardBrain(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D));

        jerry = vytvorMys();
        jerry.setBrain(new KeyboardBrain());

        vytvorVeci(4);
        chytMys();
    }

    public void chytMys() {
        chytJerryho();
    }

    public void chytJerryho() {
        int rozdilX = tom.getX() - jerry.getX(); // vysledek mensi nez 0 = Tom je vlevo; vetsi nez 0 = Tom je vpravo
        if (rozdilX < 0) {// Tom je vlevo od Jerryho
            tomKoukaNahoru(); //startovni pozice Toma
            tom.turnRight();
            while (jerry.getX() > tom.getX()) {  //dokud je Jerryho souradnice vetsi, Tom se pohybuje
                vyhniSeStromu();
                tom.moveForward();
            }
        } else if (rozdilX > 0) { //Tom je vpravo od Jerryho
            tom.turnLeft();
            tomKoukaNahoru();
            while (jerry.getX() < tom.getX()) {
                vyhniSeStromu();
                tom.moveForward();
            }


        }
        int rozdilY = tom.getY() - jerry.getY();

        if (rozdilY < 0) { //Tom je vys nez Jerry
            tomKoukaNahoru();
            tom.turnRight();
            tom.turnRight();
            while (jerry.getY() > tom.getY()) {
                vyhniSeStromu();
                tom.moveForward();
            }


        } else if (rozdilY > 0) {  //Tom je niz nez Jerry
            tomKoukaNahoru();
            while (jerry.getY() < tom.getY()) {
                vyhniSeStromu();
                tom.moveForward();

            }

        }


    }

    public void vyhniSeStromu() {
        if (tom.isPossibleToMoveForward()) {
            return;
        } else {
            tom.turnLeft();
            tom.moveForward();
            tom.turnLeft();
            return;
        }
    }

    public void tomKoukaNahoru() {
        if (tom.getOrientation() == PlayerOrientation.RIGHT) {
            tom.turnLeft();
            return;
        } else if (tom.getOrientation() == PlayerOrientation.LEFT) {
            tom.turnRight();
            return;
        } else if (tom.getOrientation() == PlayerOrientation.UP) {
            return;
        } else if (tom.getOrientation() == PlayerOrientation.DOWN) {
            tom.turnLeft();
            tom.turnLeft();
            return;
        }

    }

    public void vytvorVeci(int pocetStromu) {
        for (int i = 0; i < pocetStromu; i++) {
            vytvorStrom();
        }
        vytvorSyr();
        vytvorJitrnici();
    }

    public Tree vytvorStrom() {
        return new Tree(vytvorNahodnyBod());
    }

    public Cat vytvorKocku() {
        return new Cat(vytvorNahodnyBod());
    }

    public Mouse vytvorMys() {
        return new Mouse(vytvorNahodnyBod());
    }

    public Cheese vytvorSyr() {
        return new Cheese(vytvorNahodnyBod());
    }

    public Meat vytvorJitrnici() {
        return new Meat(vytvorNahodnyBod());
    }

    private Point vytvorNahodnyBod() {
        return new Point(random.nextInt(SIRKA_OKNA), random.nextInt(VYSKA_OKNA));
    }

}
