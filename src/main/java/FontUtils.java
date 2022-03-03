import javax.swing.UIManager;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontUtils {

  public static Font inconsolataRegular;
  public static Font inconsolataLight;
  public static Font inconsolataBold;
  public static Font xanhMonoRegular;
  public static Font sevenSegment;

  public static void loadFonts() {
    inconsolataRegular = createFont("Inconsolata-Regular.ttf");
    inconsolataLight = createFont("Inconsolata-ExtraLight.ttf");
    inconsolataBold = createFont("Inconsolata-Bold.ttf");
    xanhMonoRegular = createFont("XanhMono-Regular.ttf");
    sevenSegment = createFont("Seven-Segment.ttf");
  }

  private static Font createFont(final String name) {
    try (InputStream in = Main.class.getResourceAsStream("fonts/".concat(name))) {
      return Font.createFont(Font.TRUETYPE_FONT, in);
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void setDefaultFont(final Font font) {
    var keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      var key = keys.nextElement();
      var value = UIManager.get(key);
      if (value instanceof Font) {
        UIManager.put(key, font);
      }
    }
  }
}
