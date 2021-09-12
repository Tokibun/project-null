import React from "react";
import { Subscription } from "rxjs";
import {
  fandom$,
  currentAccount$,
  getCurrentAccount,
  getCurrentProfile,
  isUserLoggedIn,
  profile$
} from "../../subjects/pageSubjects";

import { getApolloClient } from "../../apollo/apolloClient";
import { gql } from "apollo-boost";
import { BigText } from "../profilePage/bigtext";
import { Cell } from "../cell/cell";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { LoginPage } from "../loginPage/loginPage";

const CREATE_ACCOUNT = gql`
  mutation(
    $username: String!
    $email: String!
    $password: String!
    $biography: String!
    $profileImage: String!
    $dateOfBirth: String!
  ) {
    createAccount(
      username: $username
      email: $email
      password: $password
      biography: $biography
      profileImage: $profileImage
      dateOfBirth: $dateOfBirth
    ) {
      id
      username
      email
      biography
      profileImage
      createdTimestamp
      dateOfBirth
      fandoms {
        displayName
      }
    }
  }
`;

export class CreateAccountPage extends React.Component {
  public constructor(props: any) {
    super(props);
    this.state = { ...this.props };
  }

  private createAccount() {
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: CREATE_ACCOUNT,
        variables: {
          username: state.username || "",
          email: state.email || "",
          password: state.password || "",
          biography: state.biography || "",
          profileImage: state.profileImage || "",
          dateOfBirth: state.dateOfBirth || ""
        }
      })
      .then(result => {
        if (result) {
          alert("Account successfully created");
          mainContentStream$.next(<LoginPage />);
        }
      })
      .catch(reason => alert(JSON.stringify(reason)));
  }

  public render() {
    const miscProps = { state: this.state, onChange: this.onChange.bind(this) };
    return (
      <div className="container">
        <BigText text="Create Account" />
        <Cell label="Username" value="username" {...miscProps} />
        <Cell label="Email" value="email" {...miscProps} />
        <Cell
          label="Password"
          value="password"
          className="password"
          {...miscProps}
        />
        <Cell label="Biography" value="biography" {...miscProps} />
        <Cell label="Profile Image URL" value="profileImage" {...miscProps} />
        <div className="buttons">
          <button
            className="button is-primary"
            onClick={() => {
              mainContentStream$.next(<LoginPage />);
            }}
          >
            Back to Login
          </button>

          <button
            className="button is-primary"
            onClick={this.createAccount.bind(this)}
          >
            Create Account
          </button>
        </div>
      </div>
    );
  }

  // Thanks Spencer!
  private onChange(value: string): (event: any) => void {
    const state: any = this.state;
    return (event: any) => {
      state[value] = event.target.value;
      this.setState(state);
    };
  }
}
