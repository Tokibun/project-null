/*@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class AccountQueryDataFetcherTest {

    @Autowired
    AccountRepository accountRepository;
    AccountDataFetcher dataFetcher;
    DataFetchingEnvironment environment;
    CurrentAccountService currentAccountService;
    Account account, account1;*/


import ca.utoronto.utsc.fanlinc.Application;
import ca.utoronto.utsc.fanlinc.datafetchers.FandomDataFetcher;
import ca.utoronto.utsc.fanlinc.model.Fandom;
import ca.utoronto.utsc.fanlinc.repository.FandomRepository;
import graphql.schema.DataFetchingEnvironment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class FandomQueryDataFectherTest {

    @Autowired
    FandomRepository fandomRepository;

    DataFetchingEnvironment environment;
    FandomDataFetcher dataFetcher;

    Fandom fandom;
    Fandom fandom1;

    @Before
    public void setUp() throws Exception {
        dataFetcher = new FandomDataFetcher(null, fandomRepository, null, null, null);
        environment = mock(DataFetchingEnvironment.class);

        // Mongo
        fandom = new Fandom("ilir fan club wow amazing ok", "ilir", "thing", 0, "no", "CASUAL", "FAN");
        fandom1 = new Fandom("ilir fan club memes", "ilir", "thing", 0, "no", "CASUAL", "FAN");
        fandom.setId("1");

        fandomRepository.insert(fandom);
        fandomRepository.insert(fandom1);
    }

    @After
    public void tearDown() throws Exception {
        fandomRepository.deleteById(fandom.getId());
        fandomRepository.deleteById(fandom1.getId());
    }

    @Test
    public void getFandomByIdValid() throws Exception {
        when(environment.getArgument("id")).thenReturn(fandom.getId());
        Assert.assertEquals(fandom, dataFetcher.fandomById().get(environment));
    }

    @Test
    public void getFandomByIdInvalid() throws Exception {
        when(environment.getArgument("id")).thenReturn("ryan was here");
        Assert.assertNull(dataFetcher.fandomById().get(environment));
    }

    @Test
    public void getFandomByNameValid() throws Exception {
        when(environment.getArgument("name")).thenReturn(fandom.getDisplayName());
        Assert.assertEquals(fandom, dataFetcher.fandomByName().get(environment));
    }

    @Test
    public void getFandomByNameEmpty() throws Exception {
        when(environment.getArgument("name")).thenReturn("");
        Assert.assertNull(dataFetcher.fandomByName().get(environment));
    }

    @Test
    public void getFandomByNameInvalid() throws Exception {
        when(environment.getArgument("name")).thenReturn("invaid wow ok cool stuff");
        Assert.assertNull(dataFetcher.fandomByName().get(environment));
    }

    @Test
    public void getFandomByNameSubstringExact() throws Exception {
        when(environment.getArgument("fandomNameSubstring")).thenReturn(fandom.getDisplayName());
        Assert.assertEquals(Collections.singletonList(fandom), dataFetcher.fandomsByNameSubstring().get(environment));
    }

    @Test
    public void getFandomByNameSubstringMany() throws Exception {
        when(environment.getArgument("fandomNameSubstring")).thenReturn("ilir fan club wow amazing ok");
        Assert.assertEquals(Collections.singletonList(fandom), dataFetcher.fandomsByNameSubstring().get(environment));
    }

    @Test
    public void getFandomByNameSubstringEmptyStr() throws Exception {
        when(environment.getArgument("fandomNameSubstring")).thenReturn("");
        Assert.assertEquals(Collections.emptyList(), dataFetcher.fandomsByNameSubstring().get(environment));
    }

    @Test
    public void getFandomByNameSubstringInvalid() throws Exception {
        when(environment.getArgument("fandomNameSubstring")).thenReturn("ok ok invalid stuff here");
        Assert.assertEquals(Collections.emptyList(), dataFetcher.fandomsByNameSubstring().get(environment));
    }
}
