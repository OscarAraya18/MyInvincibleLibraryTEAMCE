#ifndef VENTANAGALERIAS_H
#define VENTANAGALERIAS_H

#include <QWidget>
#include "converter.h"
namespace Ui {
class ventanaGalerias;
}
/**
 * @brief The ventanaGalerias class Permite desplegar una interfaz para poder escribir el nombre de
 * las galerias y ademas permite enviarlas al servidor.
 */
class ventanaGalerias : public QWidget
{
    Q_OBJECT

public:
    /**
     * @brief ventanaGalerias Constructor de la clase ventanaGalerias.
     * @param parent Padre.
     */
    explicit ventanaGalerias(QWidget *parent = nullptr);
    ~ventanaGalerias();
    /**
     * @brief verificarNombreGaleria Valida que no existan galerias con el mismo nombre antes de
     * enviarlas al servidor.
     * @param galeria El nombre de la galeria que se desea validar.
     * @return Un estado de aceptacion.
     */
    bool verificarNombreGaleria(QString galeria);

private slots:
    /**
     * @brief on_volverButton_clicked Valida los eventos del boton volver.
     */
    void on_volverButton_clicked();
    /**
     * @brief on_agregarButton_clicked Valida los eventos del boton agregar.
     */
    void on_agregarButton_clicked();

private:
    /**
     * @brief ui Instancia de la interfaz grafica.
     */
    Ui::ventanaGalerias *ui;
    /**
     * @brief converter Instancia de la clase converter.
     */
    Converter *converter;
};

#endif // VENTANAGALERIAS_H
