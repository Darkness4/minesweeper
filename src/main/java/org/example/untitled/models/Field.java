package org.example.untitled.models;

import java.util.Objects;

public abstract class Field {
    enum State {
        BLANK,
        FLAG,
        EXPOSED,
        HITMINE,
        WRONGMINE
    }

    State _state = State.BLANK;

    public State getState() {
        return _state;
    }

    public void setState(State state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }

        this._state = state;
    }

    public final static class Mine extends Field {
        @Override
        public String toString() {
            return "X";
        }
    }

    public final static class Empty extends Field {
        private int _neighborMines = 0;

        public int getNeighborMinesCount() {
            return _neighborMines;
        }

        public void incrementNeighborMines() {
            this._neighborMines++;
        }

        public void reset() {
            this._neighborMines = 0;
        }

        @Override
        public String toString() {
            return Integer.toString(this._neighborMines);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Empty empty = (Empty) o;
            return _neighborMines == empty._neighborMines;
        }

        @Override
        public int hashCode() {
            return Objects.hash(_neighborMines);
        }
    }
}