package com.bscl.processor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class InstructionMessageTest {

    @Test
    @Parameters(source = InstructionMessageProvider.class)
    public void validatesMessage(InstructionMessage message, boolean valid) {
        assertThat(message.isValid(), is(valid));
    }

}
