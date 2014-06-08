package damirm;

import java.util.Random;

public abstract class Meter {

    public abstract void makePulse();

    public abstract void sendToServer() throws Exception;

    public abstract int queueSize();

    public static int randInt(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }
}
