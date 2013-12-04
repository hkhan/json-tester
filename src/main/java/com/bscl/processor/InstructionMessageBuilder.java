// CHECKSTYLE:OFF
/**
 * Source code generated by Fluent Builders Generator
 * Do not modify this file
 * See generator home page at: http://code.google.com/p/fluent-builders-generator-eclipse-plugin/
 */

package com.bscl.processor;

public class InstructionMessageBuilder extends InstructionMessageBuilderBase<InstructionMessageBuilder> {
    public static InstructionMessageBuilder instructionMessage() {
        return new InstructionMessageBuilder();
    }

    public InstructionMessageBuilder() {
        super(new InstructionMessage());
    }

    public InstructionMessage build() {
        return getInstance();
    }
}

class InstructionMessageBuilderBase<GeneratorT extends InstructionMessageBuilderBase<GeneratorT>> {
    private InstructionMessage instance;

    protected InstructionMessageBuilderBase(InstructionMessage aInstance) {
        instance = aInstance;
    }

    protected InstructionMessage getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public GeneratorT withInstructionType(int aValue) {
        instance.setInstructionType(aValue);

        return (GeneratorT) this;
    }

    @SuppressWarnings("unchecked")
    public GeneratorT withProductCode(int aValue) {
        instance.setProductCode(aValue);

        return (GeneratorT) this;
    }

    @SuppressWarnings("unchecked")
    public GeneratorT withQuantity(int aValue) {
        instance.setQuantity(aValue);

        return (GeneratorT) this;
    }

    @SuppressWarnings("unchecked")
    public GeneratorT withUom(int aValue) {
        instance.setUom(aValue);

        return (GeneratorT) this;
    }

    @SuppressWarnings("unchecked")
    public GeneratorT withTimestamp(int aValue) {
        instance.setTimestamp(aValue);

        return (GeneratorT) this;
    }
}