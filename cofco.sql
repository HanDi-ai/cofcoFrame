prompt PL/SQL Developer import file
prompt Created on 2019年5月23日 by hesiodleo
set feedback off
set define off

prompt Creating SYS_LOG...
create table SYS_LOG
(
  ID          NUMBER not null,
  USERNAME    VARCHAR2(50),
  OPERATION   VARCHAR2(50),
  METHOD      VARCHAR2(200),
  IP          VARCHAR2(64),
  CREATE_DATE DATE,
  PARAMS      VARCHAR2(4000),
  SIGN        VARCHAR2(1),
  RESULT      CLOB
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column SYS_LOG.SIGN
  is '0:app端log 1:系统端log';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SYS_LOG
  add constraint PRIMARYSYS_LOG1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
prompt Creating SYS_CODE...
create table SYS_CODE
(
  ID          VARCHAR2(32) not null,
  MARK        VARCHAR2(32),
  NAME        VARCHAR2(128),
  VALUE       VARCHAR2(255),
  TYPE        VARCHAR2(255),
  PARENT_ID   VARCHAR2(32),
  SORT        VARCHAR2(4),
  REMARK      VARCHAR2(255),
  CREATE_ID   VARCHAR2(32),
  CREATE_TIME DATE,
  UPDATE_ID   VARCHAR2(32),
  UPDATE_TIME DATE,
  OPEN        VARCHAR2(5)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SYS_CODE.MARK
  is '码值唯一标识, 例如，SEX、SEX';
comment on column SYS_CODE.NAME
  is '码值的中文表示， 例如：是、否、性别';
comment on column SYS_CODE.VALUE
  is '码值 的数字表示，例如：1，2，3。。。。。、sex';
comment on column SYS_CODE.TYPE
  is '1：目录 2：字典码';
comment on column SYS_CODE.PARENT_ID
  is '父节点ID';
comment on column SYS_CODE.SORT
  is '在同一级节点中的序号';
comment on column SYS_CODE.REMARK
  is '备注';
comment on column SYS_CODE.CREATE_ID
  is '创建人';
comment on column SYS_CODE.CREATE_TIME
  is '创建时间';
comment on column SYS_CODE.UPDATE_ID
  is '修改人';
comment on column SYS_CODE.UPDATE_TIME
  is '修改时间';
comment on column SYS_CODE.OPEN
  is '是否展开 true是 false否';
alter table SYS_CODE
  add constraint PRIMARYSYS_CODE1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_CONFIG...
create table SYS_CONFIG
(
  ID     VARCHAR2(32) not null,
  KEY    VARCHAR2(50),
  VALUE  VARCHAR2(2000),
  STATUS VARCHAR2(32),
  REMARK VARCHAR2(500)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SYS_CONFIG
  add constraint PRIMARYSYS_CONFIG1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index IDX_KEYSYS_CONFIG on SYS_CONFIG (KEY)
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_LEND...
create table SYS_LEND
(
  ID           VARCHAR2(32) not null,
  USER_ID      VARCHAR2(32),
  OLD_ORGAN_ID VARCHAR2(32),
  NEW_ORGAN_ID VARCHAR2(32),
  LEND_TIME    DATE,
  STATUS       VARCHAR2(1),
  CREATE_TIME  DATE,
  CREATE_ID    VARCHAR2(32)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SYS_LEND.USER_ID
  is '用户ID';
comment on column SYS_LEND.OLD_ORGAN_ID
  is '原机构ID';
comment on column SYS_LEND.NEW_ORGAN_ID
  is '借调到机构ID';
comment on column SYS_LEND.LEND_TIME
  is '借调时间';
comment on column SYS_LEND.STATUS
  is '状态0启用 1禁用';
comment on column SYS_LEND.CREATE_TIME
  is '创建时间';
comment on column SYS_LEND.CREATE_ID
  is '创建人';
alter table SYS_LEND
  add constraint PRIMARY_KEY_ID primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_MENU...
create table SYS_MENU
(
  ID          VARCHAR2(32) not null,
  PARENT_ID   VARCHAR2(32),
  PARENT_IDS  VARCHAR2(2000),
  NAME        VARCHAR2(100),
  URL         VARCHAR2(1000),
  ICON        VARCHAR2(255),
  SORT        NUMBER,
  STATUS      VARCHAR2(6),
  PERMISSION  VARCHAR2(255),
  REMARK      VARCHAR2(255),
  CREATE_TIME DATE,
  CREATE_ID   VARCHAR2(32),
  UPDATE_ID   VARCHAR2(32),
  UPDATE_TIME DATE,
  TYPE        VARCHAR2(255),
  OPEN        VARCHAR2(5),
  BAPID       VARCHAR2(32),
  BAID        VARCHAR2(32),
  SIGN        VARCHAR2(1)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SYS_MENU.PARENT_ID
  is '父节点';
comment on column SYS_MENU.PARENT_IDS
  is '所有父节点';
comment on column SYS_MENU.NAME
  is '菜单名称';
comment on column SYS_MENU.URL
  is '菜单地址';
comment on column SYS_MENU.ICON
  is '图标';
comment on column SYS_MENU.SORT
  is '排序';
comment on column SYS_MENU.STATUS
  is '状态';
comment on column SYS_MENU.PERMISSION
  is '权限标识';
comment on column SYS_MENU.REMARK
  is '备注';
comment on column SYS_MENU.CREATE_TIME
  is '创建时间';
comment on column SYS_MENU.CREATE_ID
  is '创建人';
comment on column SYS_MENU.UPDATE_ID
  is '修改人';
comment on column SYS_MENU.UPDATE_TIME
  is '修改时间';
comment on column SYS_MENU.TYPE
  is '类型';
comment on column SYS_MENU.OPEN
  is '是否展开 true 是 false 否';
comment on column SYS_MENU.BAPID
  is '机构';
comment on column SYS_MENU.BAID
  is '部门';
comment on column SYS_MENU.SIGN
  is '0 移动端 1系统';
alter table SYS_MENU
  add constraint PRIMARYSYS_MENU1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_NOTICE...
create table SYS_NOTICE
(
  ID            VARCHAR2(32) not null,
  CONTEXT       VARCHAR2(512),
  TITLE         VARCHAR2(255),
  SOUCRE        VARCHAR2(3),
  STATUS        VARCHAR2(3),
  IS_URGENT     VARCHAR2(3),
  RELEASE_TIMEE DATE,
  CREATE_TIME   DATE,
  UPDATE_TIME   DATE,
  CREATE_ID     VARCHAR2(32),
  UPDATE_ID     VARCHAR2(32),
  REMARK        VARCHAR2(255)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SYS_NOTICE
  add constraint PRIMARYSYS_NOTICE1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_NOTICE_USER...
create table SYS_NOTICE_USER
(
  ID        VARCHAR2(32) not null,
  USER_ID   VARCHAR2(32),
  NOTICE_ID VARCHAR2(32),
  STATUS    VARCHAR2(3),
  REMARK    VARCHAR2(255)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SYS_NOTICE_USER
  add constraint PRIMARYSYS_NOTICE_USER1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_ORGAN...
create table SYS_ORGAN
(
  ID            VARCHAR2(32) not null,
  BAPID         VARCHAR2(32),
  TYPE          VARCHAR2(1),
  CODE          VARCHAR2(64),
  NAME          VARCHAR2(64),
  PARENT_ID     VARCHAR2(32),
  IS_DEL        VARCHAR2(1),
  SYSMARK       VARCHAR2(1024),
  SORT          VARCHAR2(4),
  OPEN          VARCHAR2(5),
  REMARK        VARCHAR2(255),
  CREATE_ID     VARCHAR2(32),
  CREATE_TIME   DATE,
  UPDATE_ID     VARCHAR2(32),
  UPDATE_TIME   DATE,
  SIMPLIFY_NAME VARCHAR2(64),
  LONGITUDE     NUMBER(9,6),
  LATITUDE      NUMBER(9,6)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SYS_ORGAN.BAPID
  is '该部门的归属机构ID ，只有当是部门的时候才生效
';
comment on column SYS_ORGAN.TYPE
  is '结点类型：0=根节点 ，1=机构，3=部门
';
comment on column SYS_ORGAN.CODE
  is '编码
';
comment on column SYS_ORGAN.NAME
  is '机构全名称
';
comment on column SYS_ORGAN.PARENT_ID
  is '父节点
';
comment on column SYS_ORGAN.IS_DEL
  is '是否删除 0未删除 1已删除
';
comment on column SYS_ORGAN.SYSMARK
  is '系统标识，32*10+9 最多支持11级节点。用户具体一批数据的控制
';
comment on column SYS_ORGAN.SORT
  is '在同一级节点中的序号
';
comment on column SYS_ORGAN.OPEN
  is '是否展开 true 是 false 否
  ';
comment on column SYS_ORGAN.REMARK
  is '备注
';
comment on column SYS_ORGAN.CREATE_ID
  is '创建人
';
comment on column SYS_ORGAN.CREATE_TIME
  is '创建时间
';
comment on column SYS_ORGAN.UPDATE_ID
  is '修改人
';
comment on column SYS_ORGAN.UPDATE_TIME
  is '修改时间';
comment on column SYS_ORGAN.SIMPLIFY_NAME
  is '组织简称';
comment on column SYS_ORGAN.LONGITUDE
  is '经度';
comment on column SYS_ORGAN.LATITUDE
  is '纬度';
alter table SYS_ORGAN
  add constraint PRIMARYSYS_ORGAN1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_ORGAN_ROLE...
create table SYS_ORGAN_ROLE
(
  ORGAN_ID VARCHAR2(32),
  ROLE_ID  VARCHAR2(32)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SYS_ORGAN_ROLE
  add constraint PRIMARYSYS_ORGAN_ROLE1 primary key (ORGAN_ID, ROLE_ID)
  disable;

prompt Creating SYS_OSS...
create table SYS_OSS
(
  ID          NUMBER not null,
  URL         VARCHAR2(200),
  CREATE_DATE DATE
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255;
alter table SYS_OSS
  add constraint PRIMARYSYS_OSS1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255;

prompt Creating SYS_ROLE...
create table SYS_ROLE
(
  ID          VARCHAR2(32) not null,
  BAPID       VARCHAR2(32),
  NAME        VARCHAR2(128),
  CODE        VARCHAR2(64),
  STATUS      VARCHAR2(6),
  ROLE_TYPE   VARCHAR2(6),
  REMARK      VARCHAR2(255),
  UPDATE_TIME DATE,
  CREATE_TIME DATE,
  UPDATE_ID   VARCHAR2(32),
  CREATE_ID   VARCHAR2(32),
  BAID        VARCHAR2(255)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SYS_ROLE
  add constraint PRIMARYSYS_ROLE1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_ROLE_MENU...
create table SYS_ROLE_MENU
(
  ROLE_ID VARCHAR2(32),
  MENU_ID VARCHAR2(32)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create unique index PRIMARYSYS_ROLE_MENU1 on SYS_ROLE_MENU (MENU_ID, ROLE_ID)
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_USER...
create table SYS_USER
(
  ID          VARCHAR2(32) not null,
  BAPID       VARCHAR2(32),
  USER_NAME   VARCHAR2(64),
  LOGIN_NAME  VARCHAR2(64),
  PASS_WORD   VARCHAR2(128),
  CREATE_TIME DATE,
  UPDATE_TIME DATE,
  STATUS      VARCHAR2(6),
  PHONE       VARCHAR2(64),
  PHOTO       VARCHAR2(255),
  EMAIL       VARCHAR2(128),
  CREATE_ID   VARCHAR2(32),
  UPDATE_ID   VARCHAR2(32),
  REMARK      VARCHAR2(255),
  BAID        VARCHAR2(32),
  SALT        VARCHAR2(64),
  SORT        VARCHAR2(6),
  AD_CODE     VARCHAR2(100)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SYS_USER.ID
  is '主键';
comment on column SYS_USER.BAPID
  is '机构ID';
comment on column SYS_USER.USER_NAME
  is '用户名';
comment on column SYS_USER.LOGIN_NAME
  is '登录名';
comment on column SYS_USER.PASS_WORD
  is '密码';
comment on column SYS_USER.CREATE_TIME
  is '创建时间';
comment on column SYS_USER.UPDATE_TIME
  is '更新时间';
comment on column SYS_USER.STATUS
  is '状态';
comment on column SYS_USER.PHONE
  is '手机号';
comment on column SYS_USER.PHOTO
  is '图片';
comment on column SYS_USER.EMAIL
  is '邮箱';
comment on column SYS_USER.CREATE_ID
  is '创建人';
comment on column SYS_USER.UPDATE_ID
  is '修改人';
comment on column SYS_USER.REMARK
  is '备注';
comment on column SYS_USER.BAID
  is '部门ID';
comment on column SYS_USER.SALT
  is '盐加密';
comment on column SYS_USER.AD_CODE
  is 'AD域账号';
alter table SYS_USER
  add constraint PRIMARYSYS_USER1 primary key (ID)
  using index 
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_USER_ROLE...
create table SYS_USER_ROLE
(
  USER_ID VARCHAR2(32),
  ROLE_ID VARCHAR2(32)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create unique index PRIMARYSYS_USER_ROLE1 on SYS_USER_ROLE (ROLE_ID, USER_ID)
  tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating SYS_USER_TOKEN...
create table SYS_USER_TOKEN
(
  ID      VARCHAR2(32),
  USER_ID VARCHAR2(32),
  TOKEN   VARCHAR2(64)
)
tablespace COFCO_FRAME_DATA
  pctfree 10
  initrans 1
  maxtrans 255;

prompt Disabling triggers for SYS_CODE...
alter table SYS_CODE disable all triggers;
prompt Disabling triggers for SYS_CONFIG...
alter table SYS_CONFIG disable all triggers;
prompt Disabling triggers for SYS_LEND...
alter table SYS_LEND disable all triggers;
prompt Disabling triggers for SYS_MENU...
alter table SYS_MENU disable all triggers;
prompt Disabling triggers for SYS_NOTICE...
alter table SYS_NOTICE disable all triggers;
prompt Disabling triggers for SYS_NOTICE_USER...
alter table SYS_NOTICE_USER disable all triggers;
prompt Disabling triggers for SYS_ORGAN...
alter table SYS_ORGAN disable all triggers;
prompt Disabling triggers for SYS_ORGAN_ROLE...
alter table SYS_ORGAN_ROLE disable all triggers;
prompt Disabling triggers for SYS_OSS...
alter table SYS_OSS disable all triggers;
prompt Disabling triggers for SYS_ROLE...
alter table SYS_ROLE disable all triggers;
prompt Disabling triggers for SYS_ROLE_MENU...
alter table SYS_ROLE_MENU disable all triggers;
prompt Disabling triggers for SYS_USER...
alter table SYS_USER disable all triggers;
prompt Disabling triggers for SYS_USER_ROLE...
alter table SYS_USER_ROLE disable all triggers;
prompt Disabling triggers for SYS_USER_TOKEN...
alter table SYS_USER_TOKEN disable all triggers;
prompt Loading SYS_CODE...
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('313', 'PRODUCT_TYPE', '产品类型', null, '1', 'adffbaacc25543129abc86352ecf7b52', '13', '产品类型：区分虚拟/实际，虚拟为业务平台虚构，实际为OneNET平台实际的产品信息', '-1', to_date('18-01-2019 15:20:04', 'dd-mm-yyyy hh24:mi:ss'), '-1', to_date('18-01-2019 15:21:13', 'dd-mm-yyyy hh24:mi:ss'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('315', 'PRODUCT_TYPE_001', '虚拟', 'PRODUCT_001', '2', '313', '13', '业务平台虚拟化的产品信息', '-1', to_date('18-01-2019 15:23:45', 'dd-mm-yyyy hh24:mi:ss'), '-1', to_date('18-01-2019 15:26:45', 'dd-mm-yyyy hh24:mi:ss'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('316', 'PRODUCT_TYPE_002', '实际', 'PRODUCT_002', '2', '313', '13', 'OneNET实际产品信息', '-1', to_date('18-01-2019 15:26:39', 'dd-mm-yyyy hh24:mi:ss'), '-1', to_date('18-01-2019 15:21:13', 'dd-mm-yyyy hh24:mi:ss'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('107d1d36e7444f8cb1a3320de7f1ed02', 'YES_NO_1', '否', '1', '2', '2b0281e730ac4923a1f02b4cea3ec03c', '2', '否', '026a564bbfd84861ac4b65393644beef', to_date('19-07-2017', 'dd-mm-yyyy'), null, null, '0');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('10e83c28de58444eaf2cb8135225bcf9', 'artcle_type_1', '小说', '1', '2', 'a5263b007feb4d05881b1ddc7220de10', '1', '小说', '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('27-10-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('1ba2cfd026c14174b6a25a17b8c00cc1', 'artcle_type_4', '互联技术', '4', '2', 'a5263b007feb4d05881b1ddc7220de10', '4', '互联技术', '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('02-11-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('04e15aa31a1049f6a153601fa02ee719', 'artcle_type_2', '散文', '2', '2', 'a5263b007feb4d05881b1ddc7220de10', '2', '散文', '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('27-10-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('2b0281e730ac4923a1f02b4cea3ec03c', 'YES_NO', '是否', null, '1', 'c5e12bdfd1cf4789981406323924854a', '1', null, '026a564bbfd84861ac4b65393644beef', to_date('19-07-2017', 'dd-mm-yyyy'), null, null, '0');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('4df5534dc25f43f5a0d3ae2c2baf9325', '初始化', '字典管理树', null, '1', '0', '1', '字典根据结点', '026a564bbfd84861ac4b65393644beef', to_date('19-07-2017', 'dd-mm-yyyy'), null, null, 'true');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('4f9ceb9c07394193815ef027a285b186', 'artcle_type_3', '诗歌', '3', '2', 'a5263b007feb4d05881b1ddc7220de10', '3', '诗歌', '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('68eaec38d48b40d6b7174c56d1d51374', 'is_open_false', '否', 'false', '2', 'cd9212fa11174570a2714d47658e9548', '2', '是', '026a564bbfd84861ac4b65393644beef', to_date('26-07-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('26-07-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('8baeff44925245ec91650d38dd728c35', 'artcle_type_5', '其它', '5', '2', 'a5263b007feb4d05881b1ddc7220de10', '5', '其它', '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('a33c03f863674b49b1a40dd8a56a554c', 'is_open_true', '是', 'true', '2', 'cd9212fa11174570a2714d47658e9548', '2', '是', '026a564bbfd84861ac4b65393644beef', to_date('26-07-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('26-07-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('a5263b007feb4d05881b1ddc7220de10', 'artcle_type', '文章类型', null, '1', 'adffbaacc25543129abc86352ecf7b52', '3', '文章管理', '026a564bbfd84861ac4b65393644beef', to_date('26-10-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('27-10-2017', 'dd-mm-yyyy'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('adffbaacc25543129abc86352ecf7b52', 'sys', '系统码', null, '1', '4df5534dc25f43f5a0d3ae2c2baf9325', '1', '系统码', '026a564bbfd84861ac4b65393644beef', to_date('19-07-2017', 'dd-mm-yyyy'), '026a564bbfd84861ac4b65393644beef', to_date('27-04-2018 13:59:51', 'dd-mm-yyyy hh24:mi:ss'), 'true');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('aecdbfbb7b0749f0b882ff5c06ad3753', 'YES_NO_0', '是', '0', '2', '2b0281e730ac4923a1f02b4cea3ec03c', '0', '是', '026a564bbfd84861ac4b65393644beef', to_date('19-07-2017', 'dd-mm-yyyy'), null, null, '0');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('c5e12bdfd1cf4789981406323924854a', 'public', '公用码', null, '1', '4df5534dc25f43f5a0d3ae2c2baf9325', '2', '公用码', '026a564bbfd84861ac4b65393644beef', to_date('19-07-2017', 'dd-mm-yyyy'), null, null, '0');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('cd9212fa11174570a2714d47658e9548', 'is_open', '是否打开', null, '1', 'c5e12bdfd1cf4789981406323924854a', '2', null, '026a564bbfd84861ac4b65393644beef', to_date('26-07-2017', 'dd-mm-yyyy'), null, null, 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('121', 'NOTICE_TYPE', '消息类型', null, '1', 'adffbaacc25543129abc86352ecf7b52', '1', null, '79f849b443c34cc790ee6c33deed763d', to_date('10-05-2018 13:14:08', 'dd-mm-yyyy hh24:mi:ss'), '79f849b443c34cc790ee6c33deed763d', to_date('10-05-2018 13:20:15', 'dd-mm-yyyy hh24:mi:ss'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('122', 'NOTICE_TYPE_001', '系统消息', '1', '2', '121', '1', null, '79f849b443c34cc790ee6c33deed763d', to_date('10-05-2018 13:14:38', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', to_date('27-04-2018 13:59:51', 'dd-mm-yyyy hh24:mi:ss'), 'true');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('123', 'NOTICE_TYPE_002', '报警消息', '2', '2', '121', '1', null, '79f849b443c34cc790ee6c33deed763d', to_date('10-05-2018 13:15:24', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', to_date('27-04-2018 13:59:51', 'dd-mm-yyyy hh24:mi:ss'), 'true');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('102', 'OPPR_TYPE', '操作类型', null, '1', 'adffbaacc25543129abc86352ecf7b52', '1', null, '79f849b443c34cc790ee6c33deed763d', to_date('07-05-2018 17:09:14', 'dd-mm-yyyy hh24:mi:ss'), '79f849b443c34cc790ee6c33deed763d', to_date('07-05-2018 17:16:29', 'dd-mm-yyyy hh24:mi:ss'), 'false');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('103', 'OPPR_TYPE_001', '开', '1', '2', '102', '1', null, '79f849b443c34cc790ee6c33deed763d', to_date('07-05-2018 17:10:43', 'dd-mm-yyyy hh24:mi:ss'), '79f849b443c34cc790ee6c33deed763d', to_date('07-05-2018 17:15:12', 'dd-mm-yyyy hh24:mi:ss'), 'true');
insert into SYS_CODE (ID, MARK, NAME, VALUE, TYPE, PARENT_ID, SORT, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, OPEN)
values ('104', 'OPPR_TYPE_002', '关', '2', '2', '102', '1', null, '79f849b443c34cc790ee6c33deed763d', to_date('07-05-2018 17:10:54', 'dd-mm-yyyy hh24:mi:ss'), '79f849b443c34cc790ee6c33deed763d', to_date('07-05-2018 17:15:18', 'dd-mm-yyyy hh24:mi:ss'), 'true');
commit;
prompt 24 records loaded
prompt Loading SYS_CONFIG...
prompt Table is empty
prompt Loading SYS_LEND...
insert into SYS_LEND (ID, USER_ID, OLD_ORGAN_ID, NEW_ORGAN_ID, LEND_TIME, STATUS, CREATE_TIME, CREATE_ID)
values ('028d698f00794743a1e6745dfe371834', 'a94d75557cb241e19bf31efbbbb3d09d', 'ab4a7b79db534a9eae4f9d4747b37720', '64ca824f812e47068717308da7cadbdc', to_date('11-02-2019 14:36:36', 'dd-mm-yyyy hh24:mi:ss'), '1', to_date('11-02-2019 14:36:36', 'dd-mm-yyyy hh24:mi:ss'), '-1');
insert into SYS_LEND (ID, USER_ID, OLD_ORGAN_ID, NEW_ORGAN_ID, LEND_TIME, STATUS, CREATE_TIME, CREATE_ID)
values ('bbd5a8a138fe4fa182c1f0329a6925ab', 'a94d75557cb241e19bf31efbbbb3d09d', 'ab4a7b79db534a9eae4f9d4747b37720', '64ca824f812e47068717308da7cadbdc', to_date('16-02-2019 11:25:59', 'dd-mm-yyyy hh24:mi:ss'), '1', to_date('16-02-2019 11:25:59', 'dd-mm-yyyy hh24:mi:ss'), 'a94d75557cb241e19bf31efbbbb3d09d');
insert into SYS_LEND (ID, USER_ID, OLD_ORGAN_ID, NEW_ORGAN_ID, LEND_TIME, STATUS, CREATE_TIME, CREATE_ID)
values ('6688a55df78843b7ba95e52b7b90248b', 'a94d75557cb241e19bf31efbbbb3d09d', 'ab4a7b79db534a9eae4f9d4747b37720', '64ca824f812e47068717308da7cadbdc', to_date('01-03-2019 15:28:28', 'dd-mm-yyyy hh24:mi:ss'), '0', to_date('01-03-2019 15:28:28', 'dd-mm-yyyy hh24:mi:ss'), '-1');
commit;
prompt 3 records loaded
prompt Loading SYS_MENU...
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('a947488215f14eeabf94811bf1de1452', '1', '0', '系统设置', 'sys/init.html', 'fa fa-cog', 1, '1', null, null, to_date('15-02-2019 17:20:59', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('18-02-2019', 'dd-mm-yyyy'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('9e8dc02fe5614580bdf5f6ca9a852b70', 'feb235067fd7400090b0aa5451e4a5a4', null, '系统角色', 'role/role.html', 'fa fa-rouble', 2, '1', 'sys:role:list', null, to_date('12-05-2017 09:31:31', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '79f849b443c34cc790ee6c33deed763d', to_date('03-05-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('0', null, null, '系统根目录', null, null, 2, null, null, '2017/8/29', to_date('23-04-2018', 'dd-mm-yyyy'), 'TRUE', null, to_date('23-04-2018', 'dd-mm-yyyy'), null, null, null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('c898d73ff1fb49dc89909ed13452103a', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '保存/更新', null, null, 0, '1', 'sys:role:update', null, to_date('18-05-2017 20:37:18', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 22:10:19', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('ce4a2b7afad24616abd283f4741fe3e0', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '查看信息', null, null, null, '1', 'sys:role:info', null, to_date('18-05-2017 20:34:07', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 22:10:47', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('0c410538dff54d35a1e19c7524c20e47', '1eabb55ae2fa4f5e890c7739c1678e44', '0', '删除', null, null, 17, '1', 'sys:config:delete', null, to_date('29-08-2017 22:03:44', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 11:36:06', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('1', '0', '0', '系统管理', '23', 'fa fa-cog', 1, '1', '32', null, null, null, '026a564bbfd84861ac4b65393644beef', to_date('12-10-2017 10:20:01', 'dd-mm-yyyy hh24:mi:ss'), '0', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('14c58e44d6754c92806e1b97614c49b9', 'b7ab517a15b74dea812ee7ef32847a48', '0', '所有权限', null, null, 1, '1', 'sys:organ:all', null, to_date('29-08-2017 22:11:25', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('16-08-2017 11:30:16', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('1515daa136ba41f783e82318f851d343', '1', '0', '字典管理', 'code/code.html', 'fa fa-bell-o', 6, '1', 'sys:code:codeTree', null, to_date('14-07-2017 15:14:04', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('26-09-2017 11:45:35', 'dd-mm-yyyy hh24:mi:ss'), '1', null, null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('15d2074a502f443cb760f613a40df598', '8b1f46f8ba6e455790a515c32e0329c5', null, '保存/更新', null, null, 0, '1', 'sys:user:update', null, to_date('18-05-2017 19:31:05', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 22:08:39', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('eaf376f94d744775b874a0f34a9d5022', '8ae83f604d394671ae78d763c3f41ded', null, '报警信息', 'message/message.html', 'fa fa-camera', 6, '1', null, '2017/8/29', to_date('09-05-2018 11:18:09', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('02-05-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('1eabb55ae2fa4f5e890c7739c1678e44', '1', '0', '系统参数', 'config/config.html', 'fa fa-cog', 17, '1', 'sys:config:list', null, to_date('29-06-2017 11:33:47', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 11:36:06', 'dd-mm-yyyy hh24:mi:ss'), '1', null, null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('1f936af47d064ab4989aadf6373e6502', '1515daa136ba41f783e82318f851d343', '0', '保存/更新', null, null, 6, '1', 'sys:code:update', null, to_date('24-07-2017 09:46:51', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 21:54:24', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('2', '1', '1', '菜单', 'menu/menu.html', 'fa fa-bullhorn', 12, '1', 'sys:menu:list', null, null, null, '026a564bbfd84861ac4b65393644beef', to_date('26-09-2017 10:40:07', 'dd-mm-yyyy hh24:mi:ss'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('239474f2afbf4196a6fc755c88be008d', '1', '0', 'sql监控', 'druid/sql.html', 'fa fa-tachometer', 8, '1', null, null, to_date('11-10-2017 21:07:45', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('11-10-2017 21:24:21', 'dd-mm-yyyy hh24:mi:ss'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('39cb0308a7fc4f7fb6c49440224ac2a4', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '启用', null, null, 0, '1', 'sys:role:enabled', null, to_date('15-09-2017 14:11:24', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 17:32:59', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('4', '2', '2', '保存/更新', null, null, null, '1', 'sys:menu:update', null, null, null, '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 22:04:39', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('4e053e35bfed492fa6248e4888addd67', '1515daa136ba41f783e82318f851d343', '0', '查看信息', null, null, 6, '1', 'sys:code:info', null, to_date('24-07-2017 09:46:36', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 17:32:36', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('6', '2', '2', '删除', null, null, null, '1', 'sys:menu:delete', null, null, null, '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 22:05:01', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('62bdcab5fe5f4e5fa7ab79fcd8cb47c4', '8b1f46f8ba6e455790a515c32e0329c5', null, '禁用', null, null, 0, '1', 'sys:user:delete', null, to_date('18-05-2017 19:31:29', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('15-09-2017 14:29:22', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('695388220b704cdaa72539c5f82fb254', '1eabb55ae2fa4f5e890c7739c1678e44', '0', '保存/更新', null, null, 17, '1', 'sys:config:update', null, to_date('29-08-2017 22:03:32', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 11:36:06', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('7', '2', '2', '查看信息', null, null, null, null, 'sys:menu:info', null, null, null, '026a564bbfd84861ac4b65393644beef', to_date('29-08-2017 22:05:10', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('7cec2d46083c47bba323436cb409693f', '8b1f46f8ba6e455790a515c32e0329c5', null, '启用', null, null, 1, '1', 'sys:user:enabled', null, to_date('15-09-2017 12:02:40', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('15-09-2017 14:29:55', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('80030434d10548968beaaeed79c3408b', '8b1f46f8ba6e455790a515c32e0329c5', null, '查看信息', null, null, 1, '1', 'sys:user:info', null, to_date('29-08-2017 22:09:38', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 17:33:35', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('8ae83f604d394671ae78d763c3f41ded', '0', null, '消息管理', null, 'fa fa-bullhorn', 6, '1', null, null, to_date('01-09-2017 11:35:46', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '0', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('8b1f46f8ba6e455790a515c32e0329c5', 'feb235067fd7400090b0aa5451e4a5a4', null, '系统用户', 'user/user.html', 'fa fa-ruble', 3, '1', 'sys:user:list', null, to_date('10-05-2017 14:01:31', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '1', null, null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('90ca98befe9f4a67a07d5ab5fb2f3de3', '1515daa136ba41f783e82318f851d343', '0', '删除', null, null, 6, '1', 'sys:code:delete', null, to_date('24-07-2017 09:47:19', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 17:32:36', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('9d22510a1b1a40459619c5b2fdddfdc2', '9e8dc02fe5614580bdf5f6ca9a852b70', null, '禁用', null, null, 0, '1', 'sys:role:delete', null, to_date('18-05-2017 21:35:21', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('15-09-2017 14:29:28', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('fbfcbfa4c48f4ddcaebe1eaa64c17989', '768734cb390e4fc29bfcc31575a628f0', null, '部署记录', null, null, 3, '1', 'sys:locationInformation:info', '2017/8/29', to_date('23-05-2018 14:38:15', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('23-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('fdd70e08ae994de18009bc95f4f51fff', '1', '0', '系统日志', 'log/log.html', 'fa fa-fighter-jet', 30, '1', 'sys:log:list', null, to_date('29-06-2017 21:47:49', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('26-09-2017 11:50:23', 'dd-mm-yyyy hh24:mi:ss'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('10', '0', '0', '接口管理', '234', 'fa fa-sun-o', 10, '1', '32', null, null, null, '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '0', 'false', null, null, '2');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('feb235067fd7400090b0aa5451e4a5a4', '0', null, '组织管理', null, 'fa fa-cogs', 2, '1', null, null, to_date('29-06-2017 17:31:31', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '0', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('2eceb159c96944439857b0f6a186809b', '8ae83f604d394671ae78d763c3f41ded', null, '消息级别设置', 'message/setMessage.html', 'fa fa-camera', 7, '1', null, '2017/8/29', to_date('14-06-2018 09:22:36', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('14-06-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('b7ab517a15b74dea812ee7ef32847a48', 'feb235067fd7400090b0aa5451e4a5a4', '0', '组织机构', 'organ/organ.html', 'fa fa-cog', 1, '1', 'sys:organ:all', null, to_date('19-07-2017 19:42:23', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '1', null, null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('bb5bc94a71a546aba5d9f813b6a352bb', '8b1f46f8ba6e455790a515c32e0329c5', null, '重置密码', null, null, 1, '1', 'sys:user:reset', null, to_date('15-09-2017 12:23:29', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('15-09-2017 14:31:32', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('5ed919cc120c4c608dc6389ce7dae004', 'feb235067fd7400090b0aa5451e4a5a4', null, '角色用户分配', 'role/roleAssignment.html', 'fa fa-pagelines', 4, '1', 'sys:roleAssignment:list', null, to_date('26-04-2018 09:08:06', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('316eabd9f3f441698e36300ba52bfeb7', '5ed919cc120c4c608dc6389ce7dae004', null, '角色用户授权', null, null, 2, '1', 'sys:roleAssignment:update', null, to_date('26-04-2018 09:52:53', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('28-04-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('818a0f9cbd2a4109a5d8dbdb01421a71', '1eabb55ae2fa4f5e890c7739c1678e44', '0', '查看', null, null, 17, '1', 'sys:config:info', null, to_date('02-05-2018 19:39:55', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('29-06-2017 11:36:06', 'dd-mm-yyyy hh24:mi:ss'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('88e9f56955514c64971981cbacbd30b5', '992ac87906e448e4862fc90a3f2f608f', null, '借调', null, null, 6, '1', 'sys:lend:toloan', null, to_date('04-05-2018 09:13:47', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('04-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('f49285e506704d4caec1b89b9924fa0a', '992ac87906e448e4862fc90a3f2f608f', null, '取消借调', null, null, 6, '1', 'sys:lend:relieve', null, to_date('04-05-2018 09:14:15', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('04-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('220de63071bf4c8083a5f9914433d0cc', 'e700f061dae448e58cd81a68e17b3439', '0', '开锁流程', 'demo/lockopen/list', 'fa fa-archive', 4, '1', 'act:model:all', null, to_date('15-05-2018 13:46:51', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('15-05-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '2');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('82b65f5d32044e04a8b1c679d1e59a9c', 'e700f061dae448e58cd81a68e17b3439', '0', '位置修改流程', 'demo/locationmodify/list', 'fa fa-archive', 4, '1', 'act:model:all', null, to_date('16-05-2018 10:20:10', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('16-05-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '2');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('58b182629a254ff9993c5a430207f549', '3033a8b9f63241c88caa8de30b6fd0e4', null, '回收', null, null, 5, '1', 'sys:locationfacilities:update', '2017/8/29', to_date('17-05-2018 14:20:34', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('16-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('07eacdb8da674dc6b284bef8ab16f301', '9aba4bdf8aa04b96ac927c3093e3a774', null, '用户角色解除', null, null, 4, '1', 'sys:userGrantRoles:relieve', null, to_date('27-04-2018 11:44:15', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('26-04-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('87dccbb444e04286803d6de080c3994c', 'c7da551821ed41b693a124d24a98a08e', null, '保存/更新', null, null, 2, '1', 'sys:equipment:update', '2017/8/29', to_date('28-04-2018 09:53:31', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('02-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('224e2cfa3655468b9e9d43c70db9d6f9', 'c7da551821ed41b693a124d24a98a08e', null, '禁用', null, null, 2, '1', 'sys:equipment:disable', '2017/8/29', to_date('28-04-2018 09:53:58', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('02-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('3a802d764a634f05bc4e428bd1ba6b83', 'c7da551821ed41b693a124d24a98a08e', null, '查看', null, null, 2, '1', 'sys:equipment:info', '2017/8/29', to_date('28-04-2018 09:54:51', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', null, to_date('23-04-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('923c26d02557478d9673683784cdd936', 'c7da551821ed41b693a124d24a98a08e', null, '启用', null, null, 2, '1', 'sys:equipment:enabled', '2017/8/29', to_date('28-04-2018 09:55:36', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', null, to_date('23-04-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('da9cbc83abee43f8aa7121ea5ac3a8e5', '5ed919cc120c4c608dc6389ce7dae004', null, '角色用户解除', null, null, 2, '1', 'sys:roleAssignment:update', null, to_date('28-04-2018 09:57:45', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('28-04-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('ac59dbcc07ad4b3cb7326d95feb716b7', 'c7da551821ed41b693a124d24a98a08e', null, 'Excel导入', null, null, 2, '1', 'sys:equipment:eImport', '2017/8/29', to_date('02-05-2018 10:52:07', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', null, to_date('23-04-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('f82568654dbb4b0885a9b1c621bfcd42', 'bc52f2ae33f64d14b63d93c695406c00', null, '所有权限', null, null, 2, '1', 'sys:lock:all', null, to_date('03-05-2018 09:35:38', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('03-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('d7d3c966857b40488079cb195ce2f563', '768734cb390e4fc29bfcc31575a628f0', null, '开关锁记录', null, null, 2, '1', 'sys:locationInformation:info', '2017/8/29', to_date('02-05-2018 14:05:55', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('23-05-2018', 'dd-mm-yyyy'), '2', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('992ac87906e448e4862fc90a3f2f608f', 'feb235067fd7400090b0aa5451e4a5a4', null, '借调授权', 'organ/Lend.html', 'fa fa-try', 6, '1', 'sys:lend:list', null, to_date('02-05-2018 15:05:28', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '1');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('ada02522ef7a44cea692541b358e8d1f', '10', null, 'swagger接口文档', 'http://47.94.150.129:8080/iotgms/swagger-ui.html', 'fa fa-mail-forward', null, '1', null, null, to_date('20-10-2017 17:57:47', 'dd-mm-yyyy hh24:mi:ss'), '026a564bbfd84861ac4b65393644beef', '026a564bbfd84861ac4b65393644beef', to_date('20-10-2017 18:05:12', 'dd-mm-yyyy hh24:mi:ss'), '1', 'true', null, null, '2');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('381fcdff34ba4aa49f1012355b26f7f3', '10', null, '代码生成', 'generator/generator.html', 'fa fa-bullhorn', 5, '1', 'sys:generator:list', null, to_date('25-05-2018 11:19:07', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('25-05-2018', 'dd-mm-yyyy'), '1', 'false', null, null, '2');
insert into SYS_MENU (ID, PARENT_ID, PARENT_IDS, NAME, URL, ICON, SORT, STATUS, PERMISSION, REMARK, CREATE_TIME, CREATE_ID, UPDATE_ID, UPDATE_TIME, TYPE, OPEN, BAPID, BAID, SIGN)
values ('38c12f4dfa2e49a1b4ac8b0048e2effe', '39d9dd83a1cf4ea59a5571c7aa15927e', '0', '锁具操作日志', 'equip/tblockrecord.html', 'fa fa-archive', 7, '1', null, null, to_date('26-06-2018 13:18:19', 'dd-mm-yyyy hh24:mi:ss'), '-1', '-1', to_date('20-06-2018', 'dd-mm-yyyy'), '1', null, null, null, '2');
commit;
prompt 56 records loaded
prompt Loading SYS_NOTICE...
prompt Table is empty
prompt Loading SYS_NOTICE_USER...
prompt Table is empty
prompt Loading SYS_ORGAN...
insert into SYS_ORGAN (ID, BAPID, TYPE, CODE, NAME, PARENT_ID, IS_DEL, SYSMARK, SORT, OPEN, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, SIMPLIFY_NAME, LONGITUDE, LATITUDE)
values ('6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', 'ab4a7b79db534a9eae4f9d4747b37720', '1', '000001@000001@000001', '中粮大数据公司715', 'ab4a7b79db534a9eae4f9d4747b37720', '0', null, '1', '1', '中粮大数据715测试机构', '-1', to_date('07-03-2019 14:38:01', 'dd-mm-yyyy hh24:mi:ss'), 'a94d75557cb241e19bf31efbbbb3d09d', to_date('26-01-2019 14:51:51', 'dd-mm-yyyy hh24:mi:ss'), '中粮715', 39.931078, 116.4242);
insert into SYS_ORGAN (ID, BAPID, TYPE, CODE, NAME, PARENT_ID, IS_DEL, SYSMARK, SORT, OPEN, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, SIMPLIFY_NAME, LONGITUDE, LATITUDE)
values ('132fb0e4f56545499bbceaea276a743d', '0', '1', '000001', '中粮贸易', '0', '0', null, '1', '1', '中粮贸易', '-1', to_date('12-05-2018 16:21:07', 'dd-mm-yyyy hh24:mi:ss'), '-1', to_date('29-05-2018 18:26:35', 'dd-mm-yyyy hh24:mi:ss'), '中粮贸易', 39.913009, 116.437077);
insert into SYS_ORGAN (ID, BAPID, TYPE, CODE, NAME, PARENT_ID, IS_DEL, SYSMARK, SORT, OPEN, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, SIMPLIFY_NAME, LONGITUDE, LATITUDE)
values ('ab4a7b79db534a9eae4f9d4747b37720', '0', '1', '000001@000001', '大数据', '132fb0e4f56545499bbceaea276a743d', '0', null, '1', '1', '11', '-1', to_date('29-05-2018 18:38:08', 'dd-mm-yyyy hh24:mi:ss'), 'a94d75557cb241e19bf31efbbbb3d09d', to_date('26-01-2019 14:51:51', 'dd-mm-yyyy hh24:mi:ss'), '大数据', null, null);
insert into SYS_ORGAN (ID, BAPID, TYPE, CODE, NAME, PARENT_ID, IS_DEL, SYSMARK, SORT, OPEN, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, SIMPLIFY_NAME, LONGITUDE, LATITUDE)
values ('64ca824f812e47068717308da7cadbdc', '0', '1', '000001@000002', '中粮贸易辽宁公司', '132fb0e4f56545499bbceaea276a743d', '0', null, '2', '1', null, '-1', to_date('29-05-2018 18:26:54', 'dd-mm-yyyy hh24:mi:ss'), '-1', to_date('29-05-2018 18:45:02', 'dd-mm-yyyy hh24:mi:ss'), '辽宁公司', null, null);
insert into SYS_ORGAN (ID, BAPID, TYPE, CODE, NAME, PARENT_ID, IS_DEL, SYSMARK, SORT, OPEN, REMARK, CREATE_ID, CREATE_TIME, UPDATE_ID, UPDATE_TIME, SIMPLIFY_NAME, LONGITUDE, LATITUDE)
values ('8e4bee991e18417aa50032ce76742149', '64ca824f812e47068717308da7cadbdc', '1', '000001@000002@000001', '中国华粮物流集团曲家中心粮库', '64ca824f812e47068717308da7cadbdc', '0', null, '1', '1', null, '-1', to_date('29-05-2018 18:27:56', 'dd-mm-yyyy hh24:mi:ss'), '-1', to_date('29-05-2018 18:26:35', 'dd-mm-yyyy hh24:mi:ss'), '曲家库', null, null);
commit;
prompt 5 records loaded
prompt Loading SYS_ORGAN_ROLE...
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', 'b5c7a8c709fe4c5c9d0f357cc34cb19a');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('ab4a7b79db534a9eae4f9d4747b37720', 'ded49c80344a4e368628c9d93c309408');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('132fb0e4f56545499bbceaea276a743d', '8d931486611346518ec01b7b8363de76');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('132fb0e4f56545499bbceaea276a743d', '40295616fd1e43559272f95b8d78b0ee');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('abf13a33ee4c40a99147f51214fa254d', '99321f6fcd7743cba6eebadb904858bc');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('132fb0e4f56545499bbceaea276a743d', '6072dc7bf2f1477d8634478d7ca90358');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('36523ddef6834b138e13c94395013093', '2ab8934bd9b04870b2f7bb2d95ff5b89');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('64ca824f812e47068717308da7cadbdc', '63e3b62da20e4a73ae1d8d65dbb6d13f');
insert into SYS_ORGAN_ROLE (ORGAN_ID, ROLE_ID)
values ('64ca824f812e47068717308da7cadbdc', '5f34de72a924400289624d0d25cf43c1');
commit;
prompt 9 records loaded
prompt Loading SYS_OSS...
prompt Table is empty
prompt Loading SYS_ROLE...
insert into SYS_ROLE (ID, BAPID, NAME, CODE, STATUS, ROLE_TYPE, REMARK, UPDATE_TIME, CREATE_TIME, UPDATE_ID, CREATE_ID, BAID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', '715管理员', null, '0', null, null, to_date('07-03-2019', 'dd-mm-yyyy'), to_date('07-03-2019', 'dd-mm-yyyy'), null, '-1', '6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a');
insert into SYS_ROLE (ID, BAPID, NAME, CODE, STATUS, ROLE_TYPE, REMARK, UPDATE_TIME, CREATE_TIME, UPDATE_ID, CREATE_ID, BAID)
values ('8d931486611346518ec01b7b8363de76', '132fb0e4f56545499bbceaea276a743d', '工厂硬件', null, '0', null, null, null, to_date('29-03-2019 16:52:41', 'dd-mm-yyyy hh24:mi:ss'), null, '-1', 'e7c14e165fe14b6391ce5f7ab9380c30');
insert into SYS_ROLE (ID, BAPID, NAME, CODE, STATUS, ROLE_TYPE, REMARK, UPDATE_TIME, CREATE_TIME, UPDATE_ID, CREATE_ID, BAID)
values ('40295616fd1e43559272f95b8d78b0ee', '132fb0e4f56545499bbceaea276a743d', 'ZCY', null, '0', null, null, to_date('09-04-2019', 'dd-mm-yyyy'), to_date('08-04-2019', 'dd-mm-yyyy'), null, '-1', '132fb0e4f56545499bbceaea276a743d');
insert into SYS_ROLE (ID, BAPID, NAME, CODE, STATUS, ROLE_TYPE, REMARK, UPDATE_TIME, CREATE_TIME, UPDATE_ID, CREATE_ID, BAID)
values ('ce84377c2b35468fb7b0fbed937a8ffc', '7180f5a0c3624f4bb6fb758ab2c3bda6', '系统管理员', null, '0', null, '全部权限', to_date('07-05-2018', 'dd-mm-yyyy'), to_date('03-05-2018', 'dd-mm-yyyy'), null, '026a564bbfd84861ac4b65393644beef', '7180f5a0c3624f4bb6fb758ab2c3bda6');
insert into SYS_ROLE (ID, BAPID, NAME, CODE, STATUS, ROLE_TYPE, REMARK, UPDATE_TIME, CREATE_TIME, UPDATE_ID, CREATE_ID, BAID)
values ('ded49c80344a4e368628c9d93c309408', 'ab4a7b79db534a9eae4f9d4747b37720', 'dashuju', null, '0', null, null, to_date('09-04-2019', 'dd-mm-yyyy'), to_date('17-01-2019', 'dd-mm-yyyy'), null, '-1', 'ab4a7b79db534a9eae4f9d4747b37720');
insert into SYS_ROLE (ID, BAPID, NAME, CODE, STATUS, ROLE_TYPE, REMARK, UPDATE_TIME, CREATE_TIME, UPDATE_ID, CREATE_ID, BAID)
values ('6072dc7bf2f1477d8634478d7ca90358', '132fb0e4f56545499bbceaea276a743d', '超级管理', null, '0', null, '最高权限', to_date('07-03-2019', 'dd-mm-yyyy'), to_date('24-01-2019', 'dd-mm-yyyy'), null, '-1', '132fb0e4f56545499bbceaea276a743d');
commit;
prompt 6 records loaded
prompt Loading SYS_ROLE_MENU...
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '1');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'a947488215f14eeabf94811bf1de1452');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '1515daa136ba41f783e82318f851d343');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '239474f2afbf4196a6fc755c88be008d');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'bb3e9ec79c1b4aa98f8a5ad07e570c33');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '1eabb55ae2fa4f5e890c7739c1678e44');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'fdd70e08ae994de18009bc95f4f51fff');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'feb235067fd7400090b0aa5451e4a5a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'b7ab517a15b74dea812ee7ef32847a48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '9e8dc02fe5614580bdf5f6ca9a852b70');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '8b1f46f8ba6e455790a515c32e0329c5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '5ed919cc120c4c608dc6389ce7dae004');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '992ac87906e448e4862fc90a3f2f608f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '74bf4739c23b4b61a844322e66dab978');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '768734cb390e4fc29bfcc31575a628f0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '847a8efb18fe48afb9de7e6665e4fdc9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'eda7b676ab1a4798858a78aaeddfe0a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'c7da551821ed41b693a124d24a98a08e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '0e5c172895a048eab4b89a74694accd8');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'ed744cee45924a8293a88b03451b721a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'a7a53cb33fb74bbb894a419778a57ee5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '5f06d217f64c48b4bc85467436632c26');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '83b66986639f4ddb8ba51ebdc351a466');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'c37bd95374d047f7ba5b7f3c01deefe7');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'b2aee9154e394bbe83c7c3c1d59839b9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'ea35c437ec0c4ae3b2bb3e2bed516af0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '39d9dd83a1cf4ea59a5571c7aa15927e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '5481951649c54f4791cb8ec7a50e0473');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '46cc3d89708c48a0b1710446d819f8c2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '8e5630486ffc475abbdc57467c97bb48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'bc52f2ae33f64d14b63d93c695406c00');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '8ae83f604d394671ae78d763c3f41ded');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'eaf376f94d744775b874a0f34a9d5022');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '2eceb159c96944439857b0f6a186809b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'e700f061dae448e58cd81a68e17b3439');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'a73be9e0371848e18135652f632a117f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '483c45922c6e467f8efc98fdd8ab8397');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'bbe03cab3b4a45c2a4dd972c80ef4224');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'bc82a59fa9034622a66e13fd830def06');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '624b686dd7884be48f41fcb6bf86c272');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '1000001');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'c2ae55dc2f524d02ac4bb634592d7bd0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '96248c4b94644c57b136b73ad48f237f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'feb235067fd7400090b0aa5451e4a5a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'b7ab517a15b74dea812ee7ef32847a48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '9e8dc02fe5614580bdf5f6ca9a852b70');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '8b1f46f8ba6e455790a515c32e0329c5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '5ed919cc120c4c608dc6389ce7dae004');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '992ac87906e448e4862fc90a3f2f608f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '74bf4739c23b4b61a844322e66dab978');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '768734cb390e4fc29bfcc31575a628f0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '847a8efb18fe48afb9de7e6665e4fdc9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'eda7b676ab1a4798858a78aaeddfe0a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'c7da551821ed41b693a124d24a98a08e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '0e5c172895a048eab4b89a74694accd8');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'ed744cee45924a8293a88b03451b721a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'c37bd95374d047f7ba5b7f3c01deefe7');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '5f06d217f64c48b4bc85467436632c26');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '83b66986639f4ddb8ba51ebdc351a466');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'a7a53cb33fb74bbb894a419778a57ee5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'b2aee9154e394bbe83c7c3c1d59839b9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '39d9dd83a1cf4ea59a5571c7aa15927e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '5481951649c54f4791cb8ec7a50e0473');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '46cc3d89708c48a0b1710446d819f8c2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '8e5630486ffc475abbdc57467c97bb48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'bc52f2ae33f64d14b63d93c695406c00');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '8ae83f604d394671ae78d763c3f41ded');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'eaf376f94d744775b874a0f34a9d5022');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '2eceb159c96944439857b0f6a186809b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'e700f061dae448e58cd81a68e17b3439');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'a73be9e0371848e18135652f632a117f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '483c45922c6e467f8efc98fdd8ab8397');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'bbe03cab3b4a45c2a4dd972c80ef4224');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'bc82a59fa9034622a66e13fd830def06');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '624b686dd7884be48f41fcb6bf86c272');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '1000001');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'c2ae55dc2f524d02ac4bb634592d7bd0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '0249d98a0155401586e48d03cafa278a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'e607f352b8404bc0bee07d87bf8fc567');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'a161d85b900e45949c11d5c0d8e87d2c');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'f83d8a4c8da047c89d735553a90330e5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '9e3cccc83578476e86671f809e87bf71');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '8181bcd66eaf42c0ac29152676eeda03');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '8f92f294a6064f778799f28a792b3f75');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '2579b41fe17049f9b48fdc16490d19de');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', '1b4c5ab5df8b46309ecb388974a35837');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'b93ec9fc73ad40cc8701fcfc14349e78');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'eca654d268644fce81a9c9309a9eeff4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'bfef399eeab9423bb3b222d4fa38e345');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('b5c7a8c709fe4c5c9d0f357cc34cb19a', 'adfdcdd23b0540b683192cd0454b069b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', '9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', 'c7da551821ed41b693a124d24a98a08e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', '0e5c172895a048eab4b89a74694accd8');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', 'ed744cee45924a8293a88b03451b721a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', 'c37bd95374d047f7ba5b7f3c01deefe7');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', '5f06d217f64c48b4bc85467436632c26');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', '83b66986639f4ddb8ba51ebdc351a466');
commit;
prompt 100 records committed...
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', 'a7a53cb33fb74bbb894a419778a57ee5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('8d931486611346518ec01b7b8363de76', 'b2aee9154e394bbe83c7c3c1d59839b9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '1');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'a947488215f14eeabf94811bf1de1452');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '1515daa136ba41f783e82318f851d343');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '239474f2afbf4196a6fc755c88be008d');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '1eabb55ae2fa4f5e890c7739c1678e44');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'fdd70e08ae994de18009bc95f4f51fff');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'feb235067fd7400090b0aa5451e4a5a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'b7ab517a15b74dea812ee7ef32847a48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '9e8dc02fe5614580bdf5f6ca9a852b70');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '8b1f46f8ba6e455790a515c32e0329c5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '5ed919cc120c4c608dc6389ce7dae004');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '992ac87906e448e4862fc90a3f2f608f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '74bf4739c23b4b61a844322e66dab978');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '768734cb390e4fc29bfcc31575a628f0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '847a8efb18fe48afb9de7e6665e4fdc9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'eda7b676ab1a4798858a78aaeddfe0a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'c7da551821ed41b693a124d24a98a08e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '0e5c172895a048eab4b89a74694accd8');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'ed744cee45924a8293a88b03451b721a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'c37bd95374d047f7ba5b7f3c01deefe7');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '5f06d217f64c48b4bc85467436632c26');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '83b66986639f4ddb8ba51ebdc351a466');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'a7a53cb33fb74bbb894a419778a57ee5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'b2aee9154e394bbe83c7c3c1d59839b9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '39d9dd83a1cf4ea59a5571c7aa15927e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '5481951649c54f4791cb8ec7a50e0473');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '46cc3d89708c48a0b1710446d819f8c2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '8e5630486ffc475abbdc57467c97bb48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'bc52f2ae33f64d14b63d93c695406c00');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '8ae83f604d394671ae78d763c3f41ded');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'eaf376f94d744775b874a0f34a9d5022');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '2eceb159c96944439857b0f6a186809b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '1000001');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'c2ae55dc2f524d02ac4bb634592d7bd0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '0249d98a0155401586e48d03cafa278a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'e607f352b8404bc0bee07d87bf8fc567');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'a161d85b900e45949c11d5c0d8e87d2c');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'f83d8a4c8da047c89d735553a90330e5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '9e3cccc83578476e86671f809e87bf71');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '8181bcd66eaf42c0ac29152676eeda03');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '8f92f294a6064f778799f28a792b3f75');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '2579b41fe17049f9b48fdc16490d19de');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', '1b4c5ab5df8b46309ecb388974a35837');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'b93ec9fc73ad40cc8701fcfc14349e78');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'eca654d268644fce81a9c9309a9eeff4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'bfef399eeab9423bb3b222d4fa38e345');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('6072dc7bf2f1477d8634478d7ca90358', 'adfdcdd23b0540b683192cd0454b069b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '468744d5abac4d9a97c7815ec490958b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '2e2d97e3e81f4cf1839c5421deacb3e5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '5a89703112924828a8d60a9c92b41f1d');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '3ef413c2d20147d4aae334e305265100');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '88c8a93c8e8a4cd7b616e3676883414f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '0249d98a0155401586e48d03cafa278a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'e607f352b8404bc0bee07d87bf8fc567');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'a161d85b900e45949c11d5c0d8e87d2c');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'ca8cb1c550c14c4b8f66aebdb85b2b50');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'f83d8a4c8da047c89d735553a90330e5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '9e3cccc83578476e86671f809e87bf71');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '8181bcd66eaf42c0ac29152676eeda03');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '8f92f294a6064f778799f28a792b3f75');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '2579b41fe17049f9b48fdc16490d19de');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', '1b4c5ab5df8b46309ecb388974a35837');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'b93ec9fc73ad40cc8701fcfc14349e78');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'eca654d268644fce81a9c9309a9eeff4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'bfef399eeab9423bb3b222d4fa38e345');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('ded49c80344a4e368628c9d93c309408', 'adfdcdd23b0540b683192cd0454b069b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '1');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'a947488215f14eeabf94811bf1de1452');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '1515daa136ba41f783e82318f851d343');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '239474f2afbf4196a6fc755c88be008d');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'bb3e9ec79c1b4aa98f8a5ad07e570c33');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '1eabb55ae2fa4f5e890c7739c1678e44');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'fdd70e08ae994de18009bc95f4f51fff');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'feb235067fd7400090b0aa5451e4a5a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'b7ab517a15b74dea812ee7ef32847a48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '9e8dc02fe5614580bdf5f6ca9a852b70');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '8b1f46f8ba6e455790a515c32e0329c5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '5ed919cc120c4c608dc6389ce7dae004');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '992ac87906e448e4862fc90a3f2f608f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '74bf4739c23b4b61a844322e66dab978');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '768734cb390e4fc29bfcc31575a628f0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '847a8efb18fe48afb9de7e6665e4fdc9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'eda7b676ab1a4798858a78aaeddfe0a4');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'c7da551821ed41b693a124d24a98a08e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '0e5c172895a048eab4b89a74694accd8');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'ed744cee45924a8293a88b03451b721a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'a7a53cb33fb74bbb894a419778a57ee5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '5f06d217f64c48b4bc85467436632c26');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '83b66986639f4ddb8ba51ebdc351a466');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'c37bd95374d047f7ba5b7f3c01deefe7');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'b2aee9154e394bbe83c7c3c1d59839b9');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'ea35c437ec0c4ae3b2bb3e2bed516af0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '39d9dd83a1cf4ea59a5571c7aa15927e');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '5481951649c54f4791cb8ec7a50e0473');
commit;
prompt 200 records committed...
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '46cc3d89708c48a0b1710446d819f8c2');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '8e5630486ffc475abbdc57467c97bb48');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'bc52f2ae33f64d14b63d93c695406c00');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '8ae83f604d394671ae78d763c3f41ded');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'eaf376f94d744775b874a0f34a9d5022');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '2eceb159c96944439857b0f6a186809b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '1000001');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'c2ae55dc2f524d02ac4bb634592d7bd0');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '96248c4b94644c57b136b73ad48f237f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '468744d5abac4d9a97c7815ec490958b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'bfef399eeab9423bb3b222d4fa38e345');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'adfdcdd23b0540b683192cd0454b069b');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '2e2d97e3e81f4cf1839c5421deacb3e5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '5a89703112924828a8d60a9c92b41f1d');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '3ef413c2d20147d4aae334e305265100');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '88c8a93c8e8a4cd7b616e3676883414f');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '0249d98a0155401586e48d03cafa278a');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'e607f352b8404bc0bee07d87bf8fc567');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'a161d85b900e45949c11d5c0d8e87d2c');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'ca8cb1c550c14c4b8f66aebdb85b2b50');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'f83d8a4c8da047c89d735553a90330e5');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '9e3cccc83578476e86671f809e87bf71');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '8181bcd66eaf42c0ac29152676eeda03');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '8f92f294a6064f778799f28a792b3f75');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '2579b41fe17049f9b48fdc16490d19de');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', '1b4c5ab5df8b46309ecb388974a35837');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'b93ec9fc73ad40cc8701fcfc14349e78');
insert into SYS_ROLE_MENU (ROLE_ID, MENU_ID)
values ('40295616fd1e43559272f95b8d78b0ee', 'eca654d268644fce81a9c9309a9eeff4');
commit;
prompt 228 records loaded
prompt Loading SYS_USER...
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('-2', '132fb0e4f56545499bbceaea276a743d', '工厂', 'gongchang', '6c872367731b0ef8e7650cf942cb9267134ba87ad3cce7b5803556674ea7b2f1', to_date('29-03-2019 16:51:58', 'dd-mm-yyyy hh24:mi:ss'), null, '0', null, null, null, '-1', null, null, '132fb0e4f56545499bbceaea276a743d', 'OzLF7ZddgYbZ7t4q7TUH', null, null);
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('00f28caac6a7439d8db2f335f9157c99', '6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', 'zl715', 'zl715', '01b700b3e13926a7ec2b2e1f32707344137cde929c01e0ad6927de298a0796fb', to_date('07-03-2019 14:39:24', 'dd-mm-yyyy hh24:mi:ss'), null, '0', '18341254022', null, null, '-1', null, null, '6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', '4Ex6levSKOg1Rxo8U0MY', null, null);
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('b70b2bbd1e384d198499ef2995bff954', '6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', 'gaixzh', 'gaixzh', '9ec74947e1b1f03ca6131ba99d2ee5738b4ecc05bbe45ef0c3327160b8f9bb5c', to_date('20-03-2019 13:49:07', 'dd-mm-yyyy hh24:mi:ss'), null, '0', null, null, null, 'a94d75557cb241e19bf31efbbbb3d09d', null, null, '6bf9f23a9c9c40fd8a6dfa6a2fb2ec9a', 'ltWl9xMxr2UpgAvbSb0G', null, null);
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('-1', '132fb0e4f56545499bbceaea276a743d', '超级管理员', 'admin', 'a459e9fbc01a43de4b75bc73d7c6c2bac94ff8556ffd758074f3d2268b6479d8', to_date('27-04-2017', 'dd-mm-yyyy'), to_date('09-05-2018', 'dd-mm-yyyy'), '0', '12324343', null, null, null, '026a564bbfd84861ac4b65393644beef', null, 'e7c14e165fe14b6391ce5f7ab9380c30', 'dQCXHbIgU6lhmEeqh7G3', null, null);
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('a94d75557cb241e19bf31efbbbb3d09d', 'ab4a7b79db534a9eae4f9d4747b37720', 'dashuju', 'dashuju', '0d1acd83f5174a24888ff9c5f63f165d83304cc07008df3823e9c30e837f9d71', to_date('17-01-2019 12:32:50', 'dd-mm-yyyy hh24:mi:ss'), null, '0', null, null, null, '-1', null, null, 'ab4a7b79db534a9eae4f9d4747b37720', '9QHc7GtMzYnv4h08GAxU', null, null);
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('d47ae6c87d4a4e318b2d263c20de2c0e', 'ab4a7b79db534a9eae4f9d4747b37720', 'dashuju2', 'dashuju2', '2c8e3ecf3ad15cb05d496a595918d11cb4dfa7a3dd65c137f6031f0afcbac2c0', to_date('17-01-2019 12:33:11', 'dd-mm-yyyy hh24:mi:ss'), null, '0', null, null, null, '-1', null, null, 'ab4a7b79db534a9eae4f9d4747b37720', 'v9YTjyrqmeAxdnKkLsSi', null, null);
insert into SYS_USER (ID, BAPID, USER_NAME, LOGIN_NAME, PASS_WORD, CREATE_TIME, UPDATE_TIME, STATUS, PHONE, PHOTO, EMAIL, CREATE_ID, UPDATE_ID, REMARK, BAID, SALT, SORT, AD_CODE)
values ('f32ab4224e7a453aa8f92c5e98b1b8a6', '132fb0e4f56545499bbceaea276a743d', 'ZCY', 'ZCY', 'a290c189da47c93f5093574da3fe03eb687e3fe9c604f70e8a52a0ccf4237f49', to_date('21-03-2019', 'dd-mm-yyyy'), to_date('09-04-2019', 'dd-mm-yyyy'), '0', null, null, null, 'a94d75557cb241e19bf31efbbbb3d09d', '-1', null, '132fb0e4f56545499bbceaea276a743d', 'FLfxczLQLeP1BjWF1nm6', null, null);
commit;
prompt 7 records loaded
prompt Loading SYS_USER_ROLE...
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('a94d75557cb241e19bf31efbbbb3d09d', 'ded49c80344a4e368628c9d93c309408');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('7b303216e32b4e47b27516239aba2a0a', '8d931486611346518ec01b7b8363de76');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('f32ab4224e7a453aa8f92c5e98b1b8a6', '40295616fd1e43559272f95b8d78b0ee');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('d47ae6c87d4a4e318b2d263c20de2c0e', 'ded49c80344a4e368628c9d93c309408');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('5af33cb83d984522b02b24d05fa83e26', '2ab8934bd9b04870b2f7bb2d95ff5b89');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('2012a0a1fde348f68b9a9e9ed17bccc2', '99321f6fcd7743cba6eebadb904858bc');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('a94d75557cb241e19bf31efbbbb3d09d', '5f34de72a924400289624d0d25cf43c1');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('00f28caac6a7439d8db2f335f9157c99', 'b5c7a8c709fe4c5c9d0f357cc34cb19a');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('b70b2bbd1e384d198499ef2995bff954', 'b5c7a8c709fe4c5c9d0f357cc34cb19a');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('-1', 'ce84377c2b35468fb7b0fbed937a8ffc');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('d47ae6c87d4a4e318b2d263c20de2c0e', '6072dc7bf2f1477d8634478d7ca90358');
insert into SYS_USER_ROLE (USER_ID, ROLE_ID)
values ('5af33cb83d984522b02b24d05fa83e26', '6072dc7bf2f1477d8634478d7ca90358');
commit;
prompt 12 records loaded
prompt Loading SYS_USER_TOKEN...
prompt Table is empty
prompt Enabling triggers for SYS_CODE...
alter table SYS_CODE enable all triggers;
prompt Enabling triggers for SYS_CONFIG...
alter table SYS_CONFIG enable all triggers;
prompt Enabling triggers for SYS_LEND...
alter table SYS_LEND enable all triggers;
prompt Enabling triggers for SYS_MENU...
alter table SYS_MENU enable all triggers;
prompt Enabling triggers for SYS_NOTICE...
alter table SYS_NOTICE enable all triggers;
prompt Enabling triggers for SYS_NOTICE_USER...
alter table SYS_NOTICE_USER enable all triggers;
prompt Enabling triggers for SYS_ORGAN...
alter table SYS_ORGAN enable all triggers;
prompt Enabling triggers for SYS_ORGAN_ROLE...
alter table SYS_ORGAN_ROLE enable all triggers;
prompt Enabling triggers for SYS_OSS...
alter table SYS_OSS enable all triggers;
prompt Enabling triggers for SYS_ROLE...
alter table SYS_ROLE enable all triggers;
prompt Enabling triggers for SYS_ROLE_MENU...
alter table SYS_ROLE_MENU enable all triggers;
prompt Enabling triggers for SYS_USER...
alter table SYS_USER enable all triggers;
prompt Enabling triggers for SYS_USER_ROLE...
alter table SYS_USER_ROLE enable all triggers;
prompt Enabling triggers for SYS_USER_TOKEN...
alter table SYS_USER_TOKEN enable all triggers;
set feedback on
set define on
prompt Done.
