DROP DATABASE IF EXISTS testestpsit;
CREATE DATABASE testestpsit;
USE testestpsit;

CREATE TABLE IF NOT EXISTS  Alunni (
    Nome VARCHAR(45),
    Cognome VARCHAR(45)
);

INSERT INTO Alunni (Nome, Cognome)
VALUES 
	("Francesco", "Lazzarelli"),
    ("Edoardo", "Croci"),
    ("Andrea", "Lotti"),
    ("Francesco", "Filippini"),
    ("Niccolo", "Fappani"),
    ("Samuele", "Pagliarello"),
    ("Samuele", "Marrani"),
    ("Leonardo", "Romagnoli"),
    ("Lorenzo", "Camigliano"),
    ("Alessandro", "Feri");