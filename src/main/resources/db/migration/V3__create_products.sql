create table products (
                          id BIGINT not null AUTO_INCREMENT primary key,
                          product_name varchar(255) not null,
                          product_image_url varchar(255) not null default 'nothing.jpg',
                          product_brand varchar(255) not null default 'N/A',
                          rating decimal(2,1) not null default 0,
                          product_description text ,
                          price decimal(10,2) not null default 0,
                          stock_quantity int not null default 0,
                          created_at TIMESTAMP default current_timestamp,
                          updated_at TIMESTAMP default current_timestamp on update current_timestamp,
                          deleted_at TIMESTAMP default NULL,
                          is_active BOOLEAN not null default false,
                          created_by BIGINT not null default 0000,
                          updated_by BIGINT default 0000,
                          category_id BIGINT not null,
                          foreign key (category_id) references categories(id)
);