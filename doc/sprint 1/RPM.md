We are following the [Release Planning](https://www.scrumdesk.com/start/manual-for-scrumdesk-start/release-planning/) article found in the tutorials tab of the course website.

---

### Participants

Ryan Sue, Terry Wong, Spencer Paulmark, Michelle Kee, Derick Amalraj

*Kazam (Fanlinc Representative) as the Product Owner*

### Completion Date

End of the semester (November 29)

### Major Release Goals

Reference the user stories.

### Release Plan and Tentative Sprint Goals

Our user stories in the product backlog are slotted into the 3 individual sprints. Point estimates can be found on the product backlog, in the tables below and on Jira. These are the tentative sprint goals.

#### Sprint 1

The goal of this sprint is to create the basic Account/Profiles/Fandom operations and incorporate some barebones UI to run these operations.

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

**Total:** 98

#### Sprint 2

The goal of this sprint is to finish anything from sprint 1 (if sprint 1 was too ambitious) and work on the Posts and Events feature of Fanlinc.

| Story ID (as found on Jira) | Story                        | Point Estimation |
| --------------------------- | ---------------------------- | ---------------- |
| NULL-15                     | Profiles Searching Profiles  | 8                |
| NULL-23                     | Profiles Search Fandom       | 3                |
| NULL-7                      | Event Creation               | 5                |
| NULL-9                      | Event View Event Details     | 5                |
| NULL-10                     | Event Update Details         | 8                |
| NULL-16                     | Posts Create Posts on Fandom | 5                |


**Total:** 60

#### Sprint 3

The goal of this sprint is to create chat.

| Story ID (as found on Jira) | Story                        | Point Estimation |
| --------------------------- | ----------------             | ---------------- |
| NULL-17                     | Posts View Posts on Fandom   | 5                |
| NULL-18                     | Posts Comment on Post        | 5                |
| NULL-19                     | Posts Reply to Comment       | 5                |
| NULL-20                     | Posts Edit a Post            | 5                |
| NULL-21                     | Posts Remove a Post          | 3                |
| NULL-22                     | Posts Avoid Spoilers         | 3                |

**Total:** ?



### Risks

- The team's velocity may need to be adjusted per our sprint 1 performance. We are possibly too ambitious for sprint 1 as we intend to implement basic Account and Fandom features with a dummy front end UI. The burndown of story points in sprint 1 will allow the group to decide if the velocity will be adjusted.

- Sprint 1 has such a high velocity due to external factors (reading week) and because future sprints depend heavily on work done in sprint 1.
- Using new technologies that not everyone is familiar with such as GraphQL, MongoDB and React. So, as the team gains more experience, the point estimations can be revised and re-estimated.
- The CoS for some user stories may be too ambitious which may need to be changed in the future.
- Sprint 3 is left a bit ambiguous due possibly overly ambitious sprint 1 and sprint 2 along with our inexperience in sprint planning.



### Dependencies

- Front end depends heavily on the back end.
- Back end depends on a MongoDB cloud service, [MongoDB Atlas](https://www.mongodb.com/cloud/atlas). Specifically, we are using the Free plan to host our mongo server.
- Everything in future sprints depends heavily on work done in sprint 1 due to sprint 1 being the sprint that focuses on User Accounts. Specifically, posts, events, comments and contacts.

