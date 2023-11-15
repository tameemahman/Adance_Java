package dao;

import java.util.ArrayList;
import java.util.List;
import model.Student;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class StudentDao {

    public boolean addStudent(Student s) {

        Transaction t = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            t = session.beginTransaction();
            session.saveOrUpdate(s);
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

    public boolean deleteStudent(int id) {
        Transaction t = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            t = session.beginTransaction();
            Student stud = (Student) session.load(Student.class, new Integer(id));
            session.delete(stud);
            t.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        
        return false;
    }

//    public List<Student> getbyID(int id) {
//
//        Student student = new Student();
//        List<Student> sList = new ArrayList<Student>();
//
//        Transaction trans = null;
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        try {
//            trans = session.beginTransaction();
//
//            Query query = session.createQuery("from Student where id= :id");
//            query.setInteger("id", id);
//            student = (Student) query.uniqueResult();
//            sList = query.list();
//
//            trans.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return sList;
//    }

    public List<Student> viewAllStudent() {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        List<Student> sList = session.createQuery("SELECT s FROM Student s").list();

        sList.toString();

        return sList;

    }

    public void updateStudent(Student student) {
        Transaction trans = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trans = session.beginTransaction();
            session.update(student);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
        }
        finally{
        session.close();
        }

    }
    
    
       public Student getStudentById(int id){
    	System.out.println("Get employee by Id = " +id+ "\n.....................");
        Session session = null;
        Student stu = null;
        try {
        	
            session = HibernateUtil.getSessionFactory().openSession();
            stu = (Student) session.get(Student.class, id);
 
        } catch(Exception ex) {
            ex.printStackTrace();
            // handle exception here
        } finally {
            try {if(session != null) session.close();} catch(Exception ex) {}
        }
        return stu;
    }
       
       

}
