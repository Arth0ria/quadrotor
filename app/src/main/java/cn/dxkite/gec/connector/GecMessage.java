package cn.dxkite.gec.connector;

import java.io.Serializable;

/**
 * 无人机通信的原始数据 PID 比例（proportion）、积分（integral）、导数（derivative）
 *
 * @author DXkite
 */
public class GecMessage implements Serializable {

    private byte[] message = null;
    public final static byte
            // 固定头
            HEAD = (byte) 0xAA,
    // 边界标识
    DATA = 0x1C,
    // 指令代码
    WRITE = (byte) 0xC0, TIMING_RETURN = 0x30, WRITE_OUTPID = 0x10, READ_OUTPID = 0x20, WRITE_INPID = 0x14,
            READ_INPID = 0x24, WRITE_POSE = 0x11, READ_POSE = 0x21, CONNECTED = 0x50,
    // 数据结束操作
    CR = '\r', LF = '\n';

    // 信息状态
    private byte type, led3, led4;
    // 油门
    private int power;
    // 航向
    private int course, outCourse, courseP, courseI, courseD;
    // 横滚
    private int roll, outRoll, rollP, rollI, rollD;
    // 俯仰
    private int pitch, outPitch, pitchP, pitchI, pitchD;
    // 加速度X,Y,Z
    private int accelerationX, accelerationY, accelerationZ;
    // 陀螺仪
    private int gyroscopeX, gyroscopeY, gyroscopeZ;
    // 电压
    private int voltage;

    public GecMessage(byte[] message) {
        this.message = message;
        unpack();
    }

    public GecMessage() {

    }

    public GecMessage(byte type) {
        super();
        this.type = type;
    }

    public boolean isType(int type) {
        return getType() == type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getLed3() {
        return led3;
    }

    public void setLed3(byte led3) {
        this.led3 = led3;
    }

    public byte getLed4() {
        return led4;
    }

    public void setLed4(byte led4) {
        this.led4 = led4;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getOutCourse() {
        return outCourse;
    }

    public int getCourseP() {
        return courseP;
    }

    public void setCourseP(int courseP) {
        this.courseP = courseP;
    }

    public int getCourseI() {
        return courseI;
    }

    public void setCourseI(int courseI) {
        this.courseI = courseI;
    }

    public int getCourseD() {
        return courseD;
    }

    public void setCourseD(int courseD) {
        this.courseD = courseD;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getOutRoll() {
        return outRoll;
    }

    public int getRollP() {
        return rollP;
    }

    public void setRollP(int rollP) {
        this.rollP = rollP;
    }

    public int getRollI() {
        return rollI;
    }

    public void setRollI(int rollI) {
        this.rollI = rollI;
    }

    public int getRollD() {
        return rollD;
    }

    public void setRollD(int rollD) {
        this.rollD = rollD;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getOutPitch() {
        return outPitch;
    }

    public int getPitchP() {
        return pitchP;
    }

    public void setPitchP(int pitchP) {
        this.pitchP = pitchP;
    }

    public int getPitchI() {
        return pitchI;
    }

    public void setPitchI(int pitchI) {
        this.pitchI = pitchI;
    }

    public int getPitchD() {
        return pitchD;
    }

    public void setPitchD(int pitchD) {
        this.pitchD = pitchD;
    }

    public int getAccelerationX() {
        return accelerationX+ 500;
    }

    public void setAccelerationX(int accelerationX) {
        this.accelerationX = accelerationX/* -500 */;
    }

    public int getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(int accelerationY) {
        this.accelerationY = accelerationY/* -500 */;
    }

    public int getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(int accelerationZ) {
        this.accelerationZ = accelerationZ/* -500 */;
    }

    public int getGyroscopeX() {
        return gyroscopeX;
    }

    public void setGyroscopeX(int gyroscopeX) {
        this.gyroscopeX = gyroscopeX;
    }

    public int getGyroscopeY() {
        return gyroscopeY;
    }

    public void setGyroscopeY(int gyroscopeY) {
        this.gyroscopeY = gyroscopeY;
    }

    public int getGyroscopeZ() {
        return gyroscopeZ;
    }

    public void setGyroscopeZ(int gyroscopeZ) {
        this.gyroscopeZ = gyroscopeZ;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    /**
     * 打包信息到标准信息
     *
     * @return
     */
    public byte[] toBytes() {
        byte[] message = new byte[34];

        message[0] = HEAD;
        message[1] = type;
        message[2] = DATA;


        int pos;
        // ... 数据 ...
        switch (type) {
            case WRITE:
                pos = 3;
                // 油门
                message[pos++] = (byte) ((power >>> 8) & 0xff);
                message[pos++] = (byte) (power & 0xff);
                // 航向
                message[pos++] = (byte) ((course >>> 8) & 0xff);
                message[pos++] = (byte) (course & 0xff);
                // 翻滚
                message[pos++] = (byte) ((roll >>> 8) & 0xff);
                message[pos++] = (byte) (roll & 0xff);
                // 俯仰
                message[pos++] = (byte) ((pitch >>> 8) & 0xff);
                message[pos++] = (byte) (pitch & 0xff);
                pos = 17;
                message[pos++] = led3;
                message[pos++] = led4;
                break;
            case WRITE_OUTPID:

            case WRITE_INPID:
                pos = 3;
                // 横滚
                message[pos++] = (byte) ((rollP >>> 8) & 0xff);
                message[pos++] = (byte) (rollP & 0xff);
                message[pos++] = (byte) ((rollI >>> 8) & 0xff);
                message[pos++] = (byte) (rollI & 0xff);
                message[pos++] = (byte) ((rollD >>> 8) & 0xff);
                message[pos++] = (byte) (rollD & 0xff);
                // 俯仰
                message[pos++] = (byte) ((pitchP >>> 8) & 0xff);
                message[pos++] = (byte) (pitchP & 0xff);
                message[pos++] = (byte) ((pitchI >>> 8) & 0xff);
                message[pos++] = (byte) (pitchI & 0xff);
                message[pos++] = (byte) ((pitchD >>> 8) & 0xff);
                message[pos++] = (byte) (pitchD & 0xff);
                // 航向
                message[pos++] = (byte) ((courseP >>> 8) & 0xff);
                message[pos++] = (byte) (courseP & 0xff);
                message[pos++] = (byte) ((courseI >>> 8) & 0xff);
                message[pos++] = (byte) (courseI & 0xff);
                message[pos++] = (byte) ((courseD >>> 8) & 0xff);
                message[pos++] = (byte) (courseD & 0xff);
                break;
            case WRITE_POSE:
                pos = 3;
                // 加速度
                message[pos++] = (byte) ((accelerationX >>> 8) & 0xff);
                message[pos++] = (byte) (accelerationX & 0xff);
                message[pos++] = (byte) ((accelerationY >>> 8) & 0xff);
                message[pos++] = (byte) (accelerationY & 0xff);
                message[pos++] = (byte) ((accelerationZ >>> 8) & 0xff);
                message[pos++] = (byte) (accelerationZ & 0xff);
                // 陀螺仪
                message[pos++] = (byte) ((gyroscopeX >>> 8) & 0xff);
                message[pos++] = (byte) (gyroscopeX & 0xff);
                message[pos++] = (byte) ((gyroscopeY >>> 8) & 0xff);
                message[pos++] = (byte) (gyroscopeY & 0xff);
                message[pos++] = (byte) ((gyroscopeZ >>> 8) & 0xff);
                message[pos++] = (byte) (gyroscopeZ & 0xff);
                break;
        }
        // ... 数据 ...
        message[31] = DATA;
        message[32] = CR;
        message[33] = LF;
        return message;
    }

    public boolean unpack() {

        type = message[1];
        if (type != CONNECTED) {
            // 校验数据是否正确
            int sum = 0;
            for (int i = 0; i <= 30; i++) {
                sum += message[i];
            }
            byte low8bit = (byte) (sum & 0xff);
            if (low8bit != message[31]) {
                return false;
            }
        }

        switch (type) {
            // 解析定时返回
            case TIMING_RETURN:
                // 加速度
                accelerationX = (message[3] << 8) | message[4];
                accelerationY = (message[5] << 8) | message[6];
                accelerationZ = (message[7] << 8) | message[8];
                // 陀螺仪
                gyroscopeX = (message[9] << 8) | message[10];
                gyroscopeY = (message[11] << 8) | message[12];
                gyroscopeZ = (message[13] << 8) | message[14];
                // 姿态数据
                roll = (message[15] << 8) | message[16];
                pitch = (message[17] << 8) | message[18];
                course = (message[19] << 8) | message[20];
                // 输出姿态数据
                outRoll = (message[21] << 8) | message[22];
                outPitch = (message[23] << 8) | message[24];
                outCourse = (message[25] << 8) | message[26];
                // 油门
                power = (message[27] << 8) | message[28];
                // 电压
                voltage = message[30];
                break;

            // 读取内环外环PID
            case READ_OUTPID:
            case READ_INPID:
                // 横滚
                rollP = (message[3] << 8) | message[4];
                rollI = (message[5] << 8) | message[6];
                rollD = (message[7] << 8) | message[8];
                // 俯仰
                pitchP = (message[9] << 8) | message[10];
                pitchI = (message[11] << 8) | message[12];
                pitchD = (message[13] << 8) | message[14];
                // 航向
                courseP = (message[15] << 8) | message[16];
                courseI = (message[17] << 8) | message[18];
                courseD = (message[19] << 8) | message[20];
                break;

            // 读姿态
            case READ_POSE:
                // 加速度
                accelerationX = (message[3] << 8) | message[4];
                accelerationY = (message[5] << 8) | message[6];
                accelerationZ = (message[7] << 8) | message[8];
                // 陀螺仪
                gyroscopeX = (message[9] << 8) | message[10];
                gyroscopeY = (message[11] << 8) | message[12];
                gyroscopeZ = (message[13] << 8) | message[14];
                break;
        }
        return true;


    }

    @Override
    public String toString() {
        return "GecMessage [type=" + type + ", led3=" + led3 + ", led4=" + led4 + ", power=" + power + ", course="
                + course + ", outCourse=" + outCourse + ", courseP=" + courseP + ", courseI=" + courseI + ", courseD="
                + courseD + ", roll=" + roll + ", outRoll=" + outRoll + ", rollP=" + rollP + ", rollI=" + rollI
                + ", rollD=" + rollD + ", pitch=" + pitch + ", outPitch=" + outPitch + ", pitchP=" + pitchP
                + ", pitchI=" + pitchI + ", pitchD=" + pitchD + ", accelerationX=" + accelerationX + ", accelerationY="
                + accelerationY + ", accelerationZ=" + accelerationZ + ", gyroscopeX=" + gyroscopeX + ", gyroscopeY="
                + gyroscopeY + ", gyroscopeZ=" + gyroscopeZ + ", voltage=" + voltage + "]";
    }

    public String toHexString() {
        return toHexString(true);
    }

    public String toHexString(boolean space) {
        byte[] codes = message == null ? toBytes() : message;
        return byte2hex(codes, true);
    }

    public static String byte2hex(byte[] bytes, boolean space) {
        return byte2hex(bytes,space,bytes.length);
    }

    public static String byte2hex(byte[] bytes, boolean space,int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int v = bytes[i] & 0xFF;
            String code = Integer.toHexString(v);
            if (code.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(code);
            if (space) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString().trim();
    }
}
