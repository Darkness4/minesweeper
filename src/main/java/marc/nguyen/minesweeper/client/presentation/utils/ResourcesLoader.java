package marc.nguyen.minesweeper.client.presentation.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;

/**
 * Load in RAM the resources.
 *
 * <p>Since these tiles will be used non-stop, we will store them here during runtime.
 */
public class ResourcesLoader {

  private static int SIZE = 16;

  private final BufferedImage tilemap;

  public final Image clear;
  public final Image flag;
  public final Image hitMine;
  public final Image exposedMine;
  public final Image exposed0;
  public final Image exposed1;
  public final Image exposed2;
  public final Image exposed3;
  public final Image exposed4;
  public final Image exposed5;
  public final Image exposed6;
  public final Image exposed7;
  public final Image exposed8;
  public final Image softwareLogo;

  public final List<Image> exposed;

  @Inject
  public ResourcesLoader() {
    try {
      tilemap = ImageIO.read(getClass().getResource("/img/tiles.png"));
      clear = tilemap.getSubimage(0, 0, SIZE, SIZE);
      flag = tilemap.getSubimage(SIZE, 0, SIZE, SIZE);
      hitMine = tilemap.getSubimage(SIZE * 2, 0, SIZE, SIZE);
      exposed0 = tilemap.getSubimage(SIZE * 3, 0, SIZE, SIZE);
      exposed1 = tilemap.getSubimage(0, SIZE, SIZE, SIZE);
      exposed2 = tilemap.getSubimage(SIZE, SIZE, SIZE, SIZE);
      exposed3 = tilemap.getSubimage(SIZE * 2, SIZE, SIZE, SIZE);
      exposed4 = tilemap.getSubimage(SIZE * 3, SIZE, SIZE, SIZE);
      exposed5 = tilemap.getSubimage(0, SIZE * 2, SIZE, SIZE);
      exposed6 = tilemap.getSubimage(SIZE, SIZE * 2, SIZE, SIZE);
      exposed7 = tilemap.getSubimage(SIZE * 2, SIZE * 2, SIZE, SIZE);
      exposed8 = tilemap.getSubimage(SIZE * 3, SIZE * 2, SIZE, SIZE);
      exposedMine = tilemap.getSubimage(0, SIZE * 3, SIZE, SIZE);
      exposed =
          List.of(
              exposed0, exposed1, exposed2, exposed3, exposed4, exposed5, exposed6, exposed7,
              exposed8);
      softwareLogo = ImageIO.read(getClass().getResource("/img/icon.ico"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
