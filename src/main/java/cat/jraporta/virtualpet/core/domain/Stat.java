package cat.jraporta.virtualpet.core.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Stat {

    private int value;

    public Stat(int value) {
        this.value = value;
    }

    protected void increaseStat(int value) {
        this.value = Math.max(0, Math.min(this.value + value, 100));
    }

    protected void increaseStat(long value) {
        increaseStat((int) value);
    }

    public void reset() {
        this.value = 0;
    }
}
