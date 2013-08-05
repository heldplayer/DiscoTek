
package net.specialattack.modjam.controllerLogic;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class Instruction {

    private int[] values;
    private List<Integer> selected;
    private boolean isFixture = false;
    private boolean hasError = false;
    private String error = "";
    private int action = 0;
    private boolean needsPreSelected = false;
    private boolean hasValidSelection = false;

    public int getAction() {
        return action;
    }

    public String getError() {
        return error;
    }

    public List<Integer> getSelected() {
        return selected;
    }

    public int[] getValues() {
        return values;
    }

    public boolean hasError() {
        return hasError;
    }

    public boolean isFixture() {
        return isFixture;
    }

    public boolean isHasValidSelection() {
        return hasValidSelection;
    }

    public boolean isNeedsPreSelected() {
        return needsPreSelected;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Instruction setError(String error) {
        this.hasError = true;
        this.error = error;
        return this;
    }

    public void setFixture(boolean isFixture) {
        this.isFixture = isFixture;
    }

    public void setHasValidSelection(boolean hasValidSelection) {
        this.hasValidSelection = hasValidSelection;
    }

    public void setNeedsPreSelected(boolean needsPreSelected) {
        this.needsPreSelected = needsPreSelected;
    }

    public void addValue(int value) {
        ArrayUtils.add(values, values.length, value);
    }

    public void addSelection(int id) {
        selected.add(id);
    }

    public void removeSelection(int id) {
        for (int i = 0; i < selected.size(); i++) {
            if (selected.get(i) == id) {
                selected.remove(i--);
            }
        }
    }

    public int getSelectedCount() {
        return selected.size();
    }

}
