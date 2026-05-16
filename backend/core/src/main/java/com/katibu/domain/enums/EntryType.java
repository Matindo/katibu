package com.katibu.domain.enums;

public enum EntryType {
    // Inflows (receipts)
    INITIAL_CAPITAL,
    DONATION,
    REVENUE,
    GRANT,
    LOAN_RECEIVED,
    CREDIT,

    // Outflows (payments)
    PURCHASE,
    EXPENDITURE,
    LOAN_REPAYMENT,
    DEBT_PAYMENT,
    WITHDRAWAL;

    public boolean isInflow() {
        return switch (this) {
            case INITIAL_CAPITAL, DONATION, REVENUE, GRANT, LOAN_RECEIVED, CREDIT -> true;
            default -> false;
        };
    }

    // IFRS cash flow categorisation
    public CashFlowCategory cashFlowCategory() {
        return switch (this) {
            case REVENUE, DONATION, EXPENDITURE -> CashFlowCategory.OPERATING;
            case INITIAL_CAPITAL, PURCHASE -> CashFlowCategory.INVESTING;
            case LOAN_RECEIVED, LOAN_REPAYMENT, GRANT, DEBT_PAYMENT, CREDIT, WITHDRAWAL -> CashFlowCategory.FINANCING;
        };
    }

    public enum CashFlowCategory {
        OPERATING, INVESTING, FINANCING
    }
}
