CREATE TABLE patient_identifier (
                                    id UUID PRIMARY KEY,
                                    patient_id UUID NOT NULL,

                                    identifier_type TEXT NOT NULL,
                                    identifier_value TEXT NOT NULL,

                                    created_at TIMESTAMPTZ NOT NULL,

                                    CONSTRAINT fk_identifier_patient
                                        FOREIGN KEY (patient_id)
                                            REFERENCES patient_tbl(id)
);

CREATE UNIQUE INDEX uq_patient_identifier
    ON patient_identifier (patient_id, identifier_type, identifier_value);

CREATE INDEX idx_identifier_type_value
    ON patient_identifier (identifier_type, identifier_value);

CREATE INDEX idx_identifier_email_lower
    ON patient_identifier (lower(identifier_value))
    WHERE identifier_type = 'EMAIL';
