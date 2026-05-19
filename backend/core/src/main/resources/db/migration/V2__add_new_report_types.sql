ALTER TABLE generated_files DROP CONSTRAINT generated_files_report_type_check;

ALTER TABLE generated_files
    ADD CONSTRAINT generated_files_report_type_check
    CHECK (report_type IN (
        'SUMMARY',
        'RECEIPTS_PAYMENTS',
        'CASH_FLOW',
        'FINANCIAL_POSITION',
        'LEDGER',
        'GENERAL_LEDGER',
        'TRIAL_BALANCE',
        'BALANCE_SHEET'
    ));
