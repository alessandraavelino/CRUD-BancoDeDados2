
public class Person {
	
	/*
	 * id of database
	 */
	private int id;
	
	/*
	 * person name
	 */
	private String name;
	
	/*
	 * person identity
	 */
	private String sobrenome;
	
	/*
	 * person birthday
	 */
	private String birthday;
	
	//constructor
	public Person() {
		super();
	}

	//Getter and Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

  
	
	

}
