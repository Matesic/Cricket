import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ImageUtils {

  private static final HashMap<String, BufferedImage> images = new HashMap<>();

  public static BufferedImage get(final String imageName) {
    return images.get(imageName);
  }

  public static void loadImages() {
    final File dir = new File(Main.class.getResource("images/").getPath());
    final File[] files = dir.listFiles();

    assert files != null;
    for (File file : files) {
      final String name = file.getName();
      final String nameNoExt = name.substring(0, name.indexOf("."));
      loadImage(nameNoExt);
    }
  }

  private static void loadImage(final String imageName) {
    BufferedImage image = null;
    try {
      final URL url = Main.class.getResource("images/".concat(imageName).concat(".png"));
      image = ImageIO.read(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (image != null) {
      images.put(imageName, image);
    }
  }
}
