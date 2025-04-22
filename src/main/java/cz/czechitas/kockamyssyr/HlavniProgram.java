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
        //tom.setBrain(new KeyboardBrain(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D));

        jerry = vytvorMys();
        jerry.setBrain(new KeyboardBrain());

        vytvorVeci(4);

        chytMys();
        //chytMys2();
        //chytMys3();
    }

    //region Jednoduchá varianta

    /**
     * Jednoduchá varianta – Tom jde na souřadnice, kde na začátku byl Jerry. Když tam dorazí, podívá se znovu, kde je
     * Jerry a vyrazí jeho směrem. Opakuje tak dlouho, odkud Jerryho nechytí.
     */
    public void chytMys() {
        while (jerry.isAlive()) {
            jdiNaSouradnice(jerry.getX(), jerry.getY());
        }
    }

    private void jdiNaSouradnice(int x, int y) {
        int horizontalniRozdil = tom.getX() - x; // záporný = Tom je vlevo od Jerryho, kladný = Tom je vpravo od Jerryho
        if (horizontalniRozdil < 0) {   //Tom je vlevo od Jerryho
            otocSeVpravo();
            tom.moveForward(Math.abs(horizontalniRozdil));
        } else if (horizontalniRozdil > 0) {
            otocSeVlevo();
            tom.moveForward(horizontalniRozdil);
        }

        int vertikalniRozdil = tom.getY() - y; // záporný = Tom je nahoře od Jerryho, kladný = Tom je dole od Jerryho
        if (vertikalniRozdil < 0) {   //Tom je nahoře od Jerryho
            otocSeDolu();
            tom.moveForward(Math.abs(vertikalniRozdil));
        } else if (vertikalniRozdil > 0) {
            otocSeNahoru();
            tom.moveForward(vertikalniRozdil);
        }
    }
    //endregion

    //region Efektivnější varianta

    /**
     * Efektivnější varianta – Tom jde na souřadnice, kde je Jerry. Pokud mu jde Jerry naproti, skončí Tom cetsu už
     * v okamžiku, kdy je ve stejném sloupci/řádku.
     */
    public void chytMys2() {
        while (jerry.isAlive()) {
            jdiZaJerrym();
        }
    }

    private void jdiZaJerrym() {
        int horizontalniRozdil = tom.getX() - jerry.getX(); // záporný = Tom je vlevo od Jerryho, kladný = Tom je vpravo od Jerryho
        if (horizontalniRozdil < 0) {   //Tom je vlevo od Jerryho
            otocSeVpravo();
            while (tom.getX() < jerry.getX()) {
                tom.moveForward();
            }
        } else if (horizontalniRozdil > 0) {
            otocSeVlevo();
            while (tom.getX() > jerry.getX()) {
                tom.moveForward();
            }
        }

        int vertikalniRozdil = tom.getY() - jerry.getY(); // záporný = Tom je nahoře od Jerryho, kladný = Tom je dole od Jerryho
        if (vertikalniRozdil < 0) {   //Tom je nahoře od Jerryho
            otocSeDolu();
            while (tom.getY() < jerry.getY()) {
                tom.moveForward();
            }
        } else if (vertikalniRozdil > 0) {
            otocSeNahoru();
            while (tom.getY() > jerry.getY()) {
                tom.moveForward();
            }
        }
    }
    //endregion

    //region Vyhýbání se stromům

    /**
     * Efektivnější varianta, ve které se Tom zároveň vyhýbá stromům.
     */
    public void chytMys3() {
        while (jerry.isAlive()) {
            jdiZaJerrymVyhybejSeStromum();
        }
    }

    private void jdiZaJerrymVyhybejSeStromum() {
        int horizontalniRozdil = tom.getX() - jerry.getX(); // záporný = Tom je vlevo od Jerryho, kladný = Tom je vpravo od Jerryho
        if (horizontalniRozdil < 0) {   //Tom je vlevo od Jerryho
            otocSeVpravo();
            while (tom.getX() < jerry.getX()) {
                vyhniSeStromu();
                tom.moveForward();
            }
        } else if (horizontalniRozdil > 0) {
            otocSeVlevo();
            while (tom.getX() > jerry.getX()) {
                vyhniSeStromu();
                tom.moveForward();
            }
        }

        int vertikalniRozdil = tom.getY() - jerry.getY(); // záporný = Tom je nahoře od Jerryho, kladný = Tom je dole od Jerryho
        if (vertikalniRozdil < 0) {   //Tom je nahoře od Jerryho
            otocSeDolu();
            while (tom.getY() < jerry.getY()) {
                vyhniSeStromu();
                tom.moveForward();
            }
        } else if (vertikalniRozdil > 0) {
            otocSeNahoru();
            while (tom.getY() > jerry.getY()) {
                vyhniSeStromu();
                tom.moveForward();
            }
        }
    }

    private void vyhniSeStromu() {
        if (tom.isPossibleToMoveForward()) {
            return;
        }
        tom.turnRight();
        tom.moveForward();
        tom.turnLeft();
    }
    //endregion

    //region Společné metody pro různé varianty
    private void otocSeVpravo() {
        if (tom.getOrientation() == PlayerOrientation.RIGHT) {
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.UP) {
            tom.turnRight();
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.DOWN) {
            tom.turnLeft();
            return;
        }
        tom.turnLeft();
        tom.turnLeft();
    }

    private void otocSeVlevo() {
        if (tom.getOrientation() == PlayerOrientation.LEFT) {
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.UP) {
            tom.turnLeft();
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.DOWN) {
            tom.turnRight();
            return;
        }
        tom.turnLeft();
        tom.turnLeft();
    }

    private void otocSeNahoru() {
        if (tom.getOrientation() == PlayerOrientation.UP) {
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.LEFT) {
            tom.turnRight();
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.RIGHT) {
            tom.turnLeft();
            return;
        }
        tom.turnLeft();
        tom.turnLeft();
    }

    private void otocSeDolu() {
        if (tom.getOrientation() == PlayerOrientation.DOWN) {
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.LEFT) {
            tom.turnLeft();
            return;
        }
        if (tom.getOrientation() == PlayerOrientation.RIGHT) {
            tom.turnRight();
            return;
        }
        tom.turnLeft();
        tom.turnLeft();
    }
    //endregion

    //region Metody pro vytvoření herní plochy
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
    //endregion
}
