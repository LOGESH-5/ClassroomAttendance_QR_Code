package com.example.zxing_Attendance_Sys;



import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class QRgeneratorr {

	static final String DB_URL = "jdbc:mysql://localhost:3306/attendance_system";
    static final String DB_USER = "root";
    static final String DB_PASS = "Logesh@2004";
		 

		   
		    public static void generateQR() throws WriterException, IOException {
		        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

		            Scanner sc = new Scanner(System.in);
		            System.out.println("Enter your profession [student or teacher or admin_staff]: ");
		            String p = sc.nextLine().trim();

		            System.out.println("Enter your id: ");
		            String id = sc.nextLine().trim(); 

		            System.out.println("Enter your name: ");
		            String n = sc.nextLine().trim();

		            String q = null;
		            if (p.equalsIgnoreCase("student")) {
		                q = "SELECT * FROM students WHERE student_id = ? AND name = ?";
		            } else if (p.equalsIgnoreCase("teacher")) {
		                q = "SELECT * FROM teachers WHERE teacher_id = ? AND name = ?";
		            } else if (p.equalsIgnoreCase("admin_staff")) {
		                q = "SELECT * FROM admin_staff WHERE staff_id = ? AND name = ?";
		            } else {
		                System.out.println("Invalid profession!");
		                return;
		            }

		            
		            boolean exists = false;
		            try (PreparedStatement pstmt = conn.prepareStatement(q)) {
		                pstmt.setString(1, id);
		                pstmt.setString(2, n);
		                ResultSet rs = pstmt.executeQuery();
		                exists = rs.next();
		            }

		            if (!exists) {
		                System.out.println("No matching record found. QR not generated.");
		                return;
		            }
		            
		            try (PreparedStatement pstmt = conn.prepareStatement(
		                    "INSERT INTO attendance (user_id, profession, date, time) VALUES (?, ?, ?, ?)")) {
		                pstmt.setString(1, id);
		                pstmt.setString(2, p);
		                pstmt.setDate(3, Date.valueOf(LocalDate.now()));
		                pstmt.setTime(4, Time.valueOf(LocalTime.now()));
		                pstmt.executeUpdate();
		            }
		            System.out.println("Attendance recorded for: " + n + " (" + id + ")");

		            
		            String data = "Name:" + n + "\n ID:" + id + "\n Date:" + LocalDate.now()+"\n Time:"+LocalTime.now();
		            String filePath = "C:\\temp\\a.png";


		            QRCodeWriter qrCodeWriter = new QRCodeWriter();
		            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);
		            Path path = FileSystems.getDefault().getPath(filePath);
		            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

		            System.out.println("✅ QR generated: " + filePath);

		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    public static void main(String[] args) {
		    	//insertSampleData();
		    	try{
		    		generateQR();
		    	}catch(WriterException | IOException e) {
		    		e.printStackTrace();
		    	}
		    	
		    	
		    }
		    
		}

	


