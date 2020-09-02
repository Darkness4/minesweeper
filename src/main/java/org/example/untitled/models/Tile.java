package org.example.untitled.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A tile that can be either Empty (which shows the number of adjacent mines) or Mine.
 */
public abstract class Tile {
    private State _state = State.BLANK;

    /**
     * Get the state of the tile.
     *
     * @return State of the tile
     */
    public State getState() {
        return _state;
    }

    public void setState(State state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }

        this._state = state;
    }

    /**
     * State of a tile based on the Minesweeper
     */
    public enum State {
        BLANK,
        FLAG,
        EXPOSED,
        HIT_MINE,
        WRONG_MINE
    }

    /**
     * A tile filled with a mine.
     */
    public final static class Mine extends Tile {
        @Override
        public String toString() {
            return "X";
        }
    }

    /**
     * An empty tile.
     */
    public final static class Empty extends Tile {
        private final AtomicInteger _adjacentMines = new AtomicInteger(0);

        /**
         * Get the number of adjacent mines of the empty tile.
         *
         * @return The adjacent mines.
         */
        public int getNeighborMinesCount() {
            return _adjacentMines.get();
        }

        /**
         * Increment the number of adjacent mines.
         */
        public void incrementAdjacentMines() {
            this._adjacentMines.incrementAndGet();
        }

        /**
         * Reset the number of adjacent mines to 0.
         */
        public void reset() {
            this._adjacentMines.set(0);
        }

        @Override
        public String toString() {
            return this._adjacentMines.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Empty empty = (Empty) o;
            return _adjacentMines.equals(empty._adjacentMines);
        }

        @Override
        public int hashCode() {
            return Objects.hash(_adjacentMines);
        }
    }
}