import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        char spielerO = 'O';
        char spielerX = 'X';
        char aktuellerSpieler = 'X';
        boolean spielAktiv = true;
        boolean programmAktiv = true;
        boolean hardmode = false;
        Scanner reader = new Scanner(System.in);
        int menue;

        while (programmAktiv) {

            System.out.print("-------Menü-------\n[1] Spielen\n[2] Spielen (Hardmode!)\n[3] Verlassen\n>");

            String menueInput = reader.next();

            try {
                menue = Integer.parseInt(menueInput);
            }
            catch (NumberFormatException e) {
                System.out.println("Geben Sie eine gültige Zahl ein");
                continue;
            }

            if (menue < 1 || menue > 3) {
                System.out.println("Die Zahl muss 1, 2 oder 3 betragen");
                continue;
            }

            switch (menue) {
                case 1 -> {
                    spielAktiv = true;
                    hardmode = false;
                }

                case 2 -> {
                    spielAktiv = true;
                    hardmode = true;
                }
                case 3 -> {
                    System.out.println("Vielen Dank fürs Spielen!");
                    programmAktiv = false;
                    spielAktiv = false;
                }
            }

            char[][] spielfeld = SpielfeldErstellen();

            while (spielAktiv) {
                
                if (!hardmode) {
                    SpielfeldAusgeben(spielfeld);
                }
                System.out.print("Spieler " + aktuellerSpieler + " ist an der Reihe:\n>");
                String spielerEingabe = reader.next(); //hier exception zeug noch

                int spielerInput;
                try {
                    spielerInput = Integer.parseInt(spielerEingabe);
                }
                catch (NumberFormatException e) {
                    if (Objects.equals(spielerEingabe, "exit")) {
                        System.out.println("Notausgang wurde betätigt");
                        for (int i = 0; i < 5; i++) {
                            try {
                            Loading();
                        }
                            catch (InterruptedException ignored) {
                            }
                    }
                        programmAktiv = false;
                        break;
                    }
                    System.out.println("Geben Sie eine gültige Zahl ein");
                    continue;
                }
                if (spielerInput < 1 || spielerInput > 7) {
                    System.out.println("Die Zahl muss zwischen 1 und 7 liegen!");
                    continue;
                }
                SpielfeldAuffuellen(spielfeld, aktuellerSpieler, --spielerInput);
                if (VictoryControll(spielfeld) == 'X' || VictoryControll(spielfeld) == 'O') {
                    SpielfeldAusgeben(spielfeld);
                    System.out.println("Spieler " + aktuellerSpieler + " hat gewonnen!");
                    spielAktiv = false;
                }
                aktuellerSpieler = SpielerWechsel(aktuellerSpieler, spielerX, spielerO);
            }
        }
    }

    static char[][] SpielfeldErstellen() {
        char[][] spielfeld = new char[7][7];

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                spielfeld[i][j] = ' ';
            }
        }
        return spielfeld;
    }

    static void SpielfeldAusgeben(char[][] spielfeld) {
        int[] reihenAnzeige = {1, 2, 3, 4, 5, 6, 7};
        for (char[] reihe : spielfeld) {
            System.out.println(Arrays.toString(reihe));
        }
        System.out.println(Arrays.toString(reihenAnzeige));
    }

    static void SpielfeldAuffuellen(char[][] spielfeld, char aktuellerSpieler, int spielerInput) {
        char andererSpieler;
        if (aktuellerSpieler == 'X') {
            andererSpieler = 'O';
        } else {
            andererSpieler = 'X';
        }

        for (int i = 6; i >= 0; i--) {
            if (i == 6) {
                if (spielfeld[0][spielerInput] != ' ') {
                    System.out.println("Diese Reihe ist bereits voll! Wähle eine andere. Jetzt ist aber erst mal Spieler " + andererSpieler + " dran");
                    //aktuellerSpieler bleibt
                    break;
                }
            }
            if (spielfeld[i][spielerInput] == ' ') {
                spielfeld[i][spielerInput] = aktuellerSpieler;
                break;
            }

        }
    }

    static char SpielerWechsel(char aktuellerSpieler, char spielerX, char spielerO) {
        char updatedSpieler;
        if (aktuellerSpieler == 'X') {
            updatedSpieler = spielerO;
        } else {
            updatedSpieler = spielerX;
        }
        return updatedSpieler;
    }

    static char VictoryControll(char[][] spielfeld) {
        char gewinner = ' ';
        int counterX;
        int counterO;

        for (int i = 6; i >= 0; i--) {  //von links unten nach rechts oben waagrecht
            counterX = 0;
            counterO = 0;
            for (int j = 0; j < 7; j++) {
                if (spielfeld[i][j] == ' ') {
                    counterX = 0;
                    counterO = 0;
                    continue;
                }
                if (spielfeld[i][j] == 'X') {
                    counterX++;
                    counterO = 0;
                }
                if (spielfeld[i][j] == 'O') {
                    counterO++;
                    counterX = 0;
                }

                if (counterX == 4) {
                    gewinner = 'X';
                    break;
                } else if (counterO == 4) {
                    gewinner = 'O';
                    break;
                }
            }
        }

        for (int j = 0; j < 7; j++) {
            counterX = 0;
            counterO = 0;
            for (int i = 6; i >= 0; i--) {  //von links unten nach rechts oben senkrecht
                if (spielfeld[i][j] == ' ') {
                    counterX = 0;
                    counterO = 0;
                    continue;
                }
                if (spielfeld[i][j] == 'X') {
                    counterX++;
                    counterO = 0;
                }
                if (spielfeld[i][j] == 'O') {
                    counterO++;
                    counterX = 0;
                }
                if (counterX == 4) {
                    gewinner = 'X';
                    break;
                } else if (counterO == 4) {
                    gewinner = 'O';
                    break;
                }
            }
        }

        for (int i = 6; i > 2; i--) {   //von links unten rechts oben diagonal
            for (int j = 0; j < 4; j++) {
                if (spielfeld[i][j] == spielfeld[i - 1][j + 1] && spielfeld[i][j] == spielfeld[i - 2][j + 2] && spielfeld[i][j] == spielfeld[i - 3][j + 3]) {
                    if (spielfeld[i][j] == ' ') {
                        continue;
                    }
                    if (spielfeld[i][j] == 'X') {
                        gewinner = 'X';
                    }
                    if (spielfeld[i][j] == 'O') {
                        gewinner = 'O';
                    }
                }
            }
        }


        for (int j = 6; j > 2; j--) {
            for (int i = 6; i > 2; i--) {
                if (spielfeld[i][j] == spielfeld[i - 1][j - 1] && spielfeld[i][j] == spielfeld[i - 2][j - 2] && spielfeld[i][j] == spielfeld[i - 3][j - 3]) {
                    if (spielfeld[i][j] == ' ') {
                        continue;
                    }
                    if (spielfeld[i][j] == 'X') {
                        gewinner = 'X';
                    }
                    if (spielfeld[i][j] == 'O') {
                        gewinner = 'O';
                    }
                }
            }
        }
        return gewinner;
    }
    static void Loading() throws InterruptedException {

        System.out.print('/');
        Thread.sleep(100);
        System.out.print('\b');
        Thread.sleep(100);
        System.out.print('-');
        Thread.sleep(100);
        System.out.print('\b');
        Thread.sleep(100);
        System.out.print('\\');
        Thread.sleep(100);
        System.out.print('\b');
        Thread.sleep(100);
    }
}

//bei gewinner Ausgabe Koordinaten von den 4 einzelnen Feldern, die zum Gewinn geführt haben (if count = 1, int yKoordinate = i; int x Koordinate = j oder so; und des dann 4 mal für die 4 Koords