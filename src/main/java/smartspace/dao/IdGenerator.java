package smartspace.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="ID_GENERATOR")
public class IdGenerator {

	@Id
	private String nextId;




	public IdGenerator() {}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
}
