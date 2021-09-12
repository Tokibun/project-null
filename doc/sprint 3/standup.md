These are copy-pasted from our Discord server with timestamps included. We have standups via text chat and put the major things in this separate channel dedicated to the standups.

The discord nicknames have been converted to full names for the sake of TA clarity

**Join our discord Ilir.**

## November 5-9

No progress. Midterm & other courses.

### November 10

```
[9:31 PM] Ryan Sue:
What I plan on doing: Schema Additions. Will add analogous methods to schema that don't require userid field as it uses the logged in user. Will be done by Monday night.
What I've done: null
What I've done: null
```

### November 11

```
[7:30 PM] Terry Wong:
What I plan on doing: Writing schema things for getting documents containing a substring for fandom name field
What I've done: null
Anything blocking me: Not blocked on anything
```

### November 12

```
[10:00 AM] Spencer Paulmark:
What I plan on doing: the fandom page sometime today
What I've done: null
Anything blocking me: not sure we will see
```

```
[10:52 AM] Ryan Sue: 
What I plan on doing: Finish the schema changes on the reflexive-schema branch, start to write some basic unit tests for the data fetchers.
What I've done: null
Anything blocking me: Won't merge schema stuff to master before consulting @Spencer Paulmark

[11:03 AM] Ryan Sue:
What I plan on doing: I'll also finish all the Sprint literature, or enough for us to talk and make changes if needed
(That means bullet points)
What I've done: null
Anything blocking me: null

[2:56 PM] Spencer Paulmark:
What I plan on doing: I'm changing fandomIdByName to fandomByName because it's stupid to search for a fandom and only return the ID as opposed to returning the entire fandom including the ID
What I've done: null
Anything blocking me: null

[3:35 PM] Spencer Paulmark:
What I did: I made a bIG commit
You can search for a fandom and it will appear on the fandom page
I changed all instances of "bannerImage" to "bannerImageURL" because consistency

[5:11 PM] Ryan Sue:
What I did: Moved login/logout page to react side of things.
Anything blocking me: /login?success and /login?failure display a message to the user.

[9:38 PM] Ryan Sue:
What I plan on doing: More backend testing.
What I did: Schema changes, and I made sure the front/back work. Also added the image, for graphql query/mutation debugging while we are transitioning to use the new schema changes.
Anything blocking me: No
Image: https://cdn.discordapp.com/attachments/636937555979534337/644003231613321216/unknown.png
```

### November 13

```
[8:15 AM] Michelle Kee:
What I plan on doing: Adding pop ups or something to let the user know whether create/join/leave fandom are successful. Move join fandom to fandom page.

What I did: Leave fandom WorkS

Anything blocking me: not rlly just need to figure out how to get info from fandom pg
Don't know how to get cells on fandom page too @Spencer help
```

### November 14

```
[2:48 AM] Terry Wong:
What I plan on doing: Doing search user similar to search fandom
What I have done: Finished search fandom. Please take a look and give feedback
What I am blocked on: Generic "view user" page needs to be made

[8:25 PM] Terry Wong:
What I plan on doing: Separating edit account form into two separate things. General account page and everything else
What I have done: Set up search account substring functionality. Links do not lead to account page.
What I am blocked on: Account page component needs to be separated out of edit account form
```

### November 15

```
[9:37 AM] Michelle Kee: Blocked on cells @Spencer
```

### November 16

```
[10:50 AM] Spencer Paulmark: @Michelle @Derick 
What I have done: i did it (cells.)
```

### November 17

```
[11:59 AM] Ryan Sue:
What I plan on doing: Sprint Literature, bullet points to words.
What I have done: Started working on it.
What I am blocked on: Terry, Derick answer my text, your user story for the pb.

[2:24 PM] Ryan Sue:
What I plan on doing: More tests for data fetchers.
What I am blocked on: MongoRepo is null in my tests.

[4:37 PM] Ryan Sue:
What I plan on doing: Make some argumentes non-null in schema since they shouldn't be null.
What I am blocked on: no.

[9:27 PM] Spencer Paulmark:
What I did:
I make the profile page segregate based on if you are logged in or not
I make the tags for fandoms
Some other third thing

[9:35 PM] Michelle Kee:
What I plan on doing: Adding popups/error msgs to createFandom, joinFandom, leaveFandom to the UI
What I have done: All those features mentioned, and on UI. Specifially, joinFandom is moved onto the Fandom page. Leave Fandom is added to the Fandom page. 
Block: figuring out how to add those popups- so will take time

[9:36 PM] Spencer Paulmark:
hint: probably use a library for that

```

## November 18

```
[2:42 AM] Derick Amalraj:
What I did: made it so the login page changes after you in. So that the user can't log in again. However if the user logs out then the page reverts back to a login page

[2:42 AM] Terry Wong:
What I did: 
* Split up profile page
* Previous profile page with all the edit account form on the right is now called profilePageOld. profilePageOld is now the default page. profilePageOld should not be used in the final iteration of the project.
* Profile page that contains only bio, username, profile pic (and subscribed fandoms TO BE DONE) is now called profilePage
* EditAccountPage is a page that contains the edit account form. The settings button now goes to this page.

What I plan to do:
* List all of a user's fandoms and respective levels/types on their profile page

What I am blocked on:
*How do I access the currently logged in account?
    *send aid
    
[2:56 AM] Michelle Kee: 
What I did: lets user know if something worked
What I am blocked on: How to I check if the mutations have thrown and exception in frontend?

[3:33 AM] Terry Wong:
What I am blocked on: We have no way to query for the hashmap of a user's fandom memberships. Currently figuring that out

[11:15 AM] Terry Wong:
Solved the issue 'query for fandoms' issue. Created new object, fandommembershipentry that stores a fandom object, level, and type for a specific user. Added new query that returns a list of fandommembershipentry from username

[11:16 AM] Terry Wong:
What I plan on doing:
Going to use this to display list of a user's joined fandoms. Alongside their level and type. If time permits, make a button for each list entry that goes to fandompage (That's why fandomObject is stored in fandomlistentry)

[11:52 AM] Spencer Paulmark:
What I plan on doing: im going to look into fixing up this login page

[12:02 PM] Ryan Sue: not free until 3, ill be around and do more stuff then. mainly testing and change sprint literature

[12:13 PM] Spencer Paulmark: 
What I did: there's a function that you can call now which is getCurrentAccount() which will give the username of the current account. that is the one that is logged in

[12:42 PM] Spencer Paulmark:
What I did: searching for a user makes it so that user's profile comes up -- and stays up next time you click on the profile tab. also switched over to the new profile page.

[1:20 PM] Spencer Paulmark:
What I did: When you log in, it actually shows if you're logged or not. Also, when you log in, it shows your profile by default. RIP Cat_Holic

[1:26 PM] Spencer Paulmark:
What I did: you can now just call the function getCurrentAccount() and it will get the current account.You stay logged in on the server until you click logout or reset the server, even if you refresh the app.
```

