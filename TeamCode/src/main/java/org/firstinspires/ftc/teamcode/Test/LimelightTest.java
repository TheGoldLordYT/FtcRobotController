package org.firstinspires.ftc.teamcode.Test;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp
public class LimelightTest extends OpMode {
    public final double blueX = -58.346457;
    public final double blueY = -55.629921;
    public final double redX = 0.5877852522924731;
    public final double redY = 0.8090169943749473;
    public final double redANDblueZ = 0;
    private double distance2 = 0;
    private LLResult result;
    private Limelight3A limelight;

    @Override
    public void init(){
        //Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(1);
        telemetry.addData("Limelight is on the following pipeline:", 1);

        limelight.start(); // This tells Limelight to start looking!
        telemetry.addLine("Limelight is alive");


    }
    @Override
    public void loop(){
    //DISTANCE
    //####################
        result = limelight.getLatestResult();


        if (result.isValid()) {
            Pose3D botpose = result.getBotpose();
            telemetry.addLine("Find bot pos");
            if (botpose != null) {
                double x = botpose.getPosition().x;
                double y = botpose.getPosition().y;
                telemetry.addData("Xpos", x);
                telemetry.addData("Ypos", y);

                //20 (Blue):
                // X (-58.346457 in)
                // Y (-55.629921 in)

                //24 (Red):
                // X (58.346457 in)
                // Y (55.629921 in)

                distance2 = Math.sqrt(
                        Math.pow(redX - x, 2)
                                +
                                Math.pow(redY - y, 2));

                //Since the z height is the same for both.
                distance2 = Math.sqrt(
                        Math.pow(distance2, 2)
                                +
                                Math.pow(redANDblueZ, 2));

                telemetry.addData("Distance2", distance2);


            }


        } else {
            telemetry.addLine("Looking for tag");
        }

        telemetry.update();

    }
}
