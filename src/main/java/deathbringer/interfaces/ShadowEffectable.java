package deathbringer.interfaces;

public interface ShadowEffectable {
    void triggerHalfEffect();
    default void triggerShadowplayEffect() {
        // Default empty implementation for cards without shadowplay
    }
}