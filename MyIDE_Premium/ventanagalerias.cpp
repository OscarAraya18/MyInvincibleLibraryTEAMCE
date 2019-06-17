#include "ventanagalerias.h"
#include "ui_ventanagalerias.h"
#include "ventanaprincipal.h"
#include "socketcliente.h"
#include <QDebug>

extern SocketCliente *sock;
ventanaGalerias::ventanaGalerias(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::ventanaGalerias)
{

    ui->setupUi(this);
    converter = Converter::getInstance();

    //colocar imagen de fondo de la ventana principal
    QPixmap pix(":/recursos/imagenes/fondoP.jpg");
    ui->fondoG->setPixmap(pix);

    QPalette pal = ui->volverButton->palette();
    pal.setColor(QPalette::Button, QColor(Qt::yellow));
    ui->volverButton->setAutoFillBackground(true);
    ui->volverButton->setPalette(pal);
    ui->volverButton->update();

     QPalette pal2 = ui->agregarButton->palette();
     pal2.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->agregarButton->setAutoFillBackground(true);
     ui->agregarButton->setPalette(pal2);
     ui->agregarButton->update();

      //cambiale el color a los textos
      QPalette palette = ui->nombreLabel->palette();
      palette.setColor(ui->nombreLabel->backgroundRole(), Qt::gray);
      palette.setColor(ui->nombreLabel->foregroundRole(), Qt::gray);
      ui->nombreLabel->setPalette(palette);
}

ventanaGalerias::~ventanaGalerias()
{
    delete ui;
}

bool ventanaGalerias::verificarNombreGaleria(QString galeria)
{
    if(sock->getConectado() == true){
        for(int i = 0; i < sock->galerias.size(); i++){
            if(galeria == sock->galerias.at(i)){
                return false;
            }
        }
        return true;
    }
    else{
        return false;
    }
}

void ventanaGalerias::on_volverButton_clicked()
{
    //abrir la ventana principal
    VentanaPrincipal *vp = new VentanaPrincipal();
    vp->show();
    close();
    destroy(true);
}

void ventanaGalerias::on_agregarButton_clicked()
{
    if(sock->getConectado() == true && ui->nombreGaleria->text() != ""){
        QString galeria = ui->nombreGaleria->text();
        if(verificarNombreGaleria(galeria)){
            string mensaje = converter->NombreGaleria(galeria);
            qDebug()<<mensaje.c_str();
            sock->setMensaje(mensaje.c_str());
            //agrego la galeria a la lista de socket
            sock->galerias.append(galeria);
            ui->nombreGaleria->setText("");
        }
    }


}
