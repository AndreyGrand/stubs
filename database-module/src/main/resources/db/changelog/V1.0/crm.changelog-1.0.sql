--liquibase formatted sql
--changeset gritsynada:3
create table TASK_DETAILS (
	 id NUMBER primary key,
	 SPName VARCHAR2(80),
	 SystemId VARCHAR2(80),
	 Method VARCHAR2(64),
	 Message_Login VARCHAR2(50),
	 Message_ActionId  VARCHAR2(15),
	 Response CLOB
 );
 
 create table TASK_LIST (
	 id NUMBER primary key,
	 SPName VARCHAR2(80),
	 SystemId VARCHAR2(80),
	 Method VARCHAR2(64),
	 UserLogin VARCHAR2(50),
	 OrgType VARCHAR2(80),
	 OrgNum VARCHAR2(80),
	 ApplicationType VARCHAR2(80),
	 ApplicationCRMId VARCHAR2(80),
	 ContactCRMId VARCHAR2(80),
	 BeforeDays NUMBER,
	 AfterDays NUMBER,
	 ResultRecordCount NUMBER,
	 ResultPageNum NUMBER,
	 Response CLOB
 );
 
 create table PUT_TASK (
	 id NUMBER primary key,
	 SPName VARCHAR2(80),
	 SystemId VARCHAR2(80),
	 Method VARCHAR2(64),
	 
	 DivisionCRMId VARCHAR2(15),
	 Login VARCHAR2(50),
	 InboundClientRequestId VARCHAR2(15),
	 
	 ActionId VARCHAR2(15),
	 Done DATETIME,
	 Description VARCHAR2(1000),
	 ActionType VARCHAR2(50),

	 Status VARCHAR2(50),
	 Priority VARCHAR2(50),
	 CRMId VARCHAR2(15),
	 MDMId VARCHAR2(20),
	 Planned DATETIME,
	 Started DATETIME,
	 MeetingLocation VARCHAR2(100),
	 PlannedCompletion DATETIME,
	 SBRFResult VARCHAR2(50),
	 SBRFDecision VARCHAR2(200),
	 AudioFileName VARCHAR2(255),
	 Area VARCHAR2(50),
	 Channel VARCHAR2(50),
	 Source  VARCHAR2(100),

	 SmsPhoneNumber VARCHAR2(50),
	 ApplicationCRMId VARCHAR2(15),
	 ReferenceCRMId VARCHAR2(15),
	 
	 Response CLOB
 );
 
create table CONTACT_INFO (
  ContactId VARCHAR2(15) primary key,
  PutTaskId NUMBER not null,
  CONSTRAINT fk_contact_info_put_task
    FOREIGN KEY (PutTaskId)
    REFERENCES PUT_TASK(id)
);

create table PRODUCT_INFO (
  ProductId VARCHAR2(15) primary key,
  MDMId VARCHAR2(15),
  ProductName VARCHAR2(100),
  PutTaskId NUMBER not null,
  CONSTRAINT fk_product_info_put_task
    FOREIGN KEY (PutTaskId)
    REFERENCES PUT_TASK(id)
);

create table TEAM_INFO (
  TeamLogin VARCHAR2(50) primary key,
  SBRFActionRole VARCHAR2(50),
  PrimaryFlag VARCHAR2(2),
  PutTaskId NUMBER not null,
  CONSTRAINT fk_team_info_put_task
    FOREIGN KEY (PutTaskId)
    REFERENCES PUT_TASK(id)
);