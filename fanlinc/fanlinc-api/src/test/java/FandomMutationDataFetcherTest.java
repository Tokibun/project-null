import ca.utoronto.utsc.fanlinc.Application;
import ca.utoronto.utsc.fanlinc.datafetchers.FandomDataFetcher;
import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.exceptions.FandomNotFoundException;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.model.Fandom;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import ca.utoronto.utsc.fanlinc.repository.FandomRepository;
import ca.utoronto.utsc.fanlinc.service.CurrentAccountService;
import graphql.schema.DataFetchingEnvironment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
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
@SpringBootTest(classes = Application.class)
public class FandomMutationDataFetcherTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FandomRepository fandomRepository;
    CurrentAccountService currentAccountService;

    DataFetchingEnvironment environment;
    FandomDataFetcher dataFetcher;

    Account currentAccount, account2; // account2 made fandom1 and fandom2
    Fandom fandom1, fandom2;

    @Before
    public void setUp() throws Exception {
        currentAccountService = mock(CurrentAccountService.class);
        dataFetcher = new FandomDataFetcher(accountRepository, fandomRepository, null, null, currentAccountService);
        environment = mock(DataFetchingEnvironment.class);

        currentAccount = new Account("dummydummy1234", "dummydummy@localhost", "no",
                "no", "no", 0, 0, new ArrayList<>());
        account2 = new Account("dmmydudsadk2", "michelle@localhost", "no",
                "no", "no", 0, 0, new ArrayList<>());

        fandom1 = new Fandom("fansndlkasndnaknd", "no", "no", 0,
                account2.getId(), "CASUAL", "FAN");
        fandom2 = new Fandom("fansndlkasndnaasdasdknd", "no", "no", 0,
                account2.getId(), "CASUAL", "FAN");

        accountRepository.insert(currentAccount);
        accountRepository.insert(account2);
        fandomRepository.insert(fandom1);
        fandomRepository.insert(fandom2);
        when(currentAccountService.getCurrentAccount()).thenReturn(currentAccount);
    }

    @After
    public void tearDown() throws Exception {
        accountRepository.deleteById(currentAccount.getId());
        accountRepository.deleteById(account2.getId());
        fandomRepository.deleteById(fandom1.getId());
        fandomRepository.deleteById(fandom2.getId());
    }

    /*
     * Create Fandom
     */

    @Test
    public void createFandomCurrentUserLoggedIn() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("displayName")).thenReturn("https://derick.derick");
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        when(environment.getArgument("description")).thenReturn("https://derick.derick");
        when(environment.getArgument("bannerImage")).thenReturn("https://derick.derick");

        Fandom result = (Fandom) dataFetcher.createFandom().get(environment);
        Assert.assertEquals("https://derick.derick", result.getDisplayName());
        Assert.assertEquals(Collections.singletonList(currentAccount.getId()), result.getSpecificUserCategoryTable("FAN"));
        Assert.assertEquals(Collections.singletonList(currentAccount.getId()), result.getSpecificUserCategoryTable("CASUAL"));
        Assert.assertEquals("https://derick.derick", result.getDescription());
        Assert.assertEquals("https://derick.derick", result.getBannerImageURL());

        fandomRepository.deleteById(result.getId());
    }

    @Test(expected = AccountNotFoundException.class)
    public void createFandomCurrentUserNull() throws Exception {
        when(currentAccountService.getCurrentAccount()).thenThrow(new AccountNotFoundException());

        when(environment.getArgument("displayName")).thenReturn("https://derick.derick");
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        when(environment.getArgument("description")).thenReturn("https://derick.derick");
        when(environment.getArgument("bannerImage")).thenReturn("https://derick.derick");

        dataFetcher.createFandom().get(environment);
    }

    @Test
    public void createFandomByIdValidTest() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn(currentAccount.getId());

        when(environment.getArgument("displayName")).thenReturn("https://derick.derick");
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        when(environment.getArgument("description")).thenReturn("https://derick.derick");
        when(environment.getArgument("bannerImage")).thenReturn("https://derick.derick");

        Fandom result = (Fandom) dataFetcher.createFandom().get(environment);
        Assert.assertEquals("https://derick.derick", result.getDisplayName());
        Assert.assertEquals(Collections.singletonList(currentAccount.getId()), result.getSpecificUserCategoryTable("FAN"));
        Assert.assertEquals(Collections.singletonList(currentAccount.getId()), result.getSpecificUserCategoryTable("CASUAL"));
        Assert.assertEquals("https://derick.derick", result.getDescription());
        Assert.assertEquals("https://derick.derick", result.getBannerImageURL());

        fandomRepository.deleteById(result.getId());
    }

    @Test(expected = AccountNotFoundException.class)
    public void createFandomByIdInvalidTest() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn("ryanwwwwwwwwwwwwwwwwwowowwwww");

        when(environment.getArgument("displayName")).thenReturn("https://derick.derick");
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        when(environment.getArgument("description")).thenReturn("https://derick.derick");
        when(environment.getArgument("bannerImage")).thenReturn("https://derick.derick");

        dataFetcher.createFandom().get(environment);
    }

    /*
     * Join Fandom
     */

    @Test
    public void joinFandomByFandomIdValid() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(false);
        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("COSPLAYER");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        dataFetcher.joinFandom().get(environment);

        currentAccount = accountRepository.findById(currentAccount.getId()).get(); // refresh account
        Assert.assertEquals("CASUAL", currentAccount.getFandomMembershipLevel(fandom1.getId()));
        Assert.assertEquals("COSPLAYER", currentAccount.getFandomMembershipType(fandom1.getId()));
    }

    @Test(expected = FandomNotFoundException.class)
    public void joinFandomByFandomIdInvalidId() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(false);
        when(environment.getArgument("fandomId")).thenReturn("wrongwrongwrong");
        when(environment.getArgument("fandomType")).thenReturn("COSPLAYER");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        dataFetcher.joinFandom().get(environment);
    }


    @Test
    public void joinFandomByIdWithUserId() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn(currentAccount.getId());

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("COSPLAYER");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");


        dataFetcher.joinFandom().get(environment);
        currentAccount = accountRepository.findById(currentAccount.getId()).get(); // refresh account
        Assert.assertEquals("CASUAL", currentAccount.getFandomMembershipLevel(fandom1.getId()));
        Assert.assertEquals("COSPLAYER", currentAccount.getFandomMembershipType(fandom1.getId()));
    }


    @Test(expected = FandomNotFoundException.class)
    public void joinFandomByIdInvalidFandom() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn(currentAccount.getId());

        when(environment.getArgument("fandomId")).thenReturn("wrongwrongwrong");
        when(environment.getArgument("fandomType")).thenReturn("COSPLAYER");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        dataFetcher.joinFandom().get(environment);
    }


    @Test(expected = AccountNotFoundException.class)
    public void joinFandomByInvalidUserValidFandom() throws Exception {
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn("nopeinval");

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("COSPLAYER");
        when(environment.getArgument("fandomLevel")).thenReturn("CASUAL");
        dataFetcher.joinFandom().get(environment);
    }

    /*
     * Update Fandom
     */


    @Test
    public void updateFandomMembershipValid() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.containsArgument("fandomType")).thenReturn(true);
        when(environment.getArgument("fandomLevel")).thenReturn("EXPERT");
        when(environment.containsArgument("fandomLevel")).thenReturn(true);
        dataFetcher.updateFandomMembership().get(environment);
        currentAccount = accountRepository.findById(currentAccount.getId()).get(); // refresh account

        Assert.assertEquals("EXPERT", currentAccount.getFandomMembershipLevel(fandom1.getId()));
        Assert.assertEquals("FAN", currentAccount.getFandomMembershipType(fandom1.getId()));
    }

    @Test
    public void updateFandomMembershipByIdValid() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn(currentAccount.getId());

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.containsArgument("fandomType")).thenReturn(true);
        when(environment.getArgument("fandomLevel")).thenReturn("EXPERT");
        when(environment.containsArgument("fandomLevel")).thenReturn(true);
        dataFetcher.updateFandomMembership().get(environment);
        currentAccount = accountRepository.findById(currentAccount.getId()).get(); // refresh account

        Assert.assertEquals("EXPERT", currentAccount.getFandomMembershipLevel(fandom1.getId()));
        Assert.assertEquals("FAN", currentAccount.getFandomMembershipType(fandom1.getId()));
    }

    @Test(expected = FandomNotFoundException.class)
    public void updateFandomMembershipInvalidFandom() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("fandomId")).thenReturn("helloworld34567");
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.getArgument("fandomLevel")).thenReturn("EXPERT");
        dataFetcher.updateFandomMembership().get(environment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateFandomMembershipInvalidLevel() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("FAN");
        when(environment.containsArgument("fandomType")).thenReturn(true);
        when(environment.getArgument("fandomLevel")).thenReturn("NOPE");
        when(environment.containsArgument("fandomLevel")).thenReturn(true);
        dataFetcher.updateFandomMembership().get(environment);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateFandomMembershipInvalidType() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        when(environment.getArgument("fandomType")).thenReturn("NOPE");
        when(environment.containsArgument("fandomType")).thenReturn(true);
        when(environment.getArgument("fandomLevel")).thenReturn("EXPERT");
        when(environment.containsArgument("fandomLevel")).thenReturn(true);
        dataFetcher.updateFandomMembership().get(environment);
    }

    /*
     * Leave Fandom
     */


    @Test
    public void leaveFandomWithValidId() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        dataFetcher.deleteFandomMembership().get(environment);
        Assert.assertFalse(currentAccount.getFandomMemberships().containsKey(fandom1.getId()));
    }

    @Test
    public void leaveFandomWithValidName() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(false);

        when(environment.getArgument("fandomName")).thenReturn(fandom1.getDisplayName());
        dataFetcher.leaveFandomByName().get(environment);
        Assert.assertFalse(currentAccount.getFandomMemberships().containsKey(fandom1.getId()));
    }

    @Test
    public void leaveFandomWithValidFandomIdAndUserId() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn(currentAccount.getId());

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        dataFetcher.deleteFandomMembership().get(environment);
        currentAccount = accountRepository.findById(currentAccount.getId()).get(); //update
        Assert.assertFalse(currentAccount.getFandomMemberships().containsKey(fandom1.getId()));
    }

    @Test(expected = AccountNotFoundException.class)
    public void leaveFandomWithInvalidId() throws Exception {
        joinFandom(currentAccount, fandom1, "COSPLAYER", "CASUAL");
        when(environment.containsArgument("userId")).thenReturn(true);
        when(environment.getArgument("userId")).thenReturn("nopeeee");

        when(environment.getArgument("fandomId")).thenReturn(fandom1.getId());
        dataFetcher.deleteFandomMembership().get(environment);
    }

    public void joinFandom(Account account, Fandom fandom, String type, String level) {
        fandom.addUsertoSpecificCategoryTable(account.getId(), type);
        fandom.addUsertoSpecificCategoryTable(account.getId(), level);
        fandom.addUsertoSpecificCategoryTable(account.getId(), "ALL USERS");
        account.insertFandomMembership(fandom.getId(), level, type);
        accountRepository.save(account);
        fandomRepository.save(fandom);
    }

}
