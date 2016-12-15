package org.occideas.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SetupDB {

	public static void main(String[] args) {
		//AdditionalField a = new AdditionalField();
		
		SessionFactory sf = HibernateUtility.getSessionFactory();
		Session session = sf.openSession();

		session.beginTransaction();

		Module m = new Module();
		m.setName("test");
		session.save(m);
		
		/*AdditionalField somerow = (AdditionalField) session.get(AdditionalField.class, 1L); 
		somerow.setValue("new B value");
		somerow.setType("some type");
		session.save(somerow);*/
		
		session.getTransaction().commit();
		session.close();
	}

}
