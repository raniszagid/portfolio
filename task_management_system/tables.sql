create table users(
                      id int primary key generated by default as identity,
                      username varchar not null unique,
                      password varchar not null,
                      role varchar
);

create table task(
                     id int primary key generated by default as identity,
                     title varchar,
                     description varchar,
                     status varchar not null,
                     priority int,
                     author_id int references users(id) on delete set null,
                     executor_id int references users(id) on delete set null,
                     author_name varchar references users(username) on delete set null,
                     executor_name varchar references users(username) on delete set null
);

create table comment(
                        id int primary key generated by default as identity,
                        task_id int references task(id) on delete cascade,
                        commentator_id int references users(id) on delete set null,
                        commentator_name varchar references users(username) on delete set null,
                        text varchar
);