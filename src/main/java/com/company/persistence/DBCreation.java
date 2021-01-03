package com.company.persistence;

import com.company.utilities.MD5Hash;

public class DBCreation {
    //Database creation
    //TODO change all the fields (oh god) with the psfs above
    public static final String DATABASE_CREATION_STRING =
            "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
                    "        \"_id\"         INTEGER,\n"+
                    "        \"username\"      TEXT UNIQUE,\n" +
                    "        \"email\"      TEXT,\n" +
                    "        \"hash\"  TEXT,\n" +
                    "        \"type\"  INTEGER,\n" +
                    "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS \"sales\" (\n" +
                    "        \"_id\"   INTEGER,\n" +
                    "        \"salesman\"      TEXT,\n" +
                    "        \"client\"        INTEGER,\n" +
                    "        \"product\"       INTEGER,\n" +
                    "        \"quantity\"      INTEGER,\n" +
                    "        \"discount\"      INTEGER,\n" +
                    "        \"price\" INTEGER,\n" +
                    "        \"date\" INTEGER,\n" +
                    "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS \"products\" (\n" +
                    "        \"_id\"   INTEGER,\n" +
                    "        \"name\"  TEXT,\n" +
                    "        \"price\" NUMERIC,\n" +
                    "        \"available\" INTEGER\n" +
                    "        \"discount\"      NUMERIC,\n" +
                    "        \"description\"   TEXT,\n" +
                    "        \"imageUrl\"      TEXT,\n" +
                    "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS \"clients\" (\n" +
                    "        \"_id\"   INTEGER,\n" +
                    "        \"name\"  TEXT,\n" +
                    "        \"surname\"       TEXT,\n" +
                    "        \"address\"       TEXT,\n" +
                    "        \"country\"       TEXT,\n" +
                    "        \"city\"  TEXT,\n" +
                    "        \"postalCode\"    TEXT,\n" +
                    "        \"purchases\"     INTEGER,\n" +
                    "        PRIMARY KEY(\"_id\" AUTOINCREMENT)\n" +
                    ");\n" +
                    "INSERT INTO users(username, email ,hash, type) VALUES('administrator', 'random@mail.com', '" +
                    MD5Hash.getHash("administrator") +
                    "', 1);";
}
