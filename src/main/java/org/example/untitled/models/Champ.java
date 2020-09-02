package org.example.untitled.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Champ de mines.
 */
public class Champ {
    final static int NB_MINES = 30;
    final Field[][] _fields;

    /**
     * Champ de mines de largeur et de hauteur.
     *
     * @param largeur Largeur du champ de mines
     * @param hauteur Hauteur du champ de mines
     */
    public Champ(int largeur, int hauteur) {
        _fields = new Field[largeur][hauteur];

        for (Field[] column : _fields) {
            Arrays.setAll(column, p -> new Field.Empty());
        }
    }

    /**
     * Place des mines dans le Champ selon les paramètres constantes.
     */
    public void placeMines() {
        final var randomizer = new Random();

        var minesOnField = 0;
        while (minesOnField < NB_MINES) {
            final int x = randomizer.nextInt(_fields.length);
            final int y = randomizer.nextInt(_fields[0].length);

            if (_fields[x][y] instanceof Field.Empty) {
                _fields[x][y] = new Field.Mine();
                minesOnField++;
                incrementAdjacentField(x, y);
            }
        }
    }

    /**
     * Affiche le terrain sur la console.
     */
    public void affText() {
        System.out.println(toString());
    }

    private void incrementAdjacentField(int x, int y) {
        getAdjacentFields(x, y).forEach(field -> {
            if (field instanceof Field.Empty) {
                ((Field.Empty) field).incrementNeighborMines();
            }
        });
    }

    private Iterable<Field> getAdjacentFields(int x, int y) {
        final var result = new ArrayList<Field>();
        final int maxX = _fields.length - 1;
        final int maxY = _fields[1].length - 1;

        // Opérateur ternaire pour rester dans les bords.
        for (int dx = (x > 0 ? -1 : 0); dx <= (x < maxX ? 1 : 0); dx++) {
            for (int dy = (y > 0 ? -1 : 0); dy <= (y < maxY ? 1 : 0); dy++) {
                if (dx != 0 || dy != 0) {
                    result.add(_fields[x + dx][y + dy]);
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder();

        builder.append("Champ{_places=");
        for (Field[] column : _fields) {
            builder.append(Arrays.toString(column));
            builder.append('\n');
        }
        builder.append('}');
        return builder.toString();
    }
}