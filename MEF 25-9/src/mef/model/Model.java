/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mef.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Model extends Observable {
    public static int x1, y1, x2, y2; // para las lineas

    public static boolean HayIni = false;
    public static boolean interruptor = false; // interruptor de apagado o prendido para las 

    ArrayList<Transicion> trans; 
    ArrayList<Estado> camino; 
    String CadenaAVerificar;
    
    Maquina m;

    public Model() {
        camino = new ArrayList<Estado>();
        trans = new ArrayList<Transicion>();
    }
    
   
    public ArrayList<Estado> getCamino() {
        return camino;
    }

    public void setCadenaAVerificar(String CadenaAVerificar) {
        this.CadenaAVerificar = CadenaAVerificar;
    }
    
    public String getCadenaAVerificar() {
        return CadenaAVerificar;
    }
    
    public ArrayList<Transicion> getTrans() {
        return trans;
    }

    @XmlElement
    public void setTrans(ArrayList<Transicion> trans) {
        this.trans = trans;
    }

    @XmlElement
    public void setCamino(ArrayList<Estado> camino) {
        this.camino = camino;
    }

    public void crearCelda(int Estado, int coordenadaX, int coordenadaY, String ID) {//crea una celda
        Estado celda = new Estado(Estado, coordenadaX, coordenadaY, ID);
        camino.add(celda);//revisar, creo que no hace falta
        setChanged();
        notifyObservers(null);
    }

    public void addTrans(Estado s, Estado e, String hil) {
        Transicion t = new Transicion(s, e, hil);
        trans.add(t);
        setChanged();
        notifyObservers(null);
    }

    public boolean hayOtro(String id) {
        for (int i = 0; i < camino.size(); i++) {
            if (camino.get(i).getID().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public boolean caminoRepetido(String idS, String idE, String union) {
        for (int i = 0; i < trans.size(); i++) {
            if (idS.equals(trans.get(i).getSalida().getID()) || idE.equals(trans.get(i).getSalida().getID())) {
                if ( trans.get(i).CompararUnion(union) || trans.get(i).CompararID(idS, idE)  ) {
                    return false;
                }
            }
        }
        return true;
    }
    // if ( trans.get(i).getUnion().equals(union) || trans.get(i).CompararID(idS, idE) )
    
    public void ActualizaPos(Estado e, int x, int y) {
        e.setCoordenadaX(e.getCoordenadaX() + x);
        e.setCoordenadaY(e.getCoordenadaY() + y);
        setChanged();
        notifyObservers(null);
    }

    public void LineasSalida(int x, int y, int xa, int xb) {
        x2 = xa;
        y2 = xb;
        x1 = x;
        y1 = y;
        setChanged();
        notifyObservers(null);
    }

    public void LineasLlegada(int x, int y) {
        x2 = x;
        y2 = y;
        setChanged();
        notifyObservers(null);
    }
    
    public void guardar(OutputStream xml ){
        try{
            m = new Maquina (this);
            JAXBContext ctx=JAXBContext.newInstance(Maquina.class);
            Marshaller ma = ctx.createMarshaller();
            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ma.marshal(m, xml);
        }catch(JAXBException e){
            System.out.println("Error");
        }
    }
    
    public void recuperar (InputStream xml) {
        try{
            JAXBContext ctx=JAXBContext.newInstance(Maquina.class);
            Unmarshaller um = ctx.createUnmarshaller();
            m = (Maquina) um.unmarshal(xml);
            m.adaptar(); // == normalizar()
            this.setCamino( m.model.getCamino() );
            this.setTrans( m.model.getTrans() );
            setChanged();
            notifyObservers(null);
        }catch(JAXBException e){
            System.out.println("Error");
        }
    } 
    
    public int verificarHilera(String hilera) {
        int i;
        int j;
        boolean init = true;
        boolean encontrado = false;
        Estado e = new Estado();
        if (this.trans.isEmpty()) {
            return 4;
        }
        while (hilera.length() != 0) {
            i = 0;
            if (init) {
                while (i < this.trans.size()) {
                    if (this.trans.get(i).getSalida().getEstado() == 0) { // busca el estado inicial
                        for (j = 0; j < this.trans.get(i).getUnion().length(); j++) { // utiliza al estado inicial y su union
                            if (hilera.length() != 0) { // la hilera a verificar se acabo???
                                if (hilera.substring(0, 1).equals(this.trans.get(i).union.substring(j, j + 1))) { // compara el string 
                                    // el parametro con la salida del estado inicial
                                    
                                    e = this.trans.get(i).entrada; // guarda el siguente de estado despues de el inicial
                                    hilera = hilera.substring(1); // obtiene el siguiente string de hilera
                                    init = false; // interruptor para no buscar mas al estado imicial
                                    break;
                                }
                            }
                        }
                    }
                    i++;
                }
                if (init) {
                    return e.Estado;
                }
            } else {
                if (caminoExiste(e)) {
                    encontrado = false;
                    while (i < this.trans.size()) {
                        for (j = 0; j < this.trans.get(i).getUnion().length(); j++) {
                            if (this.trans.get(i).salida.equals(e)) {
                                if (hilera.length() == 0) {
                                    return e.Estado;
                                } else if (hilera.substring(0, 1).equals(this.trans.get(i).union.substring(j, j + 1))) {
                                    e = this.trans.get(i).entrada;
                                    hilera = hilera.substring(1);
                                    encontrado = true;
                                }
                            }
                        }
                        i++;
                    }
                    if (hilera.length() != 0 && !encontrado) {
                        return 5;
                    }
                } else {
                    return e.Estado;
                }
            }
        }
        return e.Estado;
    }

    boolean caminoExiste(Estado e) {
        int i = 0;
        while (i < this.trans.size()) {
            if (this.trans.get(i).salida.equals(e)) {
                return true;
            }
            i++;
        }
        return false;
    }
    
    public void limpiarPantalla(){//MODIFIQUE AQUI
        this.camino.removeAll(this.camino);
        this.trans.removeAll(this.trans);
        setChanged();
        notifyObservers(null);
    }
    
    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers(null); 
    }
    

}
