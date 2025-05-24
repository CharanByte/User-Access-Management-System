package com.leucine.usermanagementsystem;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private final String url = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    private final String user_Name = "postgres";
    private final String db_Password = "charan";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user_Name, db_Password);

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT role ,id FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                HttpSession session = request.getSession();
                int id=rs.getInt("id");
                session.setAttribute("userId", id);
                System.out.println(id);
                session.setAttribute("username", username);
                session.setAttribute("role", role);

                switch (role.toLowerCase()) {
                    case "employee":
                        response.sendRedirect("requestAccess.jsp");
                        break;
                    case "manager":
                        response.sendRedirect("pendingRequests.jsp");
                        break;
                    case "admin":
                        response.sendRedirect("createSoftware.jsp");
                        break;
                    default:
                        request.setAttribute("error", "Unknown role.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Login failed. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
