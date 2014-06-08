package damirm;

import damirm.eagle.EagleMeter;
import damirm.obvius.ObviusMeter;

public class Main {

    Meter meter;

    public static void main(String[] args) {
        Main main = new Main();
        main.meter = new ObviusMeter();
//        main.meter = new EagleMeter();
        try {
            boolean run = true;
            while (run) {
                main.doJob();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doJob() throws Exception {
        boolean run = true;
        while (run) {
            meter.makePulse();
            meter.sendToServer();

            Thread.sleep(1000 * 60);
        }
    }
}
