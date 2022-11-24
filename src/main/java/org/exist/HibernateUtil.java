package org.exist;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    protected Session createSession() {
        Session session;
        try {
            // Create the SessionFactory from hibernate.cfg.xml and return the session
            Configuration con = new Configuration().configure()
                    .addAnnotatedClass(Person.class)
                    .addAnnotatedClass(Contact.class)
                    .addAnnotatedClass(Role.class);
            SessionFactory sf = con.buildSessionFactory();
            session = sf.openSession();
        } catch (Throwable ex) {
            // logging exception to make sure it works
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return session;
    }

}
