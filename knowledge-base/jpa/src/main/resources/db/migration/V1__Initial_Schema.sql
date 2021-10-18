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
INSERT INTO flower(name, description, image_url) VALUES ('Aster', 'Asters are daisy-like perennials with starry-shaped flower heads that range in color from white to blue to purple.', 'https://i.ibb.co/sWm3PTy/Alpen-Flora-Page-123-BHL10387337.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Azalea', 'Azaleas bloom in the spring (May and June in the temperate Northern Hemisphere, and December and January in the Southern Hemisphere)', 'https://i.ibb.co/vd9rtxr/How-to-Grow-Azaleas-Cover.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Black-Eyed Susan', 'Members of the aster family, Asteraceae, the “black eye” is named for the dark, brown-purple centers of its daisy-like flower heads.', 'https://i.ibb.co/QDWYTB5/photo-7932.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Buttercup', 'Creeping Buttercup has shiny, yellow flowers and creeping runners which root at the nodes and grows in large, often circular, patches. ', 'https://i.ibb.co/6yK3ZKY/5155.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('California Poppy', 'California poppy, the state flower of California, is native to the Pacific slope of North America from Western Oregon to Baja California.', 'https://i.ibb.co/FKQmWzv/05bfe900-ced6-4acd-ad72-37104bda59a9-CR0-265-4032-2494-PT0-SX970-V1.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Chrysanthemum', 'Chrysanthemums sometimes called mums or chrysanths, are flowering plants of the genus Chrysanthemum in the family Asteraceae.', 'https://i.ibb.co/b7JxJm1/paradiso-pink-mum-fall-flower-proven-winners-14469.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Crocus', 'Crocus flowers lead the way for other spring bloomers to follow. They bloom bright and early, bringing much needed color after a long winter.', 'https://i.ibb.co/8XXph2p/crocus-purple-flower-pixabay-12891.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Daffodil', 'he traditional daffodil flower may be a showy yellow or white, with six petals and a trumpet-shape central corona.', 'https://i.ibb.co/FHYhwsL/daffodil.jpg');
INSERT INTO flower(name, description, image_url) VALUES ('Rose', ' All roses feature soft, smooth petals that are often doubled, meaning they have multiple sets of petals. The petals come in a range of shapes..', 'https://i.ibb.co/w7LJZD9/photo-1596073419667-9d77d59f033f.png');
INSERT INTO flower(name, description, image_url) VALUES ('Sunflower', 'unflowers are usually tall annual or perennial plants that in some species can grow to a height of 300 cm (120 in) or more. Each "flower" is actually a disc..', 'https://i.ibb.co/mRN6xjC/61s-X88rh-Co-L-SX466.jpg');

END;
