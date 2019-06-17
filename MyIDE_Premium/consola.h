#ifndef CONSOLA_H
#define CONSOLA_H

#include <QWidget>
#include <QGraphicsScene>
#include "imagenid.h"
namespace Ui {
class Consola;
}
/**
 * @brief The Consola class Permite mostrar la columna que se ha seleccionado en
 * el editor de código y ademas puede mostrar la imagen asociada a la metadata seleccionada.
 */
class Consola : public QWidget
{
    Q_OBJECT

public:
    /**
     * @brief Consola Constructor de la clase consola.
     * @param parent Padre.
     */
    explicit Consola(QWidget *parent = nullptr);
    ~Consola();
    /**
     * @brief recibirMensaje Funciona para recibir la columna seleccionada en el editor de codigo
     * y ademas recibe una lista de identificadores para poder desplegar la imagen asociada.
     * @param mensaje La columna seleccionada en el editor de codigo.
     * @param imagen El nombre de la imagen asociada.
     * @param identificadores Una lista de identificadores de imagenes.
     */
    void recibirMensaje(QString mensaje, QString imagen,QList<ImagenID*> identificadores);

private slots:
    /**
     * @brief on_pushButton_clicked Valida el evento del boton volver.
     */
    void on_pushButton_clicked();
    /**
     * @brief on_verImagenButton_clicked Valida el evento del boton ver imagen asociada.
     */
    void on_verImagenButton_clicked();

private:
    /**
     * @brief ui instancia de la interfaz.
     */
    Ui::Consola *ui;
    /**
     * @brief escena escena principal.
     */
    QGraphicsScene *escena;
    /**
     * @brief imagenAsociada imagen que se va a mostrar.
     */
    QPixmap imagenAsociada;
    /**
     * @brief imagenCargada bandera que valida si ya se cargó la imagen.
     */
    bool imagenCargada;
};

#endif // CONSOLA_H
