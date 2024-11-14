#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>
#include <time.h>
#include <string.h>

typedef struct {
  int  id;
  char last_name[20];
  char first_name[20];
  char birth_date[20];
} AUTHOR;

typedef struct {
  int  id;
  char name[20];
} SUBJECT;

typedef struct {
  int   id;
  int   author_id;    // clé étrangère
  int   subject_id;   // clé étrangère
  char  title[100];
  char  isbn[20];
  int   page_count;
  int   stock_quantity;
  float price;
  int   publish_year;
} BOOK;


// Ajouté : ------------------------------------------
typedef struct {
  int   id;
  char  login[30];
  char  password[30];
} EMPLOYEES;

typedef struct {
  int   id;
  char  nom[20];
  char  prenom[20];
  int   numClient;
  char  adresse[200];
} CLIENTS;

typedef struct {
  int   id;
  int   clientId;   // clé étrangère
  char  date[20];
  bool  payed;
} CADDIES;

typedef struct {
  int   id;
  int   caddyId;    // clé étrangère
  int   bookId;     // clé étrangère
  int   quantity;
} CADDY_ITEMS;
// ---------------------------------------------------


AUTHOR authors[] = {
  {-1,"Werber","Bernard","1961-09-18"},  // id = 1
  {-1,"Brown","Dan","1964-06-22"},       // id = 2
  {-1,"Herbert","Frank","1920-10-08"},   // id = 3
  {-1,"Cixin","Liu","1963-06-23"},       // id = 4
  {-1,"Delannoy","Claude","1946-01-06"}, // id = 5
  {-1,"Le Cun","Yan","1960-07-08"},      // id = 6
  {-1,"Verne","Jules","1828-02-08"},     // id = 7
  {-1,"Harris","Thomas","1940-09-022"}   // id = 8
};
int nbAuthors = 8;

SUBJECT subjects[] = {
  {-1,"Science-Fiction"},                // id = 1
  {-1,"Thriller"},                       // id = 2
  {-1,"Informatique"}                    // id = 3
};
int nbSubjects = 3;

BOOK books[] = {
  {-1,1,1,"Les Thanatonautes","978-2253139225",505,3,9.70f,1999},
  {-1,1,1,"Les Fourmis","978-2253063339",312,10,9.99f,1998},
  {-1,1,1,"La boite de Pandore","978-2253934332",576,1,9.70f,2020},
  {-1,2,2,"Da Vinci Code","978-2709624930",574,6,22.90f,2004},
  {-1,2,2,"Anges et demons","978-2253093008",720,2,10.40f,2015},
  {-1,3,1,"Dune","978-2266320481",928,13,11.95f,2021},
  {-1,4,1,"Le probleme a trois corps","978-2330181055",512,0,11.00f,2024},
  {-1,5,3,"Programmer en C++ moderne: De C++11 a C++20","978-2212678956",848,1,38.00f,2019},
  {-1,5,3,"Programmer en Java","978-2212118896",916,8,71.18f,2016},
  {-1,6,3,"Quand la machine apprend","978-2415006563",394,2,11.50f,2023},
  {-1,7,1,"Vingt mille lieues sous les mers","978-2253006329",606,11,6.70f,2001},
  {-1,3,1,"Les heretiques de Dune","978-2221264577",598,2,24.90f,2023},
  {-1,8,2,"Le silence des agneaux","978-2266208949",377,17,7.70f,2015}
};
int nbBooks = 13;

// Ajouté : ------------------------------------------
EMPLOYEES employees[] = {
  {-1,"luca", "azerty"},                  // id = 1
  {-1,"aa", "bb"},                        // id = 2
  {-1,"bb", "cc"}                         // id = 3
};
int nbEmployees = 3;


CLIENTS clients[] = {
    {-1, "Dupont", "Martin", 1, "Rue de Paris 123, Paris, France"},   // id = 1
    {-1, "Durand", "Julie", 2, "Avenue de Lyon 456, Lyon, France"},   // id = 2
    {-1, "Bernard", "Luc", 3, "Boulevard de Liege 789, Liege, Belgique"}  // id = 3
};
int nbClients = 3;

CADDIES caddies[] = {
    {-1, 1, "2023-10-10", true},     // id = 1, clientId = 1
    {-1, 2, "2023-11-05", false},    // id = 2, clientId = 2
    {-1, 3, "2023-12-01", true}      // id = 3, clientId = 3
};
int nbCaddies = 3;

CADDY_ITEMS caddy_items[] = {
    {-1, 1, 1, 2},  // caddyId = 1, bookId = 1 ("Les Thanatonautes"), quantité = 2
    {-1, 1, 2, 1},  // caddyId = 1, bookId = 2 ("Les Fourmis"), quantité = 1
    {-1, 1, 4, 1},  // caddyId = 1, bookId = 4 ("Da Vinci Code"), quantité = 1

    {-1, 2, 5, 3},  // caddyId = 2, bookId = 5 ("Anges et demons"), quantité = 3
    {-1, 2, 6, 1},  // caddyId = 2, bookId = 6 ("Dune"), quantité = 1
    {-1, 2, 8, 2},  // caddyId = 2, bookId = 8 ("Programmer en C++ moderne"), quantité = 2

    {-1, 3, 10, 1}, // caddyId = 3, bookId = 10 ("Quand la machine apprend"), quantité = 1
    {-1, 3, 11, 4}, // caddyId = 3, bookId = 11 ("Vingt mille lieues sous les mers"), quantité = 4
    {-1, 3, 12, 2}  // caddyId = 3, bookId = 12 ("Les heretiques de Dune"), quantité = 2
};
int nbCaddyItems = 4;
// ---------------------------------------------------

void finish_with_error(MYSQL *con);

int main(int argc,char *argv[])
{
  // Connection a MySql
  printf("Connection a la BD...\n");
  MYSQL* connexion = mysql_init(NULL);
  mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0);

  // Ajouté : ------------------------------------------
  // Supprimer la table caddy_items si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS caddy_items;")) {
    finish_with_error(connexion);
  }

  // Supprimer la table caddies si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS caddies;")) {
    finish_with_error(connexion);
  }
  // ---------------------------------------------------

  // Supprimer la table books si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS books;")) {
    finish_with_error(connexion);
  }

  // Supprimer la table authors si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS authors;")) {
    finish_with_error(connexion);
  }

  // Supprimer la table subjects si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS subjects;")) {
    finish_with_error(connexion);
  }


  // Ajouté : ------------------------------------------
  // Supprimer la table employees si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS employees;")) {
    finish_with_error(connexion);
  }

  // Supprimer la table clients si elle existe déjà
  if (mysql_query(connexion, "DROP TABLE IF EXISTS clients;")) {
    finish_with_error(connexion);
  }
  // ---------------------------------------------------


  // Creation de la table authors
  printf("Creation de la table authors...\n");
  if (mysql_query(connexion,"CREATE TABLE authors ("
                      "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                      "last_name VARCHAR(20), "
                      "first_name VARCHAR(20), "
                      "birth_date DATE);")) {
    finish_with_error(connexion);
  }

  // Creation de la table subjects
  printf("Creation de la table subjects...\n");
  if (mysql_query(connexion,"CREATE TABLE subjects ("
                      "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                      "name VARCHAR(20));")) {
    finish_with_error(connexion);
  }

  // Créer la table books
  printf("Creation de la table books...\n");
  if (mysql_query(connexion, "CREATE TABLE books ("
                       "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                       "author_id INT, "
                       "subject_id INT, "
                       "title VARCHAR(100), "
                       "isbn VARCHAR(20), "
                       "page_count INT, "
                       "stock_quantity INT, "
                       "price DECIMAL(10, 2), "
                       "publish_year INT, "
                       "FOREIGN KEY (author_id) REFERENCES authors(id), "
                       "FOREIGN KEY (subject_id) REFERENCES subjects(id));")) {
    finish_with_error(connexion);
  }

  // Ajouté : ------------------------------------------
  // Créer la table Employees
  printf("Creation de la table employees...\n");
  if (mysql_query(connexion, "CREATE TABLE employees ("
                       "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                       "login VARCHAR(30), "
                       "password VARCHAR(30))")) {
    finish_with_error(connexion);
  }

  // Créer la table Clients
  printf("Creation de la table clients...\n");
  if (mysql_query(connexion, "CREATE TABLE clients ("
                       "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                       "nom VARCHAR(20), "
                       "prenom VARCHAR(20), "
                       "num_client INT, "
                       "adresse VARCHAR(200))")) {
    finish_with_error(connexion);
  }

  // Créer la table Caddies
  printf("Creation de la table caddies...\n");
  if (mysql_query(connexion, "CREATE TABLE caddies ("
                       "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                       "client_id INT, "
                       "date DATE,"
                       "payed INT, "
                       "FOREIGN KEY (client_id) REFERENCES clients(id));")) {
    finish_with_error(connexion);
  }

  // Créer la table Caddy_Items
  printf("Creation de la table caddy_items...\n");
  if (mysql_query(connexion, "CREATE TABLE caddy_items ("
                       "id INT(4) AUTO_INCREMENT PRIMARY KEY, "
                       "caddy_id INT, "
                       "book_id INT, "
                       "quantity INT, "
                       "FOREIGN KEY (caddy_id) REFERENCES caddies(id), "
                       "FOREIGN KEY (book_id) REFERENCES books(id));")) {
    finish_with_error(connexion);
  }
  // ---------------------------------------------------

// Créer le trigger après suppression dans caddy_items pour mettre à jour le stock
printf("Creation du trigger after_delete_caddy_item...\n");
if (mysql_query(connexion,
    "CREATE TRIGGER after_delete_caddy_item "
    "AFTER DELETE ON caddy_items "
    "FOR EACH ROW "
    "BEGIN "
    "   UPDATE books "
    "   SET stock_quantity = stock_quantity + OLD.quantity "
    "   WHERE id = OLD.book_id; "
    "END;")) {
  finish_with_error(connexion);
}

// Créer le trigger après suppression dans caddies pour supprimer les caddy_items associés
printf("Creation du trigger before_delete_caddy...\n");
if (mysql_query(connexion,
    "CREATE TRIGGER before_delete_caddy "
    "BEFORE DELETE ON caddies "
    "FOR EACH ROW "
    "BEGIN "
    "   DELETE FROM caddy_items WHERE caddy_id = OLD.id; "
    "END;")) {
  finish_with_error(connexion);
}

// Créer le trigger après suppression dans authors pour supprimer les books associés
printf("Creation du trigger before_delete_author...\n");
if (mysql_query(connexion,
    "CREATE TRIGGER before_delete_author "
    "BEFORE DELETE ON authors "
    "FOR EACH ROW "
    "BEGIN "
    "   DELETE FROM books WHERE author_id = OLD.id; "
    "END;")) {
  finish_with_error(connexion);
}

// Créer le trigger après suppression dans subjects pour supprimer les books associés
printf("Creation du trigger before_delete_subject...\n");
if (mysql_query(connexion,
    "CREATE TRIGGER before_delete_subject "
    "BEFORE DELETE ON subjects "
    "FOR EACH ROW "
    "BEGIN "
    "   DELETE FROM books WHERE subject_id = OLD.id; "
    "END;")) {
  finish_with_error(connexion);
}




  // Ajout de tuples dans la table authors
  printf("Ajout de %d auteurs la table authors...\n",nbAuthors);
  char request[256];
  for (int i=0 ; i<nbAuthors ; i++) {
    sprintf(request,"insert into authors values (NULL,'%s','%s','%s');",
      authors[i].last_name,authors[i].first_name,authors[i].birth_date);
    if (mysql_query(connexion,request) ) {
      finish_with_error(connexion);   
    }
  }


  // Ajout de tuples dans la table subjects
  printf("Ajout de %d sujets la table subjects...\n",nbSubjects);
  for (int i=0 ; i<nbSubjects ; i++) {
    sprintf(request,"insert into subjects values (NULL,'%s');",subjects[i].name);
    if (mysql_query(connexion,request)) {
      finish_with_error(connexion);   
    }
  }


  // Ajout de tuples dans la table books
  printf("Ajout de %d livres la table books...\n",nbBooks);
  for (int i=0 ; i<nbBooks ; i++) {
    sprintf(request,"INSERT INTO books (author_id, subject_id, title, isbn, page_count, stock_quantity, price, publish_year) VALUES (%d, %d, '%s', '%s', %d, %d, %f, %d);", 
      books[i].author_id,books[i].subject_id,books[i].title,books[i].isbn,books[i].page_count,books[i].stock_quantity,books[i].price,books[i].publish_year);
    if (mysql_query(connexion,request)) {
      finish_with_error(connexion);   
    }
  }

// Ajouté : ------------------------------------------
    // Ajout de tuples dans la table employees
    printf("Ajout de %d employes la table employees...\n", nbEmployees);
    for (int i=0 ; i < nbEmployees ; i++) {
        sprintf(request,"insert into employees values (NULL,'%s', '%s');",employees[i].login, employees[i].password);
        if (mysql_query(connexion,request)) {
          finish_with_error(connexion);
        }
    }

    // Ajout de tuples dans la table CLIENTS
    printf("Ajout de %d clients dans la table clients...\n", nbClients);
    for (int i = 0; i < nbClients; i++) {
        sprintf(request, "INSERT INTO clients VALUES (NULL, '%s', '%s', %d, '%s');",
                clients[i].nom, clients[i].prenom, clients[i].numClient, clients[i].adresse);
        if (mysql_query(connexion, request)) {
            finish_with_error(connexion);
        }
    }

    // Ajout de tuples dans la table CADDIES
    printf("Ajout de %d caddies dans la table caddies...\n", nbCaddies);
    for (int i = 0; i < nbCaddies; i++) {
        sprintf(request, "INSERT INTO caddies VALUES (NULL, %d, '%s', %d);",
                caddies[i].clientId, caddies[i].date, caddies[i].payed);
        if (mysql_query(connexion, request)) {
            finish_with_error(connexion);
        }
    }

    // Ajout de tuples dans la table CADDY_ITEMS
    printf("Ajout de %d items dans la table caddy_items...\n", nbCaddyItems);
    for (int i = 0; i < nbCaddyItems; i++) {
        sprintf(request, "INSERT INTO caddy_items VALUES (NULL, %d, %d, %d);",
                caddy_items[i].caddyId, caddy_items[i].bookId, caddy_items[i].quantity);
        if (mysql_query(connexion, request)) {
            finish_with_error(connexion);
        }
    }
// ---------------------------------------------------

  // Deconnection de la BD
  mysql_close(connexion);
  exit(0);
}

void finish_with_error(MYSQL *con) {
  fprintf(stderr, "%s\n", mysql_error(con));
  mysql_close(con);
  exit(1);
}