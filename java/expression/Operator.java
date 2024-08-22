package expression;

import java.util.Map;
import java.util.function.Function;

public class Operator extends Expression {

    public enum Type {
        SUM('+', a -> a[0] + a[1]),
        SUBTRACTION('-', a -> a[0] - a[1]),
        MULTIPLICATION('*', a -> a[0] * a[1]),
        DIVISION('/', a -> a[0] / a[1]),
        POWER('^', a -> Math.pow(a[0], a[1]));
        private final char symbol;
        private final Function<double[], Double> function;

        Type(char symbol, Function<double[], Double> function) {
            this.symbol = symbol;
            this.function = function;
        }

        public char getSymbol() {
            return symbol;
        }

    }

    private final Type type;
    private final Expression left;
    private final Expression right;

    public Operator(Type type, Expression left, Expression right) {
        super();
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return type == operator.type;
    }

    @Override
    public double evaluate(Map<Variable, Double> variables) {
        double result = type.function.apply(new double[]{left.evaluate(variables), right.evaluate(variables)});
        if (Double.isInfinite(result)) return Double.NaN;
        return result;
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, type.symbol, right);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
