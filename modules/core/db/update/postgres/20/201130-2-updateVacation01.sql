alter table VAC_VACATION add constraint FK_VAC_VACATION_ON_DECREE foreign key (DECREE_ID) references SYS_FILE(ID);
create index IDX_VAC_VACATION_ON_DECREE on VAC_VACATION (DECREE_ID);
