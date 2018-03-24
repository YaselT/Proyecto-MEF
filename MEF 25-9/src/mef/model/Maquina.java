/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mef.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Maquina {
    Model model;

    public Maquina() {
        
    }

    public Maquina(Model model) {
        this.model = model;
    }
    
    public Model getModel() {
        return model;
    }
    
    @XmlElement
    public void setModel(Model model) {
        this.model = model;
    }
    
    public void adaptar( ) {
        Estado aux;
        for (int i=0; i<model.camino.size(); i++) {
            aux = model.camino.get(i);
            for (int j=0; j<model.trans.size(); j++) {
                Transicion auxT = model.trans.get(j);
                if (aux.getID().equalsIgnoreCase(auxT.getEntrada().getID()))
                    auxT.setEntrada(aux);
                if (aux.getID().equalsIgnoreCase(auxT.getSalida().getID()))
                    auxT.setSalida(aux);
            }
        }
    }

}
