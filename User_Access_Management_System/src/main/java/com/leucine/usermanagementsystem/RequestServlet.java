package com.leucine.usermanagementsystem;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/request")
public class RequestServlet extends HttpServlet {
    private final String url = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    private final String user_Name = "postgres";
    private final String db_Password = "charan";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || !"employee".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        int softwareId = Integer.parseInt(request.getParameter("softwareId"));
        String accessType = request.getParameter("accessType");
        String reason = request.getParameter("reason");

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user_Name, db_Password);

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO requests (user_id, software_id, access_type, reason, status) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, softwareId);
            stmt.setString(3, accessType);
            stmt.setString(4, reason);
            stmt.setString(5, "Pending");
            stmt.executeUpdate();

            conn.close();
            request.setAttribute("message", "Access request submitted successfully.");
            request.getRequestDispatcher("requestAccess.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to submit access request.");
            request.getRequestDispatcher("requestAccess.jsp").forward(request, response);
        }
    }
}
