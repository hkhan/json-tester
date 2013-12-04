package com.bscl.processor;

import java.util.Comparator;

public class InstructionMessageComparator implements Comparator<InstructionMessage> {

    @Override
    public int compare(InstructionMessage first, InstructionMessage second) {
        if (first.getInstructionType() > second.getInstructionType()) {
            return 1;
        } else if (first.getInstructionType() == second.getInstructionType()) {
            return 0;
        } else {
            return -1;
        }
    }

}
