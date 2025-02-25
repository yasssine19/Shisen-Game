package tud.ai1.shisen.model;
import tud.ai1.shisen.util.TokenDisplayValueProvider;
import org.newdawn.slick.geom.Vector2f;

/**
 * Klasse die das Interface IToken implementiert und die verschiedenen
 * Merkmale eines einzelnen Felds auf dem Spielfeld festlegt.
 * 
 * @author Yassine Frih
 * @version 15.06.2023
 */
public class Token implements IToken
{
    private static int counter=0;
    private final int id;
    private TokenState state;
    private final int value;
    private Vector2f pos;
    
    /**
     * Konstruktor, der die Merkmale eines Tokens initialisiert.
     * 
     * @param value Wert des Tokens.
     * @param state Zustand des Tokens.
     * @param pos   Position des Tokens.
     */
    public Token(int value, TokenState state, Vector2f pos)
    {
        this.value=value;
        this.state=state;
        this.pos=pos;
        this.id=counter;
        counter++;
    }
    
    /**
     * Konstruktor, der die Merkmale eines Tokens initialisiert aber nur den
     * Wert entgegennimmt.
     * 
     * @param value Wert des Tokens.
     */
    public Token(int value) {
        this.value=value;
        this.state=TokenState.DEFAULT;
        this.pos= new Vector2f(0,0);
        this.id=counter;
        counter++;
    }
    
    /**
     * Methode, die den Wert eines Tokens zurueckgibt.
     * 
     * @return Wert des Tokens.
     */
    @Override
    public int getValue() {
        return value;
    }
    
    /**
     * Methode, die den Zustand eines Tokens zurueckgibt.
     * 
     * @return Zustand des Tokens.
     */
    @Override
    public TokenState getTokenState() {
        return state;
    }
    
    /**
     * Methode, die den eindeutigen Identifizierer eines Tokens zurueckgibt.
     * 
     * @return eindeutiger Identifizierer des Tokens.
     */
    @Override
    public int getID() {
        return id;
    }
    
    /**
     * Methode, die die Position eines Tokens zurueckgibt.
     * 
     * @return Position des Tokens.
     */
    @Override
    public Vector2f getPos() {
        return pos;
    }
    
    /**
     * Methode, die den Zustand eines Tokens auf einen gegebenen Wert setzt.
     * 
     * @param neuer Zustand des Tokens.
     */
    @Override
    public void setTokenState(TokenState newState) {
        this.state=newState;
    }
    
    /**
     * Methode, die die Position eines Tokens auf eine gegebene Position setzt.
     * 
     * @param neue Position des Tokens.
     */
    @Override
    public void setPos(Vector2f newPos) {
        this.pos=newPos;
    }
    
    /**
     * Methode, die den Anzeigewert eines Tokens zurueckgibt.
     * 
     * @return Anzeigewert des Tokens.
     */
    @Override
    public String getDisplayValue() {
        return TokenDisplayValueProvider.getInstance().getDisplayValue(value);
    }
}
