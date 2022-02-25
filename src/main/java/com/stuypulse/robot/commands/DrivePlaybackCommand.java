package com.stuypulse.robot.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.GamepadState;

public class DrivePlaybackCommand extends DrivetrainDriveCommand {

    // private static final List<GamepadState> readGamepadStates(Path path) {
    //     try {
    //         Gson jsonParser = new Gson();
    //         BufferedReader jsonFile = Files.newBufferedReader(path);
    //         List<GamepadState> states =
    //                 jsonParser.fromJson(jsonFile, new TypeToken<List<GamepadState>>() {}.getType());
            
    //         return states;
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return List.of(new GamepadState());
    //     }
    // }

    public DrivePlaybackCommand(Drivetrain subsystem) {
        super(subsystem, new Gamepad());
    }

    // public double getSpeed() {

    // }
    
}
