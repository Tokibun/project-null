import React, { Component } from "react";
import gql from "graphql-tag";
import { getApolloClient } from "../../apollo/apolloClient";
import { FanlincAccount } from "../../model/fanlincAccount";
import { currentAccount$ } from "../../subjects/pageSubjects";
import { CreateAccountPage } from "../createAccountPage/createAccountPage";
import { mainContentStream$ } from "../../subjects/mainContentArea";

const CHECK_USER = gql`
  query {
    currentAccount {
      username
      biography
      profileImage
      fandoms {
        displayName
      }
    }
  }
`;

interface LoginPageState {
  loading: boolean;
}
export class LoginPage extends Component<{}, LoginPageState> {
  public constructor(props: any) {
    super(props);
    this.state = { loading: true };
    this.checkAccount();
  }

  public render() {
    const acc = currentAccount$.value || null;
    return this.state.loading ? "Loading" : this.loadedPage(acc);
  }

  private checkAccount() {
    getApolloClient()
      .query({
        query: CHECK_USER,
        fetchPolicy: "network-only"
      })
      .then(result => {
        currentAccount$.next(
          !result.data.currentAccount ? null : result.data.currentAccount
        );
        this.setState({ loading: false });
      });
  }
  private loadedPage(account: FanlincAccount | null): JSX.Element {
    if (account !== null) {
      return (
        <section className="hero is-fullheight">
          <div className="hero-body">
            <div className="container">
              <p className="title">Currently logged in as {account.username}</p>
              <p className="subtitle">
                Let the fun begin! Wow! I love being a fan.
              </p>
              <div className="field">
                <div className="control">
                  <form action="http://localhost:8080/logout" method="post">
                    <button
                      className="button is-danger"
                      onClick={() => {
                        currentAccount$.next(null);
                      }}
                    >
                      Logout
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
      );
    } else {
      return (
        <section className="hero is-fullheight">
          <div className="hero-body">
            <div className="container">
              <p className="title">Fanlinc Login</p>
              <form action="http://localhost:8080/login" method="post">
                <div className="field">
                  <div className="control has-icons-left">
                    <input
                      className="input"
                      type="text"
                      placeholder="Username"
                      name="username"
                    />
                    <span className="icon is-small is-left">
                      <i className="fas fa-user"></i>
                    </span>
                  </div>
                </div>
                {/* Password */}
                <div className="field">
                  <div className="control has-icons-left">
                    <input
                      className="input"
                      type="password"
                      placeholder="Password"
                      name="password"
                    />
                    <span className="icon is-small is-left">
                      <i className="fas fa-lock"></i>
                    </span>
                  </div>
                </div>
                {/* Submit Button */}
                <div className="field">
                  <div className="control">
                    <button className="button is-primary" type="submit">
                      Login
                    </button>
                  </div>
                </div>
              </form>
              <p className="subtitle">
                Don't have an account yet?
                <a
                  onClick={() => {
                    mainContentStream$.next(<CreateAccountPage />);
                  }}
                >
                  Sign up now!
                </a>
              </p>
            </div>
          </div>
        </section>
      );
    }
  }
}
