import ApolloClient from "apollo-boost";

const client = new ApolloClient({
  uri: "http://localhost:8080/graphql",
  credentials: "include"
});

export function getApolloClient(): ApolloClient<any> {
  return client;
}
