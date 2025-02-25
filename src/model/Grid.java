package tud.ai1.shisen.model;

import java.util.List;

import org.newdawn.slick.geom.Vector2f;
import tud.ai1.shisen.util.IOOperations;
import tud.ai1.shisen.util.Consts;
import tud.ai1.shisen.util.PathFinder;

/**
 * Diese Klasse repraesentiert das Spielfeld.
 * 
 * @author Nicklas Behler, Sebastian C, Lennart Fedler, Niklas Grimm, Robert
 *         Jakobi, Max Kratz, Niklas Vogel, Yassine Frih
 *
 * @datum 15.06.2023
 */
public class Grid implements IGrid {

    private int waitTime = 1000;
    private TokenState destiny;
    private long currTime;
    private boolean timerActive = false;
    private List<IToken> list;
    private static int score = 0;
    private static IToken[][] grid;
    private IToken selectedTokenOne=null;
    private IToken selectedTokenTwo=null;

    /**
     * Konstruktor, der ein zufaelliges Grid zum Testen erzeugt.
     */
    public Grid() {
        final IToken[][] demoGrid = new IToken[10][10];
        for (int x = 0; x < demoGrid.length; x++) {
            for (int y = 0; y < demoGrid[x].length; y++) {
                demoGrid[x][y] = new Token(1);
            }
        }
        grid = demoGrid;
    }
    
    /**
     * Konstruktor, der einen Dateipfad zu einer Map-Datei bekommt und das
     * Grid erstellt.
     * 
     * @param path Dateipfad als String.
     */
    public Grid(String path) {
        grid=parseMap(path);
        fillTokenPositions();
        score=this.score;
    }
     
    /**
     * Methode, die den Token an der uebergebenen Koordinate im Grid zurueckgibt.
     * 
     * @param x horizontale Koordinate.
     * @param y vertikale Koordinate.
     * 
     * @return gesuchter Token an der übergebenen Koordinate im Grid.
     */
    public IToken getTokenAt(int x, int y) {
        if ((x<0)||(x>19)||(y<0)||(y>9)) {
            return null;
        } else {
            return grid[x][y];
        }
    }
    
    /**
     * Methode, die das Grid zurueckgibt.
     * 
     * @return das Spielfeld als Grid.
     */
    public IToken[][] getGrid() {
        return grid;
    }
    
    /**
     * Methode, die die aktiven Tokens zurueckgibt.
     * 
     * @return aktive Tokens als Tabelle.
     */
    public IToken[] getActiveTokens() {
        return new IToken[]{selectedTokenOne,selectedTokenTwo};
    }
    
    /**
     * Methode, die prueft, ob zwei Tokens ausgewaehlt wurden.
     * 
     * @return Ergebnis der Pruefung als True oder False.
     */
    public boolean bothClicked() {
        return selectedTokenOne!=null && selectedTokenTwo!=null;
    }
    
    /**
     * Methode, die die Sleketirung eines schon selektierten Token aufhebt und
     * seinen Status auf DEFAULT zuruecksetzt.
     * 
     * @param token der abzuwaehlende Token.
     */
    public void deselectToken(IToken token) {
        if (selectedTokenOne==token) {
            selectedTokenOne.setTokenState(TokenState.DEFAULT);
            selectedTokenOne=null;
        } else if (token==selectedTokenTwo) {
            selectedTokenTwo.setTokenState(TokenState.DEFAULT);
            selectedTokenTwo=null;
        } else {
            System.out.println("Der gegebene Token ist nicht selektiert !");
        }
    }
    
    /**
     * Methode, die die Sleketirung der beiden Tokens aufhebt und ihren Status
     * auf DEFAULT zuruecksetzt.
     */
    public void deselectTokens() {
        selectedTokenOne=null;
        selectedTokenTwo=null;
    }
    
    /**
     * Methode, die prueft, ob alle Felder auf dem Spielfeld geloest sind.
     * 
     * @return Ergebnis der Pruefung als True oder False.
     */
    public static boolean isSolved() {
        int x=1;
        int y=1;
        boolean s=true;
        /* das Grid wird durchlaufen, Schleife wird verlassen, direkt beim
        Finden eines SOLVED Tokens */
        while ( x<19 && s ) {
            while ( y<9 && s) {
                s=grid[x][y].getTokenState()==TokenState.SOLVED;
                y++;
            }
            y=1;
            x++;
        }
        return s;
    }
    
    /**
     * Methode, die den Pfad zu einer Map-Datei erhaelt und daraus das
     * Spielfeld konstruiert.
     * 
     * @param path Dateipfad als String.
     * 
     * @return das Spielfeld als 2-dimensionales Array.
     */
    private Token[][] parseMap(String path) {
        String map = IOOperations.readFile(path);
        String[] zeile = map.split(System.lineSeparator());
        Token[][] tokens = new Token[20][10];
        // Raender werden mit -1 und SOLVED state intialisiert
        for (int i = 0; i<20; i++) {
            tokens[i][0] = new Token(-1,TokenState.SOLVED,new Vector2f(i,0));
            tokens[i][9] = new Token(-1,TokenState.SOLVED,new Vector2f(i,9));
        }
        for (int j=1; j<9; j++) {
            tokens[0][j] = new Token(-1,TokenState.SOLVED,new Vector2f(0,j));
            tokens[19][j] = new Token(-1,TokenState.SOLVED,new Vector2f(19,j));
        }
        // die Innere 18*8 Matrix wird durch die Werte der Map initialisiert
        for (int j=0; j<8; j++) {
            String[] zahlen = zeile[j].split(",");
            for (int i = 1; i<19; i++) {
                tokens[i][j+1] = new Token(Integer.parseInt(zahlen[i-1]));
            }
        }
        return tokens;
    }
    
    /**
     * Updated den Score um incr. Sollte der Score anschliessend negativ sein, so
     * wird er bis auf 0 dekrementiert.
     * 
     * @param incr Zahl um die Score erhoeht / erniedrigt werden soll.
     */
    public void updateScore(final int incr) {
        if (score + incr >= 0) {
            score += incr;
        } else {
            score = 0;
        }
    }

    /**
     * Getter fuer score.
     *
     * @return Aktueller score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Teile jedem Token seine Position im Array mit.
     */
    private void fillTokenPositions() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].setPos(new Vector2f(x, y));
            }
        }
    }

    /**
     * Waehle einen Token auf dem Spielfeld aus und loese diesen falls moeglich.
     * 
     * @param Token Angeklickter Token.
     */
    @Override
    public void selectToken(final IToken token) {
        if (this.selectedTokenOne == null) {
            this.selectedTokenOne = token;
            selectedTokenOne.setTokenState(TokenState.CLICKED);
        } else if (this.selectedTokenTwo == null) {
            this.selectedTokenTwo = token;
            selectedTokenTwo.setTokenState(TokenState.CLICKED);
            this.list = PathFinder.getInstance().findPath(this, (int) this.selectedTokenOne.getPos().x,
                    (int) this.selectedTokenOne.getPos().y, (int) this.selectedTokenTwo.getPos().x,
                    (int) this.selectedTokenTwo.getPos().y);
            if (this.list == null || this.list.size() == 0
                    || !this.selectedTokenOne.getDisplayValue().equals(this.selectedTokenTwo.getDisplayValue())) {
                this.selectedTokenOne.setTokenState(TokenState.WRONG);
                this.selectedTokenTwo.setTokenState(TokenState.WRONG);
                this.updateScore(Consts.DECREASE_SCORE);
                this.startTimer(Consts.DISPLAY_WRONG_TIME, TokenState.DEFAULT);
            } else {
                for (final IToken tok : this.list) {
                    tok.setTokenState(TokenState.CLICKED);
                }
                this.updateScore(Consts.GAIN_SCORE);
                this.startTimer(Consts.DISPLAY_WRONG_TIME, TokenState.SOLVED);
            }
        }
    }
    
    /**
     * Startet einen Timer (Genutzt fuer Anzeigedauer bei falscher / richtiger
     * Auswahl von zwei Tokens).
     * 
     * @param waitTime Zeit in Sekunden, die gewartet werden soll.
     * @param dest     Ziel Tokenstate.
     */
    private void startTimer(final double waitTime, final TokenState dest) {
        this.timerActive = true;
        this.currTime = System.currentTimeMillis();
        this.waitTime = (int) waitTime * 1000;
        this.destiny = dest;
    }

    /**
     * Prueft ob Anzeigezeit bei falscher/richtiger Auswahl bereits ueberschritten
     * ist. Falls ja wird der entsprechende Code ausgefuehrt.
     */
    @Override
    public void getTimeOver() {
        if (this.timerActive) {
            if (System.currentTimeMillis() - this.currTime > this.waitTime) {
                try {
                    if (this.list != null) {
                        for (final IToken tok : this.list) {
                            tok.setTokenState(TokenState.SOLVED);
                        }
                    }
                    this.selectedTokenOne.setTokenState(this.destiny);
                    this.selectedTokenTwo.setTokenState(this.destiny);
                    this.selectedTokenOne = null;
                    this.selectedTokenTwo = null;
                    this.timerActive = false;
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
