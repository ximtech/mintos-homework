CREATE TABLE IF NOT EXISTS mintos.client_accounts
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT NOT NULL DEFAULT 0,
    created_by_user VARCHAR(255),
    updated_by_user VARCHAR(255),
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,
    
    client_id UUID NOT NULL,
    currency VARCHAR(255) NOT NULL,
    balance NUMERIC(15,6) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_client_id ON mintos.client_accounts(client_id);
CREATE INDEX IF NOT EXISTS idx_client_currency ON mintos.client_accounts(currency);
