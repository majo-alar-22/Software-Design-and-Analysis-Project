package com.csci2020.backend;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;
import java.util.regex.Pattern;

/**
 * Data Access Object used to control the database.
 */
public class Database {
    private Account currentUser;
    private final Logger logger = Logging.createLogger("Database", Path.of(".","latest.log"));
    private static final String QUERY_ERROR_MESSAGE = "Failed to create query: ",
        TRANSACTION_ERROR_MESSAGE = "Failed to commit transaction: ";
    private static SessionFactory factory = null;
    private final Path filepath;
    /**
     * Constructs or returns a singleton {@link SessionFactory} used to access the database.
     * This method should likely not be called outside this class, but is available for any custom queries needed.
     * @return {@link SessionFactory} singleton
     */
    public SessionFactory getFactory(){
        if(factory == null){
            Configuration conf = new Configuration();
            Properties settings = new Properties();
            settings.put("hibernate.connection.driver_class", "org.h2.Driver");
            settings.put("hibernate.connection.url","jdbc:h2:file:" + filepath.toString());
            settings.put("hibernate.connection.username", "sa");
            settings.put("hibernate.connection.password","");
            settings.put("hibernate.show_sql", "false");
            settings.put("hibernate.hbm2ddl.auto","update");
            conf.setProperties(settings);
            conf.addAnnotatedClass(Player.class);
            conf.addAnnotatedClass(Team.class);
            conf.addAnnotatedClass(Account.class);
            factory = conf.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build());
        }
        return factory;
    }

    public Database(Path filepath){
        this.filepath = filepath;
    }

    /**
     * Save a single player to the database. To save multiple players, prefer
     * {@link Database#savePlayers(List)} to avoid multiple requests.
     * @see Database#savePlayers(List)
     * @param player Player to save
     */
    public void savePlayer(Player player){
        if(player == null){
            throw new IllegalArgumentException("Player may not be null");
        }
        logger.log(Level.FINE, "Saving player");
        Transaction transaction = null;
        try(Session session = getFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(player);
            transaction.commit();
        } catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    /**
     * Save a list of players to the database. In the case of an error, transactions will not be committed.
     * @see Database#savePlayer(Player)
     * @param players List of players to save
     */
    public void savePlayers(List<Player> players) {
        if(players == null) {
            throw new IllegalArgumentException("Players may not be null");
        }
        if(players.isEmpty()){
            logger.log(Level.FINE, "Attempted to save an empty player list");
            return;
        }
        logger.log(Level.FINE, "Saving players");
        Transaction transaction = null;
        try (Session session = getFactory().openSession()) {
            transaction = session.beginTransaction();
            for(Player player : players) {
                Team team = player.getTeam();
                if(team != null){
                    if(team.getName() == null){
                        session.persist(team);
                    } else {
                        team = session.find(Team.class, team.getName());
                        player.setTeam(team);
                    }
                }
                session.merge(player);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    /**
     * Saves a list of teams to the database. In the case of an error, transactions will not be committed.
     * @see Database#saveTeam(Team)
     * @param teams List of teams to save
     */
    public void saveTeams(List<Team> teams) {
        if(teams == null){
            throw new IllegalArgumentException("Teams may not be null!");
        }
        if(teams.isEmpty()) {
            logger.log(Level.FINE, "Attempted to save an empty team list");
            return;
        }
        logger.log(Level.FINE, "Saving teams");
        Transaction transaction = null;
        try (Session session = getFactory().openSession()) {
            transaction = session.beginTransaction();
            for (Team team : teams) {
                if(team == null){
                    throw new IllegalArgumentException("Teams may not contain null elements");
                }
                session.merge(team);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    /**
     * Saves a single team to the database. If the team does not exist in the database, it will be added to it.
     * Otherwise, it will modify any existing team with the same name.<br>
     * <b>Note:</b> This will save all players associated with the team as a side-effect.<br>
     * To save multiple teams, prefer {@link Database#saveTeams(List)} to avoid multiple requests.<br>
     * In the case of an error, transactions will not be committed.
     * @see Database#saveTeams(List)
     * @param team The team to save to the database.
     */
    public void saveTeam(Team team){
        if(team == null){
            throw new IllegalArgumentException("Argument may not be null");
        }
        logger.log(Level.FINE, "Saving team " + team.getName());
        Transaction transaction = null;
        try(Session session = getFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(team);
            transaction.commit();
        } catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    /**
     * Returns a list of all teams stored within the database. In case of an error, returns an empty list.
     * @return List of all teams stored in the database
     */
    public List<Team> getAllTeams(){
        logger.log(Level.FINE, "Retrieving all teams from database");
        try(Session session = getFactory().openSession()){
            return session.createQuery("FROM Team", Team.class).getResultList();
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Returns a list of all players in the database. In case of an error, returns an empty list.
     * @return List of all players in the database.
     */
    public List<Player> getAllPlayers(){
        logger.log(Level.FINE, "Retrieving all players from database");
        try(Session session = getFactory().openSession()){
            return session.createQuery("FROM Player", Player.class).getResultList();
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Returns a single team from the database, identified by their unique name.
     * @param teamName Name to search for
     * @return A team matching the identifier, or <code>null</code> if not found.
     */
    public Team getTeamByName(String teamName){
        logger.log(Level.FINE, "Retrieving team with name " + teamName);
        try(Session session = getFactory().openSession()) {
            return session.find(Team.class, teamName);
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Deletes a team from the database
     * @param team Team to delete
     */
    public void deleteTeam(Team team){
        if(team == null){
            throw new IllegalArgumentException("Team may not be null");
        }
        logger.log(Level.FINE, "Deleting team" + team.getName());
        Transaction transaction = null;
        try(Session session = getFactory().openSession()){
            transaction = session.beginTransaction();
            session.createMutationQuery("UPDATE Player p SET p.team = null WHERE p.team.name = :name")
                    .setParameter("name", team.getName())
                    .executeUpdate();
            session.createMutationQuery("DELETE FROM Team WHERE name = :name")
                    .setParameter("name", team.getName())
                    .executeUpdate();
            transaction.commit();
        } catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    /**
     * Gets a list of all players with the given first and last name
     * @param firstName Player's first name
     * @param lastName Player's last name
     * @return List of all players with the given name
     */
    public List<Player> getPlayersByName(String firstName, String lastName){
        logger.log(Level.FINE, String.format("Retrieving players with firstName=%s, lastName=%s", firstName, lastName));
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Player WHERE firstName = :firstName AND lastName = :lastName", Player.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .getResultList();
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of all players with the given first name
     * @param firstName The player's first name
     * @return List of all players with the given first name
     */
    public List<Player> getPlayersByFirstName(String firstName){
        logger.log(Level.FINE, String.format("Retrieving players with firstName=%s", firstName));
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Player WHERE firstName = :firstName", Player.class)
                    .setParameter("firstName", firstName)
                    .getResultList();
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of all players with the given last name
     * @param lastName The player's last name
     * @return List of all players with the given last name
     */
    public List<Player> getPlayersByLastName(String lastName){
        logger.log(Level.FINE, String.format("Retrieving players with lastName=%s", lastName));
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Player WHERE lastName = :lastName", Player.class)
                    .setParameter("lastName", lastName)
                    .getResultList();
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    static Logger accountLogger = Logging.createLogger("account", Path.of(".","account.log"));
    public long getUniqueIdentifierFromUsername(String username){
//        return (long) (Math.random()*10+1);
        try(Session session = getFactory().openSession()) {
            List<String> usernames = session.createQuery("SELECT user.username FROM Account user WHERE user.username = :username OR user.username LIKE CONCAT(:username,'%')", String.class)
                    .setParameter("username",username)
                    .getResultList().stream()
                    .filter(s -> s.matches(Pattern.quote(username) + "\\d+")).toList();
            accountLogger.log(Level.INFO, username);
            for(String name : usernames){
//                System.out.printf("%s: %s%n", username, name);
                accountLogger.log(Level.INFO, "\t" + name);
            }
                    return usernames.stream().map(s -> {
                        System.out.println(s +": " + s.substring(username.length()));
                        return s.substring(username.length());
                    })
                    .mapToLong(Long::parseLong)
                    .max().orElse(0) + 1;

//            return (long) (Math.random()*10+1);
        } catch(Exception e){
//            e.printStackTrace();
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void saveAccount(Account account){
        if(account == null){
            throw new IllegalArgumentException("Account may not be null!");
        }
        logger.log(Level.FINE, "Saving account");
        Transaction transaction = null;
        try (Session session = getFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(account);
            transaction.commit();
        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    /**
     * Saves a list of teams to the database. In the case of an error, transactions will not be committed.
     * @see Database#saveTeam(Team)
     * @param teams List of teams to save
     */
    public void saveAccounts(List<Account> accounts) {
        if(accounts == null){
            throw new IllegalArgumentException("Accounts may not be null!");
        }
        if(accounts.isEmpty()) {
            logger.log(Level.FINE, "Attempted to save an empty account list");
            return;
        }
        logger.log(Level.FINE, "Saving accounts");
        Transaction transaction = null;
        try (Session session = getFactory().openSession()) {
            transaction = session.beginTransaction();
            for (Account account : accounts) {
                if(account == null){
                    throw new IllegalArgumentException("Accounts may not contain null elements");
                }
                session.merge(account);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.log(Level.SEVERE, TRANSACTION_ERROR_MESSAGE + e.getMessage(), e);
        }
    }

    public Account getAccount(String username) throws NoResultException{
        logger.log(Level.FINE, String.format("Retrieving users with username=%s", username));
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Account WHERE username = :username", Account.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch(NoResultException e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            throw e;
        }
    }
    public boolean isUsernameTaken(String username){
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Account WHERE username = :username", Account.class)
                    .setParameter("username", username).getResultCount() != 0;
        } catch(NoResultException e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            throw e;
        }
    }
    public AuthenticationResult createNewAccount(String username, String firstName, String lastName, char[] password){
        if(isUsernameTaken(username)){
            return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.INVALID_CREDENTIALS, "Username taken");
        }
        logger.log(Level.FINE, String.format("Trying to create account of username %s", username));
        Player player = new Player(firstName, lastName);
        byte[] salt = Authentication.generateSalt();
        Account acc = new Account(username, player, salt, Authentication.hashPassword(password, salt), false);
        this.saveAccount(acc);
        return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS, "Created account");
    }

    /**
     * Get an account with the given username, and try to login using the given password.
     * If the account does not exist or the password is incorrect, throws an error.
     * @param username
     * @param password
     * @return
     */
    public AuthenticationResult login(String username, char[] password){
        //TODO remove all mentions of password in output, this is just for debugging
        logger.log(Level.FINE, String.format("Retrieving account with username=%s, password=%s", username, "*".repeat(password.length)));

        try(Session session = getFactory().openSession()) {
            Account acc = session.createQuery("FROM Account WHERE username = :username", Account.class)
                    .setParameter("username", username)
                    .getSingleResultOrNull();
            if(acc == null){
                // Account with the given username doesn't exist
                logger.log(Level.INFO, "Failed to find account with provided credentials");
                Arrays.fill(password, '0');
                return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.INVALID_CREDENTIALS, "Invalid username or password");
            }
            byte[] challenge = Authentication.generateChallenge();
            byte[] userHash = Authentication.hashPassword(password, acc.getSalt());
            byte[] userHmac = Authentication.HMAC(userHash, challenge);
            byte[] serverHmac = Authentication.HMAC(acc.getPasswordHash(), challenge);
            if(Authentication.challengeResult(userHmac, serverHmac)){
                // Successful login
                currentUser = acc;
                logger.log(Level.INFO, "Successfully logged in");
                return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS, "Successfully logged in");
            } else {
                // Invalid password
                logger.log(Level.INFO, "Failed to find account with provided credentials");
                return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.INVALID_CREDENTIALS, "Invalid username or password");
            }
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.ERROR, e.getMessage());
        }
    }

    public void logout(){
        currentUser = null;
    }

    public boolean isNewDatabase(){
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Account", Account.class)
                    .getResultCount() == 0;
        } catch(NoResultException e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return false;
        }
    }

    public AuthenticationResult createAdminAccount(String firstName, String lastName, char[] password){
        if(currentUser != null && currentUser.isAdmin() || isNewDatabase()){
            String targetName = firstName + "." + lastName;
            long identifier = this.getUniqueIdentifierFromUsername(targetName);
            String username = targetName + (identifier == 1 ? "" : identifier);
            logger.log(Level.FINE, String.format("Trying to create admin account of username %s", username));
            Player player = new Player(firstName, lastName);
            byte[] salt = Authentication.generateSalt();
            Account acc = new Account(username, player, salt, Authentication.hashPassword(password, salt), true);
            this.saveAccount(acc);
            return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS, "Created admin account \"" + username + "\"");
        }
        return new AuthenticationResult(AuthenticationResult.AUTHENTICATION_STATUS.INSUFFICIENT_PERMISSIONS, "Insufficient permissions");
    }

    public Account getCurrentUser(){
        return this.currentUser;
    }

    public Player getPlayerByUsername(String username) {
        //TODO remove all mentions of password in output, this is just for debugging
        logger.log(Level.FINE, String.format("Retrieving account with username=%s", username));

        try(Session session = getFactory().openSession()) {
            Account acc = session.createQuery("FROM Account WHERE username = :username", Account.class)
                    .setParameter("username", username)
                    .getSingleResultOrNull();
            if(acc != null){
                return acc.getPlayer();
            }
            logger.log(Level.FINE, "Failed to find account with given username");
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
        }
        return null;
    }
}
