package com.example.Splitmate.ServiceBody;

public class SplitFactory {
    public static Split createSplit(String splitType) {
        switch (splitType) {
            case "EQUAL":
                return new EqualSplit(); // Return an EqualSplit instance
            case "PERCENTAGE":
                return new PercentageSplit(); // Return a PercentageSplit instance
            default:
                // Throw an exception if the split type is invalid
                throw new IllegalArgumentException("Unknown split type: " + splitType);
        }
    }
}
