#include "ventanametadata.h"
#include "ui_ventanametadata.h"
#include "ventanaprincipal.h"
#include "consola.h"
#include <QDebug>
#include <QColorDialog>
#include "socketcliente.h"


extern SocketCliente *sock;
ventanaMetadata::ventanaMetadata(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::ventanaMetadata)
{
    ui->setupUi(this);
    converter = Converter::getInstance();

    if(sock->getConectado() == true){
        connect(sock,SIGNAL(NewMensaje(QString)),SLOT(printMensaje(QString)));
    }
    QPalette palEC = ui->editorCodigo->palette();
    palEC.setColor(QPalette::Base,Qt::black);
    palEC.setColor(QPalette::Text,QColor(Qt::blue));
    ui->editorCodigo->setPalette(palEC);
    ui->editorCodigo->update();

    //colocar imagen de fondo de la ventana principal
    QPixmap pix(":/recursos/imagenes/fondoP.jpg");
    ui->fondoM->setPixmap(pix);

    //colores de los botones
    QPalette pal = ui->volverButton->palette();
    pal.setColor(QPalette::Button, QColor(Qt::yellow));
    ui->volverButton->setAutoFillBackground(true);
    ui->volverButton->setPalette(pal);
    ui->volverButton->update();

     QPalette pal2 = ui->ejecutarButton->palette();
     pal2.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->ejecutarButton->setAutoFillBackground(true);
     ui->ejecutarButton->setPalette(pal2);
     ui->ejecutarButton->update();

     QPalette pal3 = ui->commitButton->palette();
     pal3.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->commitButton->setAutoFillBackground(true);
     ui->commitButton->setPalette(pal3);
     ui->commitButton->update();

     QPalette pal4 = ui->rollbackButton->palette();
     pal4.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->rollbackButton->setAutoFillBackground(true);
     ui->rollbackButton->setPalette(pal4);
     ui->rollbackButton->update();

     QPalette pal5 = ui->discoButton->palette();
     pal5.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->discoButton->setAutoFillBackground(true);
     ui->discoButton->setPalette(pal5);
     ui->discoButton->update();

}

ventanaMetadata::~ventanaMetadata()
{
    delete ui;
}

bool ventanaMetadata::verificarSintaxis()
{
    // guardar cada linea del textEdit en una lista de QStrings
    QString data = ui->editorCodigo->toPlainText();
    lineas = data.split(QRegExp("[\n]"),QString::SkipEmptyParts);

    for(int i =0 ; i < lineas.size(); i++){
        QString linea = lineas[i];
        if(!linea.endsWith(";")){
            return false;
        }
        else if(linea.startsWith("SELECT") && linea.contains("FROM") && linea.contains("WHERE") && linea.endsWith(";")){
            qDebug()<< QString::number(i) +" : SELECT";

        }
        else if(linea.startsWith("UPDATE")){
            qDebug()<<QString::number(i) + " : UPDATE";
        }
        else if(linea.startsWith("INSERT") && linea.contains("INTO") && linea.contains("VALUES") && linea.endsWith(";")){
            qDebug()<<QString::number(i) + " INSERT";
        }
        else if(linea.startsWith("DELETE") && linea.contains("FROM") && linea.contains("WHERE") && linea.endsWith(";")){
            qDebug()<<QString::number(i) + " : DELETE";
        }
    }
    return true;
}

void ventanaMetadata::on_volverButton_clicked()
{
    //abrir la ventana principal
    blockSignals(true);
    VentanaPrincipal *vp = new VentanaPrincipal();
    vp->show();
    close();
    destroy(true);
}

void ventanaMetadata::on_ejecutarButton_clicked()
{

    if(sock->getConectado() == true && verificarSintaxis() == true){
        string mensaje = converter->Scripts(lineas);
        qDebug()<<mensaje.c_str();
        sock->setMensaje(mensaje.c_str());
        lineas.clear();
        ui->editorCodigo->clear();

    }
}

void ventanaMetadata::printMensaje(QString mensaje)
{

    QString nuevo = mensaje.remove(0,2);
    qDebug()<<"Mensaje en Metadata"<<nuevo;
    converter->funcionGeneral(mensaje.toStdString().c_str());
    Consola *c = new Consola();
   // mandar el mensaje partido
    c->recibirMensaje(converter->m, converter->m2,sock->listaImagenes);
    c->show();

}

void ventanaMetadata::on_commitButton_clicked()
{
    if(sock->getConectado() == true){
        string mensaje = converter->Commit();
        sock->setMensaje(mensaje.c_str());
        qDebug()<<mensaje.c_str();

    }
}

void ventanaMetadata::on_rollbackButton_clicked()
{
    if(sock->getConectado() == true){
        string mensaje = converter->Rollback();
        sock->setMensaje(mensaje.c_str());
        qDebug()<<mensaje.c_str();
    }

}

void ventanaMetadata::on_discoButton_clicked()
{
    if(sock->getConectado() == true){
        string mensaje = converter->EliminarDiscos(ui->discos->value());
        sock->setMensaje(mensaje.c_str());
        qDebug()<<mensaje.c_str();
    }
}
