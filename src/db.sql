
-- 1. Create enum types
CREATE TYPE consumption_type AS ENUM ('TRANSPORT', 'LOGEMENT', 'ALIMENTATION');
CREATE TYPE vehicule_type AS ENUM ('voiture', 'train');
CREATE TYPE energie_type AS ENUM ('electricité', 'gaz');
CREATE TYPE aliment_type AS ENUM ('viande', 'légume');

-- 2. Create users table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       age INT NOT NULL,
                       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Create consumption table
CREATE TABLE consumption (
                             id SERIAL PRIMARY KEY,
                             user_id UUID REFERENCES users(id),
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             amount DOUBLE PRECISION NOT NULL,
                             type consumption_type NOT NULL,  -- Using the consumption_type enum
                             impact DOUBLE PRECISION NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Create transport-specific consumption table
CREATE TABLE transport (
                           consumption_id INT PRIMARY KEY REFERENCES consumption(id),
                           distance_parcourue DOUBLE PRECISION NOT NULL,
                           type_de_vehicule vehicule_type NOT NULL  -- Using the vehicule_type enum
);

-- 5. Create logement-specific consumption table
CREATE TABLE logement (
                          consumption_id INT PRIMARY KEY REFERENCES consumption(id),
                          consommation_energie DOUBLE PRECISION NOT NULL,
                          type_energie energie_type NOT NULL  -- Using the energie_type enum
);

-- 6. Create alimentation-specific consumption table
CREATE TABLE alimentation (
                              consumption_id INT PRIMARY KEY REFERENCES consumption(id),
                              type_aliment aliment_type NOT NULL,  -- Using the aliment_type enum
                              poids DOUBLE PRECISION NOT NULL
);



CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
INSERT INTO users (id, name, age, date_created)
VALUES
    (uuid_generate_v4(), 'Alice', 30, NOW()),
    (uuid_generate_v4(), 'Bob', 25, NOW()),
    (uuid_generate_v4(), 'Charlie', 40, NOW());
