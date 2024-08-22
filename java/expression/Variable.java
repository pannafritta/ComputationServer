package expression;

import java.util.Map;
import java.util.Objects;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        super();
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public double evaluate(Map<Variable, Double> variables) {
        return variables.keySet().stream()
                .filter(this::equals)
                .map(variables::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("(ComputationException) Unvalued variable " + name + "."));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
