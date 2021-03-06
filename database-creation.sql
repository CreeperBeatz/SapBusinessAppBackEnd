CREATE TABLE IF NOT EXISTS "users" (
                    "_id"   INTEGER,
                    "username"      TEXT,
                    "email"      TEXT,
                    "hash"  TEXT,
                    "type"  INTEGER,
                    PRIMARY KEY("_id" AUTOINCREMENT)
            );
            CREATE TABLE IF NOT EXISTS "sales" (
                    "_id"   INTEGER,
                    "salesman"      INTEGER,
                    "client"        INTEGER,
                    "product"       INTEGER,
                    "quantity"      INTEGER,
                    "discount"      INTEGER,
                    "price" 	INTEGER,
		    "date" 	INTEGER,
                    PRIMARY KEY("_id" AUTOINCREMENT)
            );
            CREATE TABLE IF NOT EXISTS "products" (
                    "_id"   INTEGER,
                    "name"  TEXT,
                    "price" NUMERIC,
		    "avaivable" INTEGER,
                    "discount"      NUMERIC,
                    "description"   TEXT,
                    "imageUrl"      TEXT,
                    PRIMARY KEY("_id" AUTOINCREMENT)
            );
            CREATE TABLE IF NOT EXISTS "clients" (
                    "_id"   INTEGER,
                    "name"  TEXT,
                    "surname"       TEXT,
                    "address"       TEXT,
                    "country"       TEXT,
                    "city"  TEXT,
                    "postalCode"    TEXT,
                    "purchases"     INTEGER,
                    PRIMARY KEY("_id" AUTOINCREMENT)
            );
            INSERT INTO users(username, email ,hash, type) 
	    VALUES('administrator', 'default@mail.com', '200ceb26807d6bf99fd6f4f0d1ca54d4', 1);