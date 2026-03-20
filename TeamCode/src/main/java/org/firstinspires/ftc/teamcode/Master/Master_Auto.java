package org.firstinspires.ftc.teamcode.Master;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class Master_Auto extends LinearOpMode {
    private DcMotor shooter;
    private DcMotor intake;
    private DcMotor fly;
    private DcMotor wheel;

    private double power = 0.5;

    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        fly = hardwareMap.get(DcMotor.class, "fly");
        wheel = hardwareMap.get(DcMotor.class, "wheel");


        shooter.setPower(power);

        sleep(6000);

        intake.setPower(1);
        fly.setPower(1);
        wheel.setPower(-1);

        sleep(6000);

        shooter.setPower(0);
        intake.setPower(0);
        fly.setPower(0);
        wheel.setPower(0);
    }
}
