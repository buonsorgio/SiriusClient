package dev.sirius.client.module.setting;

public class Setting<T> {

    private final String name;
    private T value;
    private T min;
    private T max;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Setting(String name, T value, T min, T max) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public String getName() { return name; }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
    public T getMin() { return min; }
    public T getMax() { return max; }

    public boolean isBoolean() { return value instanceof Boolean; }
    public boolean isNumber() { return value instanceof Number; }
    public boolean isString() { return value instanceof String; }
    public boolean isColor() { return name.toLowerCase().contains("color"); }
}
