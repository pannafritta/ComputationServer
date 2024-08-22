package expression;

import java.util.Map;

public abstract class Expression {
    public Expression() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    public abstract double evaluate(Map<Variable, Double> variables);
}
