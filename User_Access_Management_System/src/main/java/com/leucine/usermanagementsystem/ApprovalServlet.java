package com.leucine.usermanagementsystem;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/approve")
public class ApprovalServlet extends HttpServlet {
    private final String url = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    private final String user_Name = "postgres";
    private final String db_Password = "charan";

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
            Connection conn = DriverManager.getConnection(url, user_Name, db_Password);

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
