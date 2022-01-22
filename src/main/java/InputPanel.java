import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;

public class InputPanel extends JPanel {

  public final ArrayList<JButton> buttons = new ArrayList<>();

  public InputPanel() {
    setLayout(new GridLayout(3, 7, 10, 10));
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 7; j++) {
        String text = Game.getButtonText(Game.numbers[j], i + 1);
        if (i == 1 && j == 6) text = "50";
        if (i == 2 && j == 6) {
          JButton button = new JButton("0");
          button.setName("0");
          button.setFont(FontUtils.xanhMonoRegular.deriveFont(64f));
          buttons.add(button);
          add(button);
          break;
        }
        JButton button = new JButton(text);
        button.setName(Integer.toString(Game.numbers[j]));
        button.setFont(FontUtils.xanhMonoRegular.deriveFont(64f));
        buttons.add(button);
        add(button);
      }
    }
  }

  public void setEnabled(boolean b) {
    for (JButton button : buttons) {
      button.setEnabled(b);
    }
  }

  public void setActionListener(CustomListener listener) {
    for (JButton button : buttons) {
      String key = button.getName();
      String text = button.getText();
      button.addActionListener(e -> listener.onClick(key, Game.getMultiplier(text)));
    }
  }

  public interface CustomListener {
    void onClick(String key, int value);
  }
}
