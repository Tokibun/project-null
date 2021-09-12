package ca.utoronto.utsc.fanlinc.datafetchers;

import ca.utoronto.utsc.fanlinc.exceptions.AccountNotFoundException;
import ca.utoronto.utsc.fanlinc.exceptions.FandomNotFoundException;
import ca.utoronto.utsc.fanlinc.model.Account;
import ca.utoronto.utsc.fanlinc.model.Fandom;
import ca.utoronto.utsc.fanlinc.model.Post;
import ca.utoronto.utsc.fanlinc.model.Reply;
import ca.utoronto.utsc.fanlinc.repository.AccountRepository;
import ca.utoronto.utsc.fanlinc.repository.FandomRepository;
import ca.utoronto.utsc.fanlinc.repository.PostRepository;
import ca.utoronto.utsc.fanlinc.repository.RepliesRepository;
import ca.utoronto.utsc.fanlinc.service.CurrentAccountService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class FandomDataFetcher {

    private AccountRepository accountRepository;
    private FandomRepository fandomRepository;
    private CurrentAccountService currentAccountService;
    private PostRepository postRepository;
    private RepliesRepository repliesRepository;

    @Autowired
    public FandomDataFetcher(AccountRepository accountRepository, FandomRepository fandomRepository, PostRepository postRepository, RepliesRepository repliesRepository, CurrentAccountService currentAccountService) {
        this.accountRepository = accountRepository;
        this.fandomRepository = fandomRepository;
        this.currentAccountService = currentAccountService;
        this.postRepository = postRepository;
        this.repliesRepository = repliesRepository;
    }

    /*
     * Query
     */

    /**
     * Returns the fandom with the associated id or null if not found
     */
    public DataFetcher fandomById() {
        return env -> {
            String fandomId = env.getArgument("id");
            return fandomRepository.findById(fandomId).orElse(null);
        };
    }

    /**
     * Returns the fandom with the associated name or null if not found.
     */
    public DataFetcher fandomByName() {
        return env -> {
            String fandomId = env.getArgument("name");
            return fandomRepository.findFandomByName(fandomId).orElse(null);
        };
    }

    /**
     * Returns the fandom with the associated fandom name or null if not found
     * <p>
     * Why is this needed?!
     */
    public DataFetcher fandomIdByName() {
        return env -> {
            String fandomName = env.getArgument("name");
            Fandom fandom = fandomRepository.findFandomByName(fandomName).orElse(null);

            return fandom.getId();
        };
    }

    /**
     * Iterates through all fandoms to find those with a substring in title
     *
     * @return List of fandoms that contain a substring in its title
     */
    public DataFetcher fandomsByNameSubstring() {
        return env -> {
            String substring = env.getArgument("fandomNameSubstring");
            if (substring.equals("")) {
                return new ArrayList<Fandom>();
            } else {
                //Gets all fandoms, puts relevant ones into arrayList -> returned as array
                ArrayList<Fandom> fandomList = new ArrayList<>();
                Iterator i = fandomRepository.findAll().iterator();
                while (i.hasNext()) {
                    Fandom fandom = (Fandom) i.next();
                    if (fandom.getDisplayName().contains(substring)) {
                        fandomList.add(fandom);
                    }
                }
                return fandomList;
            }
        };
    }

    /*
     * Mutation
     */

    /**
     * Creates fandom with given information where current user is the creator of the fandom
     * If the userId is specified, that user will be the creator of the fandom.
     * Else the current user is the user id. If the current user is not logged in, then no fandom is created.
     * <p>
     * Schema: createFandom, createFandomById
     */
    public DataFetcher createFandom() {
        return env -> {
            String name = env.getArgument("displayName");
            Fandom fandom = fandomRepository.findFandomByName(name).orElse(null);
            if (fandom != null) {
                throw new Exception("Fandom exists. No.");
            }

            // Information about first user being added to fandom
            String creatorType = (env.getArgument("fandomType"));
            String creatorLevel = (env.getArgument("fandomLevel"));

            Account creatorAccount;
            if (!env.containsArgument("userId")) {
                creatorAccount = currentAccountService.getCurrentAccount();
            } else {
                String creator = env.getArgument("userId");
                creatorAccount = accountRepository.findByIdNotDeleted(creator).orElse(null);
            }

            if (creatorAccount == null) throw new AccountNotFoundException(); // No creator!

            // Fandom info
            String desc = env.getArgument("description");
            String banner = env.getArgument("bannerImageURL");
            long date = System.currentTimeMillis();

            Fandom newFandom = new Fandom(name, desc, banner, date, creatorAccount.getId(), creatorLevel, creatorType);

            fandomRepository.insert(newFandom);
            creatorAccount.insertFandomMembership(newFandom.getId(), env.getArgument("fandomLevel"),
                    env.getArgument("fandomType"));
            accountRepository.save(creatorAccount);

            return fandomRepository.save(newFandom);
        };
    }

    /**
     * Adds user to fandom with desired level and type specified by the id of the fandom
     * If the userId is not specified, the current user (if non-null) will join the fandom
     * <p>
     * Schema: joinFandomByFandomId, joinFandomById
     */
    public DataFetcher joinFandom() {
        return env -> {
            String fandomId = env.getArgument("fandomId");

            //Check if the fandom and user exist
            Fandom f = fandomRepository.findById(fandomId).orElse(null);
            if (f == null) {
                throw new FandomNotFoundException();
            }
            Account person;
            if (env.containsArgument("userId")) {
                String userId = env.getArgument("userId");
                person = accountRepository.findById(userId).orElse(null);
            } else {
                person = currentAccountService.getCurrentAccount();
            }
            if (person == null) throw new AccountNotFoundException();

            String fandomType = (env.getArgument("fandomType"));
            String fandomLevel = (env.getArgument("fandomLevel"));
            f.addUsertoSpecificCategoryTable(person.getId(), fandomType);
            f.addUsertoSpecificCategoryTable(person.getId(), fandomLevel);
            f.addUsertoSpecificCategoryTable(person.getId(), "ALL USERS");
            person.insertFandomMembership(f.getId()
                    , env.getArgument("fandomLevel"), env.getArgument("fandomType"));
            accountRepository.save(person);
            //Decide on what to return when success
            return fandomRepository.save(f);
        };
    }

    /**
     * Adds user to the fandom with desired level and type specified by the name of the fandom
     * If the userId is not specified, the current user (if non-null) will join the fandom
     * <p>
     * Schema: joinFandomByFandomName, joinFandomByUsernameAndId
     */
    public DataFetcher joinFandomByFandomName() {
        return env -> {
            String fandomName = env.getArgument("fandomName");
            String fandomType = env.getArgument("fandomType");
            String fandomLevel = env.getArgument("fandomLevel");

            Account account;
            if (!env.containsArgument("username")) { // Get the logged in user
                account = currentAccountService.getCurrentAccount();
            } else {
                String username = env.getArgument("username");
                account = accountRepository.findByUsernameNotDeleted(username).orElse(null);
            }

            if (account == null) throw new AccountNotFoundException();

            //Find the fandom
            Fandom f = fandomRepository.findFandomByName(fandomName).orElse(null);
            if (f == null) throw new FandomNotFoundException();

            String userId = account.getId();
            if (account.getFandomMemberships().containsKey(f.getId()))
                throw new Exception("User already in Fandom");
            f.addUsertoSpecificCategoryTable(userId, fandomType);
            f.addUsertoSpecificCategoryTable(userId, fandomLevel);
            f.addUsertoSpecificCategoryTable(userId, "ALL USERS");
            account.insertFandomMembership(f.getId(), fandomLevel, fandomType);
            accountRepository.save(account);
            return fandomRepository.save(f);
        };
    }

    /**
     * Updates the fandom membership of a user, i.e. changes their fandom level & type for a specific fandom given
     * by the fandom id.
     * Schema: updateFandomMembership, updateFandomMembershipById
     */
    public DataFetcher updateFandomMembership() {
        return env -> {
            String fandomName = env.getArgument("fandomName");

            //Check if the fandom and user exist
            Fandom fan = fandomRepository.findFandomByName(fandomName).orElse(null);
            if (fan == null) throw new FandomNotFoundException();

            Account person;
            String userId;
            person = currentAccountService.getCurrentAccount();


            if (person == null) throw new AccountNotFoundException();
            userId = person.getId();

            if (env.containsArgument("fandomLevel")) {
                String level = env.getArgument("fandomLevel");
                if (level.equals("LIMITED") || level.equals("CASUAL") || level.equals("INVOLVED") ||
                        level.equals("EXPERT")) {
                    //fan.
                    String previousLevel = person.getFandomMembershipLevel(fan.getId());
                    fan.updateUserinCategoryTable(userId, previousLevel, level);
                    person.updateFandomMembershipLevel(fan.getId(), level);
                } else {
                    throw new IllegalArgumentException("Invalid Fandom Membership Level");
                }
            }

            if (env.containsArgument("fandomType")) {
                String type = env.getArgument("fandomType");
                if (type.equals("FAN") || type.equals("COSPLAYER") || type.equals("VENDOR")) {
                    String previousType = person.getFandomMembershipType(fan.getId());
                    fan.updateUserinCategoryTable(userId, previousType, type);
                    person.updateFandomMembershipType(fan.getId(), type);
                } else {
                    throw new IllegalArgumentException("Invalid Fandom Membership Type");
                }
            }

            fandomRepository.save(fan);
            return accountRepository.save(person);
        };
    }

    /**
     * Deletes a user from a fandom, this is analogous to leaving a fandom. If a userId is specified, such user will
     * leave the fandom, else the current user will leave the fandom.
     * <p>
     * Schema: leaveFandom, leaveFandomById
     */
    public DataFetcher deleteFandomMembership() {
        return env -> {
            String fandomId = env.getArgument("fandomId");
            String userId = env.getArgument("userId");

            //Check if fandom and user both exist
            Fandom fan = fandomRepository.findById(fandomId).orElse(null);
            if (fan == null) throw new FandomNotFoundException();

            Account person;
            if (env.containsArgument("userId")) {
                person = accountRepository.findById(userId).orElse(null);
            } else {
                person = currentAccountService.getCurrentAccount();
            }

            if (person == null) {
                throw new AccountNotFoundException();
            }

            String level = person.getFandomMembershipLevel(fandomId);
            String type = person.getFandomMembershipType(fandomId);

            fan.removeUserfromCategoryTable(userId, level, type);
            person.deleteFandomMembership(fandomId);
            fandomRepository.save(fan);
            return accountRepository.save(person);
        };
    }

    public DataFetcher leaveFandomByName() {
        return env -> {
            String fandomName = env.getArgument("fandomName");
            Account account = currentAccountService.getCurrentAccount();
            Fandom fandom = fandomRepository.findFandomByName(fandomName).orElse(null);
            if (account == null || fandom == null) {
                throw new Exception("oh no. no acc or fandom");
            }

            String fandomId = fandom.getId();
            String fandomLevel = account.getFandomMembershipLevel(fandomId);
            String fandomType = account.getFandomMembershipType(fandomId);

            account.deleteFandomMembership(fandomId);
            fandom.removeUserfromCategoryTable(account.getId(), fandomLevel, fandomType);
            accountRepository.save(account);
            fandomRepository.save(fandom);
            return fandom;
        };
    }

    public DataFetcher addPostToFandom(){
        return env->{
            String fandomName = env.getArgument("fandomName");
            Account account = currentAccountService.getCurrentAccount();
            Fandom fandom = fandomRepository.findFandomByName(fandomName).orElse(null);
            if (fandom == null) throw new FandomNotFoundException();
            if (account == null) throw new AccountNotFoundException();
            String msg = env.getArgument("message");
            String tit = env.getArgument("title");
            if (msg == null || tit == null) throw new IllegalArgumentException();
            String fandomId = fandom.getId();
            Post p = new Post(account.getId(), msg, tit, System.currentTimeMillis(), fandomId,
                    account.getFandomMembershipLevel(fandomId), account.getFandomMembershipType(fandomId),
                    account.getUsername());
            postRepository.save(p);
            return fandom;
        };
    }

    public DataFetcher deletePostFromFandom(){
        return env->{
            postRepository.deleteById(env.getArgument("postId"));
            return  "OK";
        };
    }

    public DataFetcher repliesToPost(){
        return env ->{
            String id = env.getArgument("postID");
            if(id == null) throw new Exception();
            Post a = null;
            Post p;
            Iterator i = postRepository.findAll().iterator();
            while(i.hasNext()){
                p = (Post) i.next();
                if(p.getId().equals(id)) a = p;
            }
            if(a == null) throw new Exception();
            return a.getList();
        };
    }

    public DataFetcher postsByFandomName(){
        return env->{
            Fandom f = fandomRepository.findFandomByName(env.getArgument("fandomName")).orElse(null);
            if(f == null) throw new FandomNotFoundException();
            ArrayList<Post> posts = new ArrayList<>();
            String fandomId = f.getId();
            Post p;
            Iterator i = postRepository.findAll().iterator();
            while(i.hasNext()){
                p = (Post) i.next();
                if(p.getFandomId().equals(fandomId)) posts.add(p);
            }
            return posts;
        };
    }

    public DataFetcher replyToPost(){
        return env -> {
            String id = env.getArgument("postID");
            String message = env.getArgument("message");
            if(id == null || message == null) throw new Exception();
            Post a = null;
            Post p;
            Iterator i = postRepository.findAll().iterator();
            while(i.hasNext()){
                p = (Post) i.next();
                if(p.getId().equals(id)) a = p;
            }
            if (a == null) throw  new Exception();
            Account account = currentAccountService.getCurrentAccount();
            if (account == null) throw new AccountNotFoundException();
            Reply response = new Reply(account.getId(),message,System.currentTimeMillis(),a.getFandomId(),account.getFandomMembershipLevel(a.getFandomId()),account.getFandomMembershipType(a.getFandomId()),account.getUsername());
            repliesRepository.insert(response);
            a.addReply(response);
            postRepository.save(a);
            return a;
        };
    }

    public DataFetcher deleteReplytoPost(){
        return env -> {
            String postid = env.getArgument("postID");
            String replyid = env.getArgument("replyID");
            if(postid == null || replyid == null) throw new Exception();
            Post a = null;
            Post p;
            Iterator i = postRepository.findAll().iterator();
            while(i.hasNext()){
                p = (Post) i.next();
                if(p.getId().equals(postid)) a = p;
            }
            a.removeReply(replyid);
            Reply thingy = repliesRepository.findById(replyid).orElse(null);
            repliesRepository.delete(thingy);
            postRepository.save(a);
            return "YES";
        };
    }
}