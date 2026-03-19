package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestHood extends OpMode {
    double ang = 0;

    Servo hood;

    public void init(){
        hood = hardwareMap.get(Servo.class, "hood");

        telemetry.addLine("Initilzaed");
        telemetry.update();
    }

    public void loop(){
        if (gamepad1.a){
            ang = ang + 0.2;
        }

        if (gamepad1.b){
            ang = ang - 0.2;

        }

        hood.setPosition(ang);

        telemetry.addData("Angle: ", ang);
        telemetry.update();


    }
}
