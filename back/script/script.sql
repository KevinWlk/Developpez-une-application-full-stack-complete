-- Création de la base de données avec encodage UTF-8
CREATE DATABASE IF NOT EXISTS mdd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mdd;

-- Table des utilisateurs (users)
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table des thèmes/sujets (subjects)
CREATE TABLE subjects (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des abonnements (subscriptions)
CREATE TABLE subscriptions (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               subject_id INT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
                               UNIQUE (user_id, subject_id) -- Empêche les doublons d'abonnement
);

-- Table des articles/posts
CREATE TABLE posts (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       subject_id INT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                       FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- Table des commentaires (comments)
CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          post_id INT NOT NULL,
                          user_id INT NOT NULL,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Ajout de quelques données de test
INSERT INTO users (username, email, password) VALUES
                                                  ('Kevin', 'kevin@test.com', 'Password123!'),
                                                  ('Astrid', 'astrid@test.com', 'Password456!');

INSERT INTO subjects (name, description) VALUES
                                             ('Java', 'Articles autour du langage Java'),
                                             ('Angular', 'Articles autour d\'Angular'),
    ('Spring Boot', 'Développement d\'API avec Spring Boot');

INSERT INTO subscriptions (user_id, subject_id) VALUES
                                                    (1, 1),
                                                    (1, 2),
                                                    (2, 2);

INSERT INTO posts (user_id, subject_id, title, content) VALUES
                                                            (1, 1, 'Découvrir Java', 'Un article pour les débutants en Java.'),
                                                            (1, 2, 'Introduction à Angular', 'Un guide simple pour Angular.');

INSERT INTO comments (post_id, user_id, content) VALUES
                                                     (1, 2, 'Merci pour cet article très utile !'),
                                                     (2, 1, 'Super guide, clair et concis.');
