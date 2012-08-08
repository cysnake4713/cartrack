# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        bigint auto_increment not null,
  account_name              varchar(255),
  password                  varchar(255),
  constraint uq_account_account_name unique (account_name),
  constraint pk_account primary key (id))
;

create table point (
  id                        bigint auto_increment not null,
  target_id                 bigint,
  point_record_time         datetime,
  longitude                 double,
  latitude                  double,
  mananger                  varchar(255),
  product                   integer,
  gas_cost                  integer,
  scan_document_url         varchar(255),
  comments                  varchar(255),
  constraint pk_point primary key (id))
;

create table target (
  id                        bigint auto_increment not null,
  target_tag                varchar(255),
  target_name               varchar(255),
  comments                  varchar(255),
  account_id                bigint,
  constraint pk_target primary key (id))
;

alter table point add constraint fk_point_target_1 foreign key (target_id) references target (id) on delete restrict on update restrict;
create index ix_point_target_1 on point (target_id);
alter table target add constraint fk_target_account_2 foreign key (account_id) references account (id) on delete restrict on update restrict;
create index ix_target_account_2 on target (account_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table account;

drop table point;

drop table target;

SET FOREIGN_KEY_CHECKS=1;

