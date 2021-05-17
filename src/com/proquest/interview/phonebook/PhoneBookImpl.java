package com.proquest.interview.phonebook;

import com.proquest.interview.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhoneBookImpl implements PhoneBook {
	public List<Person> people;

	@Override
	public Person findPerson(String firstName, String lastName) {
		try {
			String name = firstName.concat(" ").concat(lastName);
			people = getPersonList( name);
			return people.size() >= 1 ? people.get(0) : null;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void addPerson(Person newPerson) {
		try {
			Connection connection = DatabaseUtil.getConnection();
			Statement statement = connection.createStatement();
			String sql = String.format("INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES('%s','%s', '%s')", newPerson.name, newPerson.phoneNumber, newPerson.address);

			statement.execute(sql);
			statement.close();
			connection.commit();
		} catch (ClassNotFoundException | SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database

		PhoneBook phoneBookList = new PhoneBookImpl();

		Person jSmithPerson = new Person ();
		jSmithPerson.name = "John Smith";
		jSmithPerson.address = "1234 Sand Hill Dr, Royal Oak, MI";
		jSmithPerson.phoneNumber = "(248) 123-4567";
		phoneBookList.addPerson(jSmithPerson);

		Person cSmithPerson = new Person ();
		cSmithPerson.name = "Cynthia Smith";
		cSmithPerson.address = "875 Main St, Ann Arbor, MI";
		cSmithPerson.phoneNumber = "(824) 128-8758";
		phoneBookList.addPerson(cSmithPerson);


		List<Person> personList = getPersonList( "");

		personList.forEach(person -> System.out.println (person.name.concat(" - ").concat(person.address).concat(" - ").concat(person.phoneNumber)));

		Person person = phoneBookList.findPerson("Cynthia", "Smith");

		System.out.println (person.name.concat(" - ").concat(person.address).concat(" - ").concat(person.phoneNumber));
	}

	private static List<Person> getPersonList(String name) throws SQLException, ClassNotFoundException {
		Connection connection = DatabaseUtil.getConnection();
		String sql = (name == null  ||  name.isEmpty())
				? "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK"
				: String.format("SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK WHERE NAME = '%s'", name);

		List<Person> personList = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Person person = new Person ();
				person.name = resultSet.getString("NAME");
				person.phoneNumber = resultSet.getString("PHONENUMBER");
				person.address = resultSet.getString("ADDRESS");
				personList.add(person);
			}
			statement.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		connection.close();

		return personList;
	}
}
