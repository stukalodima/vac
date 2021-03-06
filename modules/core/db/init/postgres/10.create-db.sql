-- begin VAC_VACATION
create table VAC_VACATION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DATE_ varchar(255),
    COMPANY_ID varchar(255),
    EMPLOYEE_ID varchar(255),
    START_DATE varchar(255),
    END_DATE varchar(255),
    DAYS integer,
    VACATION_TYPE_ID varchar(255),
    DESCRIPTION varchar(255),
    APPLICATION_ID uuid,
    DECREE_ID uuid,
    --
    primary key (ID)
)^
-- end VAC_VACATION
