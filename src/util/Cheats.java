package tud.ai1.shisen.util;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import tud.ai1.shisen.model.Grid;
import tud.ai1.shisen.model.IToken;
import tud.ai1.shisen.model.TokenState;

/**
 * 
 * Diese Klasse repraesentiert die Cheats.
 * 
 * @author Niklas Vogel
 *
 */
public class Cheats {

    /**
     * Diese Klasse soll nicht initialisierbar sein, da sie nur statische Methoden
     * enthaelt.
     */
    private Cheats() {
    }

    /**
     * Methode, die für eine angeklickte Karte einen loesbaren Partner sucht.
     * 
     * @param grid Grid, auf dem ein loesbarer Token markiert werden soll.
     */
    public static void findPartner(final Grid grid) {
    if (!isCheatPossible(grid,Consts.CHEAT_FIND_PARTNER)) {
        System.out.println("Cheat nicht möglich !");
    } else {
        IToken[] activeTokens = grid.getActiveTokens();
        //prueft ob genau 1 Token selektiert ist
        if ((activeTokens[0]==null && activeTokens[1]==null )||( activeTokens[0]!=null && activeTokens[1]!=null )) {
            System.out.println("Genau 1 Token muss ausgewählt sein !");
            grid.updateScore(Consts.CHEAT_COST_FIND_PARTNER);
            return; 
        }
        List<IToken> moeglichePartner = new ArrayList<>();
        moeglichePartner=findTokensWithType(activeTokens[0],grid);
        //erstelle eine Copy der Liste moeglichePartner
        List<IToken> moegPartnerCopy = new ArrayList<>(moeglichePartner);
        //entferne von der Copy der Liste die nicht loesbaren Tokens
        int i=0;
        while (i<moegPartnerCopy.size()) {
            if (!solvable(activeTokens[0],moegPartnerCopy.get(i),grid)) {
                moegPartnerCopy.remove(moegPartnerCopy.get(i));
            } else {
                i++;
            }
        }
        //waehle irgendeinen Token mit dem gleichen Typ, falls keiner lösbar
        //ist
        if (moegPartnerCopy.size()==0) {
            grid.selectToken(moeglichePartner.get(0));
            grid.updateScore(Consts.CHEAT_COST_FIND_PARTNER);
            return;
        }
        //waehle einen loesbaren Token mit dem gleichen Typ
        grid.selectToken(moegPartnerCopy.get(0));
        System.out.println("Cheat : findPartner");
        grid.updateScore(Consts.CHEAT_COST_FIND_PARTNER+Consts.DECREASE_SCORE);
    }
    }

    /**
     * Dieser Cheat markiert einen Token, der derzeit loesbar ist.
     *
     * @param grid Grid, auf dem ein loesbarer Token markiert werden soll.
     */
    public static void useHint(final Grid grid) {
        // Wenn bereits zwei Tokens angeklickt sind, breche Cheat ab
        if (grid.bothClicked())
            return;
        if (!isCheatPossible(grid, Consts.CHEAT_HINT))
            return;
        final IToken[] tok = findValidTokens(grid);
        if (tok == null) {
            System.out.println("Cheat nicht mehr moeglich");
            return;
        }
        grid.deselectTokens();
        grid.selectToken(tok[0]);
        System.out.println("Cheat: useHint");
        grid.updateScore(Consts.CHEAT_COST_HINT+Consts.DECREASE_SCORE);
    }

    /**
     * Methode, die ein derzeit loesbares Paar im uebergebenen Grid findet
     * und loest.
     * 
     * @param grid Grid, auf dem ein loesbarer Token markiert werden soll.
     */
    public static void solvePair(final Grid grid) {
        if (isCheatPossible(grid,Consts.CHEAT_SOLVE_PAIR)) {
            IToken[] validTokens = findValidTokens(grid);
            grid.deselectTokens();
            if (validTokens==null) {
                System.out.println("Kein lösbares Paar gefunden !");
                grid.updateScore(Consts.CHEAT_COST_SOLVE_PAIR);
                return;
            }
            grid.selectToken(validTokens[0]);
            grid.selectToken(validTokens[1]);
            System.out.println("Cheat : solvePair");
            grid.updateScore(Consts.CHEAT_COST_SOLVE_PAIR);
        } else {
            System.out.println("Cheat nicht möglich !");
        }
    }

    /**
     * Findet ein Paar aus derzeit loesbaren Tokens.
     * 
     * @param grid Grid, auf dem ein loesbarer Token markiert werden soll.
     * @return Loesbares Tokenpaar, null falls kein loesbares Paar mehr vorhanden
     *         ist
     */
    private static IToken[] findValidTokens(final Grid grid) {
        final IToken[][] board = grid.getGrid();
        IToken token = null;
        Random r = new Random();
        // Offset fuer die Startposition der Suche im Grid-Array
        int n1 = r.nextInt(board.length);
        int n2 = r.nextInt(board[0].length);
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                token = board[(x + n1) % board.length][(y + n2) % board[0].length];
                if (token.getTokenState() != TokenState.DEFAULT)
                    continue;
                // Teste alle theoretich moeglichen Partner
                for (IToken partner : findTokensWithType(token, grid)) {
                    if (solvable(token, partner, grid)) {
                        IToken[] ret = { token, partner };
                        return ret;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Bestimmt ob zwei Tokens zueinander passen (loesbar sind).
     * 
     * @param token1 Token 1
     * @param token2 Token 2
     * @param grid   Spielfeld der Tokens
     * @return Boolean: Passen Tokens zusammen?
     */
    private static boolean solvable(IToken token1, IToken token2, Grid grid) {
        if (token1.getTokenState() == TokenState.SOLVED || token2.getTokenState() == TokenState.SOLVED)
            return false;
        // Set TokenStates to Clicked so Search algorithm can use them
        TokenState pre1 = token1.getTokenState();
        TokenState pre2 = token2.getTokenState();
        token1.setTokenState(TokenState.CLICKED);
        token2.setTokenState(TokenState.CLICKED);
        List<IToken> list = PathFinder.getInstance().findPath(grid, (int) token1.getPos().x, (int) token1.getPos().y,
                (int) token2.getPos().x, (int) token2.getPos().y);
        // After algorithm has finished, reset TokenStates
        token1.setTokenState(pre1);
        token2.setTokenState(pre2);
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    /**
     * Methode, die die Tokens mit dem gleichen Typ im Grid sucht.
     * 
     * @param token Token-Modell.
     * @param grid  Grid, auf dem ein loesbarer Token markiert werden soll.
     * @return Liste mit den gefundenen Tokens.
     */
    private static List<IToken> findTokensWithType(final IToken token, final Grid grid) {
        List<IToken> tokens = new ArrayList<>();
        for (int i=1; i<19; i++) {
            for (int j=1; j<9; j++) {
                IToken aktuellerToken = grid.getTokenAt(i,j);
                if (aktuellerToken.getDisplayValue()==token.getDisplayValue()
                    && aktuellerToken != token 
                    && aktuellerToken.getTokenState()!=TokenState.SOLVED) {
                    tokens.add(aktuellerToken);
                }
            }
        }
        return shuffle(tokens);
    }

    /**
     * Methode, die eine uebergebene Liste zufaellig durchmischt und
     * zurueckgibt.
     * 
     * @param list die zu durchmischende Liste.
     * @return die durchmischte Liste.
     */
    private static List<IToken> shuffle(List<IToken> list) {
    List<IToken> shuffledList = new ArrayList<>(list);
    Random random = new Random();
    //vertauscht die Ordnung der Tokens i mal, je nach der Listengroesse
    for (int i = shuffledList.size() - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        IToken moveToken = shuffledList.get(i);
        shuffledList.set(i, shuffledList.get(j));
        shuffledList.set(j, moveToken);
    }
    return shuffledList;
    }

    /**
     * Methode, die ueberprueft, ob der uebergebene Cheat derzeit im
     * uebergebenen Grid moeglich ist.
     * 
     * @param grid    Grid, auf dem ein loesbarer Token markiert werden soll.
     * @param cheatID eindeutiger Identifizierer eines Cheats.
     * @return Ergebnis der Pruefung als True oder Flase.
     */
    private static boolean isCheatPossible(final Grid grid, final int cheatID) {
        int score = grid.getScore();
        int cheatCost;
        if (cheatID==Consts.CHEAT_SOLVE_PAIR) {
            cheatCost = Consts.CHEAT_COST_SOLVE_PAIR;
        } else if (cheatID==Consts.CHEAT_HINT) {
            cheatCost = Consts.CHEAT_COST_HINT;
        } else if (cheatID==Consts.CHEAT_FIND_PARTNER) {
            cheatCost = Consts.CHEAT_COST_FIND_PARTNER ;
        } else {
            throw new IllegalArgumentException("CheatID hat einen ungültigen Wert !");
        }
        return score+cheatCost>=0;
    }

    /**
     * Liefert die Cheatkosten fuer den uebergebenen Cheat zurueck.
     *
     * @param cheatID ID des Cheats
     * @return Cheatkosten
     */
    private static int getCheatCost(final int cheatID) {
        switch (cheatID) {
        case Consts.CHEAT_HINT:
            return Consts.CHEAT_COST_HINT;
        case Consts.CHEAT_SOLVE_PAIR:
            return Consts.CHEAT_COST_SOLVE_PAIR;
        case Consts.CHEAT_FIND_PARTNER:
            return Consts.CHEAT_COST_FIND_PARTNER;
        default:
            System.out.println("Kein Cheat mit dieser ID vorhanden");
            return -1;
        }
    }
}
