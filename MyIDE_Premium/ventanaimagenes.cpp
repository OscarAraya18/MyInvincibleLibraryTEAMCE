#include "ventanaimagenes.h"
#include "ui_ventanaimagenes.h"
#include "ventanaprincipal.h"
#include <QFileDialog>
#include <QBuffer>
#include <QDebug>
#include "socketcliente.h"


extern SocketCliente *sock;
VentanaImagenes::VentanaImagenes(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::VentanaImagenes)
{
    ui->setupUi(this);
    imagenID = new ImagenID();
    converter = Converter::getInstance();
    //configuracion del socket
    if(sock->getConectado() == true){
        //colocar las galerias en la lista gràfica
        insertarGalerias(sock->galerias);
    }
    //bandera de imagen
    imagenLista = false;
    //colocar imagen de fondo de la ventana principal
    QPixmap pix(":/recursos/imagenes/fondoP.jpg");
    ui->fondoI->setPixmap(pix);

    QPalette pal = ui->volverButton->palette();
    pal.setColor(QPalette::Button, QColor(Qt::yellow));
    ui->volverButton->setAutoFillBackground(true);
    ui->volverButton->setPalette(pal);
    ui->volverButton->update();

     QPalette pal2 = ui->seleccionarImagenButton->palette();
     pal2.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->seleccionarImagenButton->setAutoFillBackground(true);
     ui->seleccionarImagenButton->setPalette(pal2);
     ui->seleccionarImagenButton->update();

      QPalette pal3 = ui->guardarButton->palette();
      pal3.setColor(QPalette::Button, QColor(Qt::yellow));
      ui->guardarButton->setAutoFillBackground(true);
      ui->guardarButton->setPalette(pal3);
      ui->guardarButton->update();
}

VentanaImagenes::~VentanaImagenes()
{
    delete ui;
}

void VentanaImagenes::generarByteArray(QPixmap imagenCargada)
{
    QByteArray ba;
    QBuffer buffer(&ba);
    buffer.open(QIODevice::WriteOnly);
    imagenCargada.save(&buffer,"PNG");
    for(int i = 0; i < ba.size(); i++){
        bytesImagen.append(int(ba[i]));
    }
    imagenLista = true;
}

void VentanaImagenes::generarImagen(QList<int> listaBytes)
{
    QByteArray byteArrayNuevo;
    QDataStream stream(&byteArrayNuevo,QIODevice::WriteOnly);
    for(auto x : listaBytes){
        stream << x;
    }
    qDebug()<<byteArrayNuevo;

}

void VentanaImagenes::insertarGalerias(QList<QString> galerias)
{
    if(sock->getConectado() == true){
        for(int i = 0; i < sock->galerias.size(); i++){
            ui->listaGalerias->addItem(galerias.at(i));
        }
    }
}

void VentanaImagenes::on_volverButton_clicked()
{
    //abrir la ventana principal
    VentanaPrincipal *vp = new VentanaPrincipal();
    vp->show();
    close();
    blockSignals(true);
    delete this;
}

void VentanaImagenes::on_seleccionarImagenButton_clicked()
{
    imagenLista = false;
    //abrir el directorio para obtener la ruta de alguna imagen png
    QPixmap imagen;
    QImage *objetoImagen;
    const QString rutaImagen = QFileDialog::getOpenFileName(this,tr("Seleccione una imagen"),"/home","Images (*.png)");
    objetoImagen = new QImage();
    objetoImagen->load(rutaImagen);
    imagen= QPixmap::fromImage(*objetoImagen);
    escena = new QGraphicsScene(this);
    escena->addPixmap(imagen);
    escena->setSceneRect(imagen.rect());
    ui->visorImagenes->setScene(escena);
    //enviarle la imagen para convertirle a bytes
    generarByteArray(imagen);
    //guardar la imagen en el identificador
    imagenID->imagen = imagen;
}

void VentanaImagenes::on_guardarButton_clicked()
{
    if(sock->getConectado() == true && imagenLista == true ){
        QString galeriaObjetivo = ui->listaGalerias->currentText();
         string mensaje = converter->BytesToJson(bytesImagen,ui->nombreImagen->text(),galeriaObjetivo);
         qDebug()<<mensaje.c_str();
         //enviar al servidor
         sock->setMensaje(mensaje.c_str());
         //guardar el nombre de la imagen en el identificador
         imagenID->nombre = ui->nombreImagen->text();
         //agregar el identificador a la lista
         sock->listaImagenes.append(imagenID);
         imagenLista = false;
         bytesImagen.clear();
         ui->nombreImagen->clear();
         //resetear el identificador
         imagenID = new ImagenID();
    }
    else{
        qDebug()<<"Error al guardar los datos";
    }
}




