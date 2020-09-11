package marc.nguyen.minesweeper.common.data.models;

public enum Level {
  EASY(10, 10, 10),
  MEDIUM(20, 20, 40),
  HARD(30, 30, 90);

  final int length;
  final int height;
  final int mines;

  Level(int length, int height, int mines) {
    this.length = length;
    this.height = height;
    this.mines = mines;
  }

  @Override
  public String toString() {
    return "Level{" + "length=" + length + ", height=" + height + ", mines=" + mines + '}';
  }
}
