package marc.nguyen.minesweeper.client.presentation.widgets;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.common.data.models.Position;
import marc.nguyen.minesweeper.common.data.models.Tile;
import marc.nguyen.minesweeper.common.data.models.Tile.Empty;

public class MineButton extends JPanel {

  private static final int SIZE = 32;
  public final Position position;
  Image image;
  private final ResourcesLoader resourcesLoader;

  public MineButton(Position position, ResourcesLoader resourcesLoader) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    this.resourcesLoader = resourcesLoader;
    this.position = position;

    setName("Button_" + position);
    setPreferredSize(new Dimension(SIZE, SIZE));
    final var font = getFont();
    setFont(font.deriveFont(font.getStyle() | Font.BOLD));
    setFocusable(false);
    image = resourcesLoader.clear;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.drawImage(image, 0, 0, SIZE, SIZE, this);
  }

  public void updateValueFromTile(Tile tile) {
    SwingUtilities.invokeLater(
        () -> {
          switch (tile.getState()) {
            case HIT_MINE:
              image = resourcesLoader.hitMine;
              setEnabled(false);
              break;
            case FLAG:
              image = resourcesLoader.flag;
              repaint();
              break;
            case EXPOSED:
              if (tile instanceof Tile.Empty) {
                final int neighbors = ((Empty) tile).getNeighborMinesCount();
                image = resourcesLoader.exposed.get(neighbors);
                setEnabled(false);
              }
              break;
            case BLANK:
              image = resourcesLoader.clear;
              repaint();
              break;
            case EXPOSED_MINE:
              image = resourcesLoader.exposedMine;
              repaint();
              break;
            default:
              break;
          }
        });
  }

  public static class Factory {

    final ResourcesLoader resourcesLoader;

    public Factory(ResourcesLoader resourcesLoader) {
      this.resourcesLoader = resourcesLoader;
    }

    public MineButton create(Position position) {
      return new MineButton(position, resourcesLoader);
    }

    public MineButton create(int x, int y) {
      return create(new Position(x, y));
    }
  }
}
