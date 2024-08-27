# java-filmorate
Репозиторий для проекта Filmorate.
## Схема базы данных
https://dbdiagram.io/d/66cd4a9f3f611e76e99165ef

<img width="994" alt="Снимок экрана 2024-08-28 в 01 17 08" src="https://github.com/user-attachments/assets/c068fdb5-8f94-4877-a710-0f93a8980e5b">

---
```SQL
CREATE TABLE "genre" (
  "film_id" int PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "film_genre" (
  "film_id" bigint,
  "genre_id" bigint,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE "users" (
  "film_id" bigint PRIMARY KEY,
  "name" varchar,
  "login" varchar,
  "email" varchar,
  "birthday" data
);

CREATE TABLE "likes" (
  "film_id" bigint,
  "user_id" bigint,
  PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE "mpa" (
  "film_id" int PRIMARY KEY,
  "name" varchar
);

CREATE TABLE "friends" (
  "user_id" bigint,
  "user_friend_id" bigint,
  "status" bool,
  PRIMARY KEY ("user_id", "user_friend_id")
);

CREATE TABLE "films" (
  "film_id" bigint PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "release_date" date,
  "duration" int,
  "mpa_id" int
);

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("film_id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "films" ADD FOREIGN KEY ("mpa_id") REFERENCES "mpa" ("film_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("film_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_friend_id") REFERENCES "users" ("film_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("film_id");
```

## Пример использования запроса
Получение списка самых популярных фильмов: 
```SQL
SELECT f.* FROM films f 
JOIN likes l ON f.ID = l.film_id 
GROUP BY f.id 
ORDER BY COUNT(l.user_id) DESC"
```

