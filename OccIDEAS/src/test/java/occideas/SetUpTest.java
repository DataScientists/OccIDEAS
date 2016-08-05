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
import org.occideas.entity.AdditionalField;
import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.entity.Fragment;
import org.occideas.entity.HibernateUtility;
import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.Note;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.entity.Rule;
@Ignore
public class SetUpTest {

//	static SessionFactory sf;
//	Session session;
//	
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		sf = HibernateUtility.getSessionFactory();
//
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//		session = sf.openSession();
//		session.beginTransaction();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		session.getTransaction().commit();
//		session.close();
//	}
//
//	@Test
//	public void test() {
//		//test to create all tables
//		@SuppressWarnings("unchecked")
//		List<Module> modules = session.createCriteria(Module.class).list();		
//		assert(modules.size()==0);
//		@SuppressWarnings("unchecked")
//		List<Fragment> fragments = session.createCriteria(Fragment.class).list();		
//		assert(fragments.size()==0);
//		@SuppressWarnings("unchecked")
//		List<Question> questions = session.createCriteria(Question.class).list();		
//		assert(questions.size()==0);
//		@SuppressWarnings("unchecked")
//		List<PossibleAnswer> possibleanswers = session.createCriteria(PossibleAnswer.class).list();		
//		assert(possibleanswers.size()==0);
//		@SuppressWarnings("unchecked")
//		List<AgentGroup> agentGroups = session.createCriteria(AgentGroup.class).list();		
//		assert(agentGroups.size()==0);
//		@SuppressWarnings("unchecked")
//		List<Agent> agents = session.createCriteria(Agent.class).list();		
//		assert(agents.size()==0);
//		@SuppressWarnings("unchecked")
//		List<Note> notes = session.createCriteria(Note.class).list();		
//		assert(notes.size()==0);
//		@SuppressWarnings("unchecked")
//		List<Rule> rules = session.createCriteria(Rule.class).list();		
//		assert(rules.size()==0);
//		@SuppressWarnings("unchecked")
//		List<AdditionalField> additionalFields = session.createCriteria(AdditionalField.class).list();		
//		assert(additionalFields.size()==0);
//		
//	}
//	
//	@Test
//	@Ignore
//	public void testModules() {
//		List<Module> l = session.createCriteria(Module.class).list();
//		
//		
//		assertEquals(3, l.size());
//		for(Module m: l){
//			System.out.println(m.getName());
//			assertTrue(m.getChildNodes().size()>0);
//			for(Node n:m.getChildNodes()){
//				System.out.println(n.getName());
//				Question q = (Question)n;
//				assertTrue(!q.getName().equalsIgnoreCase(""));
//				for(Node a:q.getChildNodes()){
//					System.out.println(a.getName());
//					PossibleAnswer pa = (PossibleAnswer)a;
//					assertTrue(!pa.getName().equalsIgnoreCase(""));
//				}
//			}
//		}
//		
//	}

}
