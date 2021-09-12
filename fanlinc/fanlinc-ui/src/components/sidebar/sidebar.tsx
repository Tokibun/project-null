import React from "react";
import { Box } from "../box/box";
import { SidebarIcon } from "../icons/sidebarIcon";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { FandomPage } from "../fandomPage/fandomPage";
import { SearchPage } from "../searchPage/searchPage";
import { LoginPage } from "../loginPage/loginPage";
import {
  fandom$,
  isUserLoggedIn,
  getCurrentAccount,
  getCurrentProfile,
  currentAccount$
} from "../../subjects/pageSubjects";
import { EditAccountPage } from "../editAccountPage/editAccountPage";
import { ProfilePage } from "../profilePage/profilePage";

export function Sidebar(): JSX.Element {
  return (
    <Box>
      <div style={{ maxWidth: 90 }}>
        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/logo-foursquare.svg"
          onClick={() => {
            window.location.reload();
          }}
        />
        <hr />
        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/md-person.svg"
          onClick={() => {
            if (isUserLoggedIn()) {
              mainContentStream$.next(<ProfilePage {...getCurrentProfile()} />);
            } else {
              mainContentStream$.next(<LoginPage />);
            }
          }}
        />
        <p className="has-text-centered">Profile</p>

        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/md-people.svg"
          onClick={() => {
            const currentFandom = fandom$.getValue();
            currentFandom
              ? mainContentStream$.next(<FandomPage {...currentFandom} />)
              : mainContentStream$.next(<SearchPage />);
          }}
        />
        <p className="has-text-centered">Fandom</p>

        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/md-search.svg"
          onClick={() => {
            mainContentStream$.next(<SearchPage />);
          }}
        />
        <p className="has-text-centered">Search</p>

        <hr />

        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/md-contact.svg"
          onClick={() => {
            if (isUserLoggedIn()) {
              mainContentStream$.next(<ProfilePage {...getCurrentAccount()} />);
            } else {
              mainContentStream$.next(<LoginPage />);
            }
          }}
        />
        <p className="has-text-centered">My Profile</p>

        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/md-log-in.svg"
          onClick={() => {
            mainContentStream$.next(<LoginPage />);
          }}
        />
        <p className="has-text-centered">Login</p>

        <SidebarIcon
          svg="https://ionicons.com/ionicons/svg/md-settings.svg"
          onClick={() => {
            if (isUserLoggedIn()) {
              mainContentStream$.next(
                <EditAccountPage {...getCurrentAccount()} />
              );
            } else {
              mainContentStream$.next(<LoginPage />);
            }
          }}
        />
        <p className="has-text-centered">Settings</p>
      </div>
    </Box>
  );
}
