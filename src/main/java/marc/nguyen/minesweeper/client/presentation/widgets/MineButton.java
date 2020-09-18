package marc.nguyen.minesweeper.client.presentation.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.common.data.models.Tile;
import marc.nguyen.minesweeper.common.data.models.Tile.Empty;

public class MineButton extends JPanel {

  private static final Map<Integer, Color> colorMap =
      Map.ofEntries(
          Map.entry(1, new Color(0x0100FE)),
          Map.entry(2, new Color(0x017F01)),
          Map.entry(3, new Color(0xFE0000)),
          Map.entry(4, new Color(0x010080)),
          Map.entry(5, new Color(0x7F0102)),
          Map.entry(6, new Color(0x008081)),
          Map.entry(7, new Color(0x000000)),
          Map.entry(8, new Color(0x808080)));
  private static final int SIZE = 20;
  public final int x;
  public final int y;
  Image image = null;
  private final ResourcesLoader resourcesLoader;

  public MineButton(int x, int y, ResourcesLoader resourcesLoader) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    this.resourcesLoader = resourcesLoader;
    this.x = x;
    this.y = y;

    //    setText(text);
    setName("Button_" + x + '_' + y);
    setPreferredSize(new Dimension(SIZE, SIZE));
    final var font = getFont();
    setFont(font.deriveFont(font.getStyle() | Font.BOLD));
    setFocusable(false);
    setBackground(Color.WHITE);
    image = resourcesLoader.clear;
    //    setMargin(new Insets(0, 0, 0, 0));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Math : Center text
    FontMetrics metrics = g.getFontMetrics(getFont());
    g.drawImage(image, 0, 0, SIZE, SIZE, this);
  }

  public void updateValueFromTile(Tile tile) {
    SwingUtilities.invokeLater(
        () -> {
          switch (tile.getState()) {
            case HIT_MINE:
              image = resourcesLoader.mine;
              setForeground(Color.BLACK);
              setBackground(Color.RED);
              setEnabled(false);
              break;
            case FLAG:
              image = resourcesLoader.flag;
              setForeground(Color.MAGENTA);
              setBackground(Color.CYAN);
              break;
            case EXPOSED:
              setBackground(Color.GRAY);
              if (tile instanceof Tile.Empty) {
                final int neighbors = ((Empty) tile).getNeighborMinesCount();
                image = resourcesLoader.exposed.get(neighbors);
                setForeground(colorMap.get(neighbors));
                setEnabled(false);
              }
              break;
            case BLANK:
              image = resourcesLoader.clear;
              setBackground(Color.WHITE);
            default:
              break;
          }
          repaint();
        });
  }

  public static class Factory {
    final ResourcesLoader resourcesLoader;

    public Factory(ResourcesLoader resourcesLoader) {
      this.resourcesLoader = resourcesLoader;
    }

    public MineButton create(int x, int y) {
      return new MineButton(x, y, resourcesLoader);
    }
  }
}
