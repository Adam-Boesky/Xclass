import com.adamboesky.riemann.TrapezoidRule;
import org.dalton.polyfun.Polynomial;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrapezoidRuleTest {

    @Test
    public void slice(){
        Polynomial poly = new Polynomial(1,2);
        assertThat(new TrapezoidRule().slice(poly, 0.0, 1.0), is(.5));
    }
}
