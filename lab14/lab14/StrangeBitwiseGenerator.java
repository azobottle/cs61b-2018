package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state = -1;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
    }

    @Override
    public double next() {
        state++;
        int weirdState = state & (state >> 7) % period;
        return (1.0 * weirdState / (period - 1) - 0.5) * 2;
    }
}
