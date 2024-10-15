package com.zahid.indexes;

import java.util.ArrayList;
import java.util.List;

public class Port {
    private final String[] indexes;
    private final List<List<Integer>> convertedArray = new ArrayList<>();
    public Port(String[] indexes) {
        this.indexes = indexes;
    }

    public String getStringOfConvertedArray() {
        convertArrayToNumberSequence();
        StringBuilder string = new StringBuilder();
        for (List<Integer> list : convertedArray) {
            string.append(list.toString().substring(1, list.toString().length() - 1)).append("\n");
        }
        return string.toString();
    }
    public List<List<Integer>> convertArrayToNumberSequence() {
        for (String item : indexes) {
            convertedArray.add(getNumberSequence(item));
        }
        return convertedArray;
    }

    public List<List<Integer>> getUniqueGroups() {
        if (convertedArray.isEmpty()) {
            convertArrayToNumberSequence();
        }
        int length = convertedArray.size();
        List<List<Integer>> groups = new ArrayList<>();
        if (convertedArray.isEmpty()) {
            return groups;
        }
        makeUniqueGroupRecursively(0, new ArrayList<>(), length, groups);
        return groups;
    }

    private void makeUniqueGroupRecursively(int currentPosition, List<Integer> currentGroup, int length, List<List<Integer>> result) {
        if (currentPosition == length) {
            result.add(currentGroup);
        } else {
            List<Integer> array = convertedArray.get(currentPosition);
            for (Integer number : array) {
                List<Integer> list = new ArrayList<>(currentGroup);
                list.add(number);
                makeUniqueGroupRecursively(currentPosition + 1, list, length, result);
            }
        }
    }
    private List<Integer> getNumberSequence(String line) {
        ArrayList<Integer> result = new ArrayList<>();
        if (line.contains(",")) {
            String[] splitNumbers = line.split(",");
            for (String s : splitNumbers) {
                if (s.contains("-")) {
                    result.addAll(getSequenceWithDash(s));
                } else {
                    result.add(Integer.parseInt(s));
                }
            }
        } else if (line.contains("-")) {
            result.addAll(getSequenceWithDash(line));
        } else {
            result.add(Integer.parseInt(line));
        }
        return result;
    }

    private List<Integer> getSequenceWithDash(String sequenceWithDash) {
        ArrayList<Integer> sequence = new ArrayList<>();
        String[] splitNumbers = sequenceWithDash.split("-");
        int startNumber = Integer.parseInt(splitNumbers[0]);
        int finishNumber = Integer.parseInt(splitNumbers[1]);
        for (; startNumber <= finishNumber; startNumber++) {
            sequence.add(startNumber);
        }
        return sequence;
    }
}
