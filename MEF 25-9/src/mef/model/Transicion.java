/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mef.model;


public class Transicion {
    Estado salida;
    Estado entrada;
    String union;
    
    public Transicion(Estado salida, Estado entrada, String union) {
        this.salida = salida;
        this.entrada = entrada;
        this.union = union;
    }
    
    public Transicion( ) {
        this.salida = null;
        this.entrada = null;
        this.union = "";
    }
    
    public Estado getSalida() {
        return salida;
    }

    public Estado getEntrada() {
        return entrada;
    }

    public String getUnion() {
        return union;
    }

    public void setSalida(Estado salida) {
        this.salida = salida;
    }

    public void setEntrada(Estado entrada) {
        this.entrada = entrada;
    }

    public void setUnion(String union) {
        this.union = union;
    }
            
    public boolean Comparar( ) {
        return entrada.equals(salida);
    } 
    
    public boolean CompararID( String idS, String idE ) {
        return ( salida.getID().equals(idS) && entrada.getID().equals(idE) ||  
                salida.getID().equals(idE) && entrada.getID().equals(idS));
           
    } 
    
    public boolean CompararUnion ( String u ) {
        int j=0; int k=0;
        String letra = u; String g; String p;
        while (letra.length() != 0 ) {
            g = letra.substring(0,1);
            System.out.println(g);
            while (k<union.length()) {
                p = union.substring(k, k+1);
                System.out.println(p);
                if ( g.equals( p ) ) {
                    System.out.println("entre2");
                    return true;
                }
            }
            letra = letra.substring(1);
        }
        return false;
    } 
}