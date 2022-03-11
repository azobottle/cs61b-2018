package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int next_period;
    private double factor;
    private int state = -1;

    AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        next_period = period;
        this.factor = factor;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        if (state == 0) {
            period = next_period;
            next_period = (int) (period * factor);
            if (next_period <= 1) {
                next_period = 2;
            }
        }
        return (1.0 * state / (period - 1) - 0.5) * 2;
    }
}
