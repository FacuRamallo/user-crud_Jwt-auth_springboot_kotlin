ALTER TABLE friendships
    alter COLUMN endedBy DROP NOT NULL;

ALTER TABLE friendships
    alter COLUMN endedBy set DEFAULT null;

