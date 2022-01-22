import javax.swing.JPanel;
import java.awt.GridLayout;

public class Scoreboard extends JPanel {

  private final StrikePanel[] strikePanels;

  public Scoreboard(Game game) {
    setLayout(new GridLayout(1, 7));

    strikePanels = new StrikePanel[Game.numbers.length];
    for (int i = 0; i < strikePanels.length; i++) {

      String key = String.valueOf(Game.numbers[i]);
      strikePanels[i] = new StrikePanel(game.getStrike(key));
      add(strikePanels[i]);
    }
  }

  public void update() {
    for (StrikePanel strikePanel : strikePanels) {
      strikePanel.update();
    }
  }
}
