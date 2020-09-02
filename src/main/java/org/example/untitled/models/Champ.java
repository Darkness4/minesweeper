package org.example.untitled.models;

import org.example.untitled.utils.LazyAdjacentTileFinder;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 * A Minefield.
 */
public class Champ {
    final static int NB_MINES = 30;
    final Tile[][] _tiles;

    /**
     * A minefield based on length and height.
     *
     * @param length Length of the minefield.
     * @param height Height of the minefield.
     */
    public Champ(int length, int height) {
        _tiles = new Tile[length][height];

        for (Tile[] column : _tiles) {
            Arrays.setAll(column, p -> new Tile.Empty());
        }
    }

    /**
     * Place the mines on the minefield.
     */
    public void placeMines() {
        final var randomizer = new Random();

        var minesOnField = 0;
        while (minesOnField < NB_MINES) {
            final int x = randomizer.nextInt(_tiles.length);
            final int y = randomizer.nextInt(_tiles[0].length);

            if (_tiles[x][y] instanceof Tile.Empty) {
                _tiles[x][y] = new Tile.Mine();
                minesOnField++;
                incrementAdjacentCounters(x, y);
            }
        }
    }

    /**
     * Show the minefield in stdout.
     */
    public void affText() {
        System.out.println(toString());
    }

    private void incrementAdjacentCounters(int x, int y) {
        getAdjacentTiles(x, y).forEach(tile -> {
            System.out.println("Shit");
            if (tile instanceof Tile.Empty) {
                ((Tile.Empty) tile).incrementAdjacentMines();
            }
        });
    }

    private Stream<Tile> getAdjacentTiles(int x, int y) {
        final var finder = new LazyAdjacentTileFinder(_tiles);

        return finder.execute(x, y);
    }

    @Override
    public String toString() {
        final var builder = new StringBuilder();

        builder.append("Champ{_places=\n");
        for (Tile[] column : _tiles) {
            builder.append(Arrays.toString(column));
            builder.append('\n');
        }
        builder.append('}');
        return builder.toString();
    }
}