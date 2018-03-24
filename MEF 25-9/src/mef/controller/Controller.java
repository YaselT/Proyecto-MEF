/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mef.controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import mef.model.Model;
import mef.view.View;

public class Controller implements MouseMotionListener, MouseListener, ActionListener {

    Model model;
    View view;
    int posAnt;
    int pos = -1;   // posicion guardada
 //   boolean interruptor = false; // interruptor de apagado o prendido para las 
    int posX, posY;
    static final String xmlData = "maquina.xml";
    FileOutputStream fos;
    FileInputStream entra;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        view.setModel(model);
        view.setController(this);
    }

    // la logica de los JJMenuItem y JButton del programa
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem jmi = (JMenuItem) e.getSource();
            if (null != jmi.getText()) {
                switch (jmi.getText()) {
                    case "Inicial":
                        view.AbrirVentanaEstados("Estado Inicial");
                        break;
                    case "Intermedio":
                        view.AbrirVentanaEstados("Estado Intermedio");
                        break;
                    case "Final":
                        view.AbrirVentanaEstados("Estado Final");
                        break;
                    case "Hilera":
                        view.AbrirVentanaHilera();
                    case "Guardar": {                        
                        try {
                            fos = new FileOutputStream(xmlData);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    model.guardar(fos);
                    break;
                    case "Recuperar": 
                        try {
                            entra = new FileInputStream(xmlData);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        model.recuperar(entra);
                        break;
                    case "Limpiar"://MODIFIQUE AQUI
                        model.limpiarPantalla();
                        Model.HayIni=false;
                        break;
                    default:
                        break;
                }
            }
        } else {
            JButton jBt = (JButton) e.getSource();
            if ("VenEstAceptar".equals(jBt.getName())) {
                String Id = view.getTxtVentEstID();
                if (model.hayOtro(Id)) {
                    switch (view.getVentanaEstados()) {
                        case "Estado Inicial":
                            if (!Model.HayIni) {
                                Model.HayIni = true;
                                model.crearCelda(0, 100, 100, Id);
                            } else {
                                JOptionPane.showMessageDialog(null, "Ya existe un estado incial", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case "Estado Intermedio":
                            model.crearCelda(1, 100, 100, Id);
                            break;
                        case "Estado Final":
                            model.crearCelda(2, 100, 100, Id);
                            break;
                        default:
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "ID repetido!!!!. Utilice otro ID para el estado", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            } else if ("SimbAceptar".equals(jBt.getName())) {
                view.AbrirVentanaSimbolos();
                if (model.caminoRepetido(model.getCamino().get(posAnt).getID(), model.getCamino().get(pos).getID(), view.getTxtVenSimb())) {
                    model.addTrans(model.getCamino().get(posAnt), model.getCamino().get(pos), view.getTxtVenSimb());
                } else {
                    JOptionPane.showMessageDialog(null, "Símbolos o transición   repetida", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            } else if ("buttHileraAceptar".equals(jBt.getName())) {
                view.AbrirVentanaHilera();
                int i = model.verificarHilera(view.getTxtVentHilera());
                switch (i) {
                   case 0:
                        JOptionPane.showMessageDialog(null, "Maquina incorrecta", "Estado inical", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Maquina incorrecta", "Estado intermedio", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Maquina correcta", "Estado final", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 4:
                        JOptionPane.showMessageDialog(null, "Maquina no inicializada", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 5:
                        JOptionPane.showMessageDialog(null, "Maquina incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
            view.CerrarVentanaEstados(); // al darle click al boton cancelar se cierran las ventanas(JDialog) pequeñas 
        }
    }

    // utilizo mouseClicked y mouseMoved para dibujar lineas
    // mouseClicked sirve cm un interruptor de prendido y apagado. MouseMoved responde solo si esta prendido 
    // abajo deja comentado cm fue q lo probe, para q lo vea.
    // en el primer click se ubica en el estado selecionado y se enciende para poder usar mouseMoved
    // ,este (mouseMoved), va actualizando la linea hasta dar click por segunda vez a otro estado, abriendo la ventana de simbolo
    public void mouseClicked(MouseEvent e) {
        pos = buscar(e.getPoint().x, e.getPoint().y);
        if (pos != -1) {
            if (!Model.interruptor) { // esta apagado???
                posAnt = pos;
                posX = e.getX();
                posY = e.getY();
                model.LineasSalida(posX, posY, posX, posY); 
            } else {
                model.LineasSalida(posX, posY, e.getX(), e.getY());
                view.AbrirVentanaSimbolos();
            }
            Model.interruptor = !Model.interruptor; // si esta prendido lo apaga y si esta apado lo prende
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (Model.interruptor) {
            model.LineasLlegada(e.getX(), e.getY());
        }
    }

    // utilizo mouseDragged y mousePressed para q el objeto se puede mover.
    // En el mouseDragged no pueden haber for xq no se actualizan bien los datos y le da embolia
    // por eso lo utilizo (el mouseDragged) para saber si se presiono a un circulo y guardo la posicion
    // en el model, para pasaserselo a mouseDragged y asi actualizar los datos.
    // El menos uno es para saber si lo encontro
    @Override
    public void mousePressed(MouseEvent e) {
        posX = e.getPoint().x;
        posY = e.getPoint().y;
        pos = buscar(posX, posY);
    }

    public void mouseDragged(MouseEvent e) {
        if (pos != -1) {
            model.ActualizaPos(model.getCamino().get(pos), e.getX() - posX, e.getY() - posY);
            posX = e.getPoint().x;
            posY = e.getPoint().y;
        }
    }

    public int buscar(int X, int Y) {
        int x, y;
        for (int i = 0; i < model.getCamino().size(); i++) {
            x = model.getCamino().get(i).getCoordenadaX();
            y = model.getCamino().get(i).getCoordenadaY();
            Rectangle r = new Rectangle(x, y, 40, 40);
            if (r.contains(X, Y)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
