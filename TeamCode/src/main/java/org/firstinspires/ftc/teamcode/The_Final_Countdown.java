package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class The_Final_Countdown extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRight");

        //Hood
        CRServo hood = hardwareMap.crservo.get("hood");
        int hoodPOS = 0;
        int hoodMIN = 0;
        int hoodMAX = 100000;

        //Turret
        CRServo turret = hardwareMap.crservo.get("turret");
        int turretANG = 0;
        int fullRotate = 1000;

        //Hood
        DcMotor shooter1 = hardwareMap.dcMotor.get("shooter1");
        DcMotor shooter2 = hardwareMap.dcMotor.get("shooter2");

        //Intake
        DcMotor intake = hardwareMap.dcMotor.get("intake");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            if (gamepad1.left_trigger != 0){
                frontLeftMotor.setPower(frontLeftPower / 4);
                backLeftMotor.setPower(backLeftPower / 4);
                frontRightMotor.setPower(frontRightPower / 4);
                backRightMotor.setPower(backRightPower/ 4);
            } else {
                frontLeftMotor.setPower(frontLeftPower);
                backLeftMotor.setPower(backLeftPower);
                frontRightMotor.setPower(frontRightPower);
                backRightMotor.setPower(backRightPower);
            }

            //The intake grabbing the balls
            if (gamepad2.left_trigger != 0){
                intake.setPower(1);
            } else {
                intake.setPower(0);
            }

            //The shooter
            if (gamepad2.right_trigger != 0){
                shooter1.setPower(1);
                shooter2.setPower(-1);
            } else {
                shooter1.setPower(0);
                shooter2.setPower(0);
            }

            //Hood power
            if (!(hoodPOS + gamepad2.left_stick_y < hoodMIN) && !(hoodPOS + gamepad2.left_stick_y > hoodMAX)){
                hood.setPower(gamepad2.left_stick_y);
            } else {
                hood.setPower(0);
            }

            //Just some debuging
            if (gamepad2.a) {
                telemetry.addData("Hood position", hoodPOS);
            }

            //Turret servo
            turret.setPower(gamepad2.left_stick_x);



            telemetry.update();
        }
    }
}