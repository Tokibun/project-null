import ca.utoronto.utsc.fanlinc.Application;
import ca.utoronto.utsc.fanlinc.datafetchers.AccountDataFetcher;
import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.exceptions.IllegalAccountModificationException;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AccountMutationDataFetcherTest {

    @Autowired
    AccountRepository accountRepository;
    CurrentAccountService currentAccountService;

    DataFetchingEnvironment environment;
    AccountDataFetcher dataFetcher;

    Account currentAccount, account2;

    @Before
    public void setUp() throws Exception {
        currentAccountService = mock(CurrentAccountService.class);
        dataFetcher = new AccountDataFetcher(accountRepository, null, currentAccountService);
        environment = mock(DataFetchingEnvironment.class);

        currentAccount = new Account("dummydummy1234", "dummydummy@localhost", "no",
                "no", "no", 0, 0, new ArrayList<>());
        account2 = new Account("dmmydudsadk2", "michelle@localhost", "no",
                "no", "no", 0, 0, new ArrayList<>());
        accountRepository.insert(currentAccount);
        accountRepository.insert(account2);
        when(currentAccountService.getCurrentAccount()).thenReturn(currentAccount);
    }

    @After
    public void tearDown() throws Exception {
        accountRepository.deleteById(currentAccount.getId());
        accountRepository.deleteById(account2.getId());
    }

    public void setAccountModificationEnvironment(String username, String email, String password, String biography, String profileURL, String dateOfBirth) {
        when(environment.getArgument("username")).thenReturn(username);
        when(environment.containsArgument("username")).thenReturn(true);
        when(environment.getArgument("email")).thenReturn(email);
        when(environment.containsArgument("email")).thenReturn(true);
        // if (accountRepository.findByEmail(env.getArgument("email")).isPresent()) {

        when(environment.getArgument("password")).thenReturn(password);
        when(environment.containsArgument("password")).thenReturn(true);
        when(environment.getArgument("biography")).thenReturn(biography);
        when(environment.containsArgument("biography")).thenReturn(true);
        when(environment.getArgument("profileImage")).thenReturn(profileURL);
        when(environment.containsArgument("profileImage")).thenReturn(true);
        when(environment.getArgument("dateOfBirth")).thenReturn(dateOfBirth);
        when(environment.containsArgument("dateOfBirth")).thenReturn(true);
    }

    @Test
    public void createAccountValid() throws Exception {
        Account account = new Account("testdummytest123wowokcool", "hello@localhost", "no",
                "no", "no", System.currentTimeMillis(), 0, new ArrayList<>());
        setAccountModificationEnvironment(account.getUsername(), account.getEmail(), account.getPassword(), account.getBiography(), account.getProfileImageURL(), "2000-01-01");

        // Comparing the fields except for creation time since currentTimeMillis()
        Account result = (Account) dataFetcher.createAccount().get(environment);

        Assert.assertEquals(account.getUsername(), result.getUsername());
        Assert.assertEquals(account.getEmail(), result.getEmail());
        Assert.assertEquals(account.getPassword(), result.getPassword());
        Assert.assertEquals(account.getBiography(), result.getBiography());
        Assert.assertEquals(account.getProfileImageURL(), result.getProfileImageURL());
        Assert.assertEquals(account.getDateOfBirth(), result.getDateOfBirth());
        Assert.assertEquals(result, accountRepository.findById(result.getId()).get());

        accountRepository.deleteById(result.getId());
    }

    @Test(expected = IllegalAccountModificationException.class)
    public void createAccountExistingEmail() throws Exception {
        Account account = new Account("testdummytest123wowokcool", "hello@localhost", "no",
                "no", "no", System.currentTimeMillis(), 0,
                new ArrayList<>());
        setAccountModificationEnvironment(account.getUsername(), currentAccount.getEmail(), account.getPassword(), account.getBiography(),
                account.getProfileImageURL(), "2000-01-01");
        dataFetcher.createAccount().get(environment);
    }

    @Test(expected = IllegalAccountModificationException.class)
    public void createAccountExistingUsername() throws Exception {
        Account account = new Account("testdummytest123wowokcool", "hello@localhost", "no",
                "no", "no", System.currentTimeMillis(), 0,
                new ArrayList<>());
        setAccountModificationEnvironment(currentAccount.getUsername(), account.getEmail(), account.getPassword(), account.getBiography(),
                account.getProfileImageURL(), "2000-01-01");
        dataFetcher.createAccount().get(environment);
    }

    @Test
    public void updateAccountValid() throws Exception {
        setAccountModificationEnvironment("ryanryanaryadjnadjan", "eggplant@localhost", "badpassword",
                "bio2", currentAccount.getProfileImageURL(), null);
        Account result = (Account) dataFetcher.updateAccount().get(environment);

        Assert.assertEquals("eggplant@localhost", result.getEmail());
        Assert.assertEquals("badpassword", result.getPassword());
        Assert.assertEquals("bio2", result.getBiography());
        Assert.assertEquals(currentAccount.getProfileImageURL(), result.getProfileImageURL());
    }

    @Test(expected = IllegalAccountModificationException.class)
    public void updateAccountExistingEmail() throws Exception {
        when(environment.containsArgument("accountId")).thenReturn(false);
        setAccountModificationEnvironment("mememe", account2.getEmail(), "badpassword",
                "bio2", currentAccount.getProfileImageURL(), null);
        dataFetcher.updateAccount().get(environment);
    }

    @Test
    public void updateAcountNoArgs() throws Exception {
        when(environment.containsArgument("accountId")).thenReturn(false);
        when(environment.containsArgument("email")).thenReturn(false);
        when(environment.containsArgument("password")).thenReturn(false);
        when(environment.containsArgument("biography")).thenReturn(false);
        when(environment.containsArgument("profileImage")).thenReturn(false);

        Assert.assertEquals(currentAccount, dataFetcher.updateAccount().get(environment));
    }

    @Test
    public void deleteCurrentAccount() throws Exception {
        Assert.assertTrue((boolean) dataFetcher.deleteAccount().get(environment));
        Assert.assertNotEquals(0, accountRepository.findById(currentAccount.getId()).get().getDeletedTimestamp());
    }

    @Test
    public void deleteAccountById() throws Exception {
        when(environment.containsArgument("accountId")).thenReturn(true);
        when(environment.getArgument("accountId")).thenReturn(currentAccount.getId());
        dataFetcher.deleteAccount().get(environment);
        Assert.assertNotEquals(0, accountRepository.findById(currentAccount.getId()).get().getDeletedTimestamp());
    }

    @Test(expected = AccountNotFoundException.class)
    public void deleteAccountInvalid() throws Exception {
        when(environment.containsArgument("accountId")).thenReturn(true);
        when(environment.getArgument("accountId")).thenReturn("e");
        dataFetcher.deleteAccount().get(environment);
    }
}
