package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

public class UserService {
	private final String USERS;

	
	public UserService(String USERS) {
		this.USERS = USERS;
	}
	
	public void tryCreateUser(String email, String username, String password) throws IOException {
		var userhome = Paths.get(USERS, username);
		System.out.println(USERS);
		System.out.println(userhome);
		if (Files.notExists(userhome)) {
			createUser(userhome, email, password);
		}
	}

	public void createUser(Path userhome, String email, String password) throws IOException {
		Files.createDirectories(userhome);

		var salt = ThreadLocalRandom.current().nextInt();
		var encrypt = String.valueOf(salt + password.hashCode());

		var profile = userhome.resolve("profile");
		try (var writer = Files.newBufferedWriter(profile)) {
			writer.write(String.format("%s\t%s\t%d", email, encrypt, salt));
		}
	}
	
	public boolean login(String username, String password) throws IOException{
		var userhome = Paths.get(USERS, username);
		 return Files.exists(userhome) && isCorrectPassword(password, userhome);
	}

	private boolean isCorrectPassword(String password, Path userhome) throws IOException {
		// TODO Auto-generated method stub
		var profile = userhome.resolve("profile");
		try(var reader = Files.newBufferedReader(profile)){
			var data = reader.readLine().split("\t");
			var encrypt = Integer.parseInt(data[1]);
			var salt = Integer.parseInt(data[2]);
			return password.hashCode() + salt == encrypt;
		}
	}
	
	public Map<Long, String> message(String username) throws IOException{
		var userhome = Paths.get(USERS, username);
		var messages = new TreeMap<Long, String>(Comparator.reverseOrder());
		try(var txts = Files.newDirectoryStream(userhome, "*.txt")){
			for (var txt: txts) {
				var millis = txt.getFileName().toString().replace(".txt","");
				var blabla = Files.readAllLines(txt).stream().collect(Collectors.joining(System.lineSeparator()));
				messages.put(Long.parseLong(millis), blabla);
			}
		}
		return messages;
	}
	
	public void addMessage(String username, String blabla) throws IOException {
	
		var txt = Paths.get(USERS, username, String.format("%s.txt", Instant.now().toEpochMilli()));
		try(var writer = Files.newBufferedWriter(txt)){
			writer.write(blabla);
		}
	}
	
	public void deleteMessage(String username, String millis) throws IOException {
		var txt = Paths.get(USERS, username, String.format("%s.txt", millis));
		Files.delete(txt);
	}

}
