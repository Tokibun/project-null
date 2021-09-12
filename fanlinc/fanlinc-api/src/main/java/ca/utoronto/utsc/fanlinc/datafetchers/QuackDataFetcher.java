package ca.utoronto.utsc.fanlinc.datafetchers;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class QuackDataFetcher {

    private HttpSession httpSession;

    @Autowired
    public QuackDataFetcher(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Increment the quack stored in the session whenever quack is requested.
     * GraphQL Playground does funny things because its a new session every request.
     * But something like this works.
     *
     * <code>http://localhost:8080/graphql?query=%7Bquack%28n%3A10%29%7D</code>
     * <p>
     * Returns n number of quacks regardless if the user is logged in.
     */
    public DataFetcher quack() {
        return env -> {
            Integer quacks = (Integer) httpSession.getAttribute("quack");
            if (quacks == null) quacks = 0;
            httpSession.setAttribute("quack", ++quacks);
            System.out.printf("%s\t%s\n", httpSession.getId(), httpSession.getAttribute("quack"));

            int n = env.getArgument("n");
            return new String(new char[n]).replace("\0", "Quack");
        };
    }
}
