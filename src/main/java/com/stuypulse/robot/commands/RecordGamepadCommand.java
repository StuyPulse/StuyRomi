package com.stuypulse.robot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// import com.google.gson.Gson;

import com.stuypulse.robot.Constants;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.GamepadState;
import com.stuypulse.stuylib.util.StopWatch;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class RecordGamepadCommand extends CommandBase {
    private final Gamepad gamepad;
    
    private final List<GamepadState> states;
    
    public RecordGamepadCommand(Gamepad gamepad) {
        this.gamepad = gamepad;
        states = new ArrayList<>();
    }


    @Override
    public void initialize() {
        states.clear();
    }

    @Override
    public void execute() {
        states.add(new GamepadState(gamepad));
    }

    private String getOutputFileName() {
        return gamepad.getGamepadName() + "-at-" + StopWatch.kMillisEngine.getRawTime();
    }

    private File getOutputFile() {
        return Constants.DEPLOY_DIRECTORY.resolve(getOutputFileName()).toFile();
    }

    public void end(boolean interrupted) {
        try {
            FileWriter writer = new FileWriter(getOutputFile());
            // new Gson().toJson(states, writer);
        } catch (IOException e) {
            DriverStation.reportWarning("didn't save gamepad states", false);
        }
    }
}
