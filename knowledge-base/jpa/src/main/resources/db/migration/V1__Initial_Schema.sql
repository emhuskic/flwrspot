---
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Let db.user have the privileges to use db objects created
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO "${db.user}";
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO "${db.user}";
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO "${db.user}";

BEGIN;

CREATE TABLE user_account (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email TEXT NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    CONSTRAINT user_email_uniq UNIQUE (email)
);

CREATE TABLE flower (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    image_url TEXT NOT NULL,
    CONSTRAINT flower_name_uniq UNIQUE (name)
);

CREATE TABLE user_flower_sighting (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    flower_id UUID NOT NULL,
    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    image_url TEXT,
    quote TEXT,
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES user_account(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT flower_fk FOREIGN KEY (flower_id) REFERENCES flower(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE user_like (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    sighting_id UUID NOT NULL,
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES user_account(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT sighting_fk FOREIGN KEY (sighting_id) REFERENCES user_flower_sighting(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


----- SEED FLOWERS
INSERT INTO flower(name, description, image_url) VALUES ('Aster', 'Asters are daisy-like perennials with starry-shaped flower heads that range in color from white to blue to purple.', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Azalea', 'Test', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Black-Eyed Susan', 'TEST', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Buttercup', 'TEST', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('California Poppy', 'TEST', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612' );
INSERT INTO flower(name, description, image_url) VALUES ('Chrysanthemum', 'TEST', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Crocus', 'TEST', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Daffodil', 'test', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Rose', 'test', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');
INSERT INTO flower(name, description, image_url) VALUES ('Sunflower', 'test', 'https://media.istockphoto.com/photos/water-lilies-blooming-in-summer-pond-picture-id1270427630?s=612x612');

END;