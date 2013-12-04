package com.bscl.processor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InstructionQueueTest {

    private static final int LOW_PRIORITY = 95;

    private static final int HIGH_PRIORITY = 5;

    private static final int MEDIUIM_PRIORITY = 50;

    private static final int VALID_VALUE = 10;

    @Mock
    private Queue<InstructionMessage> mockQueue;

    private InstructionQueue instructionQueue;

    private InstructionMessage message;

    @Before
    public void setup() {
        buildInstructionQueue();
        buildMessage();
    }

    @Test
    public void addsValidMessage() {
        when(mockQueue.add(message)).thenReturn(true);

        assertThat(instructionQueue.add(message), is(true));

        verify(mockQueue).add(message);
    }

    @Test(expected = InvalidMessageException.class)
    public void throwsExceptionWhenInvalidMessageAdded() {
        buildInvalidMessage();

        instructionQueue.add(message);
    }

    @Test
    public void removesMessage() {
        when(mockQueue.remove()).thenReturn(message);

        assertThat(instructionQueue.remove(), is(equalTo(message)));

        verify(mockQueue).remove();
    }

    @Test
    public void retrievesMessage() {
        when(mockQueue.peek()).thenReturn(message);

        assertThat(instructionQueue.peek(), is(equalTo(message)));

        verify(mockQueue).peek();
    }

    @Test
    public void returnsTrueIfEmpty() {
        when(mockQueue.isEmpty()).thenReturn(true);

        assertThat(instructionQueue.isEmpty(), is(true));

        verify(mockQueue).isEmpty();
    }

    @Test
    public void createsPriorityQueueImplementation() {
        instructionQueue = InstructionQueue.newPriorityQueue();
        instructionQueue.add(messageWith(MEDIUIM_PRIORITY));
        instructionQueue.add(messageWith(HIGH_PRIORITY));
        instructionQueue.add(messageWith(HIGH_PRIORITY));
        instructionQueue.add(messageWith(LOW_PRIORITY));

        assertThat(instructionQueue.remove().getInstructionType(), is(HIGH_PRIORITY));
        assertThat(instructionQueue.peek().getInstructionType(), is(HIGH_PRIORITY));
        assertThat(instructionQueue.remove().getInstructionType(), is(HIGH_PRIORITY));
        assertThat(instructionQueue.remove().getInstructionType(), is(MEDIUIM_PRIORITY));
        assertThat(instructionQueue.remove().getInstructionType(), is(LOW_PRIORITY));
    }

    private void buildInstructionQueue() {
        instructionQueue = new InstructionQueue(mockQueue);
    }

    private void buildMessage() {
        message = InstructionMessageBuilder.instructionMessage().withInstructionType(VALID_VALUE)
                .withProductCode(VALID_VALUE).withQuantity(VALID_VALUE)
                .withQuantity(VALID_VALUE)
                .withTimestamp(VALID_VALUE)
                .withUom(VALID_VALUE).build();
    }

    private static InstructionMessage messageWith(int type) {
        return InstructionMessageBuilder.instructionMessage().withInstructionType(type)
                .withProductCode(VALID_VALUE).withQuantity(VALID_VALUE)
                .withQuantity(VALID_VALUE)
                .withTimestamp(VALID_VALUE)
                .withUom(VALID_VALUE).build();
    }

    private void buildInvalidMessage() {
        message = InstructionMessageBuilder.instructionMessage().build();
    }
}
