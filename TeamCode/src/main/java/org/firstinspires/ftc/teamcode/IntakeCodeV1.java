package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class IntakeCodeV1 extends OpMode {
    public DcMotor intake1;
    public DcMotor intake2;
    public DcMotor hood;

    public HardwareMap hwMap;



    @Override
    public void init() {
        telemetry.addData("Hello", "World");
        intake1 = hwMap.get(DcMotor.class, "intake1");
        intake1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake2 = hwMap.get(DcMotor.class, "intake2");
        intake2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hood = hwMap.get(DcMotor.class, "hood");
        hood.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }


    @Override
    public void loop() {
        telemetry.addData("Dominic","is the best");
        telemetry.addData("gamepad y power", gamepad2.left_stick_y);
        hood.setPower(gamepad2.left_stick_y);

        intake1.setPower(gamepad2.right_stick_y);
        intake2.setPower(gamepad2.right_stick_y);
    }
}
