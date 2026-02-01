CREATE TABLE intake_record (
                               id UUID PRIMARY KEY,

    -- Demographics (may be partial)
                               first_name TEXT,
                               last_name  TEXT,
                               dob        DATE,
                               gender     TEXT,

    -- Identifiers (may be partial)
                               email   TEXT,
                               phone   TEXT,
                               address JSONB,

    -- Matching outcome
                               match_result TEXT NOT NULL,
                               resolved_patient_id UUID,

                               created_at TIMESTAMPTZ NOT NULL,
                               created_by TEXT NOT NULL,

                               CONSTRAINT fk_intake_patient
                                   FOREIGN KEY (resolved_patient_id)
                                       REFERENCES patient_tbl(id)
);
