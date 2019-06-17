#include "consola.h"
#include "ui_consola.h"
#include "ventanametadata.h"
#include <QDebug>


Consola::Consola(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Consola)
{
    ui->setupUi(this);
    imagenCargada = false;
    QPixmap pix(":/recursos/imagenes/fondoP.jpg");
    ui->fondoC->setPixmap(pix);
    escena = new QGraphicsScene(this);

    QPalette pal = ui->pushButton->palette();
    pal.setColor(QPalette::Button, QColor(Qt::yellow));
    ui->pushButton->setAutoFillBackground(true);
    ui->pushButton->setPalette(pal);
    ui->pushButton->update();

     QPalette pal2 = ui->verImagenButton->palette();
     pal2.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->verImagenButton->setAutoFillBackground(true);
     ui->verImagenButton->setPalette(pal2);
     ui->verImagenButton->update();

     QPalette palEC = ui->salidas->palette();
     palEC.setColor(QPalette::Base,Qt::black);
     palEC.setColor(QPalette::Text,QColor(Qt::blue));
     ui->salidas->setPalette(palEC);
     ui->salidas->update();
}

Consola::~Consola()
{
    delete ui;
}



void Consola::recibirMensaje(QString mensaje, QString imagen,QList<ImagenID*> identificadores)
{

    ui->salidas->insertPlainText(mensaje);
    //colocar la imagen comparando con el nombre de la imagen
    for(int i = 0; i < identificadores.size(); i++){
        if(identificadores.at(i)->nombre == imagen){
            qDebug()<<"coincidencia de nombre";
            imagenAsociada = identificadores.at(i)->imagen;
            imagenCargada = true;
            /*
            escena->addPixmap(identificadores.at(i)->imagen);
            escena->setSceneRect(identificadores.at(i)->imagen.rect());
            ui->visorImagenes->setScene(escena);*/
        }
    }
}



void Consola::on_pushButton_clicked()
{
    //abrir la ventana de metadata
    close();
    destroy(true);

}

void Consola::on_verImagenButton_clicked()
{
    if(imagenCargada == true){
        escena->addPixmap(imagenAsociada);
        escena->setSceneRect(imagenAsociada.rect());
        ui->visorImagenes->setScene(escena);
        imagenCargada = false;
    }

}
