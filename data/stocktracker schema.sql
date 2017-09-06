create table customer
(
  id int auto_increment
    primary key,
  email varchar(45) not null,
  password varchar(45) not null,
  create_date timestamp default CURRENT_TIMESTAMP not null,
  constraint id_UNIQUE
  unique (id),
  constraint idx_customer_email
  unique (email)
)
;

create table exception
(
  id int not null
    primary key,
  class_name varchar(45) not null,
  method_name varchar(45) not null,
  arguments varchar(255) not null,
  stack_trace varchar(4096) not null,
  datetime datetime not null
)
;

create table portfolio
(
  id int auto_increment
    primary key,
  name varchar(20) not null,
  customer_id int not null,
  create_date timestamp default CURRENT_TIMESTAMP not null,
  constraint id_UNIQUE
  unique (id),
  constraint FK_PORTFOLIO_CUSTOMER
  foreign key (customer_id) references customer (id)
    on delete cascade
)
;

create index idx_portfolio_customer_id
  on portfolio (customer_id)
;

create table portfolio_stock
(
  id int auto_increment
    primary key,
  customer_id int not null,
  portfolio_id int null,
  ticker_symbol varchar(5) not null,
  number_of_shares int null,
  cost_basis int null,
  realized_gain int null,
  realized_loss int null,
  stop_loss_price decimal(7,2) null,
  stop_loss_shares int null,
  profit_taking_price decimal(7,2) null,
  profit_taking_shares int null,
  sector_id int null,
  sub_sector_id int null,
  create_date timestamp default CURRENT_TIMESTAMP not null,
  constraint id_UNIQUE
  unique (id),
  constraint FK_PORTFOLIO_STOCK_CUSTOMER
  foreign key (customer_id) references customer (id)
    on delete cascade,
  constraint FK_PORTFOLIO_STOCK_PORTFOLIO
  foreign key (portfolio_id) references portfolio (id)
    on delete set null
)
;

create index FK_PORTFOLIO_STOCK_STOCK_idx
  on portfolio_stock (ticker_symbol)
;

create index idx_portfolio_stock_customer_id
  on portfolio_stock (customer_id)
;

create index idx_portfolio_stock_portfolio_id
  on portfolio_stock (portfolio_id)
;

create table stock
(
  ticker_symbol varchar(5) not null,
  company_name varchar(70) null,
  exchange varchar(10) default 'OTHER' null,
  created_by int default '1' null,
  user_entered char default 'Y' null,
  last_price decimal(7,2) null,
  last_price_update datetime null,
  last_price_change datetime null,
  create_date timestamp default CURRENT_TIMESTAMP not null,
  quote_url varchar(120) null,
  sector varchar(45) null,
  industry varchar(45) null,
  constraint ticker_symbol_UNIQUE
  unique (ticker_symbol),
  constraint FK_CUSTOMER_STOCK
  foreign key (created_by) references customer (id)
)
;

create index FK_CUSTOMER_STOCK_idx
  on stock (created_by)
;

create index idx_stock_company_name
  on stock (company_name)
;

create index idx_stock_ticker_symbol
  on stock (ticker_symbol)
;

alter table portfolio_stock
  add constraint FK_PORTFOLIO_STOCK_STOCK
foreign key (ticker_symbol) references stock (ticker_symbol)
;

create table stock_note
(
  id int auto_increment
    primary key,
  customer_id int not null,
  notes varchar(4000) not null,
  notes_source_id int null,
  notes_rating int(1) null,
  notes_date datetime not null,
  bull_or_bear tinyint(1) null,
  public_ind varchar(1) null,
  date_created datetime default CURRENT_TIMESTAMP not null,
  date_modified datetime null,
  constraint FK_STOCK_NOTES_CUSTOMER
  foreign key (customer_id) references customer (id)
)
;

create index FK_STOCK_NOTES_STOCK_NOTES_SOURCE_idx
  on stock_note (notes_source_id)
;

create index IDX_CUSTOMER
  on stock_note (customer_id)
;

create table stock_note_source
(
  id int not null
    primary key,
  name varchar(20) not null,
  customer_id int not null,
  date_created datetime default CURRENT_TIMESTAMP not null
)
;

create index IDX_CUSTOMER_ID
  on stock_note_source (customer_id, name)
;

alter table stock_note
  add constraint FK_STOCK_NOTES_STOCK_NOTES_SOURCE
foreign key (notes_source_id) references stock_note_source (id)
;

create table stock_note_stock
(
  customer_id int not null,
  stock_note_id int not null,
  ticker_symbol varchar(5) not null,
  stock_price decimal(7,2) not null,
  primary key (customer_id, stock_note_id, ticker_symbol),
  constraint FK_STOCK_NOTE_STOCKS_CUSTOMER
  foreign key (customer_id) references customer (id)
    on delete cascade,
  constraint FK_STOCK_NOTE_STOCK_STOCK_NOTE
  foreign key (stock_note_id) references stock_note (id)
    on delete cascade
)
;

create index FK_STOCK_NOTE_STOCK_STOCK_NOTE_idx
  on stock_note_stock (stock_note_id)
;

create table stock_sector
(
  id int auto_increment
    primary key,
  sector varchar(30) null
)
;

create table stock_sub_sector
(
  sector_id int not null,
  sub_sector_id int not null,
  sub_sector varchar(30) not null
)
;

create view v_portfolio_stock as
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

create view v_stock_note_count as
  SELECT
    `stocktracker`.`stock_note`.`customer_id`   AS `customer_id`,
    `stocktracker`.`stock_note`.`ticker_symbol` AS `ticker_symbol`,
    count(0)                                    AS `note_count`
  FROM `stocktracker`.`stock_note`
  GROUP BY `stocktracker`.`stock_note`.`customer_id`, `stocktracker`.`stock_note`.`ticker_symbol`
  ORDER BY `stocktracker`.`stock_note`.`ticker_symbol`;

