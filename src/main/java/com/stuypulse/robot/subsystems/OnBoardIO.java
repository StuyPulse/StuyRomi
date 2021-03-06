// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.Constants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Button;

/**
 * This class represents the onboard IO of the Romi reference robot. This
 * includes the pushbuttons and LEDs.
 *
 * <p>
 * DIO 0 - Button A (input only) DIO 1 - Button B (input) or Green LED (output)
 * DIO 2 - Button C (input) or Red LED (output) DIO 3 - Yellow LED (output only)
 */
public class OnBoardIO extends SubsystemBase {
    
    // DIO 0
    private final DigitalInput buttonA;

    // DIO 1
    private final DigitalInput buttonB;
    private final DigitalOutput greenLED;

    // DIO 2
    private final DigitalInput buttonC;
    private final DigitalOutput redLED;

    // DIO 3
    private final DigitalOutput yellowLED;

    private double nextMessageTime;

    public enum ChannelMode {
        INPUT, OUTPUT
    }

    /**
     * Constructor.
     *
     * @param dio1 Mode for DIO 1 (input = Button B, output = green LED)
     * @param dio2 Mode for DIO 2 (input = Button C, output = red LED)
     */
    public OnBoardIO(ChannelMode dio1, ChannelMode dio2) {
        buttonA = new DigitalInput(0);
        addChild("Button A", buttonA);

        if (dio1 == ChannelMode.INPUT) {
            greenLED = null;
            buttonB = new DigitalInput(1);
            addChild("Button B", buttonB);
        } else {
            buttonB = null;
            greenLED = new DigitalOutput(1);
            addChild("Green LED", greenLED);
        }

        if (dio2 == ChannelMode.INPUT) {
            redLED = null;
            buttonC = new DigitalInput(2);
            addChild("Button C", buttonC);
        } else {
            buttonC = null;
            redLED = new DigitalOutput(2);
            addChild("Red LED", greenLED);
        }

        yellowLED = new DigitalOutput(3);
        addChild("Yellow LED", yellowLED);
    }

    /** Gets if the A button is pressed. */
    public boolean getButtonAPressed() {
        return buttonA.get();
    }

    /** Gets if the B button is pressed. */
    public boolean getButtonBPressed() {
        if (buttonB != null) {
            return buttonB.get();
        }

        double currentTime = Timer.getFPGATimestamp();
        if (currentTime > nextMessageTime) {
            DriverStation.reportError("Button B was not configured", true);
            nextMessageTime = currentTime + Constants.OnBoardIO.MESSAGE_INTERVAL;
        }
        return false;
    }

    /** Gets if the C button is pressed. */
    public boolean getButtonCPressed() {
        if (buttonC != null) {
            return buttonC.get();
        }

        double currentTime = Timer.getFPGATimestamp();
        if (currentTime > nextMessageTime) {
            DriverStation.reportError("Button C was not configured", true);
            nextMessageTime = currentTime + Constants.OnBoardIO.MESSAGE_INTERVAL;
        }
        return false;
    }

    /** Get button class for the onboard A button */
    public Button getButtonA() {
        return new Button(this::getButtonAPressed);
    }

    /** Get button class for the onboard B button */
    public Button getButtonB() {
        return new Button(this::getButtonBPressed);
    }
    
    /** Get button class for the onboard C button */
    public Button getButtonC() {
        return new Button(this::getButtonCPressed);
    }

    /** Sets the green LED. */
    public void setGreenLed(boolean value) {
        if (greenLED != null) {
            greenLED.set(value);
        } else {
            double currentTime = Timer.getFPGATimestamp();
            if (currentTime > nextMessageTime) {
                DriverStation.reportError("Green LED was not configured", true);
                nextMessageTime = currentTime + Constants.OnBoardIO.MESSAGE_INTERVAL;
            }
        }
    }

    /** Sets the red LED. */
    public void setRedLed(boolean value) {
        if (redLED != null) {
            redLED.set(value);
        } else {
            double currentTime = Timer.getFPGATimestamp();
            if (currentTime > nextMessageTime) {
                DriverStation.reportError("Red LED was not configured", true);
                nextMessageTime = currentTime + Constants.OnBoardIO.MESSAGE_INTERVAL;
            }
        }
    }

    /** Sets the yellow LED. */
    public void setYellowLed(boolean value) {
        yellowLED.set(value);
    }

}
