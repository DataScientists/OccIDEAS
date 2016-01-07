package org.occideas;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public class ModuleHelper {

    public static Module getPersistantModule(Module module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            module = (Module) hibernateSession.get(Module.class, module.getIdNode());
            // NodeHelper.getAllNodes(module);
            transaction.commit();
            hibernateSession.flush();

        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not saved - " + e.getMessage());
        } finally {
            hibernateSession.close();
        }
        return module;
    }

    public static Fragment getPersistantFragment(Fragment module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            module = (Fragment) hibernateSession.get(Fragment.class, module.getIdNode());
            // NodeHelper.getAllNodes(module);
            transaction.commit();
            hibernateSession.flush();

        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not saved - " + e.getMessage());
        } finally {
            hibernateSession.close();
        }
        return module;
    }

    public static long saveOrUpdate(Node module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            hibernateSession.saveOrUpdate(module);
            transaction.commit();
            hibernateSession.flush();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not saved - " + e.getMessage());
        } finally {
            hibernateSession.close();
        }
        return module.getIdNode();
    }

    @SuppressWarnings("unchecked")
    public static List<Module> getAllModules() {
        Session hibernateSession = null;
        List<Module> objectList = new ArrayList<Module>();
        
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            Criteria criteria = hibernateSession.createCriteria(Module.class);
            objectList = criteria.list();
        
           hibernateSession.close();
        
        return (List<Module>) objectList;
    }


    public static String getAllModulesAsJson() {
/*        Session hibernateSession = null;
        Transaction transaction = null;
        List<Module> objectList = new ArrayList<Module>();
        JSONArray jsonArray = new JSONArray();
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            Criteria criteria = hibernateSession.createCriteria(Module.class).addOrder(Order.asc("name"));
            objectList = criteria.list();
            if (objectList.size() > 0) {
                for (Module module : objectList) {
                    if (!module.getDeleted().equals(Constant.NODE_DELETED)) {
                        JSONObject jsonObject = new JSONObject();
                        String name = "Not defined";
                        String description = "Not defined";
                        if (module.getDescription() != null) {
                            // For some reason these name and description are
                            // stored in the database in the opposite fields
                            name = module.getDescription();
                        }
                        if (module.getName() != null) {
                            // For some reason these name and description are
                            // stored in the database in the opposite fields
                            description = module.getName();
                        }
                        jsonObject.put("description", name);
                        jsonObject.put("name", description);
                        jsonObject.put("idmodule", module.getIdNode());
                        JSONArray jsonNotes = new JSONArray();
                        for (NodeNote note : module.getNodeNotes()) {
                            if (!note.getNote().getDeleted().equals(Constant.NOTE_DELETED)) {
                                JSONObject jsonNote = new JSONObject();
                                jsonNote.put("description", note.getNote().getText());
                                jsonNote.put("idNote", note.getNote().getIdNote());
                                jsonNotes.put(jsonNote);
                            }
                        }
                        jsonObject.put("notes", jsonNotes);
                        jsonArray.put(jsonObject);
                    }
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error", true);
                jsonObject.put("errormessage", "database may not be initialised");
                jsonArray.put(jsonObject);
            }

            hibernateSession.getTransaction().commit();
        } catch (Throwable he) {
            if (transaction != null)
                transaction.rollback();
            System.err.println(he.getMessage());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", true);
            jsonObject.put("errormessage", he.getMessage());
            jsonArray.put(jsonObject);
        } finally {
            if (hibernateSession != null) {
                hibernateSession.close();
            }
        }*/

        return null;
    }

    public static boolean checkNameExists(Node module) {
        boolean result = false;
        Session hibernateSession = null;
        Transaction transaction = null;
        // List<Module> objectList = new ArrayList<Module>();
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            String hql = "FROM Module M WHERE M.name = :name";
            Query query = hibernateSession.createQuery(hql);
            query.setParameter("name", module.getName());
            if (query.list().size() != 0) {
                result = true;
            }
            hibernateSession.getTransaction().commit();
        } catch (HibernateException he) {
            if (transaction != null)
                transaction.rollback();
            System.err.println(he.getMessage());
        } finally {
            hibernateSession.close();
        }
        return result;
    }

    public static Module merge(Module module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            hibernateSession.merge(module);
            transaction.commit();
            hibernateSession.flush();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not refreshed");
        } finally {
            hibernateSession.close();
        }
        return module;
    }

    public static Question getFirstQuestion(Module module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        Question firstQuestion = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            module = (Module) hibernateSession.get(Module.class, module.getIdNode());

            Hibernate.initialize(module.getChildNodes());
            for (Node child : module.getChildNodes()) {
                if (!child.getDeleted().equals(Constant.NODE_DELETED)) {
                    firstQuestion = (Question) child;
                    break;
                }
            }
            // firstQuestion = (Question) module.getChildNodes().get(0);
            Hibernate.initialize(firstQuestion.getChildNodes());
            transaction.commit();
            hibernateSession.flush();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not refreshed");
        } finally {
            hibernateSession.close();
        }
        return firstQuestion;
    }

    public static Question getFirstQuestion(PossibleAnswer possibleanswer) {
        Session hibernateSession = null;
        Transaction transaction = null;
        Question firstQuestion = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            possibleanswer = (PossibleAnswer) hibernateSession.get(PossibleAnswer.class, possibleanswer.getIdNode());

            Hibernate.initialize(possibleanswer.getChildNodes());
            for (Node child : possibleanswer.getChildNodes()) {
                if (!child.getDeleted().equals(Constant.NODE_DELETED)) {
                    firstQuestion = (Question) child;
                    break;
                }
            }
            Hibernate.initialize(firstQuestion.getChildNodes());
            transaction.commit();
            hibernateSession.flush();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not refreshed");
        } finally {
            hibernateSession.close();
        }
        return firstQuestion;
    }

    public static Question getRootQuestionAfter(Module module, int sequence) {
        Session hibernateSession = null;
        Transaction transaction = null;
        Question nextQuestion = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            module = (Module) hibernateSession.get(Module.class, module.getIdNode());

            Hibernate.initialize(module.getChildNodes());
            for (Node child : module.getChildNodes()) {
                if (!child.getDeleted().equals(Constant.NODE_DELETED)) {
                    if (child instanceof Question) {
                        if (child.getSequence() > sequence) {
                            nextQuestion = (Question) child;
                            break;
                        }
                    }
                }
            }
            Hibernate.initialize(nextQuestion.getChildNodes());
            transaction.commit();
            hibernateSession.flush();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not refreshed");
        } finally {
            hibernateSession.close();
        }
        return nextQuestion;
    }

    public static Question getRootQuestionAfter(PossibleAnswer possibleAnswer, int sequence) {
        Session hibernateSession = null;
        Transaction transaction = null;
        Question nextQuestion = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            possibleAnswer = (PossibleAnswer) hibernateSession.get(PossibleAnswer.class, possibleAnswer.getIdNode());

            Hibernate.initialize(possibleAnswer.getChildNodes());
            for (Node child : possibleAnswer.getChildNodes()) {
                if (!child.getDeleted().equals(Constant.NODE_DELETED)) {
                    if (child instanceof Question) {
                        if (child.getSequence() > sequence) {
                            nextQuestion = (Question) child;
                            break;
                        }
                    }
                }
            }
            Hibernate.initialize(nextQuestion.getChildNodes());
            transaction.commit();
            hibernateSession.flush();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not refreshed");
        } finally {
            hibernateSession.close();
        }
        return nextQuestion;
    }

    

    public static String getFullModuleAsJson(Module module) {
        
        return null;
    }

    

    

    
    public static void saveToFile(String filepath, String contents){
    	try {
			PrintWriter out = new PrintWriter(filepath);
			out.println(contents);
	    	out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    public static void delete(Module module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            hibernateSession.delete(module);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not saved - " + e.getMessage());
        } finally {
            hibernateSession.close();
        }
    }
    public static void delete(Fragment module) {
        Session hibernateSession = null;
        Transaction transaction = null;
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            hibernateSession.delete(module);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.out.println("object not saved - " + e.getMessage());
        } finally {
            hibernateSession.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Fragment> getAllFragments() {
        Session hibernateSession = null;
        Transaction transaction = null;
        List<Fragment> objectList = new ArrayList<Fragment>();
        try {
            hibernateSession = HibernateUtility.getSessionFactory().openSession();
            transaction = hibernateSession.beginTransaction();
            Criteria criteria = hibernateSession.createCriteria(Fragment.class).addOrder(Order.asc("type")).addOrder(Order.asc("name"));
            objectList = criteria.list();
            hibernateSession.getTransaction().commit();
        } catch (HibernateException he) {
            if (transaction != null)
                transaction.rollback();
            System.err.println(he.getMessage());
        }
        return (List<Fragment>) objectList;
    }


}