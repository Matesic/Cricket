import javax.swing.JPanel;
import java.awt.*;

public class StrikePanel extends JPanel {

  private final Strike strike;

  public StrikePanel(Strike strike) {
    this.strike = strike;
  }

  public void update() {
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics2D = (Graphics2D) g;

    Dimension dimension = getSize();
    final int radius = 40;

    graphics2D.setColor(Color.BLACK);
    graphics2D.fillRoundRect(0, 0, dimension.width, dimension.height, radius, radius);

    graphics2D.setStroke(new BasicStroke(4f));
    graphics2D.setColor(Color.YELLOW);
    graphics2D.drawRoundRect(0, 0, dimension.width, dimension.height, radius, radius);

    int offset = (int) (dimension.width * 0.12f);
    graphics2D.setColor(Color.GRAY);
    graphics2D.drawLine(
        offset, dimension.height / 2, dimension.width - offset, dimension.height / 2);

    graphics2D.setColor(Color.WHITE);
    graphics2D.setFont(FontUtils.xanhMonoRegular.deriveFont(dimension.width * 0.5f));
    graphics2D.drawString(
        strike.getName(), dimension.width / 2 - offset * 2, dimension.height / 2 - offset);

    int size = dimension.width / 4;
    int y = (int) (dimension.height * 0.75f) - size / 2;
    for (int i = 0; i < 3; i++) {
      int x = (int) (dimension.width * (0.2f + i * 0.3f)) - size / 2;
      graphics2D.setColor(strike.getStrikes() > i ? Color.RED : Color.GRAY);
      graphics2D.fillOval(x, y, size, size);
    }
  }
}
