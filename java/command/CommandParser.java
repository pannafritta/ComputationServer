package command;

import expression.Expression;
import expression.ExpressionParser;
import expression.Variable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandParser {
    private final List<String> VALID_STAT_COMMANDS = List.of("STAT_REQS", "STAT_AVG_TIME", "STAT_MAX_TIME");
    private final List<String> VALID_COMPUTATION_KINDS = List.of("MIN", "MAX", "AVG", "COUNT");
    private final List<String> VALID_VALUES_KINDS = List.of("GRID", "LIST");
    private final ExpressionParser parser = new ExpressionParser();

    public Command parseCommand(String command) {
        String[] commandParts = command.split(";");

        String firstArg = commandParts[0];
        if (isValidStatCommand(firstArg)) {
            return new StatCommand(firstArg.split("_")[1]);
        }

        if (isValidComputationCommand(firstArg)) {
            String computeKind = firstArg.split("_")[0];
            String valuesKind = firstArg.split("_")[1];
            Map<Variable, List<Double>> variableValuesMappings = parseMappings(commandParts[1]);
            List<Expression> expressions = parseExpression(Arrays.copyOfRange(commandParts, 2, commandParts.length));

            if (expressions.isEmpty()) {
                throw new IllegalArgumentException("(Expression exception) Expression part must have at least one argument.");
            }
            return new ComputationCommand(computeKind, valuesKind, variableValuesMappings, expressions);
        }

        else {
            throw new IllegalArgumentException("(Command exception) Command not recognized: " + Arrays.toString(commandParts));
        }
    }

    private LinkedHashMap<Variable, List<Double>> parseMappings(String mappingsString) {
        return Arrays.stream(mappingsString.split(","))
                .map(tuple -> Arrays.asList(tuple.split(":")))
                .peek(this::validateTuple)
                .collect(Collectors.toMap(
                        tuple -> new Variable(tuple.getFirst()),
                        this::parseSequence,
                        (oldValue, newValue) -> newValue,
                        LinkedHashMap::new
                ));
    }

    private void validateTuple(List<String> tuple) {
        if (tuple.size() != 4) {
            throw new IllegalArgumentException("(command.Command Exception) Tuple must have exactly 4 elements, but " + tuple + " has " + tuple.size() + ".");
        } else if (Double.parseDouble(tuple.get(2)) <= 0) {
            throw new IllegalArgumentException("(Computation Exception) Step must be positive in " + tuple + ".");
        }
    }

    private List<Double> parseSequence(List<String> tuple) {
        double start = Double.parseDouble(tuple.get(1));
        double step = Double.parseDouble(tuple.get(2));
        double end = Double.parseDouble(tuple.get(3));

        List<Double> sequence = new ArrayList<>();

        for (double d = start; round(d) <= end; d += step) {
            sequence.add(round(d));
        }
        return sequence;
    }

    private double round(double number) {
        return Math.round(number * 1e7) / 1e7;
    }

    private List<Expression> parseExpression(String[] expressionStrings) {
        return Stream.of(expressionStrings).map(parser::parse).toList();
    }

    public boolean isQuitCommand(String command) {
        String validQuitCommand = "BYE";
        return validQuitCommand.equals(command);
    }

    public boolean isValidStatCommand(String command) {
        return VALID_STAT_COMMANDS.contains(command);
    }

    public boolean isValidComputationCommand(String command) {
        String[] commandParts = command.split("_");
        return commandParts.length == 2 &&
                VALID_COMPUTATION_KINDS.contains(commandParts[0]) &&
                VALID_VALUES_KINDS.contains(commandParts[1]);
    }

}
