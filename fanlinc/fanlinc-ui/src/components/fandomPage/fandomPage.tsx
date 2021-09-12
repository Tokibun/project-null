import React from "react";
import { Fandom } from "../../model/fandom";
import { Subscription } from "rxjs";
import { fandom$, getCurrentAccount } from "../../subjects/pageSubjects";
import { Box, PostBox, ReplyBox } from "../box/box";
import { Topbar } from "./topBar";
import { getApolloClient } from "../../apollo/apolloClient";
import { gql } from "apollo-boost";
import { BigText } from "../profilePage/bigtext";
import { Cell } from "../cell/cell";
import { FanlincAccount } from "../../model/fanlincAccount";
import { mainContentStream$ } from "../../subjects/mainContentArea";
import { SearchPage } from "../searchPage/searchPage";

const CREATE_POST = gql`
  mutation addPostToFandom(
    $fandomName: String!
    $message: String!
    $title: String!
  ) {
    addPostToFandom(fandomName: $fandomName, message: $message, title: $title) {
      displayName
    }
  }
`;

const CREATE_REPLY = gql`
  mutation replyToPost($postID: ID!, $message: String!) {
    replyToPost(postID: $postID, message: $message) {
      title
    }
  }
`;

const GET_POSTS = gql`
  query postsByFandomName($fandomName: String!) {
    postsByFandomName(fandomName: $fandomName) {
      id
      title
      message
      authorId
      creatorLevel
      creatorType
      creatorUsername
    }
  }
`;

const GET_REPLIES = gql`
  query repliesToPost($postID: ID!) {
    repliesToPost(postID: $postID) {
      id
      message
      authorId
      creatorLevel
      creatorType
      creatorUsername
    }
  }
`;

const DELETE_POST = gql`
  mutation deletePostFromFandom($postId: ID!) {
    deletePostFromFandom(postId: $postId)
  }
`;

const DELETE_REPLY = gql`
  mutation deleteReplytoPost($postID: ID!, $replyID: ID!) {
    deleteReplytoPost(postID: $postID, replyID: $replyID)
  }
`;

const JOIN_FANDOM_BY_NAME = gql`
  mutation JoinFandomByName(
    $fandomName: String!
    $fandomLevel: String!
    $fandomType: String!
  ) {
    joinFandomByFandomName(
      fandomName: $fandomName
      fandomLevel: $fandomLevel
      fandomType: $fandomType
    ) {
      description
    }
  }
`;
const LEAVE_FANDOM = gql`
  mutation LeaveFandomByName($fandomName: String!) {
    leaveFandomByName(fandomName: $fandomName) {
      description
    }
  }
`;

const UPDATE_FANDOM = gql`
  mutation updateFandomMembership(
    $fandomName: String!
    $fandomLevel: String!
    $fandomType: String!
  ) {
    updateFandomMembership(
      fandomName: $fandomName
      fandomLevel: $fandomLevel
      fandomType: $fandomType
    ) {
      displayName
    }
  }
`;

const GET_MEMBERSHIPS = gql`
  query membershipsByUsername($username: String!) {
    membershipsByUsername(username: $username) {
      type
      level
      fandom {
        displayName
      }
    }
  }
`;
/*
Terry's notes
*/

interface FandomPageState extends Fandom {
  postTitle: String;
  postMessage: String;
  posts: Object[];
  replies: Object[];
  single_reply_body: String;
  post_identity: String;
  post_comment_number: Object[];
}

export class FandomPage extends React.Component<Fandom, FandomPageState> {
  private infandom: boolean;
  private account: FanlincAccount;
  private menu_one: boolean;
  private menu_two: boolean;
  private menu_three: boolean;
  public css: String[] = [];
  public constructor(props: Fandom) {
    super(props);
    this.state = {
      ...this.props,
      postTitle: "",
      postMessage: "",
      posts: [],
      replies: [],
      single_reply_body: "",
      post_identity: "",
      post_comment_number: []
    };
    this.infandom = false;
    this.account = getCurrentAccount();
    this.menu_one = false;
    this.menu_two = false;
    this.menu_three = false;
    this.getMemberships();
  }

  private subs: Subscription[] = [];

  /**
   * Calls mutation using state variables with values in reply body
   */
  private addReply(id: string) {
    const as = { state: this.state, id: id };
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: CREATE_REPLY,
        //fetchPolicy: "network-only",
        variables: {
          postID: as.id,
          message: state.single_reply_body
        }
      })
      .then(result => {
        alert("Replied!");
        // @ts-ignore
        this.state.single_reply_body = "";
        this.getReplies(id);
      })
      .catch(r => {
        alert(r);
      });
  }

  private createPost() {
    getApolloClient()
      .mutate({
        mutation: CREATE_POST,
        //fetchPolicy: "network-only",
        variables: {
          fandomName: this.state.displayName,
          message: this.state.postMessage,
          title: this.state.postTitle
        }
      })
      .then(result => {
        alert("Post Created!");
        // @ts-ignore
        this.state.postMessage = "";
        // @ts-ignore
        this.state.postTitle = "";
        this.getPosts();
      });
  }

  /**
   * Gets posts of the current fandom and stores into state variable
   */
  private getReplies(id: string) {
    const as = { state: this.state, id: id };
    getApolloClient()
      .query({
        query: GET_REPLIES,
        fetchPolicy: "network-only",
        variables: {
          postID: as.id
        }
      })
      .then(result => {
        this.setState((state: { replies: Object[] }) => {
          return { replies: [] };
        });
        if (!result.data.repliesToPost) {
          //RESET VARIABLE
          this.setState((state: { replies: Object[] }) => {
            return { replies: [] };
          });
        } else {
          //ITERATE THROUGH ALL AND APPEND
          const reply = result.data.repliesToPost;
          for (var i = 0; i < reply.length; i++) {
            this.setState((prevState: any) => ({
              replies: [reply[i], ...prevState.replies]
            }));
          }
        }
        this.setState({ post_identity: id });
      })
      .catch(r => {
        console.log(r);
      });
  }
  /**
   * Gets posts of the current fandom and stores into state variable
   */
  private getPosts() {
    getApolloClient()
      .query({
        query: GET_POSTS,
        fetchPolicy: "network-only",
        variables: {
          fandomName: this.state.displayName || ""
        }
      })
      .then(result => {
        this.setState((state: { posts: Object[] }) => {
          return { posts: [] };
        });
        if (!result.data.postsByFandomName) {
          //RESET VARIABLE
          this.setState((state: { posts: Object[] }) => {
            return { posts: [] };
          });
        } else {
          //ITERATE THROUGH ALL AND APPEND
          const posts = result.data.postsByFandomName;
          for (var i = 0; i < posts.length; i++) {
            this.setState((prevState: any) => ({
              posts: [posts[i], ...prevState.posts]
            }));
          }
        }
      });
  }

  public componentDidMount() {
    this.getPosts();
    this.subs.push(
      fandom$.subscribe({
        next: (fandom: Fandom | null) => {
          if (!fandom) {
            mainContentStream$.next(<SearchPage />);
            return;
          }
          this.setState(fandom);
          this.account = getCurrentAccount();
        }
      })
    );
  }

  public componentWillUnmount() {
    this.subs.forEach(sub => sub.unsubscribe());
  }

  private onChangePostTitle(value: string): (event: any) => void {
    return (event: any) => {
      const boxValue = event.target.value;
      this.setState((state: { postTitle: String }) => {
        return { postTitle: boxValue };
      });
    };
  }

  private onChangePostMessage(value: string): (event: any) => void {
    return (event: any) => {
      const boxValue = event.target.value;
      this.setState((state: { postMessage: String }) => {
        return { postMessage: boxValue };
      });
    };
  }
  private onChangeReplyMessage(value: string): (event: any) => void {
    return (event: any) => {
      const boxValue = event.target.value;
      this.setState((state: { single_reply_body: String }) => {
        return { single_reply_body: boxValue };
      });
    };
  }

  public render() {
    const postTitleProps = {
      state: this.state,
      onChange: this.onChangePostTitle.bind(this)
    };
    const postMessageProps = {
      state: this.state,
      onChange: this.onChangePostMessage.bind(this)
    };
    const replyProps = {
      state: this.state,
      onChange: this.onChangeReplyMessage.bind(this)
    };
    if (this.account.username !== "") {
      return (
        <div>
          <Topbar {...this.state} />
          <div className="columns">
            <div className="column is-half">
              <BigText text="Posts" /> <br />
              <Box>
                <ul>
                  {this.state.posts.map((post: any) => {
                    return (
                      <PostBox>
                        <li key={post.id}>
                          <b>{post.title}</b>
                          <br />
                          By:&nbsp; {post.creatorUsername}
                          <br />
                          Level:&nbsp; {post.creatorLevel}
                          <br />
                          Type:&nbsp;{post.creatorType}
                          <br /> <br />
                          <Box>{post.message}</Box>
                        </li>
                        {this.account.username == post.creatorUsername ? (
                          <div>
                            <button
                              className="button is-danger"
                              onClick={() => this.deletePost(post.id)}
                            >
                              <span>Delete</span>
                            </button>
                          </div>
                        ) : null}

                        <div>
                          {this.state.post_identity == post.id ? (
                            <div>
                              {this.infandom ? (
                                <div>
                                  <Box>
                                    <Cell
                                      label="Reply"
                                      value="single_reply_body"
                                      {...replyProps}
                                    />
                                    <div className="buttons is-left">
                                      <button
                                        className="button is-primary"
                                        onClick={() => this.addReply(post.id)}
                                      >
                                        Reply!
                                      </button>
                                      &nbsp; &nbsp;
                                      <button
                                        className="button is-right"
                                        onClick={() =>
                                          this.setState(
                                            (state: {
                                              post_identity: String;
                                              single_reply_body: String;
                                            }) => {
                                              return {
                                                single_reply_body: "",
                                                post_identity: ""
                                              };
                                            }
                                          )
                                        }
                                      >
                                        Hide Comments
                                      </button>
                                    </div>
                                  </Box>
                                </div>
                              ) : null}
                              {this.state.replies.map((replies: any) => {
                                return (
                                  <ReplyBox>
                                    <li key={replies.id}>
                                      {this.account.username ==
                                      replies.creatorUsername ? (
                                        <div>
                                          <button
                                            className="button is-danger"
                                            onClick={() =>
                                              this.deleteReply(
                                                post.id,
                                                replies.id
                                              )
                                            }
                                          >
                                            <span>Delete</span>
                                          </button>
                                        </div>
                                      ) : null}
                                      <br /> <b>{replies.creatorUsername}</b>{" "}
                                      <br />
                                      {replies.message}
                                    </li>
                                  </ReplyBox>
                                );
                              })}
                            </div>
                          ) : (
                            <div>
                              <button
                                className="button is-info"
                                onClick={() => {
                                  this.getReplies(post.id);
                                  this.setState(
                                      (state: {
                                        single_reply_body: String;
                                      }) => {
                                        return {
                                          single_reply_body: "",
                                        };
                                      });
                                }}
                              >
                                <span>Show comments</span>
                              </button>
                            </div>
                          )}
                        </div>
                      </PostBox>
                    );
                  })}
                </ul>
              </Box>
            </div>

            <div className="column is-half">
              {this.infandom ? (
                <div>
                  <BigText text="Create a Post" /> <br />
                  <Box>
                    <Cell label="Title" value="postTitle" {...postTitleProps} />
                    <Cell
                      label="Message"
                      value="postMessage"
                      {...postMessageProps}
                    />
                    <div className="buttons is-right">
                      <button
                        className="button is-primary"
                        onClick={this.createPost.bind(this)}
                      >
                        Create!
                      </button>
                      &nbsp; &nbsp;
                    </div>
                  </Box>
                </div>
              ) : null}

              {this.account.username != "" ? (
                <div className="Logged in">
                  {/*Join Fandom*/}
                  {!this.infandom ? (
                    <div>
                      <BigText text="Join this Fandom" /> <br />
                    </div>
                  ) : (
                    <div>
                      <BigText text="Change Membership Status!" />
                    </div>
                  )}
                  <Box>
                    <button
                      className="button is-primary"
                      id={"fandomLevel"}
                      onClick={this.updatemenu_oneValue.bind(this)}
                    >
                      Show Level options
                    </button>
                    &nbsp;
                    <button
                      className="button is-primary"
                      id={"fandomType"}
                      onClick={this.updatemenu_twoValue.bind(this)}
                    >
                      Show Type options
                    </button>
                    {this.menu_one ? (
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
                    ) : null}
                    {this.menu_two ? (
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
                    ) : null}
                    {this.infandom ? (
                      <div>
                        <button
                          className="button is-primary"
                          onClick={this.updateFandom.bind(this)}
                        >
                          Update Fandom Info
                        </button>
                        &nbsp; &nbsp;
                        <button
                          className="button is-primary"
                          onClick={this.leaveFandom.bind(this)}
                        >
                          Leave Fandom
                        </button>
                      </div>
                    ) : (
                      <div className="buttons is-right">
                        <button
                          className="button is-primary"
                          onClick={this.joinFandom.bind(this)}
                        >
                          Join Fandom
                        </button>
                      </div>
                    )}
                  </Box>
                </div>
              ) : null}
            </div>
          </div>
        </div>
      );
    }
  }

  private getMemberships() {
    getApolloClient()
      .query({
        query: GET_MEMBERSHIPS,
        variables: {
          username: this.account.username || ""
        }
      })
      .then(result => {
        if (
          //NULL CHECK
          result.data.membershipsByUsername === null ||
          result.data.membershipsByUsername === []
        ) {
          //RESET VARIABLE
        } else {
          //ITERATE THROUGH ALL AND APPEND
          const memberships = result.data.membershipsByUsername;
          this.infandom = false;
          for (var i = 0; i < memberships.length; i++) {
            //push all fandom[i].displayname into state.fandomsList
            //this.listOfMemberships.push(memberships[i]);
            if (memberships[i].fandom.displayName === this.state.displayName) {
              this.infandom = true;
            }
          }
        }
      });
  }

  private updateinfandomValue() {
    this.infandom = !this.infandom;
    this.forceUpdate();
  }

  private updatemenu_oneValue() {
    this.menu_one = !this.menu_one;
    this.forceUpdate();
  }

  private updatemenu_twoValue() {
    this.menu_two = !this.menu_two;
    this.forceUpdate();
  }
  private updateLevel(str: string) {
    const state: any = this.state;
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
    const state: any = this.state;
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
  private deletePost(id: string) {
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: DELETE_POST,
        variables: {
          postId: id
        }
      })
      .then(r => {
        alert("Sucessfully deleted Post");
        this.getPosts();
      })
      .catch(r => {
        alert(r);
      });
  }

  private deleteReply(postid: string, replyid: string) {
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: DELETE_REPLY,
        variables: {
          postID: postid,
          replyID: replyid
        }
      })
      .then(r => {
        this.getReplies(postid);
        alert("Sucessfully deleted Comment");
      })
      .catch(r => {
        alert(r);
      });
  }

  private joinFandom() {
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: JOIN_FANDOM_BY_NAME,
        variables: {
          fandomName: state.displayName,
          fandomLevel: state.fandomLevel,
          fandomType: state.fandomType
        }
      })
      .then(r => {
        alert("Sucessfully joined Fandom");
        this.updateinfandomValue();
      })
      .catch(r => {
        alert(
          "Couldn't join the fandom.\nDid you select a Fandom Level and Fandom Type?"
        );
      });
  }
  private leaveFandom() {
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: LEAVE_FANDOM,
        variables: { fandomName: state.displayName }
      })
      .then(r => {
        alert("Success! Left Fandom.");
        this.updateinfandomValue();
      })
      .catch(r => {
        alert(r);
      });
  }

  private updateFandom() {
    const state: any = this.state;
    getApolloClient()
      .mutate({
        mutation: UPDATE_FANDOM,
        variables: {
          fandomName: state.displayName,
          fandomLevel: state.fandomLevel || "",
          fandomType: state.fandomType || ""
        }
      })
      .then(r => {
        alert("Successfully updated fandom details");
      })
      .catch(r => {
        alert(r);
      });
  }
}
