package command;

import expression.Expression;
import expression.Variable;
import server.utils.ResponseFormatter;
import server.ServerStats;

import java.util.*;
import java.util.stream.DoubleStream;

public class CommandProcessor {

    public String processStatCommand(StatCommand command, ServerStats stats) {
        return switch (command.getStatKind()) {
            case "REQS" -> String.valueOf(stats.getNumOfOkResponses());
            case "AVG" -> ResponseFormatter.format(stats.getAvgOkResponseTime());
            case "MAX" -> ResponseFormatter.format(stats.getMaxOkResponseTime());
            default ->
                    throw new IllegalStateException("(Command exception) Unexpected command: " + command.getStatKind());
        };
    }

    public String processComputationCommand(ComputationCommand command) {

        Set<Map.Entry<Variable, List<Double>>> variableValueEntries = command.getVariableValuesEntries();

        CombinationGenerator cg = new CombinationGenerator(variableValueEntries);
        List<Map<Variable, Double>> valueCombinations = switch (command.getValuesKind()) {
            case "GRID" -> cg.generateGrid();
            case "LIST" -> cg.generateList();
            default ->
                    throw new IllegalStateException("(Command exception) Unexpected command: " + command.getValuesKind());
        };

        DoubleStream streamOfValues = generateStreamOfValues(command.getExpressions(), valueCombinations);

        double response = switch (command.getComputationKind()) {
            case "MAX" -> computeMaxCommand(streamOfValues);
            case "MIN" -> computeMinCommand(streamOfValues);
            case "AVG" -> computeAvgCommand(streamOfValues);
            case "COUNT" -> streamOfValues.count();
            default ->
                    throw new IllegalStateException("(Command exception) Unexpected command: " + command.getComputationKind());
        };

        return ResponseFormatter.format(response);
    }

    private DoubleStream generateStreamOfValues(List<Expression> expressions, List<Map<Variable, Double>> valueCombinations) {
        return expressions.stream()
                .flatMapToDouble(expression -> valueCombinations.stream().mapToDouble(expression::evaluate))
                .filter(value -> !Double.isNaN(value));
    }

    private double computeMaxCommand(DoubleStream streamOfValues) {
        return streamOfValues.max().orElseThrow(() -> new IllegalStateException("(Computing exception) No values found for MAX computation."));
    }

    private double computeMinCommand(DoubleStream streamOfValues) {
        return streamOfValues.min().orElseThrow(() -> new IllegalStateException("(Computing exception) No values found for MIN computation."));
    }

    private double computeAvgCommand(DoubleStream streamOfValues) {
        return streamOfValues.average().orElseThrow(() -> new IllegalStateException("(Computing exception) No values found for AVG computation."));
    }


}
