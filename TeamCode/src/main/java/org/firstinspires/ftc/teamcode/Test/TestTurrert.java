package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class TestTurrert extends OpMode {
    private CRServo turret;
    private double kP = 0.0001;
    private double kD = 0.0000;

    //the offset if the limelight isn't perfectly on the robot
    private double goalX = 0;
    private double lastError = 0;
    private double angleTolerance = 0.2;

    //Tune this
    private final double MAX_POWER = 1;
    private double power = 0;
    private final ElapsedTime timer = new ElapsedTime();

    private Limelight3A limelight;

    @Override
    public void init(){
        turret = hardwareMap.get(CRServo.class, "turret");

        //Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start(); // This tells Limelight to start looking!

        telemetry.addLine("It's is initialized");

        telemetry.update();


    }

    @Override
    public void start(){
        timer.reset();
    }

    @Override
    public void loop(){
        double deltaTime = timer.seconds();
        timer.reset();

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && gamepad2.left_bumper) {
            telemetry.addLine("Tag found");
            double error = goalX - result.getTx();
            double pTerm = error * kP;

            double dTerm = 0;
            if (deltaTime > 0){
                dTerm = ((error - lastError) / deltaTime) * kD;
            }

            if (Math.abs(error) < angleTolerance){
                power = 0;
            } else {
                power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER * 200);
            }

            //Take this out later, and actually set the power.
            turret.setPower(power);
            telemetry.addData("Power", power);
            lastError = error;


        } else {
            telemetry.addLine("Looking for tag");
            turret.setPower(0);
            lastError = 0;
        }

        telemetry.update();

    }

}
