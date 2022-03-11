package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int num;
    private int next_num;
    private double factor;
    private int state = -1;

    AcceleratingSawToothGenerator(int num, double factor) {
        this.num = num;
        next_num = num;
        this.factor = factor;
    }

    @Override
    public double next() {
        state = (state + 1) % num;
        if (state == 0) {
            num = next_num;
            next_num = (int) (num * factor);
            if (next_num == 0) {
                next_num = 1;
            }
        }
        return (1.0 * state / (num - 1) - 0.5) * 2;
    }
}
