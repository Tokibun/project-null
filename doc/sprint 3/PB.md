## Product Backlog

The story is the name in Jira which is a summary of the user story [per [piazza](https://piazza.com/class/jzljn1r8l261eo?cid=52)]

The story at the bottom follow the user story template.

The backlog is prioritized by descending order which is also the case in Jira.

*See Jira for task assignments. All stories are point estimated on Jira.*

| Story ID (as found on Jira) | Story                                     | Point Estimation |
| --------------------------- | ----------------------------------------- | ---------------- |
| NULL-12                     | Profiles joining fandom                   | 5                |
| NULL-23                     | Profile search fandom                     | 8                |
| NULL-13                     | Profiles update type and level for fandom | 3                |
| NULL-24                     | Profiles view fandom                      | 20               |
| NULL-70                     | Search users                              | 8                |
| NULL-69                     | Login leads to profiles page              | 8                |
| NULL-74                     | Schema/Resolver Refactoring               | 8                |
| NULL-75                     | Data Fetcher Testing                      | 20               |
| NULL-71                     | Modify Account UI                         | 8                |
| NULL-73                     | Backend Refactoring                       | 20               |
| NULL-72                     | Leave fandom                              | 8                |



## User Stories

#### Profiles joining fandom (NULL-12)

>  As a user, I want to join an existing fandom to see fandom specific content that interests me

CoS: Pressed join button of an existing fandom, then the newly joined fandom is displayed in the user's profile along with fandom level and type.

Points: 5

##### Subtasks

- Barebones join fandom page (level / type) (Michelle)
- joinFandom() graphql (Michelle)

#### Profile search fandom (NULL-23)

> As a user, I want to search for a fandom to find fandoms I am interested in.

CoS: Type in fandom name and see preview of the fandom page

Points: 8

##### Subtasks

- graphql getFandom() (Terry)
- Basic UI for fandom search (Terry)
- Indicator for successful fandom search (Terry)
- Make searching redirect to the appropriate page (Spencer)
- Display results of search in box (Terry)

#### Profiles Update Type and Level for Fandom (NULL-13)

> As a user, I want to be able to select my type  and level of fandom for a specific fandom so my content will be tailored  to my needs

CoS: By pressing a button on fandom page, the user is prompted to  select their fandom type/level which is displayed in the user's profile  along with the fandom.

Points: 3

##### Subtasks

- Barebones update page (Derick)
- Display fandoms + fandom levels on the profile page (Ryan)
- Add fandoms on the profile page
- Add fandoms on the fandom page
- graphql updateFandomLevel() (Derick)
- graphql updateFandomType() (Derick)

#### Profiles View Fandom (NULL-24)

>  As a user, I want to view a fandom page to see information about the fandom page

CoS: Press fandom link, brought to fandom home page.

Points: 20

##### Subtasks

- Click on search result lead to fandom page (Terry)

#### Search Users (NULL-70)

>  As a user, I want to be able to search for other users to view their profiles to find other users

CoS: User is able to type in substring of username to see list of users. User is able to click on username to go to profile

Points: 20

##### Subtasks

- Click on search result lead to fandom page (Terry)

#### Login leads to profiles page (NULL-69)

> As a user I would like to log into Fanlinc on the Fanlinc website (i.e. what http://localhost:3000) and not an external website (http://localhost:8080/login)

CoS: Log in to Fanlinc on the main site (i.e. http://localhost:3000)

Points: 20

##### Subtasks

- Make login page on front end (Ryan)
- Make successful login redirect to Fanlinc (Ryan)

#### Schema/Resolver Refactoring (NULL-74)

> As a developer I would like to refactor the graphql schema and resolvers(i.e. data fetchers) to use the account/session stuff developed in sprint 2

CoS: The schema has changes where it preforms the respective action on the current logged in user instead of needing to specify a user id. The backend will support this.

Points: 20

##### Subtasks

- Add reflexive calls in schema (Ryan)
- Support reflexive calls in the resolvers (Ryan)

#### Backend Refactoring (NULL-73)

> As a developer I would like to refactor the backend code to have more testable code.

CoS: Bankend that is easier to code, by splitting up everything into smaller testable pieces.

Points: 20

##### Subtasks

- General refactoring (Ryan)
- Break up large classes into small ones (Ryan)
- Stop violating SRP (Ryan)

#### Modify Account UI (NULL-71)

> As a user I should be able to update my profile and fandom types to reflect my interests.

CoS: Pressed save button after making a few account modifictions, and I should receive a notification that everything was saved and updated.

Points: 20

##### Subtasks

- Display current user's account details (Derick)
- allow option for user to select and change account details (Derick)
- Add a button to save details (Derick)
- update details put the back-end side (Derick)

#### Leave fandom (NULL-72)

> As a user I would like to leave a fandom so that I am only in fandoms I am interested in.

CoS: Click a button on the fandom page to leave the fandom

Points: 20

##### Subtasks

- Backend leave fandom (Michelle)
- UI Leave Fandom button (Michelle)
