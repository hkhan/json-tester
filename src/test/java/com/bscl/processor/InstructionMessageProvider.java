package com.bscl.processor;

import static junitparams.JUnitParamsRunner.$;

public class InstructionMessageProvider {
    private static final int VALID_VALUE = 10;

    public static Object[] provideInstructionType() {
        return $(
                $(getBuilder().withInstructionType(0).build(), false),
                $(getBuilder().withInstructionType(100).build(), false),
                $(getBuilder().withInstructionType(-1).build(), false),
                $(getBuilder().withInstructionType(1).build(), true),
                $(getBuilder().withInstructionType(99).build(), true));
    }

    public static Object[] provideProductCode() {
        return $(
                $(getBuilder().withProductCode(0).build(), false),
                $(getBuilder().withProductCode(-1).build(), false),
                $(getBuilder().withProductCode(10).build(), true));
    }

    public static Object[] provideUom() {
        return $(
                $(getBuilder().withUom(-1).build(), false),
                $(getBuilder().withUom(256).build(), false),
                $(getBuilder().withUom(1).build(), true),
                $(getBuilder().withUom(255).build(), true),
                $(getBuilder().withUom(0).build(), true));
    }

    public static Object[] provideQuantity() {
        return $(
                $(getBuilder().withQuantity(-1).build(), false),
                $(getBuilder().withQuantity(0).build(), false),
                $(getBuilder().withQuantity(1000).build(), true));
    }

    public static Object[] provideTimestamp() {
        return $(
                $(getBuilder().withTimestamp(-1).build(), false),
                $(getBuilder().withTimestamp(0).build(), false),
                $(getBuilder().withTimestamp(10).build(), true));
    }

    private static InstructionMessageBuilder getBuilder() {
        return InstructionMessageBuilder.instructionMessage()
                .withInstructionType(VALID_VALUE)
                .withProductCode(VALID_VALUE).withQuantity(VALID_VALUE)
                .withQuantity(VALID_VALUE)
                .withTimestamp(VALID_VALUE)
                .withUom(VALID_VALUE);
    }
}
