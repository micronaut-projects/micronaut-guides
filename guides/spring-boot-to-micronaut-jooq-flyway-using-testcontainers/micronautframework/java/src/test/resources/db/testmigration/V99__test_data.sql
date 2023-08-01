DELETE FROM comments;
DELETE FROM posts;
DELETE FROM users;

INSERT INTO users(id, name, email) VALUES
(1, 'Siva', 'siva@gmail.com'),
(2, 'Oleg', 'oleg@gmail.com');

INSERT INTO posts(id, title, content, created_by, created_at) VALUES
(1, 'Post 1 Title', 'Post 1 content', 1, CURRENT_TIMESTAMP),
(2, 'Post 2 Title', 'Post 2 content', 2, CURRENT_TIMESTAMP);

INSERT INTO comments(id, name, content, post_id, created_at) VALUES
(1, 'Ron', 'Comment 1', 1, CURRENT_TIMESTAMP),
(2, 'James', 'Comment 2', 1, CURRENT_TIMESTAMP),
(3, 'Robert', 'Comment 3', 2, CURRENT_TIMESTAMP);
