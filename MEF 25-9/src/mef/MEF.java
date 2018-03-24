/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mef;

import mef.controller.Controller;
import mef.model.Model;
import mef.view.View;

/**
 *
 * @author anderson
 */
public class MEF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model model=new Model() ;
        View view = new View();
        Controller controller = new Controller(model,view);
        view.setVisible(true);
    }
    
}
