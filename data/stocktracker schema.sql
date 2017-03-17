CREATE TABLE customer
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  email VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX idx_customer_email ON customer (email);
CREATE UNIQUE INDEX id_UNIQUE ON customer (id);
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
  sector_id INT(11),
  sub_sector_id INT(11),
  CONSTRAINT `PRIMARY` PRIMARY KEY (customer_id, ticker_symbol)
);
CREATE TABLE exception
(
  id INT(11) PRIMARY KEY NOT NULL,
  class_name VARCHAR(45) NOT NULL,
  method_name VARCHAR(45) NOT NULL,
  arguments VARCHAR(255) NOT NULL,
  stack_trace VARCHAR(4096) NOT NULL,
  datetime DATETIME NOT NULL
);
CREATE TABLE portfolio
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  customer_id INT(11) NOT NULL,
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT FK_PORTFOLIO_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);
CREATE INDEX idx_portfolio_customer_id ON portfolio (customer_id);
CREATE UNIQUE INDEX id_UNIQUE ON portfolio (id);
CREATE TABLE portfolio_stock
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  customer_id INT(11) NOT NULL,
  portfolio_id INT(11),
  ticker_symbol VARCHAR(5) NOT NULL,
  number_of_shares INT(11),
  cost_basis INT(11),
  realized_gain INT(11),
  realized_loss INT(11),
  stop_loss_price DECIMAL(7,2),
  stop_loss_shares INT(11),
  profit_taking_price DECIMAL(7,2),
  profit_taking_shares INT(11),
  sector_id INT(11),
  sub_sector_id INT(11),
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT FK_PORTFOLIO_STOCK_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  CONSTRAINT FK_PORTFOLIO_STOCK_PORTFOLIO FOREIGN KEY (portfolio_id) REFERENCES portfolio (id) ON DELETE SET NULL,
  CONSTRAINT FK_PORTFOLIO_STOCK_STOCK FOREIGN KEY (ticker_symbol) REFERENCES stock (ticker_symbol)
);
CREATE INDEX idx_portfolio_stock_customer_id ON portfolio_stock (customer_id);
CREATE INDEX idx_portfolio_stock_portfolio_id ON portfolio_stock (portfolio_id);
CREATE INDEX FK_PORTFOLIO_STOCK_STOCK_idx ON portfolio_stock (ticker_symbol);
CREATE UNIQUE INDEX id_UNIQUE ON portfolio_stock (id);
CREATE TABLE stock
(
  ticker_symbol VARCHAR(5) NOT NULL,
  company_name VARCHAR(70),
  stock_exchange VARCHAR(10) DEFAULT 'OTHER',
  created_by INT(11) DEFAULT '1',
  user_entered CHAR(1) DEFAULT 'Y',
  last_price DECIMAL(7,2),
  last_price_update DATETIME,
  last_price_change DATETIME,
  create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  quote_url VARCHAR(120),
  sector VARCHAR(45),
  industry VARCHAR(45)
);
CREATE INDEX idx_stock_company_name ON stock (company_name);
CREATE INDEX idx_stock_ticker_symbol ON stock (ticker_symbol);
CREATE UNIQUE INDEX ticker_symbol_UNIQUE ON stock (ticker_symbol);
CREATE TABLE stock_sector
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  sector VARCHAR(30)
);
CREATE TABLE stock_sub_sector
(
  sector_id INT(11) NOT NULL,
  sub_sector_id INT(11) NOT NULL,
  sub_sector VARCHAR(30) NOT NULL
);
CREATE TABLE v_portfolio_stock
(
  id INT(11) NOT NULL,
  portfolio_id INT(11),
  cost_basis INT(11),
  number_of_shares INT(11),
  sector_id INT(11),
  sub_sector_id INT(11),
  realized_gain INT(11),
  realized_loss INT(11),
  stop_loss_price DECIMAL(7,2),
  stop_loss_shares INT(11),
  profit_taking_price DECIMAL(7,2),
  profit_taking_shares INT(11),
  ticker_symbol VARCHAR(5) NOT NULL,
  company_name VARCHAR(70),
  last_price DECIMAL(7,2)
);