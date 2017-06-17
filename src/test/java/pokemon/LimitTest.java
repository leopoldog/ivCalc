package pokemon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.ghielmetti.pokemon.Limit;

public class LimitTest {
  @Test
  public void matches_goodAndBadInput_returnsIfTheInputMatches() {
    for (int s = 1; s <= 7; s++) {
      for (char i = '1'; i <= '4'; i++) {
        int minSum = 0;
        int maxSum = 0;
        switch (i) {
          case '4':
            maxSum = 45;
            minSum = 37;
            break;
          case '3':
            maxSum = 36;
            minSum = 30;
            break;
          case '2':
            maxSum = 29;
            minSum = 23;
            break;
          case '1':
            maxSum = 22;
            minSum = 0;
            break;
        }
        for (char l = 'a'; l <= 'd'; l++) {
          int val = 0;
          switch (l) {
            case 'a':
              val = 15;
              break;
            case 'b':
              val = 14;
              break;
            case 'c':
              val = 12;
              break;
            case 'd':
              val = 7;
              break;
          }
          String description = "" + i + "" + l + "-" + ((s & 4) == 0 ? "" : "h") + ((s & 2) == 0 ? "" : "a") + ((s & 1) == 0 ? "" : "d");
          Limit limit = new Limit(description);
          int ivd = ((s & 1) == 0 ? 0 : val);
          int iva = ((s & 2) == 0 ? 0 : val);
          int ivs = ((s & 4) == 0 ? 0 : val);
          double divisor = (((s & 4) == 0 ? 1 : 0) + ((s & 2) == 0 ? 1 : 0) + ((s & 1) == 0 ? 1 : 0));
          int missing = divisor == 0 ? 0 : (int) (((minSum - iva - ivd - ivs) / divisor) + 0.5);
          missing = missing < 0 ? 0 : missing;
          ivd = ((s & 1) == 0 ? missing : val);
          iva = ((s & 2) == 0 ? missing : val);
          ivs = ((s & 4) == 0 ? missing : val);
          if ((missing >= 0) && (missing < val) && ((iva + ivd + ivs) >= minSum) && ((iva + ivd + ivs) <= maxSum)) {
            assertTrue(description + " : " + iva + ", " + ivd + ", " + ivs + " must match " + limit, limit.matches(iva, ivd, ivs));
          } else {
            assertFalse(description + " : " + iva + ", " + ivd + ", " + ivs + " mustn't match " + limit, limit.matches(iva, ivd, ivs));
          }
        }
      }
    }
  }

  @Test
  public void toString_always_returnsTheCorrectString() {
    for (int s = 1; s <= 7; s++) {
      for (char i = '1'; i <= '4'; i++) {
        for (char l = 'a'; l <= 'd'; l++) {
          String description = "" + i + "" + l + "-" + ((s & 4) == 0 ? "" : "h") + ((s & 2) == 0 ? "" : "a") + ((s & 1) == 0 ? "" : "d");
          Limit limit = new Limit(description);
          assertEquals(description, limit.toString());
        }

        String description = "" + i + "x-" + ((s & 4) == 0 ? "" : "h") + ((s & 2) == 0 ? "" : "a") + ((s & 1) == 0 ? "" : "d");
        Limit limit = new Limit(description);
        assertEquals(description, limit.toString());
      }

      for (char l = 'a'; l <= 'd'; l++) {
        String description = "x" + l + "-" + ((s & 4) == 0 ? "" : "h") + ((s & 2) == 0 ? "" : "a") + ((s & 1) == 0 ? "" : "d");
        Limit limit = new Limit(description);
        assertEquals(description, limit.toString());
      }
    }
  }
}
