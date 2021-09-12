import React from "react";
import { Box } from "../box/box";
import { FanlincAccount } from "../../model/fanlincAccount";
import { BigText } from "./bigtext";
import { ProfilePicture } from "./profilePicture";
import { Bio } from "./bio";
import gql from "graphql-tag";
import { getApolloClient } from "../../apollo/apolloClient";
import { Fandom } from "../../model/fandom";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { FandomPage } from "../fandomPage/fandomPage";
import {
  fandom$,
  profile$,
  getCurrentProfile
} from "../../subjects/pageSubjects";
import { Subscription } from "rxjs";

const GET_MEMBERSHIPS = gql`
  query membershipsByUsername($username: String!) {
    membershipsByUsername(username: $username) {
      type
      level
      fandom {
        displayName
        description
        bannerImageURL
        createdTimestamp
      }
    }
  }
`;

/*
  This component is the main container for all the profile related things.
  It takes in a username, then queries the database for the memberships to be displayed.
*/

interface ProfilePageState extends FanlincAccount {
  listOfMemberships: Object[];
}

export class ProfilePage extends React.Component<
  FanlincAccount,
  ProfilePageState
> {
  private subs: Subscription[] = []; // This is here for a reason but i cant remember why ok
  public constructor(props: FanlincAccount) {
    super(props);
    this.state = { ...this.props, listOfMemberships: [] };
  }

  //This method runs automatically upon loading, use this to query for user's fandommemberships
  public componentDidMount() {
    console.log("mounted");
    this.getMemberships();
    this.subs.push(
      profile$.subscribe({
        next: (_: any) => {
          this.setState(getCurrentProfile());
        }
      })
    );
  }

  public componentWillUnmount() {
    this.subs.forEach(sub => sub.unsubscribe());
  }

  private loadFandomPage(fandomObject: Fandom) {
    mainContentStream$.next(<FandomPage {...fandomObject} />);
    fandom$.next(fandomObject);
  }

  /**
   * This method queries for a this user's memberships which are stored in a class varaible
   */
  private getMemberships() {
    getApolloClient()
      .query({
        query: GET_MEMBERSHIPS,
        fetchPolicy: "network-only",
        variables: {
          username: this.state.username || ""
        }
      })
      .then(result => {
        let listOfMemberships: Object[] = [];
        if (!result.data.membershipsByUsername) {
          //RESET VARIABLE
          listOfMemberships = [];
        } else {
          //ITERATE THROUGH ALL AND APPEND
          const memberships = result.data.membershipsByUsername;
          for (var i = 0; i < memberships.length; i++) {
            //push all fandom[i].displayname into state.fandomsList
            listOfMemberships.push(memberships[i]);
          }
        }
        console.log(listOfMemberships);
        this.setState({ listOfMemberships });
      });
  }

  public render() {
    return (
      <Box>
        <ProfilePicture {...this.state} />
        <br />
        <BigText text="Username:" />
        <BigText text={this.state.username} />
        <br/>

        <div className = "columns">

          <div className = "column is-half">
            <BigText text="Biography:" />
            <Bio {...this.state} />
          </div>

          <div className = "column is-half">
            <BigText text="Memberships:" />
              <Box>
              <hr/>
              <ul /*Renders details from user's memberships. */>
                {this.state.listOfMemberships.map((membership: any) => {
                  return (
                    <li key={membership.fandom.displayName}>
                      <span /*&nbsp; are empty spaces*/>
                        Fandom: &nbsp;
                        <span className="has-text-info has-text-weight-bold">
                          {membership.fandom.displayName}
                        </span>
                        &nbsp; &nbsp;
                        <button
                          className="button is-small is-primary"
                          onClick={() => this.loadFandomPage(membership.fandom)}
                        >
                          Go
                        </button>
                        <br />
                        Level: &nbsp;
                        <span className="has-text-info">{membership.level}</span>
                        <br />
                        Type: &nbsp;
                        <span className="has-text-info">{membership.type}</span>
                        <br />
                      </span>
                      <hr></hr>
                    </li>
                  );
                })}
                </ul>
                </Box>
            </div>    

        </div>
        
        
      </Box>

      /*
        ADD LIST OF USER'S FANDOMS
        */
    );
  }
}
