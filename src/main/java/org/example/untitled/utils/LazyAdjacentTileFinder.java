package org.example.untitled.utils;

import org.example.untitled.models.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * LazyAdjacentTileFinder looks for the adjacent tiles lazily for optimization purpose.
 */
public class LazyAdjacentTileFinder {
    private final Tile[][] _tiles;

    public LazyAdjacentTileFinder(Tile[][] tiles) {
        _tiles = tiles;
    }

    /**
     * Look for adjacent tiles based on coordinates.
     *
     * @param x X coordinate (tiles[x][y])
     * @param y Y coordinate (tiles[x][y])
     * @return A stream of Tile.
     */
    public Stream<Tile> execute(int x, int y) {
        final List<Coordinates> coordinatesList = new ArrayList<>();
        final int maxX = _tiles.length - 1;
        final int maxY = _tiles[1].length - 1;

        // Look for correct coordinates sequentially.
        for (int dx = (x > 0 ? -1 : 0); dx <= (x < maxX ? 1 : 0); dx++) {
            for (int dy = (y > 0 ? -1 : 0); dy <= (y < maxY ? 1 : 0); dy++) {
                if (dx != 0 || dy != 0) {
                    coordinatesList.add(new Coordinates(x + dx, y + dy));
                }
            }
        }

        // Returns lazily the tiles.
        return coordinatesList.stream().map(coordinates -> _tiles[coordinates.x][coordinates.y]);
    }

    static class Coordinates {
        final int x;
        final int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
