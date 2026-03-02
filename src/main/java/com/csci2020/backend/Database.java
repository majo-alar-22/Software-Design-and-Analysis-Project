package com.csci2020.backend;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;

/**
 * Data Access Object used to control the database.
 */
public class Database {
    private final Logger logger = Logging.createLogger("Database", Path.of(".","latest.log"));
    private static final String QUERY_ERROR_MESSAGE = "Failed to create query: ",
        TRANSACTION_ERROR_MESSAGE = "Failed to commit transaction: ";
    private static SessionFactory factory = null;
    public static Session getSession(){
        return getFactory().openSession();
    }

    /**
     * Constructs or returns a singleton {@link SessionFactory} used to access the database.
     * This method should likely not be called outside this class, but is available for any custom queries needed.
     * @return {@link SessionFactory} singleton
     */
    public static SessionFactory getFactory(){
        if(factory == null){
            Configuration conf = new Configuration();
            Properties settings = new Properties();
            settings.put("hibernate.connection.driver_class", "org.h2.Driver");
            settings.put("hibernate.connection.url","jdbc:h2:file:./test.h2");
            settings.put("hibernate.connection.username", "sa");
            settings.put("hibernate.connection.password","");
            settings.put("hibernate.show_sql", "true");
            settings.put("hibernate.hbm2ddl.auto","update");
            conf.setProperties(settings);
            conf.addAnnotatedClass(Player.class);
            conf.addAnnotatedClass(Team.class);
            factory = conf.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build());
        }
        return factory;
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
        logger.log(Level.FINER, "Saving player");
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
        logger.log(Level.FINER, "Saving players");
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
        logger.log(Level.FINER, "Saving teams");
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
        logger.log(Level.FINER, "Saving team " + team.getName());
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
        logger.log(Level.FINER, "Retrieving all teams from database");
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
        logger.log(Level.FINER, "Retrieving all players from database");
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
        logger.log(Level.FINER, "Retrieving team with name " + teamName);
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
        logger.log(Level.FINER, "Deleting team" + team.getName());
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
        logger.log(Level.FINER, String.format("Retrieving players with firstName=%s, lastName=%s", firstName, lastName));
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
        logger.log(Level.FINER, String.format("Retrieving players with firstName=%s", firstName));
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
        logger.log(Level.FINER, String.format("Retrieving players with lastName=%s", lastName));
        try(Session session = getFactory().openSession()) {
            return session.createQuery("FROM Player WHERE lastName = :lastName", Player.class)
                    .setParameter("lastName", lastName)
                    .getResultList();
        } catch(Exception e){
            logger.log(Level.SEVERE, QUERY_ERROR_MESSAGE + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
