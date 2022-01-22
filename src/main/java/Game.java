import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Game {

  private static final String GAMEINFO_FILENAME = "gameInfo.ser";
  private static HashMap<String, BigDecimal> lastGameInfo;
  private static HashMap<String, BigDecimal> savedGameInfo;
  public static final int[] numbers = {20, 19, 18, 17, 16, 15, 25};
  private static final int maxRounds = 20;
  private static final HashMap<String, Strike> strikes = new HashMap<>();
  private int gamesPlayed = 0;
  private int score = 0;
  private int round = 1;
  private int dart = 1;
  private boolean nextRound = false;
  private boolean gameEnded = false;
  private final Stack<Hit> stack = new Stack<>();
  private int averageHits = 0;

  public Game() {
    for (int number : numbers) {
      String key = String.valueOf(number);
      strikes.put(key, new Strike(key, number));
    }

    savedGameInfo = readGameInformation();
    lastGameInfo = new HashMap<>();
    saveGameInformation();
  }

  public void nextDart(final String key, final int hits) {
    if (!nextRound && strikes.containsKey(key)) {
      final Strike strike = strikes.get(key);

      if (strike.getStrikes() + hits > 3) {
        if (strike.getStrikes() <= 3) {
          score += strike.getValue() * (strike.getStrikes() + hits - 3);
        } else {
          score += strike.getValue() * hits;
        }
      }

      strike.strike(hits);
      averageHits += hits;
      stack.push(new Hit(key, hits, round));
    }

    if (dart < 3) dart++;
    else nextRound = true;

    if (hasAllStrikes()) {
      gameEnded = true;
      gamesPlayed++;
      saveGameInformation();
    }
  }

  public void nextRound() {
    nextRound = false;
    dart = 1;
    if (round + 1 > maxRounds) {
      gameEnded = true;
      gamesPlayed++;
      saveGameInformation();
    } else {
      round++;
    }
  }

  public void resetRound() {
    if (gameEnded) {
      gameEnded = false;
      gamesPlayed--;
    }
    if (nextRound) nextRound = false;
    if (round > 1 && dart == 1) round--;
    dart = 1;

    while (stack.size() > 0 && stack.peek().round == round) {
      Hit hit = stack.pop();
      final Strike strike = strikes.get(hit.key);

      if (strike.getStrikes() - hit.multiplier >= 3) {
        score -= strike.getValue() * hit.multiplier;
      } else if (strike.getStrikes() - 3 > 0) {
        score -= strike.getValue() * (strike.getStrikes() - 3);
      }

      strike.removeStrike(hit.multiplier);
      averageHits -= hit.multiplier;
      if (averageHits < 0) averageHits = 0;
    }
  }

  public void resetGame() {
    score = 0;
    averageHits = 0;
    round = 1;
    dart = 1;
    nextRound = false;
    gameEnded = false;
    stack.empty();

    for (Strike strike : strikes.values()) {
      strike.reset();
    }
  }

  private boolean hasAllStrikes() {
    for (Strike strike : strikes.values()) {
      if (strike.getStrikes() < 3) {
        return false;
      }
    }
    return true;
  }

  public String getScore() {
    return String.format("Score %d", score);
  }

  public String getRound() {
    return String.format("Round %d", round);
  }

  public String getDart() {
    return String.format("Dart %d", dart);
  }

  public String getAverageHits() {
    return String.format("Average Hits %.2f", averageHits / (float) round).replace(",", ".");
  }

  public String getGlobalAverageHits() {
    BigDecimal result;
    if (savedGameInfo != null) {
      result =
          savedGameInfo
              .get("totalAverageHits")
              .divide(savedGameInfo.get("totalRounds"), 2, RoundingMode.HALF_UP);
    } else {
      result =
          lastGameInfo
              .get("totalAverageHits")
              .divide(lastGameInfo.get("totalRounds"), 2, RoundingMode.HALF_UP);
    }
    return String.format("Total Average Hits %.2f", result).replace(",", ".");
  }

  public String getGlobalHighestScore() {
    BigDecimal result =
        savedGameInfo != null
            ? savedGameInfo.get("highestScore")
            : lastGameInfo.get("highestScore");
    return String.format("Highest Score %d", result.toBigInteger());
  }

  public String getGlobalHighestAverageHits() {
    BigDecimal result =
        savedGameInfo != null
            ? savedGameInfo.get("highestAverageHits")
            : lastGameInfo.get("highestAverageHits");
    return String.format("Highest Average Hits %.2f", result).replace(",", ".");
  }

  public String getGlobalLowestRounds() {
    BigDecimal result =
        savedGameInfo != null
            ? savedGameInfo.get("lowestRounds")
            : lastGameInfo.get("lowestRounds");
    return String.format("Lowest Rounds %d", result.toBigInteger());
  }

  public boolean isNextRound() {
    return nextRound;
  }

  public boolean isGameEnded() {
    return gameEnded;
  }

  public Strike getStrike(final String key) {
    return strikes.get(key);
  }

  public static String getButtonText(final int number, final int multiplier) {
    if (number != 25) {
      return multiplier == 2 ? "D" + number : multiplier == 3 ? "T" + number : "" + number;
    }
    return multiplier == 2 ? "50" : "" + number;
  }

  public static int getStrikeNumber(final String text) {
    if (text.equals("50")) return 25;
    if (text.length() == 2) return Integer.parseInt(text);
    return Integer.parseInt(text.substring(1, 3));
  }

  public static int getMultiplier(final String text) {
    if (text.equals("50")) return 2;
    if (text.length() == 2) return 1;
    return text.contains("D") ? 2 : 3;
  }

  public void saveGameInformation() {
    lastGameInfo.put("totalGames", BigDecimal.valueOf(gamesPlayed));
    lastGameInfo.put("totalRounds", BigDecimal.valueOf(round));
    lastGameInfo.put("totalScore", BigDecimal.valueOf(score));
    lastGameInfo.put("totalAverageHits", BigDecimal.valueOf(averageHits));
    lastGameInfo.put("highestScore", BigDecimal.valueOf(score));
    lastGameInfo.put("highestAverageHits", BigDecimal.valueOf(averageHits / (float) round));
    lastGameInfo.put("lowestRounds", BigDecimal.valueOf(round));
  }

  public void writeGameInformation() {
    savedGameInfo = readGameInformation();
    HashMap<String, BigDecimal> newGameInformation;
    if (savedGameInfo != null) {
      newGameInformation = new HashMap<>();
      for (Map.Entry<String, BigDecimal> entry : savedGameInfo.entrySet()) {
        String key = entry.getKey();
        BigDecimal value;

        if (key.startsWith("total")) {
          value = entry.getValue().add(lastGameInfo.get(key));
        } else if (key.startsWith("highest")) {
          value = entry.getValue().max(lastGameInfo.get(key));
        } else {
          value = entry.getValue().min(lastGameInfo.get(key));
        }

        newGameInformation.put(entry.getKey(), value);
      }
    } else {
      newGameInformation = lastGameInfo;
    }

    try {
      FileOutputStream fileOutputStream = new FileOutputStream(GAMEINFO_FILENAME);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

      objectOutputStream.writeObject(newGameInformation);

      fileOutputStream.close();
      objectOutputStream.close();
      savedGameInfo = newGameInformation;

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public HashMap<String, BigDecimal> readGameInformation() {
    HashMap<String, BigDecimal> result = null;
    try {
      FileInputStream fileInputStream = new FileInputStream(GAMEINFO_FILENAME);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

      result = (HashMap) objectInputStream.readObject();

      fileInputStream.close();
      objectInputStream.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

    return result;
  }

  @Data
  @AllArgsConstructor
  private static class Hit {
    public String key;
    public int multiplier;
    public int round;
  }
}
