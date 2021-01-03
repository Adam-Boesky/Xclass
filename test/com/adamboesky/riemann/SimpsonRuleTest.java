package com.adamboesky.riemann;

import com.adamboesky.riemann.SimpsonRule;
import org.dalton.polyfun.Polynomial;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpsonRuleTest {

    @Test
    public void slice(){
        Polynomial poly = new Polynomial(1,2);
        assertThat(new SimpsonRule().slice(poly, 0.0, 1.0), is(0.3333333333333333));
    }
}
