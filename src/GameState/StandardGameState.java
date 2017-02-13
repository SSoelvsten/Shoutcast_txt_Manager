package GameState;

import GameStateObserver.GameStateObserver;

import java.util.*;

/**
 * A standard implementation of a game state
 */
public class StandardGameState implements ModifiableGameState {

    private Pause pause;

    private int seriesLength;
    private HashSet<GameStateObserver> observers = new HashSet<>();
    private ArrayList<ModifiableTeam> teamsList = new ArrayList<>();
    private HashMap<Integer, Map> mapsMap = new HashMap<>();

    @Override
    public void shiftTeams() {
        //Move the first team to be the last.
        ModifiableTeam teamAtZero = teamsList.get(0);
        teamsList.remove(0);
        teamsList.add(teamAtZero);

        callObserverUpdate();
    }

    @Override
    public Team getWinner() {
        Team highestScoringTeam = null;
        int highestScore = -1;

        for(Team t : teamsList){
            if(t.getPoints() > highestScore){
                highestScoringTeam = t;
                highestScore = t.getPoints();
            } else if(t.getPoints() == highestScore){
                highestScoringTeam = null;
            }
        }

        return highestScoringTeam;
    }

    @Override
    public int getGameNumber() {
        int acc = 0;
        for(Team t : teamsList){
            acc += t.getPoints();
        }

        return acc + 1;
    }

    @Override
    public void setPauseTeam(int teamIndex, String reason) {
        this.pause = new StandardPause(teamsList.get(teamIndex), reason);

        callObserverPauseUpdate();
    }

    @Override
    public void unpause() {
        this.pause = null;

        callObserverPauseUpdate();
    }

    @Override
    public int addTeam(ModifiableTeam team) {
        teamsList.add(team);
        return teamsList.indexOf(team);
    }

    @Override
    public void removeTeam(int teamIndex) {
        teamsList.remove(teamIndex);
    }

    @Override
    public void setTeamIdentity(int teamIndex, String name, String abbreviation) {
        teamsList.get(teamIndex).setName(name);
        teamsList.get(teamIndex).setAbbreviation(abbreviation);

        callObserverNameUpdate();
    }

    @Override
    public void setTeamPoints(int teamIndex, int points) {
        teamsList.get(teamIndex).setPoints(points);

        callObserverScoreUpdate();
    }


    @Override
    public Team getTeam(int teamIndex) {
        return teamsList.get(teamIndex);
    }

    @Override
    public Iterator<Team> getTeamsIterator() {
        //TODO: What to do? Drink hot chocolate and be happy :)
        return null;
    }

    @Override
    public void setMap(int number, Map map) {
        mapsMap.put(number, map);
    }

    @Override
    public Map getMap(int mapIndex) {
        return mapsMap.get(mapIndex);
    }

    @Override
    public Iterator<Map> getMapsIterator() {
        return mapsMap.values().iterator();
    }

    @Override
    public Pause getPause() {
        return this.pause;
    }

    @Override
    public void setSeriesLength(int length) {
        this.seriesLength = length;
    }

    @Override
    public int getSeriesLength() {
        return this.seriesLength;
    }

    @Override
    public void subscribe(GameStateObserver o) {
        this.observers.add(o);
    }

    private void callObserverUpdate(){
        for(GameStateObserver o : observers){
            o.onShiftUpdate(this);
        }
    }

    private void callObserverNameUpdate(){
        for(GameStateObserver o : observers){
            o.onNameUpdate(this);
        }
    }

    private void callObserverScoreUpdate(){
        for(GameStateObserver o : observers){
            o.onScoreUpdate(this);
        }
    }

    private void callObserverPauseUpdate(){
        for(GameStateObserver o : observers){
            o.onPauseUpdate(this);
        }
    }
}
