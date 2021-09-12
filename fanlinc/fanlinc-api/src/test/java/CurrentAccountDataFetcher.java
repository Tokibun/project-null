import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.service.CurrentAccountService;
import ca.utoronto.utsc.fanlinc.datafetchers.AccountDataFetcher;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CurrentAccountDataFetcher {

    CurrentAccountService currentAccountService;
    Account dummy;

    @MockBean
    AccountRepository accountRepository;
    AccountDataFetcher dataFetcher;
    DataFetchingEnvironment environment;

    @Before
    public void setUp() throws Exception {
        currentAccountService = mock(CurrentAccountService.class);
        dummy = new Account("Dummy", "dummy@localhost", "password", null,
                null, 0, 0, new ArrayList<>());

        dataFetcher = new AccountDataFetcher(accountRepository, null, currentAccountService);
        environment = mock(DataFetchingEnvironment.class);
    }

    @Test
    public void currentAccountLoggedIn() throws Exception {
        when(currentAccountService.getCurrentAccount()).thenReturn(dummy);
        assertEquals(dummy, dataFetcher.currentAccount().get(environment));
    }

    @Test
    public void currentAccountAnonymous() throws Exception {
        when(currentAccountService.getCurrentAccount()).thenThrow(new AccountNotFoundException());
        assertNull(dataFetcher.currentAccount().get(environment));
    }
}