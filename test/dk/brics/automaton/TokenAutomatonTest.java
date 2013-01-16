package dk.brics.automaton;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenAutomatonTest {

	@Test
	public void testMatch() {
		final Object TOKEN_ID = new Object();
		final Automaton a = new TokenRegExp("ABC", TOKEN_ID).toAutomaton();
		final TokenAutomaton ta = new TokenAutomaton(a);
		final String input = "ABC";
		final TokenDetails details = new TokenDetails();
		final boolean match = ta.find(input, 0, false, details);
		
		assertTrue("match", match);
		assertSame("details.seq", input, details.seq);
		assertEquals("details.off", 0, details.off);
		assertEquals("details.len", 3, details.len);
		assertSame("details.info", TOKEN_ID, details.info);
	}
	
	@Test
	public void testMatchWithContext() {
		final Object TOKEN_ID = new Object();
		final Automaton a = new TokenRegExp("ABC", TOKEN_ID).toAutomaton();
		final TokenAutomaton ta = new TokenAutomaton(a);
		final String input = "XABCX";
		final TokenDetails details = new TokenDetails();
		final boolean match = ta.find(input, 1, false, details);
		
		assertTrue("match", match);
		assertSame("details.seq", input, details.seq);
		assertEquals("details.off", 1, details.off);
		assertEquals("details.len", 3, details.len);
		assertSame("details.info", TOKEN_ID, details.info);
	}
	
	@Test
	public void testNoMatch() {
		final Object TOKEN_ID = new Object();
		final Automaton a = new TokenRegExp("ABC", TOKEN_ID).toAutomaton();
		final TokenAutomaton ta = new TokenAutomaton(a);
		final String input = "XYZ";
		final TokenDetails details = new TokenDetails();
		final boolean match = ta.find(input, 0, false, details);
		
		assertFalse("match", match);
		assertSame("details.info", TokenDetails.NO_MATCH, details.info);
	}
	
	@Test
	public void testUnderflow() {
		final Object TOKEN_ID = new Object();
		final Automaton a = new TokenRegExp("ABC(DE)?", TOKEN_ID).toAutomaton();
		final TokenAutomaton ta = new TokenAutomaton(a);
		final String input = "ABC";
		final TokenDetails details = new TokenDetails();
		final boolean match = ta.find(input, 0, false, details);
		
		assertFalse("match", match);
		assertSame("details.info", TokenDetails.UNDERFLOW, details.info);
	}
	
	@Test
	public void testUnderflowWithEndOfInput() {
		final Object TOKEN_ID = new Object();
		final Automaton a = new TokenRegExp("ABC(DE)?", TOKEN_ID).toAutomaton();
		final TokenAutomaton ta = new TokenAutomaton(a);
		final String input = "ABC";
		final TokenDetails details = new TokenDetails();
		final boolean match = ta.find(input, 0, true, details);
		
		assertTrue("match", match);
		assertSame("details.seq", input, details.seq);
		assertEquals("details.off", 0, details.off);
		assertEquals("details.len", 3, details.len);
		assertSame("details.info", TOKEN_ID, details.info);
	}
	
	@Test
	public void testPriority() {
		final Object KEYWORD_FOO = new Object();
		final Object IDENTIFIER = new Object();
		final Automaton a = new TokenRegExp("foo", KEYWORD_FOO).toAutomaton();
		final Automaton b = new TokenRegExp("[a-z]+", IDENTIFIER).toAutomaton();
		final Automaton c = b.minus(a).union(a);
		final TokenAutomaton ta = new TokenAutomaton(c);
		final String input = "foo bar";
		final TokenDetails details = new TokenDetails();
		final boolean match = ta.find(input, 0, false, details);
		
		assertTrue("match", match);
		assertSame("details.seq", input, details.seq);
		assertEquals("details.off", 0, details.off);
		assertEquals("details.len", 3, details.len);
		assertSame("details.info", KEYWORD_FOO, details.info);
	}
}
