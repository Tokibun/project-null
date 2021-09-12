import ca.utoronto.utsc.fanlinc.Application;
import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.model.FanlincUserDTO;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import ca.utoronto.utsc.fanlinc.service.CurrentAccountService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CurrentAccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;
    private CurrentAccountService accountService;

    private SecurityContext context;
    private Authentication authentication;
    private Account account;

    @Before
    public void setUp() throws Exception {
        context = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        Account account = new Account("Dummy", "Dummy@localhost", "password", "bio", null, 0, 0, Collections.emptyList());
        accountRepository.insert(account);
    }

    @After
    public void tearDown() throws Exception {
        accountRepository.deleteById(account.getId());
    }

    @Test
    public void currentAccountLoggedIn() throws Exception {
        when(authentication.getPrincipal()).thenReturn(new FanlincUserDTO("1", "Dummy", "", Collections.emptyList()));
        Assert.assertEquals(account, accountService.getCurrentAccount());
    }

    @Test(expected = AccountNotFoundException.class)
    public void currentAccountNotLoggedIn() throws Exception {
        when(authentication.getPrincipal()).thenReturn("anonymous");
        accountService.getCurrentAccount();
    }
}
