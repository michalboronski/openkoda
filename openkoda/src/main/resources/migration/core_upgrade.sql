-- @version: 1.6.0.0

CREATE TABLE public.dynamic_entity (
    id int8 NOT NULL,
    created_by varchar(255) NULL,
    created_by_id int8 NULL,
    created_on timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
    modified_by varchar(255) NULL,
    modified_by_id int8 NULL,
    updated_on timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
    table_name varchar(255) NOT NULL,
    CONSTRAINT dynamic_entity_pkey PRIMARY KEY (id),
    CONSTRAINT uk92trgn7q4egx40p7e4upyo7ln UNIQUE (table_name)
);

ALTER TABLE public.form ADD IF NOT EXISTS filter_columns varchar(255) NULL;
ALTER TABLE public.email ADD IF NOT EXISTS sender varchar(255) NULL;

-- @version: 1.6.1.0
ALTER TABLE public.form ADD IF NOT EXISTS show_on_organization_dashboard bool NULL;


-- @version: 1.6.2.0
-- @init

create table IF NOT EXISTS public.query_report
(
    id              bigint not null
        primary key,
    created_by      varchar(255),
    created_by_id   bigint,
    created_on      timestamp with time zone default CURRENT_TIMESTAMP,
    index_string    varchar(16300)           default ''::character varying,
    modified_by     varchar(255),
    modified_by_id  bigint,
    organization_id bigint,
    updated_on      timestamp with time zone default CURRENT_TIMESTAMP,
    name            varchar(255),
    query           varchar(1000)
);

-- @version: 1.6.2.1
-- @init
update public.roles set "privileges" = "privileges" || ',(canUseAI)'  where name in ('ROLE_ADMIN')  and "privileges" not like '%canUseAI%';


-- @version: 1.6.2.2
alter table organization add column IF NOT EXISTS main_brand_color varchar(255);
alter table organization add column IF NOT EXISTS second_brand_color varchar(255);
alter table organization add column IF NOT EXISTS logo_id bigint references file(id);
alter table organization add column IF NOT EXISTS personalize_dashboard boolean;
update organization set personalize_dashboard = false;
alter table audit add column IF NOT EXISTS entity_key varchar(255);
alter table form add column IF NOT EXISTS register_as_auditable boolean ;
update form set register_as_auditable = true;

-- @version: 1.7.0.0

CREATE TABLE IF NOT EXISTS public.dynamic_privilege (
    id int8 NOT NULL,
    category varchar(255) NULL,
    privilege_group varchar(255) NULL,
    index_string varchar(16300) NULL DEFAULT ''::character varying,
    "name" varchar(255) NOT NULL,
    removable bool NULL DEFAULT true,
    updated_on timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT dynamic_privilege_pkey PRIMARY KEY (id),
    CONSTRAINT uk_j9c9gkfh338hs9ved624aqtu2 UNIQUE (name)
);
CREATE INDEX IF NOT EXISTS idxj9c9gkfh338hs9ved624aqtu2 ON public.dynamic_privilege USING btree (name);

-- @version: 1.7.0.1
ALTER TABLE public.dynamic_privilege ADD "label" varchar(255) NULL;
UPDATE public.dynamic_privilege SET label = "name" WHERE label IS NULL;
ALTER TABLE public.dynamic_privilege ALTER COLUMN label SET NOT NULL;

-- @version: 1.7.1.0

CREATE TABLE IF NOT EXISTS public.custom_event (
    "type" varchar(31) NOT NULL,
    id int8 NOT NULL,
    index_string varchar(16300) NULL DEFAULT ''::character varying,
    "name" varchar(255) NULL,
    read_privilege varchar(255) NULL,
    updated_on timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
    write_privilege varchar(255) NULL,
    event_category varchar(255) NULL,
    class_name varchar(500) NULL,
    CONSTRAINT custom_event_pkey PRIMARY KEY (id)
);

-- @version: 1.7.1.1
create table public.dynamic_entity_csv_import_row
(
    id              bigint not null
        primary key,
    created_by      varchar(255),
    created_by_id   bigint,
    created_on      timestamp with time zone default CURRENT_TIMESTAMP,
    index_string    varchar(16300)           default ''::character varying,
    modified_by     varchar(255),
    modified_by_id  bigint,
    organization_id bigint,
    updated_on      timestamp with time zone default CURRENT_TIMESTAMP,
    upload_id       bigint,
    line_number     bigint,
    valid           boolean,
    entity_key       varchar(255),
    content         jsonb
);

update roles set privileges=privileges||',(canImportData)' where name in ('ROLE_ADMIN','ROLE_ORG_ADMIN');

-- @version: 1.7.1.2
update roles set privileges=privileges||',(canCreateReports),(canReadReports)' where name in ('ROLE_ADMIN','ROLE_ORG_ADMIN');
update public.roles set "privileges" = replace("privileges",'canUseAI','canUseReportingAI')  where "privileges" like '%canUseAI%';

-- @version: 1.7.1.3
ALTER TABLE public.controller_endpoint ADD "test_data" varchar(65535) NULL;

-- @version: 1.7.2.0
create table if not exists public.document_template
(
    id              bigint not null,
    created_by      varchar(255),
    created_by_id   bigint,
    created_on      timestamp with time zone default CURRENT_TIMESTAMP,
    index_string    varchar(16300)           default ''::character varying,
    modified_by     varchar(255),
    modified_by_id  bigint,
    organization_id bigint,
    updated_on      timestamp with time zone default CURRENT_TIMESTAMP,
    condition       varchar(255),
    data_source     varchar(255),
    entity_key      varchar(255),
    file_id         bigint,
    filename        varchar(255),
    primary key (id),
    constraint fk59uh0ob7uc6w5ly2mvv02q2uk
        foreign key (organization_id) references public.organization,
    constraint fk3km07r1wo6ed5k7rd5feoank9
        foreign key (file_id) references public.file
);

create table if not exists public.placeholder
(
    id                   bigint not null,
    created_by           varchar(255),
    created_by_id        bigint,
    created_on           timestamp with time zone default CURRENT_TIMESTAMP,
    index_string         varchar(16300)           default ''::character varying,
    modified_by          varchar(255),
    modified_by_id       bigint,
    organization_id      bigint,
    updated_on           timestamp with time zone default CURRENT_TIMESTAMP,
    data_source          varchar(255),
    default_value        varchar(255),
    document_template_id bigint,
    name                 varchar(255),
    type                 varchar(255),
    fixed_value_supplier varchar(255),
    primary key (id),
    constraint fks6p4brvqkpxsyvr26i3klna4l
        foreign key (organization_id) references public.organization,
    constraint fkjxrdhjl7yg3glgyujfry9fwon
        foreign key (document_template_id) references public.document_template
);


-- @version: 1.7.2.1
-- Fix for: the one to one relationship is missing uniqueness constraint for the foreign key pointing to file table
do
$$
    begin
        if not exists(
                select 1
                from pg_constraint
                where conname = 'unique_file'
            ) then
            alter table document_template
                add constraint unique_file unique (file_id);
        end if;
    end
$$;

-- @version: 1.7.2.2
create table if not exists public.business_parameter
(
    id                   bigint not null,
    created_by           varchar(255),
    created_by_id        bigint,
    created_on           timestamp with time zone default CURRENT_TIMESTAMP,
    index_string         varchar(16300)           default ''::character varying,
    modified_by          varchar(255),
    modified_by_id       bigint,
    organization_id      bigint,
    updated_on           timestamp with time zone default CURRENT_TIMESTAMP,
    name                 varchar(255),
    value                varchar(1000),
    description          varchar(1000),
    primary key (id),
    foreign key (organization_id) references public.organization
);

-- @version: 1.7.2.3
-- update forms for manyToOne API change (field names without "Id")
update form set code = regexp_replace(code, '\.manyToOne\(\"([a-zA-Z]+)Id\"','.manyToOne("\1"', 'g');

-- @version: 1.7.2.4
-- rename DocumentTemplate -> Document
-- rename Document -> Template
ALTER TABLE placeholder RENAME COLUMN document_template_id TO document_id;
ALTER TABLE document_template RENAME TO document;

-- @version: 1.7.2.5
-- increase column length for query_report.query
DROP VIEW IF EXISTS global_search_view;
alter table query_report alter column query type varchar(262144);

-- @version: 1.7.2.6
-- rename web endpoint to widget on dashboard builder
UPDATE frontend_resource set content = replace(content,
   '<div class=\"embedded-preview-title\">Web Endpoint:',
   '<div class=\"embedded-preview-title\">Widget:')
where resource_type='DASHBOARD';


-- @version: 1.7.2.7
-- add user friendly name to frontend_resource
alter table frontend_resource add column user_friendly_name varchar(1000);
update frontend_resource set user_friendly_name = name;
alter table query_report add column widget_id bigint;
alter table query_report add constraint fk_frontend_resource foreign key (widget_id) references public.frontend_resource;

-- @version: 1.7.2.8
-- add description to server_js
alter table server_js add column description varchar(1000);

-- @version: 1.7.2.9
-- add category to business_parameter
alter table business_parameter add column category varchar(1000);

-- @version: 1.7.2.10
update form set code = replace(code, '.file("filesId"', '.file("files"');

-- @version: 1.7.2.11
-- add document generation related privilege
update roles set privileges=privileges||',(canUseDocumentGenerator)' where name in ('ROLE_ADMIN','ROLE_ORG_ADMIN');

-- @version: 1.7.2.12
-- add role_id to notification
alter table notification add column role_id bigint;
alter table notification add constraint fk_role_id foreign key (role_id) references roles;

-- following lines contains db changes not ready yet to be executed. Once ready, replace with @version
-- When adding queries always think about existing data and how to deal with them
-- @upcoming: 1.7.x.x
