import React from "react";
import { Box } from "../box/box";
import { FanlincAccount } from "../../model/fanlincAccount";
import { Subscription } from "rxjs";
import { profile$, getCurrentProfile } from "../../subjects/pageSubjects";
import { EditAccountForm } from "./editAccountForm";

export class EditAccountPage extends React.Component<
  FanlincAccount,
  FanlincAccount
> {
  private subs: Subscription[] = [];
  public constructor(props: FanlincAccount) {
    super(props);
    this.state = { ...this.props };
  }

  // the reason why this is here is because the edit account page needs to update live
  // if the account gets edited
  public componentDidMount() {
    this.subs.push(
      profile$.subscribe({
        next: () => {
          this.setState(getCurrentProfile());
        }
      })
    );
  }

  public render() {
    return (
      <Box>
        <EditAccountForm {...{ ...this.state }} />
      </Box>
    );
  }
}
