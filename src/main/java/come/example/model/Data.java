package come.example.model;

import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Data")
public class Data {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
    public String name;
    public Integer age;

    public Data(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    public Data() {
      
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id=id;
    }
}
