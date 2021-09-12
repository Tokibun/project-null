## Product Backlog

The story is the name in Jira which is a summary of the user story [per [piazza](https://piazza.com/class/jzljn1r8l261eo?cid=52)]

The story at the bottom follow the "As a <type of user\>, I want <action\>"

The backlog is prioritized by descending order which is also the case in Jira.

*See Jira for task assignments. All stories are point estimated on Jira.*

| Story ID (as found on Jira) | Story                                      | Point Estimation |
| --------------------------- | ------------------------------------------ | ---------------- |
| NULL-4                      | Account Log In/Out                         | 8                |
| NULL-11                     | Profiles Create Fandom                     | 13               |
| NULL-12                     | Profiles Joining Fandom                    | 5                |
| NULL-13                     | Profiles Update Type and Level for Fandom  | 3                |
| NULL-14                     | Profiles Create and Modify Profile Details | 13               |
| NULL-49                     | View Fandom Page                           | 20               |
| NULL -23                    | Profiles Search Fandom                     | 8                |



## Subtasks

When a subtask says GraphQL xxx() it means create the xxx datafetcher/resolver which is the logic behind the graphql field.

#### Account Log In/Out (NULL-4)

- Basic UI (Ryan)
- Figure out how to do sessions with Spring Session (Ryan)
- login() and logout() application logic (Ryan)

#### Profiles Create Fandom (NULL-11)

- GraphQL createFandom() (Michelle)
- Basic UI (Michelle)

#### Profiles Joining Fandom (NULL-12)

- GraphQL createFandom() (Michelle)
- Basic UI for joining a fandom page (Michelle)

#### Profiles Update Type and Level for Fandom (NULL-13)

- GraphQL updateFandomLevel() and updateFandomType() (John)
- Basic UI for an update page (John)

#### Profiles Create and Modify Profile Details (NULL-14)

- GraphQL updateProfile() (Terry)
- Basic UI for profile page (Terry)
- Basic UI for update profile page. (Spencer)

#### View Fandom Page (NULL-49)

- Basic UI for a fandom page. Just the details of a fandom. (Spencer)

#### Profiles Search Fandom (NULL-23)

* GraphQL  getFandom() (Terry)
* Basic UI for fandom search page (Terry)
* Basic indicator for successful search (Terry)

#### Other

* Figure out how to test graphQL api (Ryan)
* Improve UI (Terry)
* Sprint 2 literature
* Mongo enforce unique usernames and emails (Ryan)


## User Stories

### Account Log In/Out

> As a user, I want to be able to log in/out to my account so that I can access the Fanlinc website.

JIra: NULL-4

Point Estimation: 8

CoS: Login In: Enter email/password and you have access to Fanlinc. Log Out: Press button and no longer have access to Fanlinc until you log in again.

### Profiles Create Fandom

> As a user, I want to create a fandom with a description and icon so I can start fandoms that do not exist yet.

Jira: NULL-11

CoS: Type in fandom details and be able to see fandom preview

Points: 13

 ### Profiles Joining Fandom

> As a user, I want to join an existing fandom to see fandom specific content that interests me

Jira: NULL-12

CoS: Pressed join button of an existing fandom, then the newly joined fandom is displayed in the user's profile along with fandom level and type.

Points: 5

### Profiles Update Type and Level for Fandom

> As a user, I want to be able to select my type and level of fandom for a specific fandom so my content will be tailored to my needs

Jira: NULL-13

CoS: By pressing a button on fandom page, the user is prompted to select their fandom type/level which is displayed in the user's profile along with the fandom.

Points: 3

### Profiles Create and Modify Profile Details

> As a user, I want to create and modify my profile with relevant information (username, profile picture, bio, DOB, fandoms  and levels) so that I can have some digital persona of myself on Fanlinc

Jira: NULL-14

CoS: After the user inputs new information the user can view corresponding changes on their profile.

Points: 13

### View Fandom Page

>  As a user, I want to view a fandom page to see information about the fandom page

Jira: NULL-49

CoS: Press fandom link, brought to fandom home page.

Points: 20

### Profiles Search Fandom

> As a user, I want to search for a fandom to find fandoms I am interested in.

Jira: NULL-23

CoS: Type in fandom name and see preview of the fandom page

Points: 8
