/*
Use this class to store methods used for checking collisions.
I currently have only one method used for checking collisions of Rectangles.
*/

package main;

public class CollisionChecker {
    public static boolean checkCollision (Rectangle a, Rectangle b) {
        return a.intersects(b);
    }
}
