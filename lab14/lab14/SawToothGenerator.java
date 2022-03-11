package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state = -1;

    SawToothGenerator(int period) {
        this.period = period;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        return (1.0 * state / (period - 1) - 0.5) * 2;
    }
}
