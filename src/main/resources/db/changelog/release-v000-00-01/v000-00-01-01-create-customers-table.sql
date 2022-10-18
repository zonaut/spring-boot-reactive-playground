--
-- ##################################################################################################
-- ##################################################################################################
-- ####
-- #### CUSTOMERS
-- ####
-- ##################################################################################################
-- ##################################################################################################

DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS customers
(
    id                              UUID                                    PRIMARY KEY,
    created_at                      TIMESTAMP WITHOUT TIME ZONE             NOT NULL            DEFAULT CURRENT_TIMESTAMP,
    username                        VARCHAR(255)                            NOT NULL,
    info                            JSONB                                   NULL,

    CONSTRAINT customers_username_UQ UNIQUE (username)
);
