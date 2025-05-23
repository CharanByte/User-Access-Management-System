<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (role == null || !role.equalsIgnoreCase("employee")) {
        response.sendRedirect("login.jsp");
        return;
    }

    String dbUrl = "jdbc:postgresql://localhost:1234/UserManagementSystem";
    String dbUser = "postgres";
    String dbPass = "charan";
    Connection conn = null;
    ResultSet rs = null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Request Software Access</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f2f4f8;
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
            max-width: 500px;
            background: white;
            margin: 30px auto;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
            text-align: center;
        }

        label {
            display: block;
            margin: 15px 0 5px;
            font-weight: bold;
        }

        select, textarea, input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: white;
            border: none;
            margin-top: 20px;
            cursor: pointer;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }

        .message {
            text-align: center;
            margin-top: 15px;
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
    <h1>Request Software Access</h1>
</div>

<div class="container">
    <form action="request" method="post">
        <label for="softwareId">Select Software:</label>
        <select name="softwareId" id="softwareId" required>
            <option value="">-- Select Software --</option>
            <%
                try {
                    Class.forName("org.postgresql.Driver");
                    conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                    Statement stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT id, name FROM software");

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
            %>
                        <option value="<%= id %>"><%= name %></option>
            <%
                    }
                    rs.close();
                    conn.close();
                } catch (Exception e) {
                    out.println("<option disabled>Error loading software list</option>");
                    e.printStackTrace();
                }
            %>
        </select>

        <label for="accessType">Access Type:</label>
        <select name="accessType" id="accessType" required>
            <option value="Read">Read</option>
            <option value="Write">Write</option>
            <option value="Admin">Admin</option>
        </select>

        <label for="reason">Reason for Request:</label>
        <textarea name="reason" id="reason" rows="4" required></textarea>

        <input type="submit" value="Submit Request" />
    </form>

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
