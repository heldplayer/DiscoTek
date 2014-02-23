
package net.specialattack.forge.discotek.controllerLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InstructionParser {

    //Syntax
    //DIGIT = 0|1|2|3|4|5|6|7|8|9
    //NUMBER = digit{digit}
    //COMMAND = CLEAR | @
    //LEVEL = <NUMBER> | <NUMBER>% | FULL | FF | *
    //SELECTION = (FIXTURE | CHANNEL) (<NUMBER> | <NUMBER> THRU <NUMBER>) {+|- <NUMBER>}
    //COMMAND = (<SELECTION> | <SELECTION> <COMMAND> {<NUMBER>}| <COMMAND> {<NUMBER>})

    String[] actionKeys = { "CLEAR", "@" };
    //Ref by position of keyword in actionKeys
    int[] actionIds = { 0, 1 };

    //Ref by action id
    int[] argCounts = { 0, 1 };

    public Instruction validateCommand(String command) {
        String testCommand = command.toUpperCase().replace(" ", "");
        String[] commandParts = this.splitAtKeyWord(testCommand);
        int action = this.getKeyWordActionId(testCommand);
        if (action == 0) {
            //We dont care about anything else really. We just clear the selected cache
            return new Instruction().setAction(0);
        }

        Instruction instruction;
        if (commandParts.length == 2) {
            if (commandParts[0].length() == 0) {
                instruction = new Instruction();
                instruction.setAction(action);
                instruction.setNeedsPreSelected(true);
                instruction.setValue(this.validateValuesStatement(commandParts[1]));
                return instruction;
            }
            else {
                instruction = this.validateSelectionStatement(commandParts[0], action);
                instruction.setNeedsPreSelected(!instruction.isHasValidSelection());
                instruction.setAction(action);
                instruction.setValue(this.validateValuesStatement(commandParts[1]));
            }
        }
        else if (commandParts.length == 1) {
            instruction = this.validateSelectionStatement(commandParts[0], action);
            instruction.setNeedsPreSelected(!instruction.isHasValidSelection());
            instruction.setAction(action);
            instruction.settingSelection(true);
        }
        else {
            return new Instruction().setError("Syntax error on command!");
        }

        return instruction;
    }

    private int validateValuesStatement(String string) {
        if (string.contains("*") || string.contains("FULL") || string.contains("FF")) {
            return 255;
        }
        String numS = this.getNextNum(string);
        try {
            int num = Integer.parseInt(numS);
            if (string.endsWith("%")) {
                return (int) ((num / 100.0F) * 255.0F);
            }
            else {
                return num;
            }
        }
        catch (NumberFormatException e) {
            return 0;
        }

    }

    private Instruction validateSelectionStatement(String string, int action) {
        Instruction inst = new Instruction();

        //Set selection type
        if (string.startsWith("FIXTURE")) {
            inst.setFixture(true);
            string = string.substring(7);
        }
        else if (string.startsWith("F")) {
            inst.setFixture(true);
            string = string.substring(1);
        }
        else if (string.startsWith("CHANNEL")) {
            string = string.substring(7);
        }
        else if (string.startsWith("C")) {
            string = string.substring(1);
        }

        String startS = this.getNextNum(string);
        int start = 0;
        if (startS.length() == 0) {
            return inst.setError("Failed to parse selection values. No Start");
        }
        else {
            try {
                start = Integer.parseInt(startS);
                string = string.substring(startS.length());
            }
            catch (Exception e) {
                return inst.setError("Failed to parse selection values. Start is not number");
            }
        }

        int end = -1;
        if (string.startsWith("THRU") || string.startsWith("/") || string.startsWith("T")) {
            if (string.startsWith("THRU")) {
                string = string.substring(4);
            }
            else {
                string = string.substring(1);
            }
            String endS = this.getNextNum(string);
            if (endS.length() == 0) {
                return inst.setError("Failed to parse selection values after THRU.");
            }
            else {
                try {
                    end = Integer.parseInt(endS);
                    string = string.substring(endS.length());
                }
                catch (Exception e) {
                    return inst.setError("Failed to parse selection values after THRU.");
                }
            }
        }
        if (!inst.isFixture()) {
            if (start < 0 || start > 255 || end < -1 || end > 255) {
                return inst.setError("Channels out of range!");
            }
        }

        if (end > -1) {
            if (end <= start) {
                return inst.setError("Start value must be less than End value on THRU statement.");
            }
            else {
                for (int i = start; i <= end; i++) {
                    inst.addSelection(i);
                }
            }
        }
        else {
            inst.addSelection(start);
        }

        //Additional values. I.E +/- <number>
        while (string.length() > 1) {
            if (string.startsWith("+")) {
                string = string.substring(1);
                String additionalS = this.getNextNum(string);
                if (additionalS.length() == 0) {
                    return inst.setError("Failed to parse optional additional selection values (+ <number>).");
                }
                else {
                    try {
                        int additional = Integer.parseInt(additionalS);
                        string = string.substring(additionalS.length());
                        inst.addSelection(additional);
                    }
                    catch (Exception e) {
                        return inst.setError("Failed to parse optional additional selection values (+ <number>).");
                    }
                }
            }
            else if (string.startsWith("-")) {
                string = string.substring(1);
                String additionalS = this.getNextNum(string);
                if (additionalS.length() == 0) {
                    return inst.setError("Failed to parse optional additional selection values (- <number>).");
                }
                else {
                    try {
                        int additional = Integer.parseInt(additionalS);
                        string = string.substring(additionalS.length());
                        inst.removeSelection(additional);
                    }
                    catch (Exception e) {
                        return inst.setError("Failed to parse optional additional selection values (- <number>).");
                    }
                }
            }
            else {
                return inst.setError("Failed to parse command @ " + string);
            }
        }
        //Got this far... We must be good :D
        inst.setHasValidSelection(true);
        return inst;
    }

    //Forgive me java gods D:
    private String getNextNum(String string) {
        String num = "";
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (Character.isDigit(character)) {
                num += character;
            }
            else {
                break;
            }
        }
        return num;
    }

    private String[] splitAtKeyWord(String command) {
        for (int i = 0; i < this.actionKeys.length; i++) {
            if (command.indexOf(this.actionKeys[i]) > -1) {
                return command.split(this.actionKeys[i]);
            }
        }
        String[] ret = { command };
        return ret;
    }

    private int getKeyWordActionId(String command) {
        for (int i = 0; i < this.actionKeys.length; i++) {
            if (command.indexOf(this.actionKeys[i]) > -1) {
                return i;//this.actionIds[i];
            }
        }
        return -1;
    }

    //For testing
    public static BufferedReader in;
    public static InstructionParser parser = new InstructionParser();

    public static void main(String[] args) throws IOException {
        InstructionParser.in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        while (!(line = InstructionParser.in.readLine()).equalsIgnoreCase("stop")) {
            Instruction inst = InstructionParser.parser.validateCommand(line);
            if (inst.getAction() == 0) {
                System.out.println("Clearing Cache");
            }
            else if (inst.hasError()) {
                System.err.println(inst.getError());
            }
            else if (inst.isSettingSelection()) {
                System.out.println("Setting Cache");
                String chans = "";
                for (int i = 0; i < inst.getSelectedCount(); i++) {
                    chans += "," + inst.getSelectedAt(i);
                }
                System.out.println((inst.isFixture() ? "Fixtures: " : "Channels: ") + chans.substring(1));
            }
            else {
                if (!inst.isNeedsPreSelected()) {
                    String chans = "";
                    for (int i = 0; i < inst.getSelectedCount(); i++) {
                        chans += "," + inst.getSelectedAt(i);
                    }
                    System.out.println((inst.isFixture() ? "Fixtures: " : "Channels: ") + chans.substring(1));
                }
                else {
                    System.out.println("Using cache");
                    System.err.println(inst.getError());
                }
                System.out.println("Value: " + inst.getValue());
            }
        }
    }
}
