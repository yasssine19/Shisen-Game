 package tud.ai1.shisen.model;
import tud.ai1.shisen.util.IOOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, welche fuer die Verwaltung der Highscores verantworlicht ist. Laedt,
 * speichert und legt neue Highscore-Eintraege in einer ArrayList an und
 * speichert die ArrayListen zusammen mit dem Key der passenden Feldgroesse in
 * eine Hashmap.
 *
 * @author Andrej Felde, Daniel Stein, Nicklas Behler
 */
public class Highscore {

    /**
     * Maximale Anzahl an Highscore-Eintraegen in einer Highscore-Liste pro
     * Feldgroesse.
     */
    public static final int MAX_ENTRIES = 10;

    /**
     * Alle Highscore-Listen mit zugehoeriger Levelgroesse
     */
    private List<HighscoreEntry> highscores;

    /**
     * Konstruktor der Highscore-Klasse.
     */
    public Highscore() {
        highscores = new ArrayList<HighscoreEntry>();
    }

    /**
     * Methode zum Erzeugen der Highscores aus dem gespeicherten Stringformat. Teilt
     * den uebergebenen String in einzelne Eintraege und speichert diese in einem
     * String-Array. Danach werden die einzelnen Eintraege in der Hashmap, in die
     * passenden Arraylists ihrer Feldgroesse einsortiert.
     *
     * @param str String, der die vorherigen Highscores enthaelt.
     */
    public void initHighscore(String str) {
        if (str.isEmpty())
            return;

        for (String line : str.split("\\r?\\n")) {
            addEntry(new HighscoreEntry(line));
        }
    }

    /**
     * Methode, die alle aktuellen Highscores sammelt und sie in einer Datei
     * schreibt.
     * 
     * @param Name der zu speichernden Datei.
     */
    public void saveToFile(String fileName) {
        //eintraege String der alle Highscores-Eintraege enthalten wird
        String eintraege="";
        if (highscores.get(0)!=null) { 
            eintraege=highscores.get(0).toString();
        }
        int i=1;
        //alle vorhandenen Highscores werden in eintraege hinzugefuegt
        while (i<highscores.size()) {
            eintraege = eintraege+System.lineSeparator()+highscores.get(i);
            i++;
        }
        
        try {IOOperations.writeFile(fileName, eintraege);
        } catch (Exception e) {
            throw new IllegalArgumentException("Schreiben der Datei fehlerhaft !");
        }
    }

    /**
     * Getter Methode fuer Highscores einzelner Level.
     *
     * @return Gibt die Highscore-Liste zurueck
     */
    public List<HighscoreEntry> getHighscore() {
        return highscores;
    }

    /**
     * Methode, die die passende Highscore Liste aktualisiert und sortiert,
     * wenn moeglich.
     * 
     * @param neuer moeglicher Highscore-Eintrag.
     */
    public void addEntry(HighscoreEntry entry) {
        /* addieren des neuen Highscore-Eintrags und loeschen des letzten falls
        Grenzwert erreicht */
        if (highscores.size()<MAX_ENTRIES) {
            highscores.add(entry);
        } else {
            HighscoreEntry lastEntry = highscores.get(highscores.size()-1);   
            if (lastEntry.getScore()<entry.getScore() ||
            lastEntry.getScore()==entry.getScore() && lastEntry.getDuration()>entry.getDuration()) {
                highscores.remove(highscores.size()-1);
                highscores.add(entry);
            }
        }
        /* sortiert die Highscore-Eintraege nach dem Score zu erst, und erst
        dann nach der Dauer, falls Score gleich ist */
        for (int i=0; i<highscores.size()-1; i++) {
            for (int j=i+1; j<highscores.size(); j++) {
                if (highscores.get(i).getScore()<highscores.get(j).getScore()) {
                    HighscoreEntry ent = highscores.get(i);
                    highscores.set(i,highscores.get(j));
                    highscores.set(j,ent);
                } else if ((highscores.get(i).getScore()==highscores.get(j).getScore())&&
                (highscores.get(i).getDuration()>highscores.get(j).getDuration())) {
                    HighscoreEntry ent = highscores.get(i);
                    highscores.set(i,highscores.get(j));
                    highscores.set(j,ent);
                }
            }
        }
    }
} 
