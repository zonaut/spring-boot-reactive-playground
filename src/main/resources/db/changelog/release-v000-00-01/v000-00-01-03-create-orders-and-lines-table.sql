--
-- ##################################################################################################
-- ##################################################################################################
-- ####
-- #### ORDERS
-- ####
-- ##################################################################################################
-- ##################################################################################################

DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders
(
    id                              UUID                                    PRIMARY KEY,
    created_at                      TIMESTAMP WITHOUT TIME ZONE             NOT NULL            DEFAULT CURRENT_TIMESTAMP,
    customer_id                     UUID                                    NOT NULL,
    status                          order_status                            NOT NULL,

    CONSTRAINT FK_orders_customer_id FOREIGN KEY(customer_id) REFERENCES customers(id)
);

--
-- ##################################################################################################
-- ##################################################################################################
-- ####
-- #### ORDER_LINES
-- ####
-- ##################################################################################################
-- ##################################################################################################

DROP TABLE IF EXISTS order_lines;
CREATE TABLE IF NOT EXISTS order_lines
(
    id                              BIGSERIAL                               PRIMARY KEY,
    created_at                      TIMESTAMP WITHOUT TIME ZONE             NOT NULL            DEFAULT CURRENT_TIMESTAMP,
    order_id                        UUID                                    NOT NULL,
    product_id                      UUID                                    NOT NULL,
    number_of                       INT                                     NOT NULL,

    CONSTRAINT FK_order_lines_order_id FOREIGN KEY(order_id) REFERENCES orders(id),
    CONSTRAINT FK_order_lines_product_id FOREIGN KEY(product_id) REFERENCES products(id)
);