package ca.utoronto.utsc.fanlinc.graphql;

import ca.utoronto.utsc.fanlinc.datafetchers.AccountDataFetcher;
import ca.utoronto.utsc.fanlinc.datafetchers.FandomDataFetcher;
import ca.utoronto.utsc.fanlinc.datafetchers.QuackDataFetcher;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * GraphQL Time
 * Schema, Types & Datafetchers come together to do stuff and /graphql
 */
@Component
public class GraphQLProvider {

    private final AccountDataFetcher accountDataFetcher;
    private final FandomDataFetcher fandomDataFetcher;
    private final QuackDataFetcher quackDataFetcher;
    private GraphQL graphQL;

    @Autowired
    public GraphQLProvider(AccountDataFetcher accountDataFetcher, FandomDataFetcher fandomDataFetcher,
                           QuackDataFetcher quackDataFetcher) {
        this.accountDataFetcher = accountDataFetcher;
        this.fandomDataFetcher = fandomDataFetcher;
        this.quackDataFetcher = quackDataFetcher;
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphql");

        // Schema
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser()
                .parse(Resources.toString(url, Charsets.UTF_8));
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,
                createRuntimeWiring());
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring createRuntimeWiring() {
        Map<String, DataFetcher> queryDataFetcherMap = new HashMap<>();
        Map<String, DataFetcher> mutationDataFetcherMap = new HashMap<>();

        // Quack Debugging
        queryDataFetcherMap.put("quack", quackDataFetcher.quack());

        // Account Query
        queryDataFetcherMap.put("currentAccount", accountDataFetcher.currentAccount());
        queryDataFetcherMap.put("accountById", accountDataFetcher.accountById());
        queryDataFetcherMap.put("accountByUsername", accountDataFetcher.accountByUsername());
        queryDataFetcherMap.put("accountsByUsernameSubstring", accountDataFetcher.accountsByUsernameSubstring());
        queryDataFetcherMap.put("membershipsByUsername", accountDataFetcher.membershipsByUsername());

        // Account Mutation
        mutationDataFetcherMap.put("createAccount", accountDataFetcher.createAccount());
        mutationDataFetcherMap.put("updateAccount", accountDataFetcher.updateAccount());
        mutationDataFetcherMap.put("updateAccountById", accountDataFetcher.updateAccount());
        mutationDataFetcherMap.put("deleteAccount", accountDataFetcher.deleteAccount());
        mutationDataFetcherMap.put("deleteAccountById", accountDataFetcher.deleteAccount());

        // Fandom Query
        queryDataFetcherMap.put("fandomById", fandomDataFetcher.fandomById());
        queryDataFetcherMap.put("fandomByName", fandomDataFetcher.fandomByName());
        queryDataFetcherMap.put("fandomIdByName", fandomDataFetcher.fandomIdByName());
        queryDataFetcherMap.put("fandomsByNameSubstring", fandomDataFetcher.fandomsByNameSubstring());

        queryDataFetcherMap.put("postsByFandomName", fandomDataFetcher.postsByFandomName());
        queryDataFetcherMap.put("repliesToPost", fandomDataFetcher.repliesToPost());

        // Fandom Mutation
        mutationDataFetcherMap.put("createFandom", fandomDataFetcher.createFandom());
        mutationDataFetcherMap.put("createFandomById", fandomDataFetcher.createFandom());

        mutationDataFetcherMap.put("joinFandomByFandomId", fandomDataFetcher.joinFandom());
        mutationDataFetcherMap.put("joinFandomById", fandomDataFetcher.joinFandom());
        mutationDataFetcherMap.put("joinFandomByFandomName", fandomDataFetcher.joinFandomByFandomName());
        mutationDataFetcherMap.put("joinFandomByUsernameAndId", fandomDataFetcher.joinFandomByFandomName());

        mutationDataFetcherMap.put("updateFandomMembership", fandomDataFetcher.updateFandomMembership());
        mutationDataFetcherMap.put("updateFandomMembershipById", fandomDataFetcher.updateFandomMembership());

        mutationDataFetcherMap.put("leaveFandom", fandomDataFetcher.deleteFandomMembership());
        mutationDataFetcherMap.put("leaveFandomByName", fandomDataFetcher.leaveFandomByName());
        mutationDataFetcherMap.put("leaveFandomById", fandomDataFetcher.deleteFandomMembership());

        mutationDataFetcherMap.put("addPostToFandom", fandomDataFetcher.addPostToFandom());
        mutationDataFetcherMap.put("deletePostFromFandom", fandomDataFetcher.deletePostFromFandom());

        mutationDataFetcherMap.put("replyToPost", fandomDataFetcher.replyToPost());
        mutationDataFetcherMap.put("deleteReplytoPost",fandomDataFetcher.deleteReplytoPost());

        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetchers(queryDataFetcherMap))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetchers(mutationDataFetcherMap))
                .build();
    }
}
