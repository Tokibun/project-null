## Sprint 1 Goal

The goal of this sprint is to implement basic Account/Profiles/Fandom features and also incorporate some barebones UI to interact with the backend. This is also an opportunity for the team to gain experience with the front end stuff used (i.e. react) to use in later sprints.



## Sprint Backlog

User stories in this sprint. The story is the name in Jira which is a summary of the user story [per [piazza](https://piazza.com/class/jzljn1r8l261eo?cid=52)]

In the **product backlog document** the story follows the "As a <type of user\>, I want <action\>"

*See Jira for task assignments. All stories are point estimated on Jira.*

| Story ID (as found on Jira) | Story                                      | Point Estimation |
| --------------------------- | ------------------------------------------ | ---------------- |
| NULL-3                      | Account Creation                           | 20               |
| NULL-4                      | Account Log In/Out                         | 8                |
| NULL-6                      | Account Modification                       | 13               |
| NULL-11                     | Profiles Create Fandom                     | 13               |
| NULL-12                     | Profiles Joining Fandom                    | 5                |
| NULL-13                     | Profiles Update Type and Level for Fandom  | 3                |
| NULL-14                     | Profiles Create and Modify Profile Details | 13               |
| NULL-5                      | Account Deletion                           | 3                |
| NULL-49                     | View Fandom Page                           | 20               |



### Subtasks

When a subtask says GraphQL xxx() it means create the xxx datafetcher/resolver which is the logic behind the graphql field.

#### Account Creation (NULL-3)

- Basic Profile Page UI  (Spencer)
- Date of Birth date picker
- Larger biography text box
- Account type into dropdown
- Add UI and API folders into project (Spencer)
- Connect front/back end (Spencer)
- **NULL 58 is unclear - ask spencer**

- Mongo Setup (Ryan)
- Figure out what should be stored in Mongo DB (Ryan)
- GraphQL Schema (Ryan)
- GraphQL CreateAccount() (Ryan)

#### Account Log In/Out (NULL-4)

- Basic UI (Spencer)
- Figure out how to do sessions with Spring Session (Ryan
- GraphQL login() and logout() (Spencer)

#### Account Modification (NULL-6)

- GraphQL updateAccount() (Ryan)
- Basic UI (Terry)

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

#### Account Deletion (NULL-5)

- GraphQL deleteAccount() (Ryan)
- Basic UI (Ryan)

#### View Fandom Page (NULL-49)

- Basic UI for a fandom page. Just the details of a fandom. (Spencer)

#### Other

* System Design Document (Ryan)
* Sprint Document (Ryan)
* Release Planning Meeting Document (Terry)

# Meeting Minutes

### Meeting Minutes 1 (during October 10)

- Participants: All Team + Kazam (Fanlinc Product Owner)
- Reviewed product backlog as a group and started creating subtasks on Jira.
- Discussed the planned features in the RPM and ideas for RPM.

### Meeting Minutes 2 (during October 17)

> We are having lots of trouble finding time where everyone can call during reading week. This meeting was just meant for creating more subtasks and starting the graphql api. There will be a meeting with everyone soon.

- Participants: All-Michelle
- GraphQL, MongoDB, System Design
- Writing up the project literature (RPM, sprint1.md, crc cards & system design) with discussions from tutorial.
- Created and prioritised more subtasks in Jira.
- Assigned tasks to present participants

### Meeting Minutes 3 (during October 18)

Participants: All-Spencer

- Talked about GraphQL. Went over a small graphql+mongo demo

- Assigned the leftover tasks.

- Going over the system-design.md file, diagrams and CRC cards. Ryan will make changes.

