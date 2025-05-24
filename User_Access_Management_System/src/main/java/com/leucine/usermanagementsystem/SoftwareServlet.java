package com.leucine.usermanagementsystem;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = "/software")
public class SoftwareServlet extends HttpServlet {
    private final String url = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    private final String user_Name = "postgres";
    private final String db_Password = "charan";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
            Connection conn = DriverManager.getConnection(url, user_Name, db_Password);

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
