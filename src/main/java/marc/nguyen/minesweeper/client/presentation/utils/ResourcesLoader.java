package marc.nguyen.minesweeper.client.presentation.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;

public class ResourcesLoader {

  private final BufferedImage tilemap;

  public final Image clear;
  public final Image flag;
  public final Image mine;
  public final Image exposed0;
  public final Image exposed1;
  public final Image exposed2;
  public final Image exposed3;
  public final Image exposed4;
  public final Image exposed5;
  public final Image exposed6;
  public final Image exposed7;
  public final Image exposed8;

  public final List<Image> exposed;

  @Inject
  public ResourcesLoader() {
    try {
      tilemap = ImageIO.read(getClass().getResource("/img/minesweeper_tiles.jpg"));
      clear = tilemap.getSubimage(0, 0, 32, 32);
      flag = tilemap.getSubimage(32, 0, 32, 32);
      mine = tilemap.getSubimage(64, 0, 32, 32);
      exposed0 = tilemap.getSubimage(96, 0, 32, 32);
      exposed1 = tilemap.getSubimage(0, 32, 32, 32);
      exposed2 = tilemap.getSubimage(32, 32, 32, 32);
      exposed3 = tilemap.getSubimage(64, 32, 32, 32);
      exposed4 = tilemap.getSubimage(96, 32, 32, 32);
      exposed5 = tilemap.getSubimage(0, 64, 32, 32);
      exposed6 = tilemap.getSubimage(32, 64, 32, 32);
      exposed7 = tilemap.getSubimage(64, 64, 32, 32);
      exposed8 = tilemap.getSubimage(96, 64, 32, 32);
      exposed =
          List.of(
              exposed0, exposed1, exposed2, exposed3, exposed4, exposed5, exposed6, exposed7,
              exposed8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
