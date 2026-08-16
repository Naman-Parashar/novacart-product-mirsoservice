CREATE TABLE categories (
                            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            category_name VARCHAR(255) NOT NULL UNIQUE,
                            created_at TIMESTAMP NOT NULL default current_timestamp,
                            updated_at TIMESTAMP NOT NULL default current_timestamp on update current_timestamp,
                            deleted_at TIMESTAMP default NULL,
                            is_active BOOLEAN not null default false,
                            created_by BIGINT not null default 0000,
                            updated_by BIGINT  default 0000
);