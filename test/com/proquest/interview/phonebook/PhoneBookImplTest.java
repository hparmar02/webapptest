package com.proquest.interview.phonebook;

import com.proquest.interview.util.DatabaseUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhoneBookImplTest {
	private final PhoneBook phoneBook = new PhoneBookImpl();

	@Before
	public void setUp() {
		DatabaseUtil.initDB();
	}

	@Test
	public void shouldAddFindPerson() {

		Person newPerson = new Person();
		newPerson.name = "Matt Smith";
		newPerson.phoneNumber = "(824) 137-8952";
		newPerson.address = "965 N. Main St, Ann Arbor, MI";
		phoneBook.addPerson(newPerson);


		Person person = phoneBook.findPerson("Matt", "Smith");
		assertEquals(person.name, "Matt Smith");
		assertEquals(person.phoneNumber, "(824) 137-8952");
	}
}
