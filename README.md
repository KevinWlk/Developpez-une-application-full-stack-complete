# MDD - Full Stack Application

Ce projet est une application full-stack développée avec **Spring Boot** pour le back-end et **Angular** avec **Material** et **Tailwind CSS** pour le front-end.


---

## 📌 Prérequis

- **Back-end** :
    - Java 17
    - Maven
    - MySQL
    - Docker (optionnel pour exécuter MySQL en conteneur)

- **Front-end** :
    - Node.js (v16 ou supérieur)
    - Angular CLI (v14.1.3)

---

## 🚀 Installation et lancement

### 🛠️ Back-end (Spring Boot)

1. **Cloner le projet :**
   ```sh
   git clone https://github.com/KevinWlk/Developpez-une-application-full-stack-complete.git
   ```
   - Aller sur le dossier back
   
    ```sh
   cd back
   ```

2. **Configurer l'environnement** :
    - Copier le fichier `.env.exemple` en `.env` :
      ```sh
      cp .env.exemple .env
      ```
    - Modifier les valeurs de `DB_USERNAME`, `DB_PASSWORD`, et `JWT_SECRET_KEY` avec vos données personnelles.
### 📂 Docker-Compose

4. **Permet de démarrer la base de données** :
    - Assurez-vous que MySQL est installé et que la base de données `mdd` existe.

5. **Permet de lancer le back-end** :
   - Le serveur démarre sur `http://localhost:3001`

       ```sh
       docker-compose up -d
       ```

### 📌 Création des tables MySQL

```sql
CREATE DATABASE IF NOT EXISTS mdd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mdd;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    subject_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    UNIQUE (user_id, subject_id)
);
```

### 🔍 Données de test

```sql
INSERT INTO users (username, email, password) VALUES
('Kevin', 'kevin@test.com', 'Password123!'),
('Mika', 'mika@test.com', 'Password456!');

INSERT INTO subjects (name, description) VALUES
('Java', 'Articles autour du langage Java'),
('Angular', 'Articles autour d’Angular');
```

---

### 🎨 Front-end (Angular)

1. **Se déplacer dans le dossier `front` :**
   ```sh
   cd ../front
   ```

2. **Installer les dépendances :**
   ```sh
   npm install
   ```

3. **Lancer l’application :**
   ```sh
   ng serve
   ```

    - L’application sera accessible sur `http://localhost:4200`

---

## 🛡️ Sécurité et Configuration

- Les variables sensibles sont stockées dans `.env`.
- Les JWT sont utilisés pour l'authentification.
- Spring Security permet de préconfigurer et de personnaliser des fonctions de sécurité au sein d'une application Java.
---

## 📚 Technologies utilisées

- **Back-end :** Spring Boot, Spring Security, JWT, Hibernate, MySQL
- **Front-end :** Angular, Angular Material, Tailwind CSS
- **Autres :** Docker, Lombok, MapStruct

---
