package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp
public class Test extends OpMode {
    //20 (Blue):
    // X (-58.346457 in)
    // Y (-55.629921 in)

    //24 (Red):
    // X (58.346457 in)
    // Y (55.629921 in)

    public final double blueX = -58.346457;
    public final double blueY = -55.629921;
    public final double redX = 58.346457;
    public final double redY = 55.629921;
    public final double redANDblueZ = 29.488189;

    private double distance2 = 0;

    private DcMotor shooter;
    private DcMotor intake;
    private CRServo hood;

    private Limelight3A limelight;

    public double hoodPOS = 0;

    public final double hoodMAX = 0;
    public final double hoodMIN = -1380.0601;
    public String team = "blue";

    public double power = 0;

    @Override
    public void init(){

        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        hood = hardwareMap.get(CRServo.class, "hood");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        //Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(0);
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
            Pose3D botpose = result.getBotpose();
            if (botpose != null) {
                double x = botpose.getPosition().x;
                double y = botpose.getPosition().y;

                //20 (Blue):
                // X (-58.346457 in)
                // Y (-55.629921 in)

                //24 (Red):
                // X (58.346457 in)
                // Y (55.629921 in)
                if (team.equals("blue")) {
                    distance2 = Math.sqrt(
                            Math.pow(blueX - x,2)
                            +
                            Math.pow(blueY - y,2));
                } else {
                    distance2 = Math.sqrt(
                            Math.pow(redX - x,2)
                            +
                            Math.pow(redY - y,2));
                }

                //Since the z height is the same for both.
                distance2 = Math.sqrt(
                        Math.pow(distance2,2)
                        +
                        Math.pow(redANDblueZ,2) );

                telemetry.addData("Distance2", distance2);


            }


        }

        if (((hoodPOS + gamepad1.left_stick_y) > hoodMIN) && (hoodPOS + gamepad1.left_stick_y) < hoodMAX){
            if (Math.abs(gamepad1.left_stick_y) == 1 || gamepad1.left_stick_y == 0) {
                hood.setPower(gamepad1.left_stick_y);
                hoodPOS += gamepad1.left_stick_y;
            }
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
            shooter.setPower(-power);
            telemetry.addData("intakePOWER", power);
        }

        if (gamepad1.left_bumper) {
            power = gamepad1.right_stick_y;
            telemetry.addData("Power", power);
        }

        telemetry.update();

    }

    public double getDistanceFromAprilTag(double ta){
        double scale = 30665.95;
        double distance = (scale/ta);
        return distance;

    }



}
