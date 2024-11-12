package be.hepl.model.dao;

import be.hepl.accessDB.ConnectDB;
import be.hepl.model.entity.Employee;
import be.hepl.model.viewmodel.EmployeeSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDAO implements CRUD<Employee, EmployeeSearchVM> {
    private ConnectDB connectDB;
    private ArrayList<Employee> employees;

    public EmployeeDAO() {
        connectDB = ConnectDB.getInstance();
        employees = new ArrayList<>();
    }

    @Override
    public ArrayList<Employee> getList() {
        return employees;
    }

    @Override
    public Employee getById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Employee> read() {
        return this.read(null);
    }

    // language=sql
    @Override
    public ArrayList<Employee> read(EmployeeSearchVM esvm) {
        try {
            String sql = "SELECT " +
                    "id, " +
                    "login, " +
                    "password " +
                    "FROM employees ";

            if(esvm != null) {
                String where = "WHERE 1=1 ";

                if(esvm.getId() != null) {
                    where += "AND id = ? ";
                }

                if(esvm.getLogin() != null) {
                    where += "AND login = ? ";
                }

                if(esvm.getPassword() != null) {
                    where += "AND password = ? ";
                }

                sql += where + "ORDER BY id ASC;";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            if(esvm != null) {
                int paramNumber = 0;

                if(esvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, esvm.getId());
                }

                if(esvm.getLogin() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, esvm.getLogin());
                }

                if(esvm.getPassword() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, esvm.getPassword());
                }
            }

            ResultSet rs = stmt.executeQuery();     // donne un tableau à 2 dimensions

            employees.clear();

            while(rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setLogin(rs.getString("login"));
                employee.setPassword(rs.getString("password"));

                employees.add(employee);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return employees;
        }
    }

    @Override
    public void save(Employee employee) {
        if (employee != null) {
            if (employee.getId() != null) { // UPDATE
                update(employee);
            } else { // CREATE
                create(employee);
            }
        }
    }

    @Override
    public void create(Employee employee) {
        try {
            String sql;
            sql = "INSERT INTO employees (login, password) VALUES (?, ?)";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, employee.getLogin());
            pStmt.setString(2, employee.getPassword());
            pStmt.executeUpdate();
            ResultSet rs = pStmt.getGeneratedKeys();

            if (rs.next()) {
                employee.setId(rs.getInt(1));         // set the generated ID
            }

            rs.close();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Employee employee) {
        try {
            String sql;
            sql = "UPDATE employees SET " +
                    "login = ?, " +
                    "password = ? " +
                    "WHERE id = ?";
            PreparedStatement pStmt = connectDB.getConn().prepareStatement(sql);
            pStmt.setString(1, employee.getLogin());
            pStmt.setString(2, employee.getPassword());
            pStmt.setInt(3, employee.getId());
            pStmt.executeUpdate();
            pStmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Employee employee) {
        if(employee != null && employee.getId() != null) {
            this.delete(employee.getId());
        }
    }

    @Override
    public void delete(Integer id) {
        if(id != null) {
            try {
                String sql = "DELETE FROM employees WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);
                stmt.setInt(1, id);;
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
