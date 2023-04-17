DROP TABLE  IF EXISTS tb_event;
DROP TABLE  IF EXISTS tb_user;

CREATE TABLE tb_user{
    id SERIAL,
    email varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    primary key (id)

};


CREATE TABLE tb_event(
    id SERIAl,
    title varchar(100) NOT NULL,
    description varchar(100) NOT NULL,
    price DOUBLE NOT NULL ,
    date  timestamp NOT NULL,
    creator_id INTEGER NOT NULL,
    primary key (id),
    constraint fk_created_id foreign key (creator_id) references tb_user(id)

);



