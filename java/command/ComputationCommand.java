package command;

import expression.Expression;
import expression.Variable;

import java.util.Set;
import java.util.List;
import java.util.Map;

public class ComputationCommand extends Command {

    private final String computationKind;
    private final String valuesKind;
    private final Map<Variable, List<Double>> variableValuesMappings;
    private final List<Expression> expressions;

    public ComputationCommand(String computeKind, String valuesKind, Map<Variable, List<Double>> variableValuesMappings, List<Expression> expressions) {
        this.computationKind = computeKind;
        this.valuesKind = valuesKind;
        this.variableValuesMappings = variableValuesMappings;
        this.expressions = expressions;
    }

    @Override
    public boolean isStatCommand() {
        return false;
    }

    @Override
    public boolean isComputationCommand() {
        return true;
    }

    public String getComputationKind() {
        return computationKind;
    }

    public String getValuesKind() {
        return valuesKind;
    }

    public Set<Map.Entry<Variable, List<Double>>> getVariableValuesEntries() {
        return variableValuesMappings.entrySet();
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

}
