package com.bscl.processor;

import static java.lang.String.format;

import java.util.PriorityQueue;
import java.util.Queue;

public class InstructionQueue {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    private Queue<InstructionMessage> queue;

    public static InstructionQueue newPriorityQueue() {
        Queue<InstructionMessage> q = new PriorityQueue<>(DEFAULT_INITIAL_CAPACITY, new InstructionMessageComparator());
        return new InstructionQueue(q);
    }

    public static InstructionQueue newInstructionQueue(Queue<InstructionMessage> queue) {
        return new InstructionQueue(queue);
    }

    public InstructionQueue(Queue<InstructionMessage> queue) {
        this.queue = queue;
    }

    public boolean add(InstructionMessage message) {
        check(message);
        return queue.add(message);
    }

    public InstructionMessage remove() {
        return queue.remove();
    }

    public InstructionMessage peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private static void check(InstructionMessage message) {
        if (!message.isValid()) {
            throw new InvalidMessageException(format("Invalid message: %s", message));
        }
    }
}
