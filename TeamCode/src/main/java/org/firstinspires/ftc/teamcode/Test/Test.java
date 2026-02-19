package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Test extends OpMode {
    private DcMotor shooter;
    private DcMotor intake;
    private CRServo hood;

    private Limelight3A limelight;

    public double hoodPOS = 0;

    public final double hoodMAX = 0;
    public final double hoodMIN = -1380.0601;

    @Override
    public void init(){

        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        hood = hardwareMap.get(CRServo.class, "hood");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        //Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!

        telemetry.addLine("Everything is initialized");

        telemetry.update();
    }

    @Override
    public void loop(){

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && gamepad1.a) {
            telemetry.addData("Ta", result.getTa());
            telemetry.addData("Distance", getDistanceFromAprilTag(result.getTa()));
        }

        if (((hoodPOS + gamepad1.left_stick_y) > hoodMIN) && (hoodPOS + gamepad1.left_stick_y) < hoodMAX){
            hood.setPower(gamepad1.left_stick_y);
            hoodPOS += gamepad1.left_stick_y;
        }
        telemetry.addData("HoodPOS", hoodPOS);

        if (gamepad1.right_bumper){
            hoodPOS = 0;
        }

        if (gamepad1.x){
            shooter.setPower(1);
            telemetry.addData("intakePOWER", 1);
        }

        if (gamepad1.y){
            shooter.setPower(0);
            telemetry.addData("intakePOWER", 0);
        }

        if (gamepad1.b){
            shooter.setPower(0.5);
            telemetry.addData("intakePOWER", 0.5);
        }

        intake.setPower(gamepad1.right_stick_y);

        telemetry.update();

    }

    public double getDistanceFromAprilTag(double ta){
        double scale = 30665.95;
        double distance = (scale/ta);
        return distance;

    }

}
