package com.adamboesky.riemann;

import com.adamboesky.riemann.LeftHandRule;
import org.dalton.polyfun.Polynomial;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LeftHandRuleTest {

    @Test
    public void slice(){
        Polynomial poly = new Polynomial(1,2);
        assertThat(new LeftHandRule().slice(poly, 0.0, 1.0), is(0.0));
    }
}
