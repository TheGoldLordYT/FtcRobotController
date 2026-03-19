package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestHood extends OpMode {
    double power = 0;
    double realPower = 0;

    DcMotor shooter;

    public void init(){
        shooter = hardwareMap.get(DcMotor.class, "shooter");

        telemetry.addLine("Initilzaed");
        telemetry.update();
    }

    public void loop(){
        if (gamepad1.a){
            power = 0;
        }

        if (gamepad1.b){
            power = 1;

        }

        if (gamepad1.left_bumper) {
            power = gamepad1.left_stick_y;
        }

        if (gamepad1.x) {
            shooter.setPower(power);
            realPower = power;
        }
        if (gamepad1.y){
            shooter.setPower(0);
            realPower = 0;
        }

        telemetry.addData("Power: ", power);
        telemetry.update();


    }
}
