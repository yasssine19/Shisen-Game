package tud.ai1.shisen.view.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.component.RenderComponent;

/**
 * Re-implementation of the ImageRenderComponent with a setter to modify the
 * image and a Bug-fix for the scale.
 * 
 * @author Andrej Felde
 *
 */
public class BIRC extends RenderComponent {

    /**
     * the internal Image used for this component
     */
    private Image image;

    /**
     * Create a new ImageRenderComponent with the given image.
     * 
     * Note that no further actions are needed for this component, as it will be
     * drawn automatically whenever the "update" method is invoked, and will be
     * updated if the underlying entity was moved or rotated.
     * 
     * @param theImage the image to render for this component
     */
    public BIRC(Image theImage) {
        super("BIRC");

        // assign the image
        image = theImage;
    }

    /**
     * Rendering an image is performed based on the underlying entity's current
     * position and scale. The image is adapted according to this regarding its size
     * and location.
     * 
     * @param gc              the GameContainer object that handles the game loop,
     *                        recording of the frame rate, and managing the input
     *                        system
     * @param sb              the StateBasedGame that isolates different stages of
     *                        the game (e.g., menu, ingame, highscores etc.) into
     *                        different states so they can be easily managed and
     *                        maintained.
     * @param graphicsContext the graphics context necessary for painting
     *                        ("rendering") the component on the game container
     *                        display
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sb, Graphics graphicsContext) {

        // retrieve the underlying entity's position
        Vector2f pos = owner.getPosition();

        // retrieve the underlying entity's size
        Vector2f size = owner.getSize();

        // render the image at the target position (including
        // the size and scale information) and at the target scale
        image.draw(pos.x - size.x / 2, pos.y - size.y / 2, size.x, size.y);
    }

    /**
     * Returns the size of the component as a 2D floating point vector
     * 
     * @return the size of the RenderComponent as a 2D floating point vector using
     *         Slick's @{link org.newdawn.slick.geom.Vector2f}
     */
    @Override
    public Vector2f getSize() {
        return new Vector2f(image.getWidth() * owner.getScale(), image.getHeight() * owner.getScale());
    }

    /**
     * Update the image regarding its current scale and rotation angle
     * 
     * @param gc    the {@link org.newdawn.slick.GameContainer} object that handles
     *              the game loop, recording of the frame rate, and managing the
     *              input system
     * @param sb    the {@link org.newdawn.slick.state.StateBasedGame} that isolates
     *              different stages of the game (e.g., menu, ingame, highscores
     *              etc.) into different states so they can be easily managed and
     *              maintained.
     * @param delta the time passed in nanoseconds (ns) since the start of the
     *              event, used to interpolate the current target position
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {

        // retrieve the current scale factor for the entity
        float scale = owner.getScale();

        // adjust the center of rotation to the shape's center
        image.setCenterOfRotation(image.getWidth() / 2.0f * scale, image.getHeight() / 2.0f * scale);

        // rotate the image according to the desired rotation angle
        image.rotate(owner.getRotation() - image.getRotation());
    }

    /**
     * Change the Image
     * 
     * @param img The new image
     */
    public void setImage(Image img) {
        this.image = img;
    }
}
