#ifndef VENTANAIMAGENES_H
#define VENTANAIMAGENES_H

#include <QWidget>
#include <QGraphicsScene>
#include <QtNetwork/QNetworkReply>
#include "socketcliente.h"
#include "converter.h"
#include "imagenid.h"

namespace Ui {
class VentanaImagenes;
}
/**
 * @brief The VentanaImagenes class Despliega una interfaz para poder tomar imagenes PNG del
 * directorio de la maquina, escribir el nombre de la imagen a enviar y ademas indicar la galeria a la que se
 * desea enviar la imagen.
 */
class VentanaImagenes : public QWidget
{
    Q_OBJECT

public:
    /**
     * @brief VentanaImagenes Constructor de la clase VentanaImagenes.
     * @param parent Padre.
     */
    explicit VentanaImagenes(QWidget *parent = nullptr);
    ~VentanaImagenes();
    /**
     * @brief generarByteArray Recibe una imagen cargada en un Pixmap y la convierte a bytes.
     * @param imagenCargada Una imagen cargada en un Pixmap.
     */
    void generarByteArray(QPixmap imagenCargada);
    /**
     * @brief generarImagen Convierte una lista de bytes en una image.
     * @param listaBytes La lista de bytes que se desean convertir.
     */
    void generarImagen(QList<int> listaBytes);
    /**
     * @brief insertarGalerias Toma los nombres de las galerias creadas y los añade a una lista gràfica.
     * @param galerias La lista de galerias creadas.
     */
    void insertarGalerias(QList<QString> galerias);

private slots:
    /**
     * @brief on_volverButton_clicked Valida los eventos del boton volver.
     */
    void on_volverButton_clicked();
    /**
     * @brief on_seleccionarImagenButton_clicked Valida los eventos del boton que selecciona las imagenes.
     */
    void on_seleccionarImagenButton_clicked();
    /**
     * @brief on_guardarButton_clicked Valida los eventos del boton guardar.
     */
    void on_guardarButton_clicked();

private:
    /**
     * @brief ui Instancia de la intefaz grafica.
     */
    Ui::VentanaImagenes *ui;
    /**
     * @brief escena Escena principal.
     */
    QGraphicsScene *escena;
    /**
     * @brief bytesImagen Una lista con los bytes de la imagen seleccionada.
     */
    QList<int> bytesImagen;
    /**
     * @brief imagenLista bandera de estados de imagen cargada.
     */
    bool imagenLista;
    /**
     * @brief converter Instancia de la clase Converter
     */
    Converter *converter;
    /**
     * @brief imagenID Objeto que va a almacenar el nombre de la imagen seleccionada y un
     * Pixmap de la imagen.
     */
    ImagenID *imagenID;
};

#endif // VENTANAIMAGENES_H
