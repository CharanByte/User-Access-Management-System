package com.leucine.usermanagementsystem;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = "/software")
public class SoftwareServlet extends HttpServlet {
    private final String DB_URL = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "charan";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] accessLevels = request.getParameterValues("accessLevels");

        if (name == null || description == null || accessLevels == null) {
            request.setAttribute("error", "Please fill all fields and select at least one access level.");
            request.getRequestDispatcher("createSoftware.jsp").forward(request, response);
            return;
        }

        String accessLevelsStr = String.join(",", accessLevels);

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO software (name, description, access_levels) VALUES (?, ?, ?)"
            );
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, accessLevelsStr);
            stmt.executeUpdate();

            conn.close();

            request.setAttribute("message", "Software added successfully.");
            request.getRequestDispatcher("createSoftware.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to add software. Please try again.");
            request.getRequestDispatcher("createSoftware.jsp").forward(request, response);
        }
    }
}
