package com.bscl.processor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class InstructionMessageComparatorTest {

    private static final int LOW_PRIORITY = 95;

    private static final int HIGH_PRIORITY = 5;

    private static final int VALID_VALUE = 10;

    private InstructionMessageComparator comparator;

    private InstructionMessage highPriorityMessage;

    private InstructionMessage lowPriorityMessage;

    @Before
    public void setup() {
        comparator = new InstructionMessageComparator();
        highPriorityMessage = messageWith(HIGH_PRIORITY);
        lowPriorityMessage = messageWith(LOW_PRIORITY);
    }

    @Test
    public void returnsPositiveIntegerForGreaterThan() {
        assertThat(compare(lowPriorityMessage, highPriorityMessage), is(1));
    }

    @Test
    public void returnsZeroForFirstPriorityEqualToSecondPriority() {
        assertThat(compare(highPriorityMessage, highPriorityMessage), is(0));
    }

    @Test
    public void returnsNegativeIntegerForLessThan() {
        assertThat(compare(highPriorityMessage, lowPriorityMessage), is(-1));
    }

    private int compare(InstructionMessage first, InstructionMessage second) {
        return comparator.compare(first, second);
    }

    private InstructionMessage messageWith(int type) {
        return InstructionMessageBuilder.instructionMessage()
                .withInstructionType(type)
                .withProductCode(VALID_VALUE).withQuantity(VALID_VALUE)
                .withQuantity(VALID_VALUE)
                .withTimestamp(VALID_VALUE)
                .withUom(VALID_VALUE).build();
    }

}
