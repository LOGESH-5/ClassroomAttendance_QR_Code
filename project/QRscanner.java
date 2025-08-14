package com.example.zxing_Attendance_Sys;

import java.io.File;
import java.io.IOException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class QRscanner {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\temp\\a.png"); 
            BufferedImage bufferedImage = ImageIO.read(file);

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);

            System.out.println("✅ QR Code Data:\n " + result.getText());
        } catch (IOException | NotFoundException e) {
            System.out.println("❌ Error reading QR Code: " + e.getMessage());
        }
    }
}
