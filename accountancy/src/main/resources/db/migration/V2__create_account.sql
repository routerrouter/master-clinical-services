-- public.tb_accounts definition

-- Drop table

-- DROP TABLE public.tb_accounts;

CREATE TABLE public.tb_accounts (
	account_id uuid NOT NULL,
	creation_date timestamp NOT NULL,
	last_update_date timestamp NOT NULL,
	account_type varchar(255) NULL,
	description varchar(255) NOT NULL,
	number varchar(255) NOT NULL,
    class_id uuid NOT NULL,
    CONSTRAINT fk_tb_account_class foreign key (class_id) references tb_account_class (account_class_id),
	CONSTRAINT tb_accounts_pkey PRIMARY KEY (account_id)
);


