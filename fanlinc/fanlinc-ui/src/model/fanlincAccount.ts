export class FanlincAccount {
  public id: string = "";
  public username: string = "";
  public email: string = "";
  public biography: string = "";
  public profileImage: string = "";
  public accountType: string = "";
  public createdTimestamp: Date = new Date();
  public fandoms: any[] = [];

  public constructor(init: FanlincAccount) {
    Object.assign(this, init);
  }
}
