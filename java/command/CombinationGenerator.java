package command;

import expression.Variable;

import java.util.*;

public class CombinationGenerator {
    Set<Map.Entry<Variable, List<Double>>> variableValueEntries;

    public CombinationGenerator(Set<Map.Entry<Variable, List<Double>>> variableValueEntries) {
        this.variableValueEntries = variableValueEntries;
    }

    public List<Map<Variable, Double>> generateGrid() {
        List<Map<Variable, Double>> combinations = initializeCombinations(1);

        for (Map.Entry<Variable, List<Double>> entry : variableValueEntries) {
            combinations = getNewCombinations(entry, combinations);
        }
        return combinations;
    }

    public List<Map<Variable, Double>> generateList() {
        int size = getFirstEntrySize();

        if (anyDontMatch(size)) {
            throw new IllegalArgumentException("(Computing exception) Tuples must have the same size for LIST option.");
        }

        List<Map<Variable, Double>> valueCombinations = initializeCombinations(size);

        for (Map.Entry<Variable, List<Double>> entry : variableValueEntries) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                valueCombinations.set(i, getNewCombination(entry, entry.getValue().get(i), valueCombinations.get(i)));
            }
        }
        return valueCombinations;
    }

    private static ArrayList<Map<Variable, Double>> initializeCombinations(int size) {
        return new ArrayList<>(Collections.nCopies(size, new LinkedHashMap<>()));
    }

    private List<Map<Variable, Double>> getNewCombinations(Map.Entry<Variable, List<Double>> entry, List<Map<Variable, Double>> valueCombinations) {
        List<Map<Variable, Double>> newCombinations = new ArrayList<>();
        for (double value : entry.getValue()) {
            for (Map<Variable, Double> combination : valueCombinations) {
                newCombinations.add(getNewCombination(entry, value, combination));
            }
        }
        return newCombinations;
    }

    private Map<Variable, Double> getNewCombination(Map.Entry<Variable, List<Double>> entry, double value, Map<Variable, Double> combination) {
        Map<Variable, Double> newCombination = new LinkedHashMap<>(combination);
        newCombination.put(entry.getKey(), value);
        return newCombination;
    }

    private int getFirstEntrySize() {
        return variableValueEntries.iterator().next().getValue().size();
    }

    private boolean anyDontMatch(int size) {
        return variableValueEntries.stream().map(Map.Entry::getValue).anyMatch(tuple -> tuple.size() != size);
    }
}
