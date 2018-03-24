
package mef.model;

public class Estado {

    int Estado;
    int coordenadaX;
    int coordenadaY;
    String ID;

    public Estado() {
        Estado = 0;
        coordenadaX = 0;
        coordenadaY = 0;
        ID = "";
    }

    public Estado(int Estado, int coordenadaX, int coordenadaY, String ID) {
        this.Estado = Estado;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.ID = ID;
    }
   
    public void setEstado(int Estado) {
        this.Estado = Estado;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    
    public int getEstado() {
        return Estado;
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }
    
    public String getID() {
        return ID;
    }
    
}
