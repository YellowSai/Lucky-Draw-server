CREATE SCHEMA IF NOT EXISTS mars_lottery default character set utf8mb4 collate utf8mb4_unicode_ci;

#mysql8以下
grant all privileges on mars_lottery.* to 'mars_lottery'@'%' identified by 'Mars_lottery@2021$%^';

#mysql8.0+
create user 'mars_lottery'@'127.0.0.1' identified by 'Mars_lottery@2021$%^';
grant all privileges on mars_lottery.* to 'mars_lottery'@'127.0.0.1';