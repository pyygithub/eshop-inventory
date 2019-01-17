

create database if not exists eshop charcacter set utf8;

use eshop;

create table user(
  id varchar(32) PRIMARY key,
  name varchar(100),
  age int
);

insert into user('su1i32i234l234n2123', 'admin', 12);

create table product_inventory(
  product_id varchar(32) PRIMARY key,
  inventory_cnt int
);

insert product_inventory values('1', 100);