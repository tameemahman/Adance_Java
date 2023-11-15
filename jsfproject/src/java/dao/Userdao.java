/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Usertable;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author Tameem
 */
public class Userdao {

    public boolean adduser(Usertable u) {

        Transaction t = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            t = session.beginTransaction();
            session.saveOrUpdate(u);
            t.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            t.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public List<Usertable> ViewAllUser() {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        List<Usertable> uList = session.createQuery("SELECT u FROM Usertable u ").list();

        uList.toString();
        return uList;

    }

    
    
    
    
    
    
    
    
    
    
    
    
}
