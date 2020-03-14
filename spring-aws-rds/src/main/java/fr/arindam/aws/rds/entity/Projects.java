package fr.arindam.aws.rds.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="projects")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Projects {

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="project_name")
	private String projectName;
}
