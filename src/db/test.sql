DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'consumption_type') THEN
            CREATE TYPE consumption_type AS ENUM ('TRANSPORT', 'HOUSING', 'FOOD');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'vehicle_type') THEN
            CREATE TYPE vehicle_type AS ENUM ('CAR', 'TRAIN');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'energy_type') THEN
            CREATE TYPE energy_type AS ENUM ('ELECTRICITY', 'GAS');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'food_type') THEN
            CREATE TYPE food_type AS ENUM ('MEAT', 'VEGETABLE');
        END IF;
    END $$;

-- Create the 'users' table
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(255) NOT NULL,
                       age INT NOT NULL,
                       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the 'consumption' table
CREATE TABLE consumption (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             user_id UUID REFERENCES users(id),
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             amount DOUBLE PRECISION NOT NULL,
                             type consumption_type NOT NULL,
                             impact DOUBLE PRECISION NOT NULL, -- Impact will be calculated in Java
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the 'transport' table
CREATE TABLE transport (
                           consumption_id UUID PRIMARY KEY REFERENCES consumption(id),
                           distance_traveled DOUBLE PRECISION NOT NULL,
                           vehicle_type vehicle_type -- Enum for vehicle type (CAR, TRAIN)
);

-- Create the 'housing' table
CREATE TABLE housing (
                         consumption_id UUID PRIMARY KEY REFERENCES consumption(id),
                         energy_consumption DOUBLE PRECISION NOT NULL,
                         energy_type energy_type -- Enum for energy type (ELECTRICITY, GAS)
);

-- Create the 'food' table
CREATE TABLE food (
                      consumption_id UUID PRIMARY KEY REFERENCES consumption(id),
                      weight DOUBLE PRECISION NOT NULL,
                      food_type food_type -- Enum for food type (MEAT, VEGETABLE)
);