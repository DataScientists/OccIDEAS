package occideas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.occideas.Agent;
import org.occideas.AgentGroup;
import org.occideas.HibernateUtility;
import org.occideas.Module;
import org.occideas.Node;
import org.occideas.Note;
import org.occideas.PossibleAnswer;
import org.occideas.Question;
import org.occideas.Rule;

public class DBPersistenceTest {

	static SessionFactory sf;
	Session session;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sf = HibernateUtility.getSessionFactory();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		session = sf.openSession();
		session.beginTransaction();
	}

	@After
	public void tearDown() throws Exception {
		session.getTransaction().commit();
		session.close();
	}

	@Test
	public void testModule() {
		
		Module m = new Module();
		m.setName("test");
		
		session.save(m);
		session.delete(m);
	}
	
	@Test
	public void testModuleNotes() {
		
		Module m = new Module();
		m.setName("test");
		
		session.save(m);
		
		Note note = new Note();		
		note.setText("This is a new note");
		note.setNode((Node)m);
		session.save(note);
		session.delete(note);
		session.delete(m);
	}
	@Test
	public void testModuleRules() {
		
		Module m = new Module();
		m.setName("test");
		
		session.save(m);
		
		Question q = new Question();
		q.setName("Question 1");
		q.setParent(m);
		q.setTopNodeId(m.getIdNode());
		
		session.save(q);
		
		PossibleAnswer pa = new PossibleAnswer();
		pa.setName("Possible Answer 1");
		pa.setParent(q);
		pa.setTopNodeId(m.getIdNode());
		
		session.save(pa);
		
		
		AgentGroup ag = new AgentGroup();
		ag.setName("ag test");
		session.save(ag);
		
		Agent a = new Agent();
		a.setName("a test");
		a.setGroup(ag);
		session.save(a);
		
		Rule sr = new Rule();
		sr.setAgentId(a.getIdAgent());
		sr.getConditions().add(pa);
		session.save(sr);
		
		session.delete(sr);
		session.delete(a);
		session.delete(ag);
		session.delete(pa);
		session.delete(q);
		session.delete(m);
		
		
	}
	@Test
	public void testModuleRulesChildQuestions() {
	
		Module m = new Module();
		m.setName("test");
		
		session.save(m);
		
		Question q = new Question();
		q.setName("Question 1");
		q.setParent(m);
		q.setTopNodeId(m.getIdNode());
		
		session.save(q);
		
		PossibleAnswer pa = new PossibleAnswer();
		pa.setName("Possible Answer 1");
		pa.setParent(q);
		pa.setTopNodeId(m.getIdNode());
		
		session.save(pa);
		
		Question q1 = new Question();
		q1.setName("Question 2");
		q1.setParent(m);
		q1.setTopNodeId(m.getIdNode());
		
		session.save(q1);
		
		PossibleAnswer pa1 = new PossibleAnswer();
		pa1.setName("Possible Answer 2");
		pa1.setParent(q);
		pa1.setTopNodeId(m.getIdNode());
		
		session.save(pa1);
		
		
		AgentGroup ag = new AgentGroup();
		ag.setName("ag test");
		session.save(ag);
		
		Agent a = new Agent();
		a.setName("a test");
		a.setGroup(ag);
		session.save(a);
		
		Rule sr = new Rule();
		sr.setAgentId(a.getIdAgent());
		sr.getConditions().add(pa1);
		session.save(sr);
		
		/*session.delete(sr);
		session.delete(a);
		session.delete(ag);
		session.delete(pa1);
		session.delete(q1);
		session.delete(pa);
		session.delete(q);
		session.delete(m);*/
			
	}
	
	@Test
	@Ignore
	public void testModules() {
		List<Module> l = session.createCriteria(Module.class).list();
		
		
		assertEquals(3, l.size());
		for(Module m: l){
			System.out.println(m.getName());
			assertTrue(m.getChildNodes().size()>0);
			for(Node n:m.getChildNodes()){
				System.out.println(n.getName());
				Question q = (Question)n;
				assertTrue(!q.getName().equalsIgnoreCase(""));
				for(Node a:q.getChildNodes()){
					System.out.println(a.getName());
					PossibleAnswer pa = (PossibleAnswer)a;
					assertTrue(!pa.getName().equalsIgnoreCase(""));
				}
			}
		}
		
	}

}
