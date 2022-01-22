import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {

  private static Game game;
  private static Scoreboard scoreboard;
  private static JLabel score, round, dart, average;
  private static JLabel highestAverage, highestScore, averageHits, lowestRounds;
  private static JButton next, saveGame;
  private static InputPanel inputPanel;

  public static void main(String... args) {

    FontUtils.loadFonts();
    ImageUtils.loadImages();

    FlatDarkLaf.setup();

    game = new Game();

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setIconImage(ImageUtils.get("icon"));
    frame.setTitle("Cricket");
    // frame.setResizable(false);
    frame.setMinimumSize(new Dimension(1600, 900));
    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    frame.setLayout(new GridLayout(3, 1));

    scoreboard = new Scoreboard(game);
    frame.add(scoreboard);

    Font xanhMono = FontUtils.xanhMonoRegular.deriveFont(64f);
    Font inconsolata = FontUtils.inconsolataRegular.deriveFont(56f);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(xanhMono);

    JPanel infoPanel = new JPanel(new GridLayout(1, 2));
    infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel leftInfo = new JPanel(new GridLayout(4, 1));

    highestScore = new JLabel(game.getGlobalHighestScore());
    highestScore.setFont(inconsolata);
    leftInfo.add(highestScore);

    highestAverage = new JLabel(game.getGlobalHighestAverageHits());
    highestAverage.setFont(inconsolata);
    leftInfo.add(highestAverage);

    lowestRounds = new JLabel(game.getGlobalLowestRounds());
    lowestRounds.setFont(inconsolata);
    leftInfo.add(lowestRounds);

    averageHits = new JLabel(game.getGlobalAverageHits());
    averageHits.setFont(inconsolata);
    leftInfo.add(averageHits);

    infoPanel.add(leftInfo);

    JPanel rightInfo = new JPanel(new GridLayout(3, 1));

    JPanel roundDart = new JPanel(new GridLayout(1, 2));

    round = new JLabel(game.getRound(), SwingConstants.CENTER);
    round.setFont(inconsolata.deriveFont(96f));
    roundDart.add(round);

    dart = new JLabel(game.getDart(), SwingConstants.CENTER);
    dart.setFont(inconsolata.deriveFont(96f));
    roundDart.add(dart);

    rightInfo.add(roundDart);

    score = new JLabel(game.getScore(), SwingConstants.CENTER);
    score.setFont(inconsolata.deriveFont(96f));
    rightInfo.add(score);

    average = new JLabel(game.getAverageHits(), SwingConstants.CENTER);
    average.setFont(inconsolata.deriveFont(48f));
    rightInfo.add(average);

    infoPanel.add(rightInfo);

    frame.add(infoPanel);

    JPanel inputSection = new JPanel(new GridLayout(1, 2));

    inputPanel = new InputPanel();
    inputPanel.setActionListener(
        (key, value) -> {
          game.nextDart(key, value);
          updateUI();
        });

    inputSection.add(inputPanel);

    JPanel controlSection = new JPanel(new GridLayout(1, 2));

    JPanel leftControls = new JPanel(new GridLayout(2, 2, 10, 10));
    leftControls.setBorder(new EmptyBorder(10, 10, 10, 10));

    JButton resetGame = new JButton(getMultilineText("Reset\nGame", inconsolata));
    resetGame.addActionListener(
        e -> {
          game.resetGame();
          updateUI();
        });
    leftControls.add(resetGame);

    JButton resetRound = new JButton(getMultilineText("Reset\nRound", inconsolata));
    resetRound.addActionListener(
        e -> {
          game.resetRound();
          updateUI();
        });
    leftControls.add(resetRound);

    saveGame = new JButton(getMultilineText("Save\nGame", inconsolata));
    saveGame.setEnabled(game.isGameEnded());
    saveGame.addActionListener(
        e -> {
          game.writeGameInformation();
          game.resetGame();
          updateUI();
          updateGameInformation();
        });
    leftControls.add(saveGame);

    leftControls.add(new JPanel());

    controlSection.add(leftControls);

    JPanel rightControls = new JPanel(new GridLayout(1, 1));
    rightControls.setBorder(new EmptyBorder(10, 10, 10, 10));

    next = new JButton(getMultilineText("Next\nRound", inconsolata.deriveFont(96f)));
    next.addActionListener(
        e -> {
          game.nextRound();
          updateUI();
        });
    rightControls.add(next);

    controlSection.add(rightControls);

    inputSection.add(controlSection);
    frame.add(inputSection);

    frame.setVisible(true);
  }

  private static void updateUI() {
    scoreboard.update();
    score.setText(game.getScore());
    round.setText(game.getRound());
    average.setText(game.getAverageHits());
    dart.setText(game.getDart());
    next.setBackground(game.isNextRound() ? Color.RED : Color.DARK_GRAY);
    next.setEnabled(!game.isGameEnded());
    saveGame.setBackground(game.isGameEnded() ? Color.GREEN : Color.DARK_GRAY);
    saveGame.setEnabled(game.isGameEnded());
    inputPanel.setEnabled(!game.isGameEnded());
  }

  private static void updateGameInformation() {
    highestScore.setText(game.getGlobalHighestScore());
    highestAverage.setText(game.getGlobalHighestAverageHits());
    lowestRounds.setText(game.getGlobalLowestRounds());
    averageHits.setText(game.getGlobalAverageHits());
  }

  private static String getMultilineText(final String text, final Font font) {
    return String.format(
        "<html><div style='text-align: center; font-size: %d'><font face='%s'>%s</font></div></html>",
        font.getSize(), font.getFamily(), text.replaceAll("\\n", "<br>"));
  }
}
