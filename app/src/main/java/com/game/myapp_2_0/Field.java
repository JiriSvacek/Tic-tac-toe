package com.game.myapp_2_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Field {
    private int inRow;
    private int lengthRow;
    private int lengthColumn;
    private List<List<String>> field;

    public int getLengthRow() {
        return lengthRow;
    }

    public int getLengthColumn() {
        return lengthColumn;
    }

    public Field(int inRow, int fieldSize) {
        this.inRow = inRow;
        createField(fieldSize);
        lengthRow = field.size();
        lengthColumn = field.get(0).size();
    }

    private void createField(int fieldSize) {
        this.field = new ArrayList<>();
        for (int h = 0; h < fieldSize; h++) {
            this.field.add(new ArrayList<>());
            for (int v = 0; v < fieldSize; v++) {
                this.field.get(h).add("");
            }
        }
    }

    public boolean writeToField(String position, String player) {
        String[] coordinates = position.split(",");
        int posX = Integer.parseInt(coordinates[0]);
        int posY = Integer.parseInt(coordinates[1]);
        if (Objects.equals(this.field.get(posX).get(posY), "")) {
            this.field.get(posX).set(posY, player);
            return true;
        } else {return false;}
    }

    public static <T> List<List<T>> zip(List<T>... lists) {
        List<List<T>> zipped = new ArrayList<List<T>>();
        for (List<T> list : lists) {
            for (int i = 0, listSize = list.size(); i < listSize; i++) {
                List<T> list2;
                if (i >= zipped.size())
                    zipped.add(list2 = new ArrayList<T>());
                else
                    list2 = zipped.get(i);
                list2.add(list.get(i));
            }
        }
        return zipped;
    }

    public boolean checkIfInRow(String position) {
        String[] coordinates = position.split(",");
        int posX = Integer.parseInt(coordinates[0]);
        int posY = Integer.parseInt(coordinates[1]);
        HashMap<String, List<Integer>> directions = createListForCheck(posX, posY);
        int minusX = checkInRowForX(directions.get("x-"), posX, posY);
        int minusY = checkInRowForY(directions.get("y-"), posX, posY);
        int plusX = checkInRowForX(directions.get("x+"), posX, posY);
        int plusY = checkInRowForY(directions.get("y+"), posX, posY);
        int diagMinusMinus = checkInDiagonal(directions.get("x-"), directions.get("y-"), posX, posY);
        int diagMinusPlus = checkInDiagonal(directions.get("x-"), directions.get("y+"), posX, posY);
        int diagPlusPlus = checkInDiagonal(directions.get("x+"), directions.get("y+"), posX, posY);
        int diagPlusMinus= checkInDiagonal(directions.get("x+"), directions.get("y-"), posX, posY);
        return ((minusX + plusX + 1 >= inRow) || (minusY + plusY + 1 >= inRow)
                || (diagMinusMinus + diagPlusPlus + 1 >= inRow) || (diagPlusMinus + diagMinusPlus + 1 >= inRow));
    }

    private int checkInRowForY(List<Integer> direction, int posX, int posY) {
        if (direction.isEmpty()) {return 0;}
        int row = 0;
        for (Integer i : direction) {
            if (Objects.equals(field.get(posX).get(posY), field.get(posX).get(i))) {
                row++;
            } else { break;}
        }
        return row;
    }

    private int checkInRowForX(List<Integer> direction, int posX, int posY) {
        if (direction.isEmpty()) {return 0;}
        int row = 0;
        for (Integer i : direction) {
            if (Objects.equals(field.get(posX).get(posY), field.get(i).get(posY))) {
                row++;
            } else {break;}
        }
        return row;
    }

    private int checkInDiagonal(List<Integer> dirX, List<Integer> dirY, int posX, int posY) {
        int row = 0;
        if (dirX.isEmpty() || dirY.isEmpty())
            return row;
        for (List<Integer> i : zip(dirX, dirY)) {
            if (i.size() < 2) {
                break;
            } else if (Objects.equals(field.get(posX).get(posY), field.get(i.get(0)).get(i.get(1)))) {
                row++;
            } else {break;}
        }
        return row;
    }


    private HashMap<String, List<Integer>> createListForCheck(int posX, int posY) {
        HashMap<String, List<Integer>> wholeMap = new HashMap<>();
        wholeMap.put("x-", createListOfIntLower(posX));
        wholeMap.put("y-", createListOfIntLower(posY));
        wholeMap.put("x+", createListOfIntHigher(posX));
        wholeMap.put("y+", createListOfIntHigher(posY));
        return wholeMap;
    }

    private ArrayList<Integer> createListOfIntHigher(int pos) {
        ArrayList<Integer> list = new ArrayList<>();
        int step = 1;
        int iterate = pos + 1;
        for (;iterate < lengthColumn  && step<= inRow;iterate++,step++) {
            list.add(iterate);
        }
        return list;
    }

    private ArrayList<Integer> createListOfIntLower(int pos) {
        ArrayList<Integer> list = new ArrayList<>();
        int step = 1;
        int iterate = pos - 1;
        for (;iterate>=0 && step<= inRow;iterate--,step++) {
            list.add(iterate);
        }
        return list;
    }
}

