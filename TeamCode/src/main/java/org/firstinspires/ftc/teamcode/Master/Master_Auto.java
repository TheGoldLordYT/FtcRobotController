package org.firstinspires.ftc.teamcode.Master;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class Master_Auto extends LinearOpMode {
    private DcMotor shooter;
    private DcMotor intake;
    private DcMotor fly;
    private DcMotor wheel;
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;

    private double power = 0.77;

    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        fly = hardwareMap.get(DcMotor.class, "fly");
        wheel = hardwareMap.get(DcMotor.class, "wheel");

        frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        backLeftMotor = hardwareMap.dcMotor.get("backLeft");
        frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        backRightMotor = hardwareMap.dcMotor.get("backRight");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("AUTO READY");
        telemetry.update();

        waitForStart();

        telemetry.addLine("ON");
        telemetry.update();


        shooter.setPower(-power);

        sleep(6000);

        intake.setPower(-1);
        fly.setPower(1);
        wheel.setPower(1);

        sleep(500);

        intake.setPower(0);
        fly.setPower(0);
        wheel.setPower(0);

        sleep(5000);

        intake.setPower(-1);
        fly.setPower(1);
        wheel.setPower(1);

        sleep(2000);

        shooter.setPower(0);
        intake.setPower(0);
        fly.setPower(0);
        wheel.setPower(0);

        frontLeftMotor.setPower(1);
        backLeftMotor.setPower(1);
        frontRightMotor.setPower(1);
        backRightMotor.setPower(1);

        sleep(1000);

        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);




    }
}
