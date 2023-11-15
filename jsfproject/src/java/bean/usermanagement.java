/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.Userdao;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.Usertable;

/**
 *
 * @author Tameem
 */
@ManagedBean
@SessionScoped
public class usermanagement {

    private Usertable ut = new Usertable();

    public Usertable getUt() {
        return ut;
    }

    public void setUt(Usertable ut) {
        this.ut = ut;
    }

    public String saveUsertable() {

        Userdao dao = new Userdao();
        boolean status = dao.adduser(ut);

        if (status) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Saved", ""));
            return "index.xhtml?faces-redirect=true";

    }
        
        else{
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Data not Saved", ""));
       }
        return  null ;
        
    }

public  List<Usertable> showAllUsertables(){

Userdao dao = new Userdao();
List<Usertable> ulist = dao.ViewAllUser();
return ulist;

}
    
    
    
    
    
    
    
    
    
    
    
    
    
    




    
    
}

 
