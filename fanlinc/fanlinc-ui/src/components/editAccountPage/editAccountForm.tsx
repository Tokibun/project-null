import React from "react";
import { BigText } from "../profilePage/bigtext";
import { FanlincAccount } from "../../model/fanlincAccount";
import gql from "graphql-tag";
import { getApolloClient } from "../../apollo/apolloClient";
import {
  fandom$,
  profile$,
  currentAccount$
} from "../../subjects/pageSubjects";
import { Cell } from "../cell/cell";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { LoginPage } from "../loginPage/loginPage";

// This is a complex component.
// It takes in a fanlinc account as it's props to set the initial values.

// It also stores one in it's state, this is to let us update the values live in the form.

// A lot of these queries and components are going to have to go to different files eventually.
// This is just a temporary debug safe space.

const EDIT_ACCOUNT = gql`
  mutation EditAccount(
    $email: String!
    $password: String!
    $biography: String!
    $profileImage: String!
  ) {
    updateAccount(
      email: $email
      password: $password
      biography: $biography
      profileImage: $profileImage
    ) {
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

const CREATE_FANDOM = gql`
  mutation createFandom(
    $displayName: String!
    $description: String!
    $bannerImageURL: String!
    $fandomLevel: String!
    $fandomType: String!
  ) {
    createFandom(
      displayName: $displayName
      description: $description
      bannerImageURL: $bannerImageURL
      fandomLevel: $fandomLevel
      fandomType: $fandomType
    ) {
      displayName
    }
  }
`;

const DELETE_ACCOUNT = gql`
  mutation {
    deleteAccount
  }
`;

interface EditAccountFormState {
  account: FanlincAccount;
}

export class EditAccountForm extends React.Component<
  FanlincAccount,
  EditAccountFormState
> {
  public constructor(props: FanlincAccount) {
    super(props);
    // When you see someone doing { ... x } it means they're making a shallow clone of x.
    // In this case it's important because we are going to be editing these values, in the state,
    // and we don't want them showing up somewhere else and updating there too. That would cause
    // problems.
    this.state = { account: { ...props }};
  }

  public componentDidUpdate(oldProps: FanlincAccount, _: any) {
    if (oldProps !== this.props) {
      this.setState({ account: { ...this.props } });
    }
  }

  public render() {
    const miscProps = { state: this.state, onChange: this.onChange.bind(this) };
    return (
      <div>
        <BigText text="Edit Account" />
        <br></br>
        <div className="columns is-multiline is-mobile">
          <Cell label="Email" value="email" {...miscProps} />
          <Cell
            label="Password"
            value="password"
            className="password"
            {...miscProps}
          />
          {this.bioTextArea("Biography", "biography")}
          <Cell
            label="Profile Picture URL"
            value="profileImage"
            {...miscProps}
          />
        </div>
        <div className="buttons is-right">
          <button
            className="button is-primary"
            onClick={this.editAccount.bind(this)}
          >
            Submit
          </button>
        </div>
        <div>
          <BigText text="Create your own Fandom!" />
          <Cell label="Fandom Name" value="displayName" {...miscProps} />
          <Cell label="Fandom Description" value="description" {...miscProps} />
          <Cell
            label="Fandom Banner URL"
            value="bannerImageURL"
            {...miscProps}
          />
            <div className="menu">
                <br />
                <b>Level:</b> &nbsp;
                <button
                    className="button is-info"
                    id={"LIMITED"}
                    onClick={() => this.updateLevel("LIMITED")}
                >
                    Limited
                </button>
                &nbsp; &nbsp;
                <button
                    className="button is-info"
                    id={"CASUAL"}
                    onClick={() => this.updateLevel("CASUAL")}
                >
                    Casual
                </button>
                &nbsp; &nbsp;
                <button
                    className="button is-info"
                    id={"INVOLVED"}
                    onClick={() => this.updateLevel("INVOLVED")}
                >
                    Involved
                </button>
                &nbsp; &nbsp;
                <button
                    className="button is-info"
                    id={"EXPERT"}
                    onClick={() => this.updateLevel("EXPERT")}
                >
                    Expert
                </button>
            </div>
            <div className="menu">
                <br />
                <b>Type:</b> &nbsp;
                <button
                    className="button is-info"
                    id={"FAN"}
                    onClick={() => this.updateType("FAN")}
                >
                    Fan
                </button>
                &nbsp; &nbsp;
                <button
                    className="button is-info"
                    id={"COSPLAYER"}
                    onClick={() => this.updateType("COSPLAYER")}
                >
                    Cosplayer
                </button>
                &nbsp; &nbsp;
                <button
                    className="button is-info"
                    id={"VENDOR"}
                    onClick={() => this.updateType("VENDOR")}
                >
                    Vendor
                </button>
            </div>
        </div>
        <div className="buttons is-right">
          <button
            className="button is-primary"
            onClick={this.createNewFandom.bind(this)}
          >
            Create Fandom
          </button>
        </div>
        <BigText text="Delete Account" />
        <p>Deleting your account means you cannot login!</p>
        <div className="buttons is-right">
          <button
            className="button is-primary is-danger is-light is-outlined is-rounded"
            onClick={this.deleteAccount.bind(this)}
          >
            Delete Account
          </button>
        </div>
      </div>
    );
  }

  private editAccount() {
    const state: any = this.state.account;
    getApolloClient()
      .mutate({
        mutation: EDIT_ACCOUNT,
        variables: {
          email: state.email || "",
          password: state.password || "",
          biography: state.biography || "",
          profileImage: state.profileImage || ""
        }
      })
      .then(result => {
        if (result) profile$.next(result.data.updateAccount);
      });
  }

  private deleteAccount() {
    getApolloClient()
      .mutate({
        mutation: DELETE_ACCOUNT
      })
      .then(result => {
        alert("Account deleted.");
        currentAccount$.next(null);
        mainContentStream$.next(<LoginPage />);
      });
  }

  private createNewFandom() {
    const state: any = this.state.account;
    console.log(state.fandomLevel+ "  asdsa  "+ state.fandomType);
    getApolloClient()
      .mutate({
        mutation: CREATE_FANDOM,
        variables: {
          // userId: state.userId || "",
          displayName: state.displayName || "",
          description: state.description || "",
          bannerImageURL: state.bannerImageURL || "",
          fandomLevel: state.fandomLevel || "",
          fandomType: state.fandomType || ""
        }
      })
      .then(result => {
        if (result !== null) {
          fandom$.next(result.data);
          alert("Succesfully created fandom");
        }
      })
      .catch(r => {
        alert(r);
      });
  }
  // This uses lambdas to bind the generic cell components to the field they are responsible for.
  // It's a dependency injection thing.
  private onChange(value: string): (event: any) => void {
    const account: any = this.state.account;
    return (event: any) => {
      account[value] = event.target.value;
      this.setState(account);
    };
  }

  private bioTextArea(
    label: string,
    value: string,
    className?: string
  ): JSX.Element {
    const account: any = this.state.account;
    return (
      <div className="column is-12">
        <div className="columns">
          <div className="column is-marginless">
            <small>
              <b>{label}</b>
            </small>
          </div>
          <div className="column is-half is-marginless">
            <textarea
              //rows doesn't work somereason
              className="textarea has-fixed-size" //changed to make box fixed
              disabled={className === "disabled"}
              //type={className || "text"} //removed
              onChange={this.onChange(value)}
              value={`${account[value] || ""}`}
            ></textarea>
          </div>
        </div>
      </div>
    );
  }

    private updateLevel(str: string) {
        const state: any = this.state.account;
        state.fandomLevel = str;
        if (str !== "LIMITED") {
            // @ts-ignore
            document
                .getElementById("LIMITED")
                .classList.replace("is-danger", "is-info");
        }
        if (str !== "CASUAL") {
            // @ts-ignore
            document
                .getElementById("CASUAL")
                .classList.replace("is-danger", "is-info");
        }
        if (str !== "INVOLVED") {
            // @ts-ignore
            document
                .getElementById("INVOLVED")
                .classList.replace("is-danger", "is-info");
        }
        if (str !== "EXPERT") {
            // @ts-ignore
            document
                .getElementById("EXPERT")
                .classList.replace("is-danger", "is-info");
        }
        // @ts-ignore
        document.getElementById(str).classList.replace("is-info", "is-danger");
    }

    private updateType(str: string) {
        const state: any = this.state.account;
        state.fandomType = str;
        if (str !== "FAN") {
            // @ts-ignore
            document.getElementById("FAN").classList.replace("is-danger", "is-info");
        }
        if (str !== "COSPLAYER") {
            // @ts-ignore
            document
                .getElementById("COSPLAYER")
                .classList.replace("is-danger", "is-info");
        }
        if (str !== "VENDOR") {
            // @ts-ignore
            document
                .getElementById("VENDOR")
                .classList.replace("is-danger", "is-info");
        }
        // @ts-ignore
        document.getElementById(str).classList.replace("is-info", "is-danger");
    }
}
