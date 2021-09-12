import { FanlincAccount } from "./fanlincAccount";

export class Fandom {
  displayName: string = "";
  description: string = "";
  bannerImageURL: string = "";
  createdTimestamp: string = "";
  accounts: FanlincAccount[] = [];

  public constructor(init: Fandom) {
    Object.assign(this, init);
  }
}
