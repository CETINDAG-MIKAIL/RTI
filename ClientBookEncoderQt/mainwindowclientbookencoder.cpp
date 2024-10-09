#include "mainwindowclientbookencoder.h"
#include "ui_mainwindowclientbookencoder.h"
#include "unistd.h"
#include <QInputDialog>
#include <QMessageBox>
#include <iostream>

#include "protocoleC.h"
#include "protocole.h"
#include "TCP.h"
#include "FileHelper.h"


using namespace std;

MainWindowClientBookEncoder::MainWindowClientBookEncoder(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindowClientBookEncoder)
{
    ui->setupUi(this);
    //::close(2);

    //this->setFixedSize(1068, 301);

    // Configuration de la table des employes (Personnel Garage)
    ui->tableWidgetEncodedBooks->setColumnCount(9);
    ui->tableWidgetEncodedBooks->setRowCount(0);
    QStringList labelsTableEmployes;
    labelsTableEmployes << "Id" << "Titre" << "Auteur" << "Sujet" << "ISBN" << "Pages" << "Année" << "Prix" << "Stock";
    ui->tableWidgetEncodedBooks->setHorizontalHeaderLabels(labelsTableEmployes);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setVisible(true);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetEncodedBooks->verticalHeader()->setVisible(false);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setStyleSheet("background-color: lightyellow");
    int columnWidths[] = {35, 250, 200, 200, 150, 50, 50, 50, 40};
    for (int col = 0; col < 9; ++col)
        ui->tableWidgetEncodedBooks->setColumnWidth(col, columnWidths[col]);

    this->logoutOk();

    // Exemples d'utilisation (à supprimer)
    this->addTupleTableBooks(1,"Les Thanatonautes","Bernard Werber","Science-Fiction","978-2253139225",505,1999,9.7f,3);
    this->addTupleTableBooks(6,"Dune","Frank Herbert","Science-Fiction","978-2266320481",929,2021,11.95f,13);
    this->addTupleTableBooks(13,"Le silence des agneaux","Thomas Harris","Thriller","978-2266208949",377,2015,7.7f,17);

    this->addComboBoxAuthors("Bernard Werber");
    this->addComboBoxAuthors("Dan Brown");

    this->addComboBoxSubjects("Roman");
    this->addComboBoxSubjects("Science-fiction");


    clearComboBoxAuthors();
    clearComboBoxSubjects();
   


    int portServeur = GetSettingInt("../config.txt", "PORT_ENCODING");


    printf("-----**************$ port serveur = %d\n\n", portServeur);

    char ipServeur[30] = {"localhost"};

    // Connexion sur le serveur  
    if ((sClient = ClientSocket((char *)"localhost", portServeur)) == -1) 
    { 
        perror("Erreur de ClientSocket"); 
        exit(1); 
    } 
    printf("Connecte sur le serveur.\n");


}

MainWindowClientBookEncoder::~MainWindowClientBookEncoder() {
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table des livres encodés (ne pas modifier) ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::addTupleTableBooks(int id,
                                                     string title,
                                                     string author,
                                                     string subject,
                                                     string isbn,
                                                     int pageCount,
                                                     int publishYear,
                                                     float price,
                                                     int stockQuantity)
{
    int nb = ui->tableWidgetEncodedBooks->rowCount();
    nb++;
    ui->tableWidgetEncodedBooks->setRowCount(nb);
    ui->tableWidgetEncodedBooks->setRowHeight(nb-1,10);

    // id
    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(id));
    ui->tableWidgetEncodedBooks->setItem(nb-1,0,item);

    // title
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setText(QString::fromStdString(title));
    ui->tableWidgetEncodedBooks->setItem(nb-1,1,item);

    // author
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(author));
    ui->tableWidgetEncodedBooks->setItem(nb-1,2,item);

    // subject
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(subject));
    ui->tableWidgetEncodedBooks->setItem(nb-1,3,item);

    // isbn
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(isbn));
    ui->tableWidgetEncodedBooks->setItem(nb-1,4,item);

    // pageCount
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(pageCount));
    ui->tableWidgetEncodedBooks->setItem(nb-1,5,item);

    // publishYear
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(publishYear));
    ui->tableWidgetEncodedBooks->setItem(nb-1,6,item);

    // price
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(price));
    ui->tableWidgetEncodedBooks->setItem(nb-1,7,item);

    // stockQuantity
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(stockQuantity));
    ui->tableWidgetEncodedBooks->setItem(nb-1,8,item);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::clearTableBooks() {
    ui->tableWidgetEncodedBooks->setRowCount(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles des comboboxes (ne pas modifier) //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::addComboBoxAuthors(string author){
    ui->comboBoxAuthors->addItem(QString::fromStdString(author));
}

string MainWindowClientBookEncoder::getSelectionAuthor() const {
    return ui->comboBoxAuthors->currentText().toStdString();
}

void MainWindowClientBookEncoder::clearComboBoxAuthors() {
    ui->comboBoxAuthors->clear();
}

void MainWindowClientBookEncoder::addComboBoxSubjects(string subject){
    ui->comboBoxSubjects->addItem(QString::fromStdString(subject));
}

string MainWindowClientBookEncoder::getSelectionSubject() const {
    return ui->comboBoxSubjects->currentText().toStdString();
}

void MainWindowClientBookEncoder::clearComboBoxSubjects() {
    ui->comboBoxSubjects->clear();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonction utiles de la fenêtre (ne pas modifier) ////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientBookEncoder::getTitle() const {
    return ui->lineEditTitle->text().toStdString();
}

string MainWindowClientBookEncoder::getIsbn() const {
    return ui->lineEditIsbn->text().toStdString();
}

int MainWindowClientBookEncoder::getPageCount() const {
    return ui->spinBoxPageCount->value();
}

float MainWindowClientBookEncoder::getPrice() const {
    return ui->doubleSpinBoxPrice->value();
}

int MainWindowClientBookEncoder::getPublishYear() const {
    return ui->spinBoxPublishYear->value();
}

int MainWindowClientBookEncoder::getStockQuantity() const {
    return ui->spinBoxStockQuantity->value();
}

void MainWindowClientBookEncoder::loginOk() {
    ui->pushButtonClear->setEnabled(true);
    ui->pushButtonAddBook->setEnabled(true);
    ui->pushButtonAddAuthor->setEnabled(true);
    ui->pushButtonAddSubject->setEnabled(true);
    ui->actionLogin->setEnabled(false);
    ui->actionLogout->setEnabled(true);
}

void MainWindowClientBookEncoder::logoutOk() {
    ui->pushButtonClear->setEnabled(false);
    ui->pushButtonAddBook->setEnabled(false);
    ui->pushButtonAddAuthor->setEnabled(false);
    ui->pushButtonAddSubject->setEnabled(false);
    ui->actionLogin->setEnabled(true);
    ui->actionLogout->setEnabled(false);
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier) ///////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::dialogMessage(const string& title,const string& message) {
   QMessageBox::information(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::dialogError(const string& title,const string& message) {
   QMessageBox::critical(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientBookEncoder::dialogInputText(const string& title,const string& question) {
    return QInputDialog::getText(this,QString::fromStdString(title),QString::fromStdString(question)).toStdString();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientBookEncoder::dialogInputInt(const string& title,const string& question) {
    return QInputDialog::getInt(this,QString::fromStdString(title),QString::fromStdString(question));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions gestion des boutons et items de menu (TO DO) /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::on_pushButtonAddAuthor_clicked() {
    string lastName = this->dialogInputText("Nouvel auteur","Nom ?");
    string firstName = this->dialogInputText("Nouvel auteur","Prénom ?");
    string birthDate = this->dialogInputText("Nouvel auteur","Date de naissance (yyyy-mm-dd) ?");
    cout << "Nom : " << lastName << endl;
    cout << "Prénom : " << firstName << endl;
    cout << "Date de naissance : " << birthDate << endl;
    
    string reponse = AjouterAuteur(lastName, firstName, birthDate, sClient);
    dialogMessage("Ajout auteur",reponse);//6le message
    vector<string> VecSujet;
    addComboBoxSubjects(lastName);
    VecSujet = GetAuteur(sClient);
    for (int i = 0; i < VecSujet.size(); ++i)
    {
        addComboBoxSubjects(VecSujet[i]);
    }
    // GetAuteur(sClient); //1#15#lukas1#26#mikail 
}
    

void MainWindowClientBookEncoder::on_pushButtonAddSubject_clicked() {
    string name = this->dialogInputText("Nouveau sujet","Nom ?");
    cout << "Nom : " << name << endl;

    vector<string> reponse =  AjouterSujet(name, sClient);
    dialogMessage("Ajout sujet",reponse[1]);
    vector<string> VecSujet;
    addComboBoxSubjects(name);
    VecSujet = GetSubjects(sClient);
    for (int i = 0; i < VecSujet.size(); ++i)
    {
        addComboBoxSubjects(VecSujet[i]);
    }
}

void MainWindowClientBookEncoder::on_pushButtonAddBook_clicked() {
    cout << "title = " << this->getTitle() << endl;
    cout << "Isbn = " << this->getIsbn() << endl;
    cout << "PageCount = " << this->getPageCount() << endl;
    cout << "Price = " << this->getPrice() << endl;
    cout << "PublishYear = " << this->getPublishYear() << endl;
    cout << "Stock = " << this->getStockQuantity() << endl;

    cout << "selection auteur = " << this->getSelectionAuthor() << endl;
    cout << "selection sujet  = " << this->getSelectionSubject() << endl;
    vector<string> VecLivre; 

    VecLivre.push_back(getSelectionAuthor());
    VecLivre.push_back(getSelectionSubject());
    VecLivre.push_back(getTitle());
    VecLivre.push_back(getIsbn());
    VecLivre.push_back(to_string(getPageCount()));
    VecLivre.push_back(to_string(getStockQuantity()));
    VecLivre.push_back(to_string(getPrice()));
    VecLivre.push_back(to_string(getPublishYear()));


    vector<string> reponse = AjouterLivre(VecLivre, sClient);
    cout << "AjouterLivre" << endl;
    for (int i = 0; i < reponse.size(); i++)
    {
        cout << "reponse" << reponse[i] << endl;
    }

    if(reponse[1] == "ok"){
        dialogMessage("Ajouter Livre",reponse[1]);
        // addTupleTableBooks
        clearTableBooks();

    }

    else
        dialogError("Ajouter Livre",reponse[1]);
 

}

void MainWindowClientBookEncoder::on_pushButtonClear_clicked() {
    ui->lineEditTitle->clear();
    ui->lineEditIsbn->clear();
    ui->spinBoxPageCount->setValue(0);
    ui->doubleSpinBoxPrice->setValue(0);
    ui->spinBoxPublishYear->setValue(0);
    ui->spinBoxStockQuantity->setValue(0);
}

void MainWindowClientBookEncoder::on_actionLogin_triggered() {
    string varLogin = this->dialogInputText("Entrée en session","Login ?");
    cout << "------ login = " << varLogin << endl;

    string password = this->dialogInputText("Entrée en session","Password ?");
    cout << "------ password = " << password << endl;
    
    vector<string> reponse = Login(varLogin, password, sClient);
    cout << "reponse.size() : " << reponse.size() << endl;
    for (int i = 0; i < reponse.size(); i++)
    {
        cout << "reponse : " <<  reponse[i] << endl;
    }
    
    if(reponse[0] == "ok"){
        dialogMessage("Login",reponse[1]);
        vector<string> VecSujet;
        vector<string> VecAuteur;

        VecSujet = GetSubjects(sClient);
        VecAuteur = GetAuteur(sClient);

        for (int i = 0; i < VecSujet.size(); ++i)
        {
            addComboBoxSubjects(VecSujet[i]);
        }
        for (int i = 0; i < VecAuteur.size(); ++i)
        {
            addComboBoxSubjects(VecAuteur[i]);
        }

        this->loginOk();
    }
    else
        if(reponse[0] == "ko")
            dialogError("Login",reponse[1]);   
}

void MainWindowClientBookEncoder::on_actionLogout_triggered() {
    // informer le serveur qu'il y a un logout 
    vector<string> reponse = Logout(sClient);

    if(reponse[0] == "ok"){
        dialogMessage("Logout",reponse[1]);
        this->loginOk();
    }
    else
        dialogError("Logout",reponse[1]);

    this->logoutOk();
}

void MainWindowClientBookEncoder::on_actionQuitter_triggered(){
    QApplication::exit(0);
}
