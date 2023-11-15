/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.StudentDao;
import java.util.List;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.Student;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class StudentManageBean {

    private Student student = new Student();

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String saveStudent() {
        StudentDao dao = new StudentDao();
        boolean status = dao.addStudent(student);

        if (status) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Saved", ""));
            return "index.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Data Not Saved", ""));
        }
        return null;
    }

    public List<Student> showAllStudent() {
        StudentDao dao = new StudentDao();
        List<Student> stlist = dao.viewAllStudent();
        return stlist;
    }

    public void delete(int id) {
        StudentDao dao = new StudentDao();
        boolean status = dao.deleteStudent(id);

        if (status) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete successful!", null));
            try {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect("index.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed!", null));
        }

    }
    
    public String editStudent(int id) {
        StudentDao dao = new StudentDao();
        student= dao.getStudentById(id);

        if (student != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("student", student);
            return "editstudent.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Product not found", ""));
            return null;
        }
    }
    

}
