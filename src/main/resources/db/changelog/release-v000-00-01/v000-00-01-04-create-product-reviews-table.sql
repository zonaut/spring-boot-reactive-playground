--
-- ##################################################################################################
-- ##################################################################################################
-- ####
-- #### PRODUCT_REVIEWS
-- ####
-- ##################################################################################################
-- ##################################################################################################

DROP TABLE IF EXISTS product_reviews;
CREATE TABLE IF NOT EXISTS product_reviews
(
    id                              UUID                                    PRIMARY KEY,
    created_at                      TIMESTAMP WITHOUT TIME ZONE             NOT NULL            DEFAULT CURRENT_TIMESTAMP,
    updated_at                      TIMESTAMP WITHOUT TIME ZONE             NULL,
    customer_id                     UUID                                    NOT NULL,
    product_id                      UUID                                    NOT NULL,
    content                         VARCHAR                                 NOT NULL,
    stars                           INT                                     NOT NULL,

    CONSTRAINT FK_product_reviews_customer_id FOREIGN KEY(customer_id) REFERENCES customers(id),
    CONSTRAINT FK_product_reviews_product_id FOREIGN KEY(product_id) REFERENCES products(id)
);