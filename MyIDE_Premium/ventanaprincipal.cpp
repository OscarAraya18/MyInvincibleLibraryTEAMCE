#include "ventanaprincipal.h"
#include "ui_ventanaprincipal.h"
#include "ventanametadata.h"
#include "ventanaimagenes.h"
#include "ventanagalerias.h"


VentanaPrincipal::VentanaPrincipal(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::VentanaPrincipal)
{
    ui->setupUi(this);
    //colocar imagen de fondo de la ventana principal
    QPixmap pix(":/recursos/imagenes/fondoP.jpg");
    ui->fondo->setPixmap(pix);

    QPixmap pixDino(":/recursos/imagenes/dino.png");
    ui->dinoLabel->setPixmap(pixDino);

    QPixmap pixDino3(":/recursos/imagenes/dino3.png");
    ui->dinoLabel2->setPixmap(pixDino3);

    //ponerle colores a los botones
    QPalette pal = ui->metadataButton->palette();
    pal.setColor(QPalette::Button, QColor(Qt::yellow));
    ui->metadataButton->setAutoFillBackground(true);
    ui->metadataButton->setPalette(pal);
    ui->metadataButton->update();

     QPalette pal2 = ui->imagenesButton->palette();
     pal2.setColor(QPalette::Button, QColor(Qt::yellow));
     ui->imagenesButton->setAutoFillBackground(true);
     ui->imagenesButton->setPalette(pal2);
     ui->imagenesButton->update();

      QPalette pal3 = ui->galeriasButton->palette();
      pal3.setColor(QPalette::Button, QColor(Qt::yellow));
      ui->galeriasButton->setAutoFillBackground(true);
      ui->galeriasButton->setPalette(pal3);
      ui->galeriasButton->update();
}

VentanaPrincipal::~VentanaPrincipal()
{
    delete ui;
}

void VentanaPrincipal::on_metadataButton_clicked()
{
    //abrir la ventana de metadata
       ventanaMetadata *vm = new ventanaMetadata();
       vm->show();
       close();
       destroy(true);
}

void VentanaPrincipal::on_imagenesButton_clicked()
{
    //abiri la ventana imagenes
    VentanaImagenes *vi = new VentanaImagenes();
    vi->show();
    close();
    destroy(true);
}

void VentanaPrincipal::on_galeriasButton_clicked()
{
    //abrir la ventana de galerias
    ventanaGalerias *vg = new ventanaGalerias();
    vg->show();
    close();
    destroy(true);
}
