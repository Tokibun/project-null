import ca.utoronto.utsc.fanlinc.datafetchers.QuackDataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QuackDataFetcherTest {

    QuackDataFetcher quackDataFetcher;
    HttpSession session;
    DataFetchingEnvironment dataFetchingEnvironment;
    int quack;

    @Before
    public void setUp() {
        session = new MockHttpSession();
        quackDataFetcher = new QuackDataFetcher(session);
        dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    }

    @Test
    public void testQuack1() throws Exception {
        when(dataFetchingEnvironment.getArgument("n")).thenReturn(1);
        assertEquals("Quack", (String) quackDataFetcher.quack().get(dataFetchingEnvironment));
        assertEquals(1, session.getAttribute("quack"));
    }
}