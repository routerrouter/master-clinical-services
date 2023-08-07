-- public.tb_account_class definition

-- Drop table

-- DROP TABLE public.tb_account_class;

CREATE TABLE public.tb_account_class (
	account_class_id uuid NOT NULL,
	creation_date timestamp NOT NULL,
	description varchar(255) NOT NULL,
	last_update_date timestamp NOT NULL,
	"number" varchar(255) NOT NULL,
	CONSTRAINT tb_account_class_pkey PRIMARY KEY (account_class_id)
);