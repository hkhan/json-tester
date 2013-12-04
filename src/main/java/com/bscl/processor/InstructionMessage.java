package com.bscl.processor;

import java.util.Objects;

public class InstructionMessage {

    private int instructionType;
    private int productCode;
    private int quantity;
    private int uom;
    private int timestamp;

    public boolean isValid() {
        return checkRange(instructionType, 0, 100)
                && checkRange(uom, -1, 256)
                && checkGreaterThanZero(productCode, quantity, timestamp);
    }

    private static boolean checkRange(int value, int min, int max) {
        return value > min && value < max;
    }

    private static boolean checkGreaterThanZero(int... values) {
        for (int value : values) {
            if (value <= 0) {
                return false;
            }
        }
        return true;
    }

    public int getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(int instructionType) {
        this.instructionType = instructionType;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUom() {
        return uom;
    }

    public void setUom(int uom) {
        this.uom = uom;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof InstructionMessage))
            return false;
        InstructionMessage other = (InstructionMessage) obj;
        if (instructionType != other.instructionType)
            return false;
        if (productCode != other.productCode)
            return false;
        if (quantity != other.quantity)
            return false;
        if (timestamp != other.timestamp)
            return false;
        if (uom != other.uom)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructionType, productCode, quantity, timestamp, uom);
    }

    @Override
    public String toString() {
        return "InstructionMessage [instructionType=" + instructionType + ", productCode=" + productCode +
                ", quantity=" + quantity + ", uom=" + uom + ", timestamp=" + timestamp + "]";
    }

}
