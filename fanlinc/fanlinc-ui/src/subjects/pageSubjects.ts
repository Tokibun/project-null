import { BehaviorSubject } from "rxjs";
import { FanlincAccount } from "../model/fanlincAccount";
import { Fandom } from "../model/fandom";

const emptyAccount = {
  id: "",
  username: "",
  email: "",
  biography: "",
  profileImage: "",
  accountType: "",
  createdTimestamp: new Date(),
  fandoms: []
};

// The selected profile on the profile page
export const profile$ = new BehaviorSubject<FanlincAccount | null>(null);

export function getCurrentProfile(): FanlincAccount {
  return profile$.value || currentAccount$.value || emptyAccount;
}

// The account which is logged in
export const currentAccount$ = new BehaviorSubject<FanlincAccount | null>(null);

export function getCurrentAccount(): FanlincAccount {
  return currentAccount$.value || emptyAccount;
}

export function isUserLoggedIn(): boolean {
  return currentAccount$.value !== null;
}

// The selected fandom on the fandom page
export const fandom$ = new BehaviorSubject<Fandom | null>(null);
