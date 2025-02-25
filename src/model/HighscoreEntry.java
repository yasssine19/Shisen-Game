package tud.ai1.shisen.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasse die einen einzelnen Highscore-Eintrag repraesentiert.
 *
 * @author Andrej Felde, Daniel Stein, Nicklas Behler
 *
 */
public class HighscoreEntry implements Comparable<HighscoreEntry> {

    /**
     * Datum des gespielten Spiels
     */
    private LocalDateTime date;

    /**
     * Benoetigte Zeit in Sekunden
     */
    private double duration;

    /**
     * Erreichte Punktzahl
     */
    private int score;

    /**
     * Separator zum parsen und schreiben der Highscore-Datei
     */
    private static final String separator = ";";

    /**
     * Formatter um String in LocalDateTime zu formatieren
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Konstruktor des HighscoreEntrys erzeugt neue Instanz eines
     * Highscore-Eintrags.
     *
     * @param date     Datum des gespielten Spiels.
     * @param score    Erspielter Score.
     * @param duration Benoetigte Zeit in Sekunden.
     */
    public HighscoreEntry(LocalDateTime date, int score, double duration) {
        validate(date,score,duration);
        this.date=date;
        this.score=score;
        this.duration=duration;
    }

    /**
     * Konstruktor des HighscoreEntrys erzeugt neue Instanz eines
     * Highscore-Eintrags.
     *
     * @param data Highscore-Eintrag im Format "dd.MM.yyyy HH:mm;score;duration".
     */
    public HighscoreEntry(String data) {
        if ((data==null)||(data.length()==0)) {
            throw new IllegalArgumentException("Highscore-Eintrag darf nicht null oder leer sein !");
        }
        // teilt den String in Teile nach der Position von ";"
        String[] teile = data.split(";");
        if (teile.length!=3) {
            throw new IllegalArgumentException("Highscore hat ein ungütliges Format !");
        }
        String dateString=teile[0].trim();
        String scoreString=teile[1].trim();
        String durationString=teile[2].trim();
        try {
            int score =Integer.parseInt(scoreString);
            double duration=Double.parseDouble(durationString);
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime date= LocalDateTime.parse(dateString, formatter);
            validate(date,score,duration);
            this.date=date;
            this.score=score;
            this.duration=duration;
        } catch (Exception e) {
            throw new IllegalArgumentException("Highscore hat ein ungütliger Format !");
        }
    }

    /**
     * Methode, die die Gueltigkeit der uebergebenen Parameter ueberprueft.
     * 
     * @param date     Datum des gespielten Spiels.
     * @param score    Erspielter Score.
     * @param duration Benoetigte Zeit in Sekunden.
     */
    public void validate(LocalDateTime date, int score, double duration) {
        if (date==null) {
            throw new IllegalArgumentException("Datum darf nicht null sein !");
        } else {
            this.date=date;
        }
        if (duration<0) {
            throw new IllegalArgumentException("Duration darf nicht negativ sein !");
        } else {
            this.duration=duration;
        }
        if ((score<0)||(score>1000)) {
            throw new IllegalArgumentException("Score darf nicht negativ oder größer als 1000 sein !");
        } else {
            this.score=score;
        }
    }

    /**
     * Methode, die den aktuellen Highscore-Entry mit dem uebergebenen Parameter
     * vergleicht.
     * 
     * @param obj neuer Highscore-Eintrag.
     * 
     * @return Ergebnis, ob die beiden Parameter uebereinstimmen.
     */
    @Override
    public boolean equals(Object obj) {
        boolean prüfung=((obj!=null)&&(getClass()!=obj.getClass()));
        if (prüfung) {
            prüfung=obj==this;
        }
        return prüfung;
    }

    /**
     * Getter fuer date als String.
     * 
     * @return String - Spieldatum und Uhrzeit
     */
    public String getDate() {
        return String.format("%02d.%02d. %02d:%02d", date.getDayOfMonth(), date.getMonthValue(), date.getHour(),
                date.getMinute());
    }

    /**
     * Getter fuer die Punktzahl.
     * 
     * @return Erreichte Punktzahl
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter fuer die Dauer.
     * 
     * @return Benoetigte Zeit in Sekunden
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Getter, um das Datum in ein Format zum Speichern zu ueberfuehren.
     * 
     * @return String dd.MM.yyyy hh:mm
     */
    private String dateToSaveFormat() {
        return String.format("%02d.%02d.%02d %02d:%02d", date.getDayOfMonth(), date.getMonthValue(), date.getYear(),
                date.getHour(), date.getMinute());
    }

    /**
     * Methode, die die Werte der Highscore-Eintraege vergleicht.
     * 
     * @param other neuer Highscore-Eintrag.
     * 
     * @return Ergebnis vom Vergleich als 1 oder -1.
     */
    @Override
    public int compareTo(HighscoreEntry other) {
        if (this.score<other.score) {
            return 1;
        } else if (this.score>other.score) {
            return -1;
        } else if (this.duration>other.duration) {
            return 1 ;
        } else if (this.duration<other.duration) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Diese Methode gibt die String-Repraesentation des Objekts zurueck.
     * 
     * @return String-Repraesentation
     */
    @Override
    public String toString() {
        return dateToSaveFormat() + separator + score + separator + duration;
    }
}
