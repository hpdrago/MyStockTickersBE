CREATE TABLE customer
(
    id INT(11) unsigned NOT NULL AUTO_INCREMENT,
    email VARCHAR(45) NOT NULL,
    password VARCHAR(45) NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE customer_portfolio
(
    customer_id INT(11) NOT NULL,
    portfolio_id INT(11) NOT NULL AUTO_INCREMENT
);
CREATE TABLE customer_stock
(
    customer_id INT(11) NOT NULL,
    ticker_symbol VARCHAR(5) NOT NULL,
    number_of_shares INT(11),
    cost_basis INT(11),
    realized_gain INT(11),
    realized_loss INT(11),
    stop_loss_price DECIMAL(7,2),
    stop_loss_shares INT(11),
    profit_taking_price DECIMAL(7,2),
    profit_taking_shares INT(11),
    sector_id INT(11) unsigned,
    sub_sector_id INT(11) unsigned
);
CREATE TABLE exception
(
    id INT(11) unsigned NOT NULL,
    class_name VARCHAR(45) NOT NULL,
    method_name VARCHAR(45) NOT NULL,
    arguments VARCHAR(255) NOT NULL,
    stack_trace VARCHAR(4096) NOT NULL,
    datetime DATETIME NOT NULL
);
CREATE TABLE portfolio
(
    id INT(11) unsigned NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    customer_id INT(11) unsigned NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE portfolio_stock
(
    id INT(11) unsigned NOT NULL AUTO_INCREMENT,
    customer_id INT(11) unsigned NOT NULL,
    portfolio_id INT(11) unsigned NOT NULL,
    ticker_symbol VARCHAR(5) NOT NULL,
    number_of_shares INT(11),
    cost_basis INT(11),
    realized_gain INT(11),
    realized_loss INT(11),
    stop_loss_price DECIMAL(7,2),
    stop_loss_shares INT(11),
    profit_taking_price DECIMAL(7,2),
    profit_taking_shares INT(11),
    sector_id INT(11) unsigned,
    sub_sector_id INT(11) unsigned,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE stock
(
    ticker_symbol VARCHAR(5) NOT NULL,
    company_name VARCHAR(70),
    exchange VARCHAR(10) DEFAULT 'OTHER',
    created_by INT(11) unsigned DEFAULT '1',
    user_entered CHAR(1) DEFAULT 'Y',
    last_price DECIMAL(7,2),
    last_price_update DATETIME,
    last_price_change DATETIME,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE stock_sector
(
    id INT(11) unsigned NOT NULL AUTO_INCREMENT,
    sector VARCHAR(30)
);
CREATE TABLE stock_sub_sector
(
    sector_id INT(11) unsigned NOT NULL,
    sub_sector_id INT(11) unsigned NOT NULL,
    sub_sector VARCHAR(30) NOT NULL
);
CREATE TABLE v_portfolio_stock
(
);

CREATE VIEW `v_portfolio_stock` AS
  SELECT
    `ps`.`id`                   AS `id`,
    `ps`.`portfolio_id`         AS `portfolio_id`,
    `ps`.`cost_basis`           AS `cost_basis`,
    `ps`.`number_of_shares`     AS `number_of_shares`,
    `ps`.`sector_id`            AS `sector_id`,
    `ps`.`sub_sector_id`        AS `sub_sector_id`,
    `ps`.`realized_gain`        AS `realized_gain`,
    `ps`.`realized_loss`        AS `realized_loss`,
    `ps`.`stop_loss_price`      AS `stop_loss_price`,
    `ps`.`stop_loss_shares`     AS `stop_loss_shares`,
    `ps`.`profit_taking_price`  AS `profit_taking_price`,
    `ps`.`profit_taking_shares` AS `profit_taking_shares`,
    `s`.`ticker_symbol`         AS `ticker_symbol`,
    `s`.`company_name`          AS `company_name`,
    `s`.`last_price`            AS `last_price`
  FROM (`stocktracker`.`portfolio_stock` `ps`
    JOIN `stocktracker`.`stock` `s` ON ((`s`.`ticker_symbol` = `ps`.`ticker_symbol`)));