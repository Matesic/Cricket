import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Strike {
  private final String name;
  private final int value;
  private int strikes = 0;

  public void strike(int count) {
    strikes += count;
  }

  public void removeStrike(int count) {
    strikes -= count;
  }

  public void reset() {
    strikes = 0;
  }
}
