-- public.tb_subaccounts definition

-- Drop table

-- DROP TABLE public.tb_subaccounts;

CREATE TABLE public.tb_subaccounts (
	sub_account_id uuid NOT NULL,
	creation_date timestamp NOT NULL,
	description varchar(255) NULL,
	last_update_date timestamp NOT NULL,
	movement varchar(255) NULL,
	"number" varchar(255) NULL,
	account_id uuid NOT NULL,
	CONSTRAINT fk_tb_accounts foreign key (account_id) references tb_accounts (account_id),
	CONSTRAINT tb_subaccounts_pkey PRIMARY KEY (sub_account_id)
);
