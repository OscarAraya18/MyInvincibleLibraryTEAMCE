#ifndef IMAGENID_H
#define IMAGENID_H

#include <QByteArray>
#include <QString>
#include <QPixmap>

/**
 * @brief The ImagenID class Permite tener el nombre de una imagen enviada al servidor y además tener
 * un Pixmap de la imagen.
 */
class ImagenID
{
public:
    /**
     * @brief ImagenID Constructor de la clase ImagenID.
     */
    ImagenID();
    /**
     * @brief imagen Imagen asociada al nombre.
     */
    QPixmap imagen;
    /**
     * @brief nombre Nombre de la imagen.
     */
    QString nombre;
};

#endif // IMAGENID_H
