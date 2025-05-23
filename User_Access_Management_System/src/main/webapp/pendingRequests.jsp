<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (role == null || !role.equalsIgnoreCase("manager")) {
        response.sendRedirect("login.jsp");
        return;
    }

    String dbUrl = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    String dbUser = "postgres";
    String dbPass = "charan";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pending Access Requests</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7fa;
            margin: 0;
            padding: 0;
        }

        .header {
            background-color: #343a40;
            color: white;
            padding: 1px;
            text-align: center;
        }

        .container {
            max-width: 1000px;
            margin: 30px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid #dee2e6;
        }

        th, td {
            padding: 12px 15px;
            text-align: center;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f8f9fa;
        }

        input[type="submit"] {
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            color: white;
            cursor: pointer;
            font-size: 14px;
        }

        input[value="Approve"] {
            background-color: #28a745;
        }

        input[value="Reject"] {
            background-color: #dc3545;
            margin-left: 8px;
        }

        .message {
            margin-top: 20px;
            text-align: center;
            font-size: 16px;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }
    </style>
</head>
<body>

<div class="header">
    <h1>Pending Access Requests</h1>
</div>

<div class="container">
    <table>
        <tr>
            <th>Request ID</th>
            <th>Employee Name</th>
            <th>Software</th>
            <th>Access Type</th>
            <th>Reason</th>
            <th>Action</th>
        </tr>

        <%
            try {
                Class.forName("org.postgresql.Driver");
                Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                Statement stmt = conn.createStatement();

                String query = "SELECT r.id, u.username, s.name, r.access_type, r.reason " +
                               "FROM requests r " +
                               "JOIN users u ON r.user_id = u.id " +
                               "JOIN software s ON r.software_id = s.id " +
                               "WHERE r.status = 'Pending'";
                ResultSet rs = stmt.executeQuery(query);

                boolean hasRequests = false;
                while (rs.next()) {
                    hasRequests = true;
        %>
        <tr>
            <td><%= rs.getInt("id") %></td>
            <td><%= rs.getString("username") %></td>
            <td><%= rs.getString("name") %></td>
            <td><%= rs.getString("access_type") %></td>
            <td><%= rs.getString("reason") %></td>
            <td>
                <form action="approve" method="post" style="display:inline;">
                    <input type="hidden" name="requestId" value="<%= rs.getInt("id") %>"/>
                    <input type="submit" name="action" value="Approve"/>
                    <input type="submit" name="action" value="Reject"/>
                </form>
            </td>
        </tr>
        <%
                }
                if (!hasRequests) {
        %>
        <tr>
            <td colspan="6">No pending requests</td>
        </tr>
        <%
                }
                conn.close();
            } catch (Exception e) {
        %>
        <tr><td colspan="6">Error loading requests</td></tr>
        <%
                e.printStackTrace();
            }
        %>
    </table>

    <div class="message">
        <% if (request.getAttribute("message") != null) { %>
            <p class="success"><%= request.getAttribute("message") %></p>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
    </div>
</div>

</body>
</html>
