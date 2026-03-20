package org.firstinspires.ftc.teamcode.Master;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class Auto extends OpMode {
    public void init(){
        telemetry.addData("Status", "OFF");

    }

    public void loop(){
        telemetry.addData("Status", "ON");
        telemetry.update();
    }
}
