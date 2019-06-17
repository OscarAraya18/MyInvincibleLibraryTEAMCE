#ifndef VENTANAPRINCIPAL_H
#define VENTANAPRINCIPAL_H

#include <QWidget>

namespace Ui {
class VentanaPrincipal;
}
/**
 * @brief The VentanaPrincipal class Despliega una interfaz que permite direccionar al usuario a otras
 * ventanas como metadata, agregar imagenes o agregar galerias.
 */
class VentanaPrincipal : public QWidget
{
    Q_OBJECT

public:
    /**
     * @brief VentanaPrincipal Constructor de la clase.
     * @param parent padre.
     */
    explicit VentanaPrincipal(QWidget *parent = nullptr);
    ~VentanaPrincipal();

private slots:
    /**
     * @brief on_metadataButton_clicked Envia al usuario a la ventana de metadata.
     */
    void on_metadataButton_clicked();
    /**
     * @brief on_imagenesButton_clicked Envia al usuario a la ventana de agregar imagenes.
     */
    void on_imagenesButton_clicked();
    /**
     * @brief on_galeriasButton_clicked Envia al usuario a la ventana de agregar galerias.
     */
    void on_galeriasButton_clicked();

private:
    /**
     * @brief ui Instancia de la interfaz grafica.
     */
    Ui::VentanaPrincipal *ui;
};

#endif // VENTANAPRINCIPAL_H
