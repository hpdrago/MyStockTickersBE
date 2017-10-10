CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_customer_email` (`email`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `exception` (
  `id` int(11) NOT NULL,
  `class_name` varchar(45) NOT NULL,
  `method_name` varchar(45) NOT NULL,
  `arguments` varchar(255) NOT NULL,
  `stack_trace` varchar(4096) NOT NULL,
  `datetime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `portfolio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `idx_portfolio_customer_id` (`customer_id`),
  CONSTRAINT `FK_PORTFOLIO_CUSTOMER` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `portfolio_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `portfolio_id` int(11) DEFAULT NULL,
  `ticker_symbol` varchar(5) NOT NULL,
  `number_of_shares` int(11) DEFAULT NULL,
  `cost_basis` int(11) DEFAULT NULL,
  `realized_gain` int(11) DEFAULT NULL,
  `realized_loss` int(11) DEFAULT NULL,
  `stop_loss_price` decimal(7,2) DEFAULT NULL,
  `stop_loss_shares` int(11) DEFAULT NULL,
  `profit_taking_price` decimal(7,2) DEFAULT NULL,
  `profit_taking_shares` int(11) DEFAULT NULL,
  `sector_id` int(11) DEFAULT NULL,
  `sub_sector_id` int(11) DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `idx_portfolio_stock_customer_id` (`customer_id`),
  KEY `idx_portfolio_stock_portfolio_id` (`portfolio_id`),
  KEY `FK_PORTFOLIO_STOCK_STOCK_idx` (`ticker_symbol`),
  CONSTRAINT `FK_PORTFOLIO_STOCK_CUSTOMER` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_PORTFOLIO_STOCK_PORTFOLIO` FOREIGN KEY (`portfolio_id`) REFERENCES `portfolio` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION,
  CONSTRAINT `FK_PORTFOLIO_STOCK_STOCK` FOREIGN KEY (`ticker_symbol`) REFERENCES `stock` (`ticker_symbol`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stock` (
  `ticker_symbol` varchar(5) NOT NULL,
  `company_name` varchar(70) DEFAULT NULL,
  `exchange` varchar(10) DEFAULT 'OTHER',
  `created_by` int(11) DEFAULT '1',
  `user_entered` char(1) DEFAULT 'Y',
  `last_price` decimal(7,2) DEFAULT NULL,
  `last_price_update` datetime DEFAULT NULL,
  `last_price_change` datetime DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `quote_url` varchar(120) DEFAULT NULL,
  `sector` varchar(45) DEFAULT NULL,
  `industry` varchar(45) DEFAULT NULL,
  UNIQUE KEY `ticker_symbol_UNIQUE` (`ticker_symbol`),
  KEY `FK_CUSTOMER_STOCK_idx` (`created_by`),
  FULLTEXT KEY `idx_stock_ticker_symbol` (`ticker_symbol`),
  FULLTEXT KEY `idx_stock_company_name` (`company_name`),
  CONSTRAINT `FK_CUSTOMER_STOCK` FOREIGN KEY (`created_by`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stock_note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `notes` varchar(4000) NOT NULL,
  `notes_source_id` int(11) DEFAULT NULL,
  `notes_rating` int(1) DEFAULT NULL,
  `notes_date` datetime NOT NULL,
  `bull_or_bear` tinyint(1) DEFAULT NULL,
  `public_ind` varchar(1) DEFAULT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_STOCK_NOTES_STOCK_NOTES_SOURCE_idx` (`notes_source_id`),
  KEY `IDX_CUSTOMER` (`customer_id`),
  CONSTRAINT `FK_STOCK_NOTES_CUSTOMER` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

CREATE TABLE `stock_sector` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sector` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stock_sub_sector` (
  `sector_id` int(11) NOT NULL,
  `sub_sector_id` int(11) NOT NULL,
  `sub_sector` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_portfolio_stock` AS select `ps`.`id` AS `id`,`ps`.`portfolio_id` AS `portfolio_id`,`ps`.`cost_basis` AS `cost_basis`,`ps`.`number_of_shares` AS `number_of_shares`,`ps`.`sector_id` AS `sector_id`,`ps`.`sub_sector_id` AS `sub_sector_id`,`ps`.`realized_gain` AS `realized_gain`,`ps`.`realized_loss` AS `realized_loss`,`ps`.`stop_loss_price` AS `stop_loss_price`,`ps`.`stop_loss_shares` AS `stop_loss_shares`,`ps`.`profit_taking_price` AS `profit_taking_price`,`ps`.`profit_taking_shares` AS `profit_taking_shares`,`s`.`ticker_symbol` AS `ticker_symbol`,`s`.`company_name` AS `company_name`,`s`.`last_price` AS `last_price` from (`portfolio_stock` `ps` join `stock` `s` on((`s`.`ticker_symbol` = `ps`.`ticker_symbol`)));

CREATE ALGORITHM=UNDEFINED DEFINER=`stocktracker`@`%` SQL SECURITY DEFINER VIEW `v_stock_note_count` AS select `sns`.`customer_id` AS `customer_id`,`sns`.`ticker_symbol` AS `ticker_symbol`,count(0) AS `note_count` from `stock_note_stock` `sns` group by `sns`.`customer_id`,`sns`.`ticker_symbol` order by `sns`.`customer_id`,`sns`.`ticker_symbol`;
