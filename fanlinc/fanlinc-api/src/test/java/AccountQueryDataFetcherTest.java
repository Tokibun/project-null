import ca.utoronto.utsc.fanlinc.Application;
import ca.utoronto.utsc.fanlinc.datafetchers.AccountDataFetcher;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import ca.utoronto.utsc.fanlinc.service.CurrentAccountService;
import graphql.schema.DataFetchingEnvironment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class AccountQueryDataFetcherTest {

    @Autowired
    AccountRepository accountRepository;
    AccountDataFetcher dataFetcher;
    DataFetchingEnvironment environment;
    CurrentAccountService currentAccountService;
    Account account, account1;

    @Before
    public void setUp() throws Exception {
        currentAccountService = mock(CurrentAccountService.class);
        dataFetcher = new AccountDataFetcher(accountRepository, null, null);
        environment = mock(DataFetchingEnvironment.class);

        // Mongo
        account = new Account("dlsandksubstringggnanlan4lak4414244ee", "dummy@localhost", "password", null,
                null, 0, 0, new ArrayList<>());
        account1 = new Account("dlsandksubstringggnanlan4lak4414244ff", "dummy@localhost", "password", null,
                null, 0, 0, new ArrayList<>());
        account.setId("1");
        account1.setId("2");
        accountRepository.insert(account);
        accountRepository.insert(account1);
    }

    @After
    public void tearDown() throws Exception {
        accountRepository.deleteById(account.getId());
        accountRepository.deleteById(account1.getId());
    }

    @Test
    public void getValidAccountWithIdTest() throws Exception {
        when(environment.getArgument("id")).thenReturn("1");
        Assert.assertEquals(account, dataFetcher.accountById().get(environment));
    }

    @Test
    public void getInvalidAccountWithIdTest() throws Exception {
        when(environment.getArgument("id")).thenReturn("10");
        Assert.assertNull(dataFetcher.accountById().get(environment));
    }

    @Test
    public void getValidAccountWithUsernameTest() throws Exception {
        when(environment.getArgument("username")).thenReturn(account.getUsername());
        Assert.assertEquals(account, dataFetcher.accountByUsername().get(environment));
    }

    @Test
    public void getInvalidAccountWithUsernameTest() throws Exception {
        when(environment.getArgument("username")).thenReturn("invalid man hehehe");
        Assert.assertNull(dataFetcher.accountByUsername().get(environment));
    }

    @Test
    public void getAccountBySubstringEntireName() throws Exception {
        when(environment.getArgument("accountUsernameSubstring")).thenReturn(account.getUsername());
        Assert.assertEquals(Collections.singletonList(account), dataFetcher.accountsByUsernameSubstring().get(environment));
    }

    @Test
    public void getAccountBySubstringMultiple() throws Exception {
        when(environment.getArgument("accountUsernameSubstring")).thenReturn("substringgg");
        Assert.assertEquals(Arrays.asList(account, account1), dataFetcher.accountsByUsernameSubstring().get(environment));
    }

    @Test
    public void getAccountBySubstringInvalid() throws Exception {
        when(environment.getArgument("accountUsernameSubstring")).thenReturn("neo4j supreme ilir dema");
        Assert.assertEquals(Collections.emptyList(), dataFetcher.accountsByUsernameSubstring().get(environment));
    }

    @Test
    public void getAccountBySubstringEmptyStr() throws Exception {
        when(environment.getArgument("accountUsernameSubstring")).thenReturn("");
        Assert.assertEquals(Collections.emptyList(), dataFetcher.accountsByUsernameSubstring().get(environment));
    }
}