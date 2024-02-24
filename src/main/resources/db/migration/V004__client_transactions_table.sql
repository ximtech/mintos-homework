CREATE TABLE IF NOT EXISTS mintos.client_transactions
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT NOT NULL DEFAULT 0,
    created_by_user VARCHAR(255),
    updated_by_user VARCHAR(255),
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,
    
    account_id UUID NOT NULL,
    target_account_id UUID NOT NULL,
    amount NUMERIC(15,6) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES mintos.client_accounts(id),
    FOREIGN KEY (target_account_id) REFERENCES mintos.client_accounts(id)
);

CREATE INDEX IF NOT EXISTS idx_account_id ON mintos.client_transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_target_account_id ON mintos.client_transactions(target_account_id);
