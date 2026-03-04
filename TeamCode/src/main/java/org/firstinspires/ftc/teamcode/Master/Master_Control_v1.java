package org.firstinspires.ftc.teamcode.Master;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Test.TestFieldCentricDrive;

@TeleOp
public class Master_Control_v1 extends OpMode {
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
    private CRServo turret;
    private DcMotor transfer;

    private IMU imu;
    private Limelight3A limelight;

    public double hoodPOS = 0;

    public final double hoodMAX = 0;
    public final double hoodMIN = -1380.0601;
    public String team = "blue";

    public double power = 0;

    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private final ElapsedTime timer = new ElapsedTime();
    private double kP = 0.0001;
    private double kD = 0.0000;

    //the offset if the limelight isn't perfectly on the robot
    private double goalX = 0;
    private double lastError = 0;
    private double angleTolerance = 0.2;

    //Tune this
    private final double MAX_POWER = 1;
    private double powerTURRET = 0;

    @Override
    public void init(){

        //timer
        timer.reset();


        // Declare our motors
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        backLeftMotor = hardwareMap.dcMotor.get("backLeft");
        frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        backRightMotor = hardwareMap.dcMotor.get("backRight");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);


        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        hood = hardwareMap.get(CRServo.class, "hood");
        turret = hardwareMap.get(CRServo.class, "turret");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        //Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        if (team.equals("blue")) {
            limelight.pipelineSwitch(0);
            telemetry.addData("Limelight is on the following pipeline:", 0);
        } else {
            limelight.pipelineSwitch(1);
            telemetry.addData("Limelight is on the following pipeline:", 1);
        }


        limelight.start(); // This tells Limelight to start looking!
        telemetry.addData("Pipeline has been configured for team:", team);
        telemetry.addLine("Everything is initialized");

        telemetry.update();
    }

    @Override
    public void loop(){

        //DRIVE (Using the test feild centric)
        Double[] powers = TestFieldCentricDrive.drive(-gamepad2.left_stick_y, gamepad2.left_stick_x, gamepad2.right_stick_x, imu, true);
        frontLeftMotor.setPower(powers[0]);
        backLeftMotor.setPower(powers[1]);
        frontRightMotor.setPower(powers[2]);
        backRightMotor.setPower(powers[3]);


        //Auto turret aim
        double deltaTime = timer.seconds();
        timer.reset();

        //Get the limelight result
        LLResult result = limelight.getLatestResult();


        if (result != null && result.isValid() && gamepad2.left_bumper) {
            telemetry.addLine("Tag found");
            double error = goalX - result.getTx();
            telemetry.addData("Error", error);
            double pTerm = error * kP;

            double dTerm = 0;
            if (deltaTime > 0){
                dTerm = ((error - lastError) / deltaTime) * kD;
            }

            if (Math.abs(error) < angleTolerance){
                powerTURRET = 0;
            } else {
                powerTURRET = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER * 200);
            }

            //Take this out later, and actually set the power.
            turret.setPower(powerTURRET);
            telemetry.addData("Power", powerTURRET);
            lastError = error;


        } else {
            telemetry.addLine("Looking for tag");
            turret.setPower(0);
            lastError = 0;
        }


        //Ressiting IMU
        if (gamepad2.options) {
            imu.resetYaw();
        }

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

        //Using the hood. Added some limits so the hood position is more accurate (it is still not good.)
        if (((hoodPOS + gamepad1.left_stick_y) > hoodMIN) && (hoodPOS + gamepad1.left_stick_y) < hoodMAX){
            if (Math.abs(gamepad1.left_stick_y) == 1 || gamepad1.left_stick_y == 0) {
                hood.setPower(gamepad1.left_stick_y);
                hoodPOS += gamepad1.left_stick_y;
            }
        }
        //Hood position added to the telemetry.
        telemetry.addData("HoodPOS", hoodPOS);

        if (gamepad1.right_bumper){
            hoodPOS = 0;
        }

        if (gamepad1.x){
            shooter.setPower(1);
            telemetry.addData("shooterPOWER", 1);
        }

        if (gamepad1.y){
            shooter.setPower(0);
            telemetry.addData("shooterPOWER", 0);
        }

        if (gamepad1.b){
            shooter.setPower(-power);
            telemetry.addData("shooterPOWER", power);
        }

        if (gamepad1.left_bumper) {
            power = gamepad1.right_stick_y;
            telemetry.addData("Power", power);
        }

        telemetry.update();

        //Intake power
        if (gamepad1.dpad_down){
            intake.setPower(-1);
        } else if (gamepad1.dpad_up){
            intake.setPower(1);
        } else {
            intake.setPower(0);
        }

        //Trasfer controls. Right trigger = one direction, left trigger = the other.
        if (gamepad1.right_trigger_pressed){
            transfer.setPower(1);
        } else if (gamepad1.left_trigger_pressed) {
            transfer.setPower(-1);
        } else {
            transfer.setPower(0);
        }

        //Moving the turret manually
        if (gamepad1.dpad_left) {
            turret.setPower(1);
        } else if (gamepad1.dpad_right) {
            turret.setPower(-1);
        } else {
            turret.setPower(0);
        }

    }

    public double getDistanceFromAprilTag(double ta){
        double scale = 30665.95;
        double distance = (scale/ta);
        return distance;

    }



}
