CREATE TABLE patient_tbl (
                         id UUID PRIMARY KEY,

                         first_name TEXT NOT NULL,
                         last_name TEXT NOT NULL,
                         dob DATE NOT NULL,
                         gender TEXT,
                         status TEXT NOT NULL,

                         created_at TIMESTAMPTZ NOT NULL,
                         created_by TEXT NOT NULL
);

CREATE INDEX idx_patient_name_dob
    ON patient_tbl (lower(first_name), lower(last_name), dob);
