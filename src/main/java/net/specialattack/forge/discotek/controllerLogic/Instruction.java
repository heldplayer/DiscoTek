package net.specialattack.forge.discotek.controllerLogic;

import java.util.ArrayList;
import java.util.List;

public class Instruction {

    private int value;
    private List<Integer> selected;
    private boolean isFixture = false;
    private boolean hasError = false;
    private String error = "";
    private int action = 0;
    private boolean needsPreSelected = false;
    private boolean hasValidSelection = false;
    private boolean settingSelection = false;
    private String rawTyped = "";

    public Instruction() {
        this.selected = new ArrayList<Integer>();
    }

    public int getAction() {
        return this.action;
    }

    public Instruction setAction(int action) {
        this.action = action;
        return this;
    }

    public String getError() {
        return this.error;
    }

    public Instruction setError(String error) {
        this.hasError = true;
        this.error = error;
        return this;
    }

    public List<Integer> getSelected() {
        return this.selected;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public boolean isFixture() {
        return this.isFixture;
    }

    public void setFixture(boolean isFixture) {
        this.isFixture = isFixture;
    }

    public boolean isHasValidSelection() {
        return this.hasValidSelection;
    }

    public void setHasValidSelection(boolean hasValidSelection) {
        this.hasValidSelection = hasValidSelection;
    }

    public boolean isNeedsPreSelected() {
        return this.needsPreSelected;
    }

    public void setNeedsPreSelected(boolean needsPreSelected) {
        this.needsPreSelected = needsPreSelected;
    }

    public void addSelection(int id) {
        this.selected.add(id);
    }

    public void removeSelection(int id) {
        for (int i = 0; i < this.selected.size(); i++) {
            if (this.selected.get(i) == id) {
                this.selected.remove(i--);
            }
        }
    }

    public int getSelectedCount() {
        return this.selected.size();
    }

    public int getSelectedAt(int i) {
        return this.selected.get(i);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int val) {
        this.value = val;
    }

    public boolean isSettingSelection() {
        return this.settingSelection;
    }

    public void settingSelection(boolean b) {
        this.settingSelection = b;
    }

    public String getRawTyped() {
        return this.rawTyped;
    }

}
