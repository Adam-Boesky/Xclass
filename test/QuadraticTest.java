import com.adamboesky.rustRemoval.Quadratic;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class QuadraticTest {

    @Test
    public void getA(){
        Quadratic q = new Quadratic(1,2,3);
        assertThat(q.getA(), is(1.0));
    }

    @Test
    public void getB(){
        Quadratic q = new Quadratic(1,2,3);
        assertThat(q.getB(), is(2.0));
    }

    @Test
    public void getC(){
        Quadratic q = new Quadratic(1,2,3);
        assertThat(q.getC(), is(3.0));
    }

    @Test
    public void hasRealRoots(){
        Quadratic q = new Quadratic(1, 5, 1);
        assertThat(q.hasRealRoots(), is(true));
    }

    @Test
    public void numberOfRoots(){
        Quadratic q = new Quadratic(1,0,1);
        assertThat(q.numberOfRoots(), is(0));
    }

    @Test
    public void getRootArray(){
        Quadratic q = new Quadratic(1,2,1);
        double[] checker = {-1.0 , -1.0};
        assertThat(q.getRootArray()[0], is(checker[0]));
        assertThat(q.getRootArray()[1], is(checker[1]));
    }

    @Test
    public void getAxisOfSymmetry(){
        Quadratic q = new Quadratic(1,2,3);
        assertThat(q.getAxisOfSymmetry(), is(-1.0));
    }

    @Test
    public void getExtremeValue(){
        Quadratic q = new Quadratic(1, 2, 1);
        assertThat(q.getExtremeValue(), is(0.0));
    }
    @Test
    public void isMax(){
        Quadratic q = new Quadratic(-1, 2, 3);
        assertThat(q.isMax(), is(false));
    }

    @Test
    public void evaluateWith(){
        Quadratic q = new Quadratic(1, 2, 3);
        assertThat(q.evaluateWith(1), is(6.0));
    }
}
