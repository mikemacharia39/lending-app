package com.ezra.lending_app.domain.enums;

public enum RepaymentFrequencyType {
    ONE_OFF(1),
    DAILY(1),
    WEEKLY(7),
    MONTHLY(30),
    ANNUALLY(365);

    RepaymentFrequencyType(int repaymentFrequency) {
    }
}
