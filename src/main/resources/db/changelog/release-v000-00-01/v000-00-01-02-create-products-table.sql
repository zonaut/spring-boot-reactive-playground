--
-- ##################################################################################################
-- ##################################################################################################
-- ####
-- #### PRODUCTS
-- ####
-- ##################################################################################################
-- ##################################################################################################

DROP TABLE IF EXISTS products;
CREATE TABLE IF NOT EXISTS products
(
    id                              UUID                                    PRIMARY KEY,
    created_at                      TIMESTAMP WITHOUT TIME ZONE             NOT NULL            DEFAULT CURRENT_TIMESTAMP,
    name                            VARCHAR(255)                            NOT NULL,
    category                        VARCHAR(255)                            NOT NULL,
    features                        JSONB                                   ,
    active                          BOOLEAN                                 NOT NULL            DEFAULT FALSE,
    price                           NUMERIC(10, 4)                          NOT NULL,

    CONSTRAINT products_name_UQ UNIQUE (name)
);
