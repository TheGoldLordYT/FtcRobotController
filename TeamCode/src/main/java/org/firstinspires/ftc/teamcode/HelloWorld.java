package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class HelloWorld extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Hello", "World");
    }


    @Override
    public void loop() {
        telemetry.addData("Dominic","is the best");
        telemetry.addData("gamepad", gamepad2.left_stick_x);

    }
}
