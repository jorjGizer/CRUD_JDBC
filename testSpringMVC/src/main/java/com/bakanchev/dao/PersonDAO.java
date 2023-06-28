package com.bakanchev.dao;
import com.bakanchev.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class PersonDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD= "postgres";
    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        try(Statement stmt = connection.createStatement()){
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = stmt.executeQuery(SQL);
            while (resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return people;
    }

    public Person show(int id) {
        Person person = null;
        try(PreparedStatement stmt = connection.prepareStatement(
                "SELECT * from Person  where id=?"
        )){
                stmt.setInt(1, id);
                ResultSet resultSet = stmt.executeQuery();

                resultSet.next();

                person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return person;
    }

    public void save(Person person) {
        try(PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Person(name, age, email) VALUES(?, ?, ?)")){
            stmt.setString(1, person.getName());
            stmt.setInt(2, person.getAge());
            stmt.setString(3, person.getEmail());
            stmt.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void update(int id, Person updatePerson){
        try(PreparedStatement stmt = connection.prepareStatement(
                "UPDATE Person  SET name=?, age=?, email=? " +
                        "WHERE id=?"
        )){
            stmt.setString(1, updatePerson.getName());
            stmt.setInt(2, updatePerson.getAge());
            stmt.setString(3, updatePerson.getEmail());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public void delete(int id){
        try(PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM Person WHERE id=? "
        )){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}