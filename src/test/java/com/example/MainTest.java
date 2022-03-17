import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MainTest {
     @Test
     public void evaluatesExpression() {
        int sum = 1+2+3+6;
        assertEquals(6, sum);
     }
}