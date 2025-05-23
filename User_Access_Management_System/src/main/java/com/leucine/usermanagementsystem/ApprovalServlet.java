package com.leucine.usermanagementsystem;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/approve")
public class ApprovalServlet extends HttpServlet {
    private final String DB_URL = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "charan";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("manager")) {
            response.sendRedirect("login.jsp");
            return;
        }

        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String action = request.getParameter("action");
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE requests SET status = ? WHERE id = ?"
            );
            stmt.setString(1, action);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();

            conn.close();
            request.setAttribute("message", "Request " + action.toLowerCase() + "ed successfully.");
            request.getRequestDispatcher("pendingRequests.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update request.");
            request.getRequestDispatcher("pendingRequests.jsp").forward(request, response);
        }
    }
}
