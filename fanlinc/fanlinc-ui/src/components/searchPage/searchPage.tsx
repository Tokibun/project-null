import React from "react";
import { Box } from "../box/box";
import { FanlincAccount } from "../../model/fanlincAccount";
import { getApolloClient } from "../../apollo/apolloClient";
import { BigText } from "../profilePage/bigtext";
import gql from "graphql-tag";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { FandomPage } from "../fandomPage/fandomPage";
import { fandom$, profile$ } from "../../subjects/pageSubjects";
import { ProfilePage } from "../profilePage/profilePage";
import { Fandom } from "../../model/fandom";

/**
 * Query for fandom display name substring search
 */
const SEARCH_FANDOM_NAME_SUBSTRING = gql`
  query fandomsByNameSubstring($fandomNameSubstring: String!) {
    fandomsByNameSubstring(fandomNameSubstring: $fandomNameSubstring) {
      displayName
      description
      bannerImageURL
      createdTimestamp
    }
  }
`;

/**
 * Query for account user name substring search
 */
const SEARCH_USERNAME_SUBSTRING = gql`
  query accountsByUsernameSubstring($accountUsernameSubstring: String!) {
    accountsByUsernameSubstring(
      accountUsernameSubstring: $accountUsernameSubstring
    ) {
      username
      email
      biography
      profileImage
    }
  }
`;

export class SearchPage extends React.Component<any, any> {
  public constructor(props: FanlincAccount) {
    super(props);
    this.state = {
      searchFandomString: "default",
      fandomsObjectList: [],
      searchAccountString: "default",
      accountsObjectList: []
    };
  }

  /**
   * Queries Fandoms for a substring and puts relevant fandom names into state list
   */
  private searchFandomSubstring() {
    getApolloClient()
      .query({
        query: SEARCH_FANDOM_NAME_SUBSTRING,
        variables: {
          fandomNameSubstring: this.state.searchFandomString || ""
        }
      })
      .then(result => {
        this.setState((state: { fandomsObjectList: [string] }) => {
          return { fandomsObjectList: [] };
        });
        if (
          result.data.fandomsByNameSubstring === null ||
          result.data.fandomsByNameSubstring === []
        ) {
          this.setState((state: { fandomsObjectList: [string] }) => {
            return { fandomsObjectList: [] };
          });
        } else {
          const fandoms = result.data.fandomsByNameSubstring;
          for (let i = 0; i < fandoms.length; i++) {
            //push all fandom[i].displayname into state.fandomsList
            this.setState((prevState: any) => ({
              fandomsObjectList: [...prevState.fandomsObjectList, fandoms[i]]
            }));
          }
        }
      });
  }

  /**
   * Queries Accounts for a substring in the usernames and puts relevant accounts into list inside of state
   */
  private searchAccountSubstring() {
    getApolloClient()
      .query({
        query: SEARCH_USERNAME_SUBSTRING,
        variables: {
          accountUsernameSubstring: this.state.searchAccountString || ""
        }
      })
      .then(result => {
        this.setState((state: { fandomsObjectList: [string] }) => {
          return { accountsObjectList: [] };
        });
        if (
          result.data.accountsByUsernameSubstring === null ||
          result.data.accountsByUsernameSubstring === []
        ) {
          this.setState((state: { accountsObjectList: [string] }) => {
            return { accountsObjectList: [] };
          });
        } else {
          const accounts = result.data.accountsByUsernameSubstring;
          for (let i = 0; i < accounts.length; i++) {
            //push all fandom[i].displayname into state.fandomsList
            this.setState((prevState: any) => ({
              accountsObjectList: [...prevState.accountsObjectList, accounts[i]]
            }));
          }
        }
      });
  }

  private loadFandomPage(fandomObject: Fandom) {
    mainContentStream$.next(<FandomPage {...fandomObject} />);
    fandom$.next(fandomObject);
  }

  private loadAccountPage(accountObject: FanlincAccount) {
    profile$.next(accountObject);
    mainContentStream$.next(<ProfilePage {...accountObject} />);
  }

  public render() {
    return (
      <div className="columns">
        <div className="column is-half">
          <Box>
            <BigText text="Search for a Fandom:" />
            <br />
            {this.fandomSearchCell("Fandom Name", "fandomname")}
            <br />
            <small className="column is-marginless">
              <b> Fandoms Found: </b>
            </small>

            <Box>
              <hr></hr>
              <ul className="is-$info">
                {this.state.fandomsObjectList.map((fandom: any) => {
                  return (
                    <li
                      className="has-text-info has-text-weight-bold"
                      key={fandom.displayName}
                    >
                      {fandom.displayName}
                      &nbsp;&nbsp;
                      <button
                        className="button is-small is-primary"
                        onClick={() => this.loadFandomPage(fandom)}
                      >
                        Go
                      </button>
                      <hr></hr>
                    </li>
                  );
                })}
              </ul>
            </Box>
          </Box>
        </div>

        <div className="column is-half">
          <Box>
            <BigText text="Search for a User:" />
            <br></br>
            {this.accountSearchCell("Username", "username")}
            <br></br>
            <small className="column is-marginless">
              <b> Accounts Found: </b>
            </small>
            <Box>
              <hr></hr>
              <ul className="is-$info">
                {this.state.accountsObjectList.map((account: any) => {
                  return (
                    <li
                      className="has-text-info has-text-weight-bold"
                      key={account.username}
                    >
                      {account.username}
                      &nbsp;&nbsp;
                      <button
                        className="button is-small is-primary"
                        onClick={() => this.loadAccountPage(account)}
                      >
                        Go
                      </button>
                      <hr></hr>
                    </li>
                  );
                })}
              </ul>
            </Box>
          </Box>
        </div>
      </div>
    );
  }

  /**
   * Updates state's fandom object values as user types into searchbar
   *
   * @param value Current string held in searchbar
   */
  private onChangeFandom(value: string): (event: any) => void {
    return (event: any) => {
      const boxValue = event.target.value;
      this.setState((state: { searchFandomString: string }) => {
        return { searchFandomString: boxValue };
      }, this.searchFandomSubstring);
    };
  }

  //Copy from spencer, replace this because copypaste is naughty
  private fandomSearchCell(
    label: string,
    value: string,
    className?: string
  ): JSX.Element {
    return (
      <div className="column is-12">
        <div className="columns">
          <div className="column is-marginless">
            <small>
              <b>{label}</b>
            </small>
          </div>
          <div className="column is-half is-marginless">
            <input
              className="input"
              disabled={className === "disabled"}
              type={className || "text"}
              onChange={this.onChangeFandom(value)}
            ></input>
          </div>
        </div>
      </div>
    );
  }

  /**
   * Updates state's account object values as user types into searchbar
   *
   * @param value Current string held in searchbar
   */
  private onChangeAccount(value: string): (event: any) => void {
    return (event: any) => {
      const boxValue = event.target.value;
      this.setState((state: { searchAccountString: string }) => {
        return { searchAccountString: boxValue };
      }, this.searchAccountSubstring);
    };
  }

  /**
   * Search bar that update's state as string is typed
   *
   * @param label Label for the input box
   * @param value Value of input box
   */
  private accountSearchCell(
    label: string,
    value: string,
    className?: string
  ): JSX.Element {
    return (
      <div className="column is-12">
        <div className="columns">
          <div className="column is-marginless">
            <small>
              <b>{label}</b>
            </small>
          </div>
          <div className="column is-half is-marginless">
            <input
              className="input"
              disabled={className === "disabled"}
              type={className || "text"}
              onChange={this.onChangeAccount(value)}
            ></input>
          </div>
        </div>
      </div>
    );
  }
}
